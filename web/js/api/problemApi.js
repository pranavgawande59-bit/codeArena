/**
 * @file problemApi.js
 * @description API service endpoints for Algorithmic Problem Bank and code execution.
 */

import { apiClient } from './apiClient.js';

export const problemApi = {
  getProblems(filters = {}) {
    return apiClient.get('/problems', filters);
  },

  getProblemById(id) {
    return apiClient.get(`/problems/${id}`);
  },

  runCode(problemId, language, code) {
    return apiClient.post(`/problems/${problemId}/run`, { language, code });
  },

  submitSolution(problemId, language, code) {
    return apiClient.post(`/problems/${problemId}/submit`, { language, code });
  }
};
