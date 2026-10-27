/**
 * @file env.js
 * @description Centralized environment configuration loader for CODE-ARENA Web Server.
 * Reads environment variables from process.env and parses .env file natively without external dependencies.
 */

const fs = require('fs');
const path = require('path');

function parseDotEnv() {
  const envPath = path.resolve(__dirname, '../../.env');
  const envVars = {};

  if (fs.existsSync(envPath)) {
    try {
      const content = fs.readFileSync(envPath, 'utf8');
      const lines = content.split(/\r?\n/);
      for (const line of lines) {
        const trimmed = line.trim();
        if (!trimmed || trimmed.startsWith('#')) continue;
        const eqIdx = trimmed.indexOf('=');
        if (eqIdx !== -1) {
          const key = trimmed.substring(0, eqIdx).trim();
          let val = trimmed.substring(eqIdx + 1).trim();
          // Strip enclosing quotes if present
          if ((val.startsWith('"') && val.endsWith('"')) || (val.startsWith("'") && val.endsWith("'"))) {
            val = val.substring(1, val.length - 1);
          }
          envVars[key] = val;
        }
      }
    } catch (err) {
      console.warn('[Config Warning] Failed to parse .env file:', err.message);
    }
  }

  return envVars;
}

const fileEnv = parseDotEnv();

const config = Object.freeze({
  PORT: parseInt(process.env.PORT || fileEnv.PORT || '3000', 10),
  HOST: process.env.HOST || fileEnv.HOST || 'localhost',
  NODE_ENV: process.env.NODE_ENV || fileEnv.NODE_ENV || 'development',
  API_PREFIX: process.env.API_PREFIX || fileEnv.API_PREFIX || '/api/v1',
  ENABLE_CORS: (process.env.ENABLE_CORS || fileEnv.ENABLE_CORS || 'true').toLowerCase() === 'true',
  IS_PRODUCTION: (process.env.NODE_ENV || fileEnv.NODE_ENV) === 'production'
});

module.exports = config;
