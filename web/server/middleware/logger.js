/**
 * @file logger.js
 * @description HTTP request/response logging middleware.
 */

function loggerMiddleware(req, res, next) {
  const start = Date.now();
  const originalEnd = res.end;

  res.end = function (...args) {
    const duration = Date.now() - start;
    const status = res.statusCode;
    const colorCode = status >= 500 ? '\x1b[31m' : (status >= 400 ? '\x1b[33m' : (status >= 300 ? '\x1b[36m' : '\x1b[32m'));
    const resetCode = '\x1b[0m';

    console.log(`[HTTP] ${req.method} ${req.url} ${colorCode}${status}${resetCode} (${duration}ms)`);
    return originalEnd.apply(this, args);
  };

  if (typeof next === 'function') next();
}

module.exports = loggerMiddleware;
