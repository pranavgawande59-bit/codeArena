/**
 * @file adminApi.js
 * @description API service endpoints for administrator operations.
 */

import { apiClient } from './apiClient.js';

export const adminApi = {
  getQuestions(filters = {}) {
    return apiClient.get('/admin/questions', filters);
  },

  createQuestion(questionData) {
    return apiClient.post('/admin/questions', questionData);
  },

  updateQuestion(id, updates) {
    return apiClient.put(`/admin/questions/${id}`, updates);
  },

  deleteQuestion(id) {
    return apiClient.delete(`/admin/questions/${id}`);
  }
};
