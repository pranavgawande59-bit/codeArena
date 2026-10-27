/**
 * @file bodyDecoder.js
 * @description Asynchronously parses incoming JSON request bodies.
 */

function parseJsonBody(req) {
  return new Promise((resolve, reject) => {
    // Only parse body for mutating HTTP methods
    if (!['POST', 'PUT', 'PATCH', 'DELETE'].includes(req.method)) {
      return resolve({});
    }

    let rawData = '';
    req.on('data', chunk => {
      rawData += chunk;
      // Protect against body payload exceeding 1MB
      if (rawData.length > 1e6) {
        req.destroy();
        reject(new Error('Payload Too Large: Body exceeds 1MB limit'));
      }
    });

    req.on('end', () => {
      if (!rawData || rawData.trim() === '') {
        return resolve({});
      }
      try {
        const parsed = JSON.parse(rawData);
        resolve(parsed);
      } catch (err) {
        reject(new Error('Malformed JSON payload: ' + err.message));
      }
    });

    req.on('error', err => {
      reject(err);
    });
  });
}

module.exports = parseJsonBody;
