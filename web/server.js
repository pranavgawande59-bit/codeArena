/**
 * @file server.js
 * @description CODE-ARENA Web Application & REST API Master Server Entry Point.
 */

const http = require('http');
const env = require('./server/config/env');
const handleRequest = require('./server/app');

const server = http.createServer((req, res) => {
  handleRequest(req, res).catch(err => {
    console.error('[Unhandled Request Error]', err);
    if (!res.headersSent) {
      res.writeHead(500, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ success: false, error: 'Internal Server Error' }));
    }
  });
});

server.on('error', (err) => {
  if (err.code === 'EADDRINUSE') {
    console.log(`[CODE-ARENA] Port ${env.PORT} is already in use. Server is active at http://${env.HOST}:${env.PORT}/`);
    process.exit(0);
  } else {
    console.error('[Server Error]', err);
    process.exit(1);
  }
});

server.listen(env.PORT, env.HOST, () => {
  console.log(`================================================================`);
  console.log(`⚔️  CODE-ARENA Platform Server Running`);
  console.log(`🌐 Web UI:   http://${env.HOST}:${env.PORT}/`);
  console.log(`📡 REST API: http://${env.HOST}:${env.PORT}${env.API_PREFIX}/health`);
  console.log(`⚙️  Mode:     ${env.NODE_ENV.toUpperCase()}`);
  console.log(`================================================================`);
});

// Graceful shutdown handling
process.on('SIGINT', () => {
  console.log('\n[CODE-ARENA] Gracefully shutting down HTTP server...');
  server.close(() => {
    console.log('[CODE-ARENA] Server stopped.');
    process.exit(0);
  });
});
