/**
 * @file leaderboardView.js
 * @description UI View controller for Platform Leaderboard (Screen 8).
 */

import { store } from '../core/store.js';
import { toast } from '../components/Toast.js';

export const LeaderboardView = {
  render() {
    const tbody = document.getElementById('leaderboard-tbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    const state = store.getState();

    state.leaderboard.forEach(user => {
      const tr = document.createElement('tr');
      if (user.isCurrent) {
        tr.className = 'highlighted-self-row';
      }

      const avatarBg = user.rank === 1 ? '#eab308' : (user.rank === 2 ? '#94a3b8' : (user.rank === 3 ? '#d97706' : '#2563eb'));

      tr.innerHTML = `
        <td><div class="rank-cell-content">${user.medal}</div></td>
        <td>
          <div class="user-cell-content">
            <div class="table-user-avatar" style="background: ${avatarBg};">${user.name.charAt(0)}</div>
            <span class="user-name-title">${user.name}</span>
          </div>
        </td>
        <td>${user.quizzes}</td>
        <td>${user.accuracy}</td>
        <td style="font-weight: 700; color: var(--primary-600);">${user.score}</td>
      `;
      tbody.appendChild(tr);
    });
  },

  setTab(btn, timeframe) {
    document.querySelectorAll('.lb-tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    toast.info(`📊 Filtered leaderboard by: ${timeframe}`);
  }
};
