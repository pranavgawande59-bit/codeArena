/**
 * @file dashboardView.js
 * @description UI View controller for Student Learning & Performance Portal (Screen 3).
 */

import { store } from '../core/store.js';

export const DashboardView = {
  render() {
    const state = store.getState();
    const { currentUser } = state;

    // Greeting
    const firstName = currentUser.name.split(' ')[0];
    const greeting = document.getElementById('dashboard-greeting-msg');
    if (greeting) {
      greeting.textContent = `Welcome back, ${firstName}! Keep going, you're doing great!`;
    }

    // Top profile pill displays
    document.querySelectorAll('.user-name-display').forEach(el => el.textContent = `${firstName} ▾`);
    document.querySelectorAll('.user-name-full').forEach(el => el.textContent = currentUser.name);
    document.querySelectorAll('.user-avatar-pill').forEach(el => el.textContent = currentUser.avatarText);
  }
};
