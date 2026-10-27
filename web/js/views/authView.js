/**
 * @file authView.js
 * @description UI View controller for Student Authentication & Profiles (Screen 2).
 */

import { store } from '../core/store.js';
import { ACTIONS } from '../core/events.js';
import { toast } from '../components/Toast.js';
import { router } from '../core/router.js';

export const AuthView = {
  init() {
    const pwdToggle = document.getElementById('password-toggle-btn');
    const pwdInput = document.getElementById('login-password-input');
    if (pwdToggle && pwdInput) {
      pwdToggle.addEventListener('click', () => {
        pwdInput.type = pwdInput.type === 'password' ? 'text' : 'password';
      });
    }
  },

  handleLogin(e) {
    if (e) e.preventDefault();
    const userInput = document.getElementById('login-email-input')?.value?.trim();
    const displayName = userInput ? (userInput.includes('@') ? userInput.split('@')[0] : userInput) : 'Pranav Gawande';

    const parts = displayName.split(' ');
    const avatar = parts.map(p => p[0]).join('').substring(0, 2).toUpperCase() || 'ST';

    store.dispatch(ACTIONS.LOGIN_SUCCESS, {
      user: {
        name: displayName,
        avatarText: avatar
      }
    });

    this.updateUserUI();
    toast.success(`👋 Welcome back, ${displayName}! Logged in successfully.`);
    router.switchView('dashboard-view');
  },

  handleLogout() {
    store.dispatch(ACTIONS.LOGOUT);
    this.updateUserUI();
    toast.info('🚪 You have been logged out.');
    router.switchView('landing-view');
  },

  updateUserUI() {
    const state = store.getState();
    const { currentUser, isAuthenticated } = state;

    document.querySelectorAll('.user-name-full').forEach(el => el.textContent = currentUser.name);
    const firstName = currentUser.name.split(' ')[0];
    document.querySelectorAll('.user-name-display').forEach(el => el.textContent = `${firstName} ▾`);
    document.querySelectorAll('.user-avatar-pill').forEach(el => el.textContent = currentUser.avatarText);

    const greeting = document.getElementById('dashboard-greeting-msg');
    if (greeting) {
      greeting.textContent = `Welcome back, ${firstName}! Keep going, you're doing great!`;
    }

    const modalNameDisplay = document.getElementById('profile-modal-name-display');
    if (modalNameDisplay) {
      modalNameDisplay.textContent = currentUser.name;
    }
  }
};
