/**
 * @file leaderboardService.js
 * @description Leaderboard rankings and metric aggregation service.
 */

const leaderboardData = require('../data/leaderboardData');

let leaderboard = JSON.parse(JSON.stringify(leaderboardData));

const leaderboardService = {
  /**
   * Retrieves current leaderboard records.
   */
  getLeaderboard(timeframe = 'All Time') {
    return leaderboard;
  },

  /**
   * Updates user rank or score in leaderboard.
   */
  updateUserScore(userName, additionalPoints) {
    const user = leaderboard.find(u => u.name.includes(userName) || u.isCurrent);
    if (user) {
      user.score += additionalPoints;
      // Re-sort
      leaderboard.sort((a, b) => b.score - a.score);
      leaderboard.forEach((item, idx) => {
        item.rank = idx + 1;
        item.medal = item.rank === 1 ? '🥇' : (item.rank === 2 ? '🥈' : (item.rank === 3 ? '🥉' : String(item.rank)));
      });
    }
    return leaderboard;
  }
};

module.exports = leaderboardService;
