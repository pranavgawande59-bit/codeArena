/**
 * @file router.js
 * @description Client-side view router with route guards and active navigation styling.
 */

import { store } from './store.js';
import { ACTIONS } from './events.js';
import { toast } from '../components/Toast.js';

const PROTECTED_VIEWS = [
  'dashboard-view',
  'quiz-runner-view',
  'results-view',
  'problem-bank-view',
  'code-workspace-view',
  'leaderboard-view',
  'admin-view'
];

class ClientRouter {
  constructor() {
    this.viewHooks = new Map();
    this.historyStack = [];
    this.currentViewId = null;
  }

  registerViewHook(viewId, hookFn) {
    this.viewHooks.set(viewId, hookFn);
  }

  switchView(targetViewId, trackHistory = true) {
    let viewId = targetViewId;
    const state = store.getState();

    // Check authentication guard for protected portals
    if (PROTECTED_VIEWS.includes(viewId) && !state.isAuthenticated) {
      toast.warning('🔒 Please log in to access the CODE-ARENA platform.');
      viewId = 'auth-view';
    }

    if (trackHistory && this.currentViewId && this.currentViewId !== viewId) {
      this.historyStack.push(this.currentViewId);
      if (this.historyStack.length > 25) this.historyStack.shift();
    }
    this.currentViewId = viewId;

    // Hide all view sections
    const views = document.querySelectorAll('.view-section');
    views.forEach(v => v.classList.remove('active-view'));

    // Activate target view
    const targetElement = document.getElementById(viewId);
    if (targetElement) {
      targetElement.classList.add('active-view');
      store.dispatch(ACTIONS.NAVIGATE_TO, viewId);
    }

    // Update active state in sidebars
    this.updateSidebarActive(viewId);

    // Trigger registered view lifecycle hooks
    if (this.viewHooks.has(viewId)) {
      try {
        this.viewHooks.get(viewId)();
      } catch (err) {
        console.error(`[Router Hook Error for ${viewId}]`, err);
      }
    }

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  goBack(fallbackView = 'dashboard-view') {
    if (this.historyStack.length > 0) {
      const prevView = this.historyStack.pop();
      this.switchView(prevView, false);
    } else {
      this.switchView(fallbackView, false);
    }
  }

  updateSidebarActive(viewId) {
    const sidebarLinks = document.querySelectorAll('.sidebar-nav-item');
    sidebarLinks.forEach(link => {
      link.classList.remove('active');
      const targetAttr = link.getAttribute('data-view-target');
      if (targetAttr === viewId) {
        link.classList.add('active');
      }
    });
  }
}

export const router = new ClientRouter();
