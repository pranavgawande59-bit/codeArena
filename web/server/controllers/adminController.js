/**
 * @file adminController.js
 * @description HTTP controllers for Admin question management CRUD.
 */

const adminService = require('../services/adminService');
const { sendSuccess, sendError } = require('../middleware/errorHandler');

const adminController = {
  getQuestions(req, res, query) {
    try {
      const list = adminService.getQuestions(query);
      sendSuccess(res, {
        total: list.length,
        questions: list
      }, 200);
    } catch (err) {
      sendError(res, err, 500);
    }
  },

  createQuestion(req, res, body) {
    try {
      const created = adminService.createQuestion(body);
      sendSuccess(res, created, 201);
    } catch (err) {
      sendError(res, err, 400);
    }
  },

  updateQuestion(req, res, params, body) {
    try {
      const updated = adminService.updateQuestion(params.id, body);
      sendSuccess(res, updated, 200);
    } catch (err) {
      sendError(res, err, 400);
    }
  },

  deleteQuestion(req, res, params) {
    try {
      const result = adminService.deleteQuestion(params.id);
      sendSuccess(res, result, 200);
    } catch (err) {
      sendError(res, err, 404);
    }
  }
};

module.exports = adminController;
