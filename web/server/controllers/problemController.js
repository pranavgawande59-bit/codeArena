/**
 * @file problemController.js
 * @description HTTP controllers for Algorithmic Problem Bank endpoints.
 */

const problemService = require('../services/problemService');
const authService = require('../services/authService');
const { sendSuccess, sendError } = require('../middleware/errorHandler');

const problemController = {
  getProblems(req, res, query) {
    try {
      const list = problemService.getProblems(query);
      sendSuccess(res, {
        total: list.length,
        problems: list
      }, 200);
    } catch (err) {
      sendError(res, err, 500);
    }
  },

  getProblemById(req, res, params) {
    try {
      const problem = problemService.getProblemById(params.id);
      sendSuccess(res, problem, 200);
    } catch (err) {
      sendError(res, err, 404);
    }
  },

  runCode(req, res, params, body) {
    try {
      const { language = 'python', code = '' } = body || {};
      const result = problemService.runTestCases(params.id, language, code);
      sendSuccess(res, result, 200);
    } catch (err) {
      sendError(res, err, 400);
    }
  },

  submitSolution(req, res, params, body) {
    try {
      const { language = 'python', code = '' } = body || {};
      const result = problemService.submitSolution(params.id, language, code);
      if (result.isAccepted) {
        authService.addScore(50);
      }
      sendSuccess(res, result, 200);
    } catch (err) {
      sendError(res, err, 400);
    }
  }
};

module.exports = problemController;
