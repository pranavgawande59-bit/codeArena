/**
 * @file problemBankView.js
 * @description UI View controller for Algorithmic Problem Bank (Screen 5).
 */

import { store } from '../core/store.js';
import { WorkspaceView } from './workspaceView.js';

export const ProblemBankView = {
  render() {
    const tbody = document.getElementById('problem-bank-tbody');
    if (!tbody) return;

    const state = store.getState();
    const searchVal = (document.getElementById('problem-search-input')?.value || '').toLowerCase().trim();
    const langFilter = document.getElementById('problem-lang-filter')?.value || 'All Languages';
    const topicFilter = document.getElementById('problem-topic-filter')?.value || 'All Topics';
    const diffFilter = document.getElementById('problem-diff-filter')?.value || 'All Difficulties';

    tbody.innerHTML = '';

    const filtered = state.problems.filter(p => {
      const matchSearch = !searchVal || p.title.toLowerCase().includes(searchVal) || p.topic.toLowerCase().includes(searchVal);
      const matchLang = langFilter === 'All Languages' || (p.languages && p.languages.includes(langFilter));
      const matchTopic = topicFilter === 'All Topics' || p.topic === topicFilter;
      const matchDiff = diffFilter === 'All Difficulties' || p.difficulty === diffFilter;
      return matchSearch && matchLang && matchTopic && matchDiff;
    });

    if (filtered.length === 0) {
      const emptyTr = document.createElement('tr');
      emptyTr.innerHTML = `
        <td colspan="4" style="text-align: center; padding: 32px 16px; color: var(--slate-500);">
          <div style="font-size: 1.5rem; margin-bottom: 6px;">🔍</div>
          <strong>No coding problems found matching your filters.</strong>
          <div style="font-size: 0.85rem; margin-top: 4px; color: var(--slate-400);">Try adjusting your search keyword, topic, or difficulty.</div>
        </td>
      `;
      tbody.appendChild(emptyTr);
      return;
    }

    filtered.forEach(p => {
      const tr = document.createElement('tr');
      tr.className = 'problem-link-row';
      tr.onclick = () => WorkspaceView.openProblem(p.id);

      const diffBadgeClass = p.difficulty.toLowerCase() === 'easy' ? 'badge-easy' : (p.difficulty.toLowerCase() === 'medium' ? 'badge-medium' : 'badge-hard');
      const statusBadgeClass = p.status === 'Solved' ? 'badge-solved' : 'badge-not-attempted';

      tr.innerHTML = `
        <td class="problem-title-cell">
          <strong>${p.title}</strong>
        </td>
        <td><span class="topic-tag-cell">${p.topic}</span></td>
        <td><span class="badge-pill ${diffBadgeClass}">${p.difficulty}</span></td>
        <td><span class="badge-pill ${statusBadgeClass}">${p.status}</span></td>
      `;
      tbody.appendChild(tr);
    });
  }
};
