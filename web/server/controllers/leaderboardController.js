/**
 * @file leaderboardController.js
 * @description HTTP controllers for leaderboard data and student rankings.
 */

const leaderboardService = require('../services/leaderboardService');
const { sendSuccess, sendError } = require('../middleware/errorHandler');

const leaderboardController = {
  getLeaderboard(req, res, query) {
    try {
      const records = leaderboardService.getLeaderboard(query.timeframe);
      sendSuccess(res, {
        total: records.length,
        leaderboard: records
      }, 200);
    } catch (err) {
      sendError(res, err, 500);
    }
  }
};

module.exports = leaderboardController;
