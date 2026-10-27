/**
 * @file Modal.js
 * @description Universal modal dialog component with keyboard escape and backdrop click handling.
 */

class ModalManager {
  constructor() {
    this.activeModal = null;
    this.initGlobalListeners();
  }

  initGlobalListeners() {
    // Dismiss active modal on ESC key
    window.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && this.activeModal) {
        this.close(this.activeModal);
      }
    });

    // Close on clicking backdrop outside modal content
    window.addEventListener('click', (e) => {
      if (e.target && e.target.classList.contains('modal-backdrop')) {
        this.close(e.target.id);
      }
    });
  }

  open(modalId) {
    this.closeAllDropdowns();
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.classList.add('open');
      this.activeModal = modalId;
    }
  }

  close(modalId) {
    const targetId = modalId || this.activeModal;
    if (!targetId) return;
    const modal = document.getElementById(targetId);
    if (modal) {
      modal.classList.remove('open');
      if (this.activeModal === targetId) {
        this.activeModal = null;
      }
    }
  }

  closeAllDropdowns() {
    document.querySelectorAll('.user-dropdown-menu').forEach(m => m.classList.remove('show'));
  }
}

export const modalManager = new ModalManager();
