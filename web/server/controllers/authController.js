/**
 * @file authController.js
 * @description HTTP controllers for authentication and profile management.
 */

const authService = require('../services/authService');
const { sendSuccess, sendError } = require('../middleware/errorHandler');

const authController = {
  login(req, res, body) {
    try {
      const result = authService.login(body);
      sendSuccess(res, result, 200);
    } catch (err) {
      sendError(res, err, 400);
    }
  },

  logout(req, res) {
    try {
      const result = authService.logout();
      sendSuccess(res, result, 200);
    } catch (err) {
      sendError(res, err, 500);
    }
  },

  getProfile(req, res) {
    try {
      const result = authService.getCurrentUser();
      sendSuccess(res, result, 200);
    } catch (err) {
      sendError(res, err, 500);
    }
  },

  updateProfile(req, res, body) {
    try {
      const updated = authService.updateProfile(body);
      sendSuccess(res, updated, 200);
    } catch (err) {
      sendError(res, err, 400);
    }
  }
};

module.exports = authController;
