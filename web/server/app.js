/**
 * @file app.js
 * @description Master HTTP server request pipeline integrating routing, static asset delivery, and middleware.
 */

const fs = require('fs');
const path = require('path');
const env = require('./config/env');
const corsMiddleware = require('./middleware/cors');
const loggerMiddleware = require('./middleware/logger');
const parseJsonBody = require('./middleware/bodyDecoder');
const { sendError } = require('./middleware/errorHandler');
const apiRouter = require('./routes/apiRouter');

const MIME_TYPES = {
  '.html': 'text/html; charset=UTF-8',
  '.css': 'text/css; charset=UTF-8',
  '.js': 'text/javascript; charset=UTF-8',
  '.json': 'application/json; charset=UTF-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.woff2': 'font/woff2',
  '.woff': 'font/woff',
  '.ttf': 'font/ttf'
};

const WEB_ROOT = path.resolve(__dirname, '..');

async function handleRequest(req, res) {
  // 1. Logger
  loggerMiddleware(req, res);

  // 2. CORS Handling
  const handledCors = corsMiddleware(req, res);
  if (handledCors) return;

  const parsedUrl = new URL(req.url, `http://${req.headers.host || 'localhost'}`);
  const pathname = parsedUrl.pathname;
  const query = Object.fromEntries(parsedUrl.searchParams.entries());

  // 3. API Route Dispatch
  if (pathname.startsWith(env.API_PREFIX)) {
    const routeMatch = apiRouter.match(req.method, pathname);
    if (routeMatch) {
      try {
        const body = await parseJsonBody(req);
        return routeMatch.handler(req, res, {
          params: routeMatch.params,
          query,
          body
        });
      } catch (err) {
        return sendError(res, err, 400);
      }
    } else {
      return sendError(res, new Error(`Route not found: ${req.method} ${pathname}`), 404);
    }
  }

  // 4. Static Asset Delivery
  let relativeFilePath = pathname;
  if (relativeFilePath === '/' || relativeFilePath === '') {
    relativeFilePath = '/index.html';
  }

  // Normalize and prevent directory traversal
  const safePath = path.normalize(relativeFilePath).replace(/^(\.\.[\/\\])+/, '');
  const absoluteFilePath = path.join(WEB_ROOT, safePath);

  // Check if file is within WEB_ROOT
  if (!absoluteFilePath.startsWith(WEB_ROOT)) {
    res.writeHead(403, { 'Content-Type': 'text/plain' });
    return res.end('403 Forbidden: Directory traversal prohibited');
  }

  fs.stat(absoluteFilePath, (err, stats) => {
    if (err || !stats.isFile()) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      return res.end(`404 Not Found: ${pathname}`);
    }

    const ext = path.extname(absoluteFilePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    // Set cache control headers - disable caching during development
    if (ext === '.html' || env.NODE_ENV === 'development') {
      res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
      res.setHeader('Pragma', 'no-cache');
      res.setHeader('Expires', '0');
    } else {
      res.setHeader('Cache-Control', 'public, max-age=3600');
    }

    res.writeHead(200, { 'Content-Type': contentType });
    const readStream = fs.createReadStream(absoluteFilePath);
    readStream.on('error', streamErr => {
      sendError(res, streamErr, 500);
    });
    readStream.pipe(res);
  });
}

module.exports = handleRequest;
