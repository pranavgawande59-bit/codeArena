/**
 * @file cors.js
 * @description Cross-Origin Resource Sharing (CORS) middleware.
 */

const env = require('../config/env');

function corsMiddleware(req, res) {
  if (!env.ENABLE_CORS) return false;

  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization, X-Requested-With');

  // Handle pre-flight OPTIONS request
  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return true; // Handled
  }

  return false;
}

module.exports = corsMiddleware;
