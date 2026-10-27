/**
 * @file errorHandler.js
 * @description Centralized error response utility and exception handler.
 */

function sendError(res, error, statusCode = 500) {
  const status = error.statusCode || statusCode;
  const message = error.message || 'An unexpected internal error occurred';

  console.error(`[API Error ${status}]`, error.stack || error.message || error);

  if (!res.headersSent) {
    res.writeHead(status, { 'Content-Type': 'application/json' });
  }

  res.end(JSON.stringify({
    success: false,
    error: {
      status,
      message,
      timestamp: new Date().toISOString()
    }
  }));
}

function sendSuccess(res, data, statusCode = 200) {
  if (!res.headersSent) {
    res.writeHead(statusCode, { 'Content-Type': 'application/json' });
  }

  res.end(JSON.stringify({
    success: true,
    data,
    timestamp: new Date().toISOString()
  }));
}

module.exports = {
  sendError,
  sendSuccess
};
