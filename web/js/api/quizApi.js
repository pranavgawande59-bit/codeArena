/**
 * @file quizApi.js
 * @description API service endpoints for assessment quizzes.
 */

import { apiClient } from './apiClient.js';

export const quizApi = {
  getQuestions(language = 'Python', difficulty = null, limit = 10) {
    const query = { language, limit };
    if (difficulty) query.difficulty = difficulty;
    return apiClient.get('/quizzes', query);
  },

  submitQuiz(submissionData) {
    return apiClient.post('/quizzes/submit', submissionData);
  }
};
