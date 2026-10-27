/**
 * @file quizController.js
 * @description HTTP controllers for assessment quizzes and result grading.
 */

const quizService = require('../services/quizService');
const { sendSuccess, sendError } = require('../middleware/errorHandler');

const quizController = {
  getQuestions(req, res, query) {
    try {
      const language = query.language || 'Python';
      const difficulty = query.difficulty || null;
      const limit = parseInt(query.limit || '10', 10);
      const questions = quizService.getQuestions(language, difficulty, limit);
      sendSuccess(res, {
        language,
        total: questions.length,
        questions
      }, 200);
    } catch (err) {
      sendError(res, err, 500);
    }
  },

  submitQuiz(req, res, body) {
    try {
      if (!body) throw new Error('Submission payload is required');
      const scorecard = quizService.evaluateAttempt(body);
      sendSuccess(res, scorecard, 200);
    } catch (err) {
      sendError(res, err, 400);
    }
  }
};

module.exports = quizController;
