/**
 * @file adminView.js
 * @description UI View controller for Academic Administration Portal (Screen 9).
 */

import { store } from '../core/store.js';
import { toast } from '../components/Toast.js';
import { modalManager } from '../components/Modal.js';

export const AdminView = {
  render() {
    const tbody = document.getElementById('admin-questions-tbody');
    if (!tbody) return;

    const state = store.getState();
    const searchVal = (document.getElementById('admin-search-input')?.value || '').toLowerCase().trim();
    const topicFilter = document.getElementById('admin-topic-filter')?.value || 'All Topics';
    const diffFilter = document.getElementById('admin-diff-filter')?.value || 'All Difficulty';

    tbody.innerHTML = '';

    const filtered = state.adminQuestions.filter(q => {
      const matchSearch = !searchVal || q.text.toLowerCase().includes(searchVal) || q.id.toLowerCase().includes(searchVal) || q.topic.toLowerCase().includes(searchVal);
      const matchTopic = topicFilter === 'All Topics' || q.topic === topicFilter;
      const matchDiff = diffFilter === 'All Difficulty' || q.difficulty === diffFilter;
      return matchSearch && matchTopic && matchDiff;
    });

    filtered.forEach(q => {
      const tr = document.createElement('tr');
      const diffBadgeClass = q.difficulty.toLowerCase() === 'easy' ? 'badge-easy' : (q.difficulty.toLowerCase() === 'medium' ? 'badge-medium' : 'badge-hard');

      tr.innerHTML = `
        <td style="font-family: var(--font-mono); font-weight: 600;">${q.id}</td>
        <td style="font-weight: 500;">${q.text}</td>
        <td>${q.topic}</td>
        <td><span class="badge-pill ${diffBadgeClass}">${q.difficulty}</span></td>
        <td>
          <div class="admin-action-btn-group">
            <button class="btn-icon-action" title="Edit Question" onclick="editAdminQuestion('${q.id}')">
              <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"></path><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path></svg>
            </button>
            <button class="btn-icon-action btn-icon-delete" title="Delete Question" onclick="deleteAdminQuestion('${q.id}')">
              <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
            </button>
          </div>
        </td>
      `;
      tbody.appendChild(tr);
    });
  },

  openAddModal() {
    modalManager.open('add-question-modal');
  },

  closeAddModal() {
    modalManager.close('add-question-modal');
  },

  saveNewQuestion(e) {
    if (e) e.preventDefault();
    const textInput = document.getElementById('modal-q-text');
    const topicInput = document.getElementById('modal-q-topic');
    const diffInput = document.getElementById('modal-q-diff');

    if (!textInput || !textInput.value.trim()) {
      toast.error('Please enter question text');
      return;
    }

    const state = store.getState();
    const nextIdNum = state.adminQuestions.length + 1;
    const newId = `Q00${nextIdNum}`;

    const newQuestion = {
      id: newId,
      text: textInput.value.trim(),
      topic: topicInput?.value || 'General',
      difficulty: diffInput?.value || 'Easy'
    };

    store.dispatch('adminQuestions', [newQuestion, ...state.adminQuestions]);

    textInput.value = '';
    this.closeAddModal();
    this.render();
    toast.success(`✨ Question ${newId} created successfully!`);
  },

  editQuestion(id) {
    const state = store.getState();
    const q = state.adminQuestions.find(item => item.id === id);
    if (!q) return;

    const newText = prompt(`Edit Question ${id}:`, q.text);
    if (newText && newText.trim()) {
      q.text = newText.trim();
      store.dispatch('adminQuestions', [...state.adminQuestions]);
      this.render();
      toast.success(`✏️ Question ${id} updated.`);
    }
  },

  deleteQuestion(id) {
    if (confirm(`Are you sure you want to delete question ${id}?`)) {
      const state = store.getState();
      const updated = state.adminQuestions.filter(q => q.id !== id);
      store.dispatch('adminQuestions', updated);
      this.render();
      toast.info(`🗑️ Question ${id} deleted.`);
    }
  }
};
