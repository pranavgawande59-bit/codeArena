/**
 * @file apiRouter.js
 * @description Master API route registry mapping endpoints to controllers.
 */

const Router = require('./router');
const env = require('../config/env');
const authController = require('../controllers/authController');
const quizController = require('../controllers/quizController');
const problemController = require('../controllers/problemController');
const leaderboardController = require('../controllers/leaderboardController');
const adminController = require('../controllers/adminController');
const { sendSuccess } = require('../middleware/errorHandler');

const router = new Router();
const prefix = env.API_PREFIX; // e.g. /api/v1

// Healthcheck endpoint
router.get(`${prefix}/health`, (req, res) => {
  sendSuccess(res, {
    status: 'UP',
    name: 'CODE-ARENA REST API',
    version: '1.0.0',
    environment: env.NODE_ENV,
    timestamp: new Date().toISOString()
  });
});

// Authentication routes
router.post(`${prefix}/auth/login`, (req, res, { params, query, body }) => authController.login(req, res, body));
router.post(`${prefix}/auth/logout`, (req, res) => authController.logout(req, res));
router.get(`${prefix}/auth/profile`, (req, res) => authController.getProfile(req, res));
router.put(`${prefix}/auth/profile`, (req, res, { params, query, body }) => authController.updateProfile(req, res, body));

// Quiz assessment routes
router.get(`${prefix}/quizzes`, (req, res, { params, query }) => quizController.getQuestions(req, res, query));
router.post(`${prefix}/quizzes/submit`, (req, res, { params, query, body }) => quizController.submitQuiz(req, res, body));

// Algorithmic Problem Bank routes
router.get(`${prefix}/problems`, (req, res, { params, query }) => problemController.getProblems(req, res, query));
router.get(`${prefix}/problems/:id`, (req, res, { params }) => problemController.getProblemById(req, res, params));
router.post(`${prefix}/problems/:id/run`, (req, res, { params, body }) => problemController.runCode(req, res, params, body));
router.post(`${prefix}/problems/:id/submit`, (req, res, { params, body }) => problemController.submitSolution(req, res, params, body));

// Leaderboard routes
router.get(`${prefix}/leaderboard`, (req, res, { params, query }) => leaderboardController.getLeaderboard(req, res, query));

// Admin CRUD routes
router.get(`${prefix}/admin/questions`, (req, res, { params, query }) => adminController.getQuestions(req, res, query));
router.post(`${prefix}/admin/questions`, (req, res, { params, body }) => adminController.createQuestion(req, res, body));
router.put(`${prefix}/admin/questions/:id`, (req, res, { params, body }) => adminController.updateQuestion(req, res, params, body));
router.delete(`${prefix}/admin/questions/:id`, (req, res, { params }) => adminController.deleteQuestion(req, res, params));

module.exports = router;
