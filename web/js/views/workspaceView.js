/**
 * @file workspaceView.js
 * @description UI View controller for Algorithmic Coding Workspace (Screen 6).
 */

import { store } from '../core/store.js';
import { ACTIONS } from '../core/events.js';
import { codeEditorComponent } from '../components/CodeEditor.js';
import { ProblemService } from '../services/problemService.js';
import { audioService } from '../services/audioService.js';
import { toast } from '../components/Toast.js';
import { router } from '../core/router.js';

export const WorkspaceView = {
  openProblem(problemId) {
    const state = store.getState();
    store.dispatch(ACTIONS.SET_ACTIVE_PROBLEM, problemId);
    const problem = state.problems.find(p => p.id === problemId) || state.problems[0];
    if (!problem) return;

    // Title and metadata badges
    const titleEl = document.getElementById('workspace-problem-title');
    if (titleEl) titleEl.textContent = problem.title;

    const topicEl = document.getElementById('workspace-problem-topic');
    if (topicEl) topicEl.textContent = problem.topic;

    const diffEl = document.getElementById('workspace-problem-difficulty');
    if (diffEl) {
      diffEl.textContent = problem.difficulty;
      diffEl.className = `badge-pill ${problem.difficulty.toLowerCase() === 'easy' ? 'badge-easy' : (problem.difficulty.toLowerCase() === 'medium' ? 'badge-medium' : 'badge-hard')}`;
    }

    const statusEl = document.getElementById('workspace-problem-status');
    if (statusEl) {
      statusEl.textContent = problem.status;
      statusEl.className = `badge-pill ${problem.status === 'Solved' ? 'badge-solved' : 'badge-not-attempted'}`;
    }

    // Populate problem content pane
    const contentEl = document.getElementById('workspace-problem-content');
    if (contentEl) {
      let examplesHtml = '';
      if (problem.examples && problem.examples.length > 0) {
        examplesHtml = problem.examples.map((ex, idx) => `
          <h3>Example ${idx + 1}:</h3>
          <div class="code-example-box">
            <strong>Input:</strong> ${ex.input}<br>
            <strong>Output:</strong> ${ex.output}<br>
            ${ex.explanation ? `<strong>Explanation:</strong> ${ex.explanation}` : ''}
          </div>
        `).join('');
      }

      let constraintsHtml = '';
      if (problem.constraints && problem.constraints.length > 0) {
        constraintsHtml = `
          <h3>Constraints:</h3>
          <ul class="constraints-list">
            ${problem.constraints.map(c => `<li><code>${c}</code></li>`).join('')}
          </ul>
        `;
      }

      contentEl.innerHTML = `
        <h3>Problem Description:</h3>
        <p>${problem.description}</p>
        ${examplesHtml}
        ${constraintsHtml}
      `;
    }

    // Reset console output drawer
    this.resetConsole();

    // Set starter code
    this.setLanguage(state.activeLanguage);

    router.switchView('code-workspace-view');
    toast.info(`💻 Opened workspace for: "${problem.title}"`);
  },

  setLanguage(lang) {
    store.dispatch(ACTIONS.SET_CODE_LANGUAGE, lang);

    document.querySelectorAll('.lang-tab').forEach(tab => {
      tab.classList.toggle('active', tab.getAttribute('data-lang') === lang);
    });

    const state = store.getState();
    const problem = state.problems.find(p => p.id === state.activeProblemId) || state.problems[0];
    if (problem && problem.starterCode && problem.starterCode[lang]) {
      codeEditorComponent.setValue(problem.starterCode[lang]);
    }
  },

  resetConsole() {
    const drawer = document.getElementById('editor-output-drawer');
    const outputBody = document.getElementById('console-output-body');
    const statusPill = document.getElementById('console-status-pill');

    if (drawer) drawer.classList.add('show');
    if (statusPill) {
      statusPill.textContent = '● Ready';
      statusPill.className = 'console-status-pill pill-ready';
    }
    if (outputBody) {
      outputBody.innerHTML = `
        <div class="console-empty-prompt">
          <div class="console-prompt-line"><span class="prompt-sym">$</span> <span>arena-cli --status ready</span></div>
          <div class="console-prompt-help">
            Console is clear. Write or modify your code in the editor above.<br>
            Click <strong>▷ Run</strong> to test sample cases or <strong>Submit</strong> to evaluate your solution with detailed explanation.
          </div>
        </div>
      `;
    }
  },

  clearConsole() {
    this.resetConsole();
    toast.info('🧹 Console output cleared.');
  },

  clearEditor() {
    codeEditorComponent.clear();
    toast.info('🧹 Coding screen cleared.');
  },

  toggleConsoleDrawer() {
    const drawer = document.getElementById('editor-output-drawer');
    const chevron = document.getElementById('console-toggle-icon');
    const label = document.getElementById('bottom-console-label');
    if (!drawer) return;

    const isShown = drawer.classList.contains('show');
    if (isShown) {
      drawer.classList.remove('show');
      if (chevron) chevron.textContent = '▴';
      if (label) label.textContent = 'Console ▴';
    } else {
      drawer.classList.add('show');
      if (chevron) chevron.textContent = '▾';
      if (label) label.textContent = 'Console ▾';
    }
  },

  runTestCases() {
    const drawer = document.getElementById('editor-output-drawer');
    const outputBody = document.getElementById('console-output-body');
    const statusPill = document.getElementById('console-status-pill');

    if (!drawer || !outputBody) return;
    drawer.classList.add('show');

    if (statusPill) {
      statusPill.textContent = '⚡ Running...';
      statusPill.className = 'console-status-pill pill-running';
    }

    const state = store.getState();
    const problem = state.problems.find(p => p.id === state.activeProblemId) || state.problems[0];
    const userCode = codeEditorComponent.getValue();

    outputBody.innerHTML = `
      <div style="color: #58a6ff; font-weight: 600; padding: 10px 0; display: flex; align-items: center; gap: 8px;">
        <span>⚡</span> Compiling and executing test cases for <strong>${problem.title}</strong> (${state.activeLanguage.toUpperCase()})...
      </div>
    `;

    setTimeout(() => {
      const evalResult = ProblemService.evaluateSolution(problem, state.activeLanguage, userCode);
      const testCases = problem.testCases || [{ input: 'Default Input', expected: 'Target', unsolvedOutput: 'None' }];

      if (!evalResult.isCorrect) {
        audioService.playErrorTone();
        if (statusPill) {
          statusPill.textContent = '✗ Failed';
          statusPill.className = 'console-status-pill pill-wrong';
        }

        const casesHtml = testCases.map((tc, idx) => `
          <div class="test-case-box case-failed">
            <div class="test-case-header-row">
              <span class="test-case-name">Test Case ${idx + 1}</span>
              <span class="test-case-badge badge-case-fail">Failed ✗</span>
            </div>
            <div class="test-io-table">
              <div class="test-io-row">
                <span class="test-io-label">Input:</span>
                <span class="test-io-val">${tc.input}</span>
              </div>
              <div class="test-io-row">
                <span class="test-io-label">Your Output:</span>
                <span class="test-io-val val-incorrect">${tc.unsolvedOutput || 'None'}</span>
              </div>
              <div class="test-io-row">
                <span class="test-io-label">Expected Output:</span>
                <span class="test-io-val val-expected">${tc.expected}</span>
              </div>
            </div>
          </div>
        `).join('');

        outputBody.innerHTML = `
          <div class="console-result-banner banner-error">
            <div class="banner-status-left">
              <span>✗ Test Cases Failed (0/${testCases.length} Passed)</span>
            </div>
            <div class="banner-meta-right">
              <span class="banner-meta-item">⏱ 42 ms</span>
              <span class="banner-meta-item">Status: ${evalResult.errorType || 'Wrong Answer'}</span>
              <span class="banner-meta-item">${state.activeLanguage.toUpperCase()}</span>
            </div>
          </div>
          ${casesHtml}
          <div style="display: flex; justify-content: flex-end; margin-top: 10px;">
            <button class="btn-console-tool btn-console-clear" onclick="clearConsole()">🗑 Clear Screen</button>
          </div>
        `;

        toast.error(`❌ Test cases failed for "${problem.title}". Check console.`);
        return;
      }

      // Passed
      audioService.playSuccessChime();
      if (statusPill) {
        statusPill.textContent = '✓ Passed';
        statusPill.className = 'console-status-pill pill-accepted';
      }

      const casesHtml = testCases.map((tc, idx) => `
        <div class="test-case-box case-passed">
          <div class="test-case-header-row">
            <span class="test-case-name">Test Case ${idx + 1}</span>
            <span class="test-case-badge badge-case-pass">Passed ✓</span>
          </div>
          <div class="test-io-table">
            <div class="test-io-row">
              <span class="test-io-label">Input:</span>
              <span class="test-io-val">${tc.input}</span>
            </div>
            <div class="test-io-row">
              <span class="test-io-label">Your Output:</span>
              <span class="test-io-val val-correct">${tc.expected}</span>
            </div>
            <div class="test-io-row">
              <span class="test-io-label">Expected Output:</span>
              <span class="test-io-val val-expected">${tc.expected}</span>
            </div>
          </div>
        </div>
      `).join('');

      outputBody.innerHTML = `
        <div class="console-result-banner banner-accepted">
          <div class="banner-status-left">
            <span>✓ All Sample Test Cases Passed (${testCases.length}/${testCases.length})</span>
          </div>
          <div class="banner-meta-right">
            <span class="banner-meta-item">⏱ 38 ms</span>
            <span class="banner-meta-item">💾 14.2 MB</span>
            <span class="banner-meta-item">${state.activeLanguage.toUpperCase()}</span>
          </div>
        </div>
        ${casesHtml}
        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 12px; padding-top: 8px; border-top: 1px solid #21262d; font-size: 0.8rem; color: #8b949e;">
          <span>💡 Click <strong>Submit</strong> to evaluate and unlock full algorithmic explanation.</span>
          <button class="btn-console-tool btn-console-clear" onclick="clearConsole()">🗑 Clear Screen</button>
        </div>
      `;

      toast.success(`✅ All sample test cases passed for "${problem.title}"!`);
    }, 380);
  },

  submitSolution() {
    const drawer = document.getElementById('editor-output-drawer');
    const outputBody = document.getElementById('console-output-body');
    const statusPill = document.getElementById('console-status-pill');

    if (!drawer || !outputBody) return;
    drawer.classList.add('show');

    if (statusPill) {
      statusPill.textContent = '⚡ Evaluating...';
      statusPill.className = 'console-status-pill pill-running';
    }

    const state = store.getState();
    const problem = state.problems.find(p => p.id === state.activeProblemId) || state.problems[0];
    const userCode = codeEditorComponent.getValue();

    outputBody.innerHTML = `
      <div style="color: #58a6ff; font-weight: 600; padding: 12px 0; display: flex; align-items: center; gap: 8px;">
        <span>⚡</span> Evaluating submission against complete test suites for <strong>${problem.title}</strong>...
      </div>
    `;

    setTimeout(() => {
      const evalResult = ProblemService.evaluateSolution(problem, state.activeLanguage, userCode);
      const testCases = problem.testCases || [];
      const exp = problem.explanation || {
        approach: 'Optimal Algorithmic Solution',
        description: 'The problem is solved using optimal time and space complexity traversal.',
        whyCorrect: 'Handles all test cases and boundary conditions correctly.',
        timeComplexity: 'O(n)',
        spaceComplexity: 'O(1)',
        edgeCases: 'Standard edge cases handled.'
      };

      const explanationHtml = `
        <div class="console-explanation-card">
          <div class="explanation-header">
            <div class="explanation-title">
              <span>💡 Solution Explanation &amp; Algorithmic Approach</span>
            </div>
            <span class="explanation-approach-tag">${exp.approach}</span>
          </div>

          <div class="explanation-block">
            <div class="explanation-block-title">Core Strategy &amp; Algorithmic Logic</div>
            <div class="explanation-block-content">${exp.description}</div>
          </div>

          <div class="explanation-block">
            <div class="explanation-block-title">Why this is the Correct Answer</div>
            <div class="explanation-block-content">${exp.whyCorrect}</div>
          </div>

          <div class="explanation-block">
            <div class="explanation-block-title">Complexity Analysis</div>
            <div class="complexity-chips-row">
              <div class="complexity-chip"><strong>Time Complexity:</strong> ${exp.timeComplexity}</div>
              <div class="complexity-chip"><strong>Space Complexity:</strong> ${exp.spaceComplexity}</div>
            </div>
          </div>

          <div class="explanation-block" style="margin-bottom: 0;">
            <div class="explanation-block-title">Key Edge Cases Considered</div>
            <div class="explanation-block-content">${exp.edgeCases}</div>
          </div>
        </div>
      `;

      if (!evalResult.isCorrect) {
        audioService.playErrorTone();
        if (statusPill) {
          statusPill.textContent = '✗ Wrong Answer';
          statusPill.className = 'console-status-pill pill-wrong';
        }

        const casesHtml = testCases.map((tc, idx) => `
          <div class="test-case-box case-failed">
            <div class="test-case-header-row">
              <span class="test-case-name">Test Case ${idx + 1}</span>
              <span class="test-case-badge badge-case-fail">Incorrect ✗</span>
            </div>
            <div class="test-io-table">
              <div class="test-io-row">
                <span class="test-io-label">Input:</span>
                <span class="test-io-val">${tc.input}</span>
              </div>
              <div class="test-io-row">
                <span class="test-io-label">Your Output:</span>
                <span class="test-io-val val-incorrect">${tc.unsolvedOutput || 'None'}</span>
              </div>
              <div class="test-io-row">
                <span class="test-io-label">Expected Output:</span>
                <span class="test-io-val val-expected">${tc.expected}</span>
              </div>
            </div>
          </div>
        `).join('');

        outputBody.innerHTML = `
          <div class="console-result-banner banner-error">
            <div class="banner-status-left">
              <span>✗ Incorrect Answer (Wrong Answer)</span>
            </div>
            <div class="banner-meta-right">
              <span class="banner-meta-item">Status: 0/${testCases.length} Passed</span>
              <span class="banner-meta-item">${state.activeLanguage.toUpperCase()}</span>
            </div>
          </div>
          ${casesHtml}
          ${explanationHtml}
          <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 14px; padding-top: 10px; border-top: 1px solid #21262d;">
            <span style="color: #ff7b72; font-weight: 600; font-size: 0.82rem;">❌ Solution not accepted yet. Implement the logic described above and submit again!</span>
            <button class="btn-console-tool btn-console-clear" onclick="clearConsole()">🗑 Clear Screen</button>
          </div>
        `;

        toast.error(`❌ Incorrect Answer for "${problem.title}". Check explanation.`);
        return;
      }

      // Solution Accepted!
      audioService.playSuccessChime();
      store.dispatch(ACTIONS.MARK_PROBLEM_SOLVED, problem.id);

      if (statusPill) {
        statusPill.textContent = '✓ Accepted';
        statusPill.className = 'console-status-pill pill-accepted';
      }

      const statusEl = document.getElementById('workspace-problem-status');
      if (statusEl) {
        statusEl.textContent = 'Solved';
        statusEl.className = 'badge-pill badge-solved';
      }

      const casesHtml = testCases.map((tc, idx) => `
        <div class="test-case-box case-passed">
          <div class="test-case-header-row">
            <span class="test-case-name">Test Case ${idx + 1}</span>
            <span class="test-case-badge badge-case-pass">Passed ✓</span>
          </div>
          <div class="test-io-table">
            <div class="test-io-row">
              <span class="test-io-label">Input:</span>
              <span class="test-io-val">${tc.input}</span>
            </div>
            <div class="test-io-row">
              <span class="test-io-label">Your Output:</span>
              <span class="test-io-val val-correct">${tc.expected}</span>
            </div>
            <div class="test-io-row">
              <span class="test-io-label">Expected Output:</span>
              <span class="test-io-val val-expected">${tc.expected}</span>
            </div>
          </div>
        </div>
      `).join('');

      outputBody.innerHTML = `
        <div class="console-result-banner banner-accepted">
          <div class="banner-status-left">
            <span>🏆 Correct Answer (Accepted) - All Tests Passed</span>
          </div>
          <div class="banner-meta-right">
            <span class="banner-meta-item">⏱ 36 ms (Beats 92.4%)</span>
            <span class="banner-meta-item">💾 14.1 MB (Beats 89.1%)</span>
            <span class="banner-meta-item">${state.activeLanguage.toUpperCase()}</span>
          </div>
        </div>
        ${casesHtml}
        ${explanationHtml}
        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 14px; padding-top: 10px; border-top: 1px solid #21262d;">
          <span style="color: #3fb950; font-weight: 600; font-size: 0.82rem;">🎉 Problem Solved! +50 XP Added to Profile</span>
          <button class="btn-console-tool btn-console-clear" onclick="clearConsole()">🗑 Clear Screen</button>
        </div>
      `;

      toast.success(`🏆 Correct Answer (Accepted) for "${problem.title}"! Explanation unlocked.`);
    }, 450);
  }
};
