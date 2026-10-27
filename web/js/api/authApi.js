/**
 * @file authApi.js
 * @description API service endpoints for authentication.
 */

import { apiClient } from './apiClient.js';

export const authApi = {
  login(credentials) {
    return apiClient.post('/auth/login', credentials);
  },

  logout() {
    return apiClient.post('/auth/logout');
  },

  getProfile() {
    return apiClient.get('/auth/profile');
  },

  updateProfile(profileData) {
    return apiClient.put('/auth/profile', profileData);
  }
};
