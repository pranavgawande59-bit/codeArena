/**
 * @file leaderboardApi.js
 * @description API service endpoints for platform leaderboard.
 */

import { apiClient } from './apiClient.js';

export const leaderboardApi = {
  getLeaderboard(timeframe = 'All Time') {
    return apiClient.get('/leaderboard', { timeframe });
  }
};
