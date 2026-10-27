/**
 * @file landingView.js
 * @description UI View controller for Landing Page (Screen 1).
 */

import { store } from '../core/store.js';

export const LandingView = {
  render() {
    const state = store.getState();
    const landingActions = document.getElementById('landing-auth-actions');

    if (landingActions) {
      if (state.isAuthenticated) {
        landingActions.innerHTML = `
          <button class="btn-primary btn-pill" onclick="switchView('dashboard-view')">Go to Dashboard →</button>
          <button class="btn-secondary" onclick="handleLogout()">Logout</button>
        `;
      } else {
        landingActions.innerHTML = `
          <button class="btn-secondary" onclick="switchView('auth-view')">Login</button>
          <button class="btn-primary btn-pill" onclick="switchView('auth-view')">Sign Up</button>
        `;
      }
    }
  }
};
