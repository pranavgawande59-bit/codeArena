/**
 * @file main.js
 * @description Master entry point wiring Store, Router, Views, and API Services together.
 */

import { store } from './core/store.js';
import { ACTIONS } from './core/events.js';
import { router } from './core/router.js';
import { toast } from './components/Toast.js';
import { modalManager } from './components/Modal.js';
import { codeEditorComponent } from './components/CodeEditor.js';
import { audioService } from './services/audioService.js';

import { LandingView } from './views/landingView.js';
import { AuthView } from './views/authView.js';
import { DashboardView } from './views/dashboardView.js';
import { QuizView } from './views/quizView.js';
import { ResultsView } from './views/resultsView.js';
import { ProblemBankView } from './views/problemBankView.js';
import { WorkspaceView } from './views/workspaceView.js';
import { LeaderboardView } from './views/leaderboardView.js';
import { AdminView } from './views/adminView.js';

import { problemApi } from './api/problemApi.js';
import { quizApi } from './api/quizApi.js';
import { leaderboardApi } from './api/leaderboardApi.js';
import { adminApi } from './api/adminApi.js';

// =============================================================================
// 1. DATA INITIALIZATION & API HYDRATION
// =============================================================================
async function initializeData() {
  try {
    // 1. Fetch Problems
    const problemsRes = await problemApi.getProblems().catch(() => null);
    if (problemsRes && problemsRes.problems) {
      store.dispatch('problems', { problems: problemsRes.problems });
    }

    // 2. Fetch Initial Quiz Questions
    const quizRes = await quizApi.getQuestions('Python').catch(() => null);
    if (quizRes && quizRes.questions) {
      store.dispatch(ACTIONS.START_QUIZ, { language: 'Python', questions: quizRes.questions });
    }

    // 3. Fetch Leaderboard
    const lbRes = await leaderboardApi.getLeaderboard().catch(() => null);
    if (lbRes && lbRes.leaderboard) {
      store.dispatch('leaderboard', { leaderboard: lbRes.leaderboard });
    }

    // 4. Fetch Admin Questions
    const adminRes = await adminApi.getQuestions().catch(() => null);
    if (adminRes && adminRes.questions) {
      store.dispatch('adminQuestions', { adminQuestions: adminRes.questions });
    }
  } catch (err) {
    console.warn('[Data Init Warning] Running with offline state fallback:', err.message);
  }

  // Render initial tables
  ProblemBankView.render();
  LeaderboardView.render();
  AdminView.render();
}

// =============================================================================
// 2. ROUTER HOOKS REGISTRATION
// =============================================================================
router.registerViewHook('landing-view', () => LandingView.render());
router.registerViewHook('auth-view', () => AuthView.updateUserUI());
router.registerViewHook('dashboard-view', () => DashboardView.render());
router.registerViewHook('quiz-runner-view', () => {
  QuizView.startTimer();
  QuizView.renderQuestion();
});
router.registerViewHook('results-view', () => ResultsView.render());
router.registerViewHook('problem-bank-view', () => ProblemBankView.render());
router.registerViewHook('code-workspace-view', () => codeEditorComponent.updateLineNumbers());
router.registerViewHook('leaderboard-view', () => LeaderboardView.render());
router.registerViewHook('admin-view', () => AdminView.render());

// =============================================================================
// 3. WINDOW BRIDGES FOR SEAMLESS HTML COMPATIBILITY
// =============================================================================
window.switchView = (viewId) => router.switchView(viewId);
window.handleLogin = (e) => AuthView.handleLogin(e);
window.handleGoogleLogin = () => AuthView.handleLogin();
window.handleLogout = () => AuthView.handleLogout();
window.handleGetStarted = () => {
  const state = store.getState();
  if (state.isAuthenticated) {
    router.switchView('dashboard-view');
  } else {
    router.switchView('auth-view');
  }
};

window.selectOption = (qIdx, optIdx) => QuizView.selectOption(qIdx, optIdx);
window.nextQuizQuestion = () => QuizView.nextQuestion();
window.prevQuizQuestion = () => QuizView.prevQuestion();
window.jumpToQuestion = (idx) => QuizView.jumpToQuestion(idx);
window.toggleMarkQuestion = (idx) => QuizView.toggleMark(idx);
window.clearSelectedOption = (idx) => QuizView.clearOption(idx);
window.exitQuizToDashboard = () => QuizView.exitQuiz();
window.goBack = (fallback) => router.goBack(fallback);
window.submitQuiz = () => QuizView.submitQuiz();
window.startQuizReview = () => QuizView.startReview();
window.retakeQuiz = () => QuizView.retakeQuiz();

window.startQuizForLanguage = (lang) => {
  store.dispatch(ACTIONS.START_QUIZ, { language: lang });
  toast.info(`🚀 Starting ${lang} assessment quiz!`);
  router.switchView('quiz-runner-view');
};

window.openProblemInWorkspace = (id) => WorkspaceView.openProblem(id);
window.runCodeTestCases = () => WorkspaceView.runTestCases();
window.submitCodeSolution = () => WorkspaceView.submitSolution();
window.clearConsole = () => WorkspaceView.clearConsole();
window.clearEditorScreen = () => WorkspaceView.clearEditor();
window.toggleConsoleDrawer = () => WorkspaceView.toggleConsoleDrawer();
window.setEditorLanguage = (lang) => WorkspaceView.setLanguage(lang);

window.setLeaderboardTab = (btn, timeframe) => LeaderboardView.setTab(btn, timeframe);
window.renderProblemBankTable = () => ProblemBankView.render();

// Modals
window.openLanguagesModal = () => modalManager.open('languages-modal');
window.closeLanguagesModal = () => modalManager.close('languages-modal');
window.openProblemBankWithLanguage = (lang) => {
  modalManager.close('languages-modal');
  router.switchView('problem-bank-view');
  const langFilter = document.getElementById('problem-lang-filter');
  if (langFilter) {
    langFilter.value = lang;
    ProblemBankView.render();
  }
  toast.info(`💻 Filtered Problem Bank for: ${lang}`);
};

window.openProfileModal = () => {
  modalManager.open('profile-modal');
  const state = store.getState();
  const nameInput = document.getElementById('edit-profile-name');
  const emailInput = document.getElementById('edit-profile-email');
  if (nameInput) nameInput.value = state.currentUser.name;
  if (emailInput) emailInput.value = state.currentUser.email || 'pranav.gawande@student.edu';
};
window.closeProfileModal = () => modalManager.close('profile-modal');

window.saveUserProfile = (e) => {
  if (e) e.preventDefault();
  const nameInput = document.getElementById('edit-profile-name');
  const emailInput = document.getElementById('edit-profile-email');
  const programInput = document.getElementById('edit-profile-program');

  if (nameInput && nameInput.value.trim()) {
    const newName = nameInput.value.trim();
    const parts = newName.split(' ');
    const initials = parts.map(p => p[0]).join('').substring(0, 2).toUpperCase() || 'ST';

    store.dispatch(ACTIONS.UPDATE_PROFILE, {
      name: newName,
      email: emailInput?.value?.trim() || 'pranav.gawande@student.edu',
      avatarText: initials
    });

    AuthView.updateUserUI();

    if (programInput) {
      const progLabel = document.getElementById('dropdown-program-label');
      if (progLabel) progLabel.textContent = `Student • ${programInput.value}`;
    }

    modalManager.close('profile-modal');
    toast.success('✨ Profile updated successfully!');
  }
};

window.openSettingsModal = () => modalManager.open('settings-modal');
window.closeSettingsModal = () => modalManager.close('settings-modal');

window.savePlatformSettings = (e) => {
  if (e) e.preventDefault();
  const timerSelect = document.getElementById('setting-quiz-timer');
  const soundToggle = document.getElementById('setting-sound-fx');

  const updates = {};
  if (timerSelect) {
    updates.quizTimerMinutes = parseInt(timerSelect.value, 10);
  }
  if (soundToggle) {
    updates.soundFx = soundToggle.checked;
    audioService.setEnabled(soundToggle.checked);
  }

  store.dispatch(ACTIONS.UPDATE_SETTINGS, updates);
  modalManager.close('settings-modal');
  toast.success('⚙️ Settings saved successfully!');
};

window.applyEditorTheme = (theme) => codeEditorComponent.applyTheme(theme);
window.applyEditorFontSize = (size) => codeEditorComponent.applyFontSize(size);

window.openAddQuestionModal = () => AdminView.openAddModal();
window.closeAddQuestionModal = () => AdminView.closeAddModal();
window.saveNewQuestion = (e) => AdminView.saveNewQuestion(e);
window.editAdminQuestion = (id) => AdminView.editQuestion(id);
window.deleteAdminQuestion = (id) => AdminView.deleteQuestion(id);

window.showToast = (msg) => toast.info(msg);

window.toggleUserMenu = (e) => {
  if (e) e.stopPropagation();
  modalManager.closeAllDropdowns();
  const menu = document.getElementById('user-dropdown-menu');
  if (menu) menu.classList.toggle('show');
};

window.toggleAdminMenu = (e) => {
  if (e) e.stopPropagation();
  modalManager.closeAllDropdowns();
  const menu = document.getElementById('admin-dropdown-menu');
  if (menu) menu.classList.toggle('show');
};

window.closeAllDropdowns = () => modalManager.closeAllDropdowns();

// Global click listener to close dropdowns
window.addEventListener('click', () => {
  modalManager.closeAllDropdowns();
});

// =============================================================================
// 4. DOM READY INITIALIZATION
// =============================================================================
document.addEventListener('DOMContentLoaded', () => {
  // Bind top navbar prototype navigation buttons
  document.querySelectorAll('.screen-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const targetView = btn.getAttribute('data-view');
      router.switchView(targetView);
    });
  });

  // Attach Code Editor interactive listeners
  codeEditorComponent.attachListeners();

  // Attach Auth listeners
  AuthView.init();

  // Initialize and hydrate data
  initializeData();

  // Initialize view based on URL hash or default to landing-view
  const hash = window.location.hash.replace('#', '');
  if (hash && document.getElementById(hash)) {
    router.switchView(hash);
  } else {
    router.switchView('landing-view');
  }
});
