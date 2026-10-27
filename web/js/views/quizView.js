/**
 * @file quizView.js
 * @description UI View controller for MCQ Quiz Assessment Engine (Screen 4 & Screen 7 Review Mode).
 */

import { store } from '../core/store.js';
import { ACTIONS } from '../core/events.js';
import { QuizService } from '../services/quizService.js';
import { toast } from '../components/Toast.js';
import { router } from '../core/router.js';

let timerInterval = null;

function escapeHtml(str) {
  if (str === null || str === undefined) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

export const QuizView = {
  startTimer() {
    if (timerInterval) clearInterval(timerInterval);

    const timerEl = document.getElementById('quiz-timer-display');
    const updateDisplay = () => {
      const state = store.getState();
      if (timerEl) {
        timerEl.textContent = QuizService.formatTime(state.quiz.timerSeconds);
      }
    };

    updateDisplay();

    timerInterval = setInterval(() => {
      const state = store.getState();
      if (state.quiz.timerSeconds > 0) {
        store.dispatch(ACTIONS.TICK_TIMER);
        updateDisplay();
      } else {
        clearInterval(timerInterval);
        toast.warning('⏰ Time is up! Submitting quiz answers...');
        this.submitQuiz();
      }
    }, 1000);
  },

  stopTimer() {
    if (timerInterval) {
      clearInterval(timerInterval);
      timerInterval = null;
    }
  },

  renderQuestion() {
    const state = store.getState();
    const { quiz } = state;
    const qIndex = quiz.currentQuestionIndex;
    const question = quiz.questions[qIndex];
    if (!question) return;

    // Header progress
    const progressText = document.getElementById('quiz-progress-text');
    if (progressText) {
      progressText.textContent = quiz.isReviewMode 
        ? `Reviewing Question ${qIndex + 1} of ${quiz.totalQuestions}`
        : `Question ${qIndex + 1} of ${quiz.totalQuestions}`;
    }

    const progressBar = document.getElementById('quiz-progress-bar-fill');
    if (progressBar) {
      const percent = ((qIndex + 1) / quiz.totalQuestions) * 100;
      progressBar.style.width = `${percent}%`;
    }

    // Question tag & statement
    const tagEl = document.getElementById('quiz-question-tag');
    if (tagEl) {
      tagEl.textContent = quiz.isReviewMode 
        ? `${question.topic} • 📖 Review Mode`
        : `${question.topic} • Multiple Choice`;
    }

    const stmtEl = document.getElementById('quiz-question-statement');
    if (stmtEl) stmtEl.textContent = question.text;

    // Options list
    const optionsContainer = document.getElementById('quiz-options-container');
    if (optionsContainer) {
      optionsContainer.innerHTML = '';
      const selectedOpt = quiz.answers[qIndex];

      question.options.forEach((optText, optIdx) => {
        const isSelected = selectedOpt === optIdx;
        const optDiv = document.createElement('div');

        // Radio circle indicator
        const radioDiv = document.createElement('div');
        radioDiv.className = 'option-radio';

        // Option text element - textContent safely treats strings like "<class 'tuple'>" as literal text without parsing as HTML tags!
        const textDiv = document.createElement('div');
        textDiv.className = 'option-text';
        textDiv.textContent = optText;

        optDiv.appendChild(radioDiv);
        optDiv.appendChild(textDiv);

        if (quiz.isReviewMode) {
          let reviewClass = '';
          if (optIdx === selectedOpt && selectedOpt === question.correct) {
            reviewClass = 'review-correct';
            const badge = document.createElement('span');
            badge.className = 'review-badge-user-correct';
            badge.textContent = '✓ Your Selected Answer (Correct)';
            optDiv.appendChild(badge);
          } else if (optIdx === selectedOpt && selectedOpt !== question.correct) {
            reviewClass = 'review-incorrect';
            const badge = document.createElement('span');
            badge.className = 'review-badge-user-incorrect';
            badge.textContent = '✗ Your Selected Answer (Incorrect)';
            optDiv.appendChild(badge);
          } else if (optIdx === question.correct) {
            reviewClass = 'review-correct-target';
            const badge = document.createElement('span');
            badge.className = 'review-badge-correct-target';
            badge.textContent = '✓ Correct Answer';
            optDiv.appendChild(badge);
          }

          optDiv.className = `option-item ${reviewClass}`;
        } else {
          optDiv.className = `option-item ${isSelected ? 'selected' : ''}`;
          optDiv.onclick = () => this.selectOption(qIndex, optIdx);
        }

        optionsContainer.appendChild(optDiv);
      });
    }

    // Status pills (Attempted / Marked)
    const isAnswered = quiz.answers.hasOwnProperty(qIndex) && quiz.answers[qIndex] !== undefined;
    const isMarked = !!(quiz.marked && quiz.marked[qIndex]);

    const attemptedPill = document.getElementById('quiz-attempted-pill');
    if (attemptedPill) {
      if (isAnswered) {
        attemptedPill.className = 'quiz-status-pill attempted';
        attemptedPill.textContent = '✓ Attempted';
      } else {
        attemptedPill.className = 'quiz-status-pill unattempted';
        attemptedPill.textContent = 'Not Attempted';
      }
    }

    const markedPill = document.getElementById('quiz-marked-pill');
    if (markedPill) {
      markedPill.style.display = isMarked ? 'inline-flex' : 'none';
    }

    // Explanation Box for Review Mode
    const explBox = document.getElementById('quiz-explanation-box');
    if (explBox) {
      if (quiz.isReviewMode) {
        explBox.style.display = 'block';
        const userAnsIdx = quiz.answers[qIndex];
        const isUserCorrect = userAnsIdx === question.correct;
        const statusBadge = userAnsIdx !== undefined
          ? (isUserCorrect ? '<span style="color: #16a34a; font-weight: 700;">✓ Correct</span>' : '<span style="color: #dc2626; font-weight: 700;">✗ Incorrect</span>')
          : '<span style="color: #64748b; font-weight: 700;">Skipped</span>';

        const correctOptText = question.options[question.correct] || '';

        explBox.innerHTML = `
          <div class="quiz-explanation-header">
            <span>💡 Explanation &amp; Core Concept</span>
            <span style="font-size: 0.8rem; margin-left: auto;">Your Result: ${statusBadge}</span>
          </div>
          <div class="quiz-explanation-content">
            <p style="margin-bottom: 6px;"><strong>Correct Answer:</strong> Option ${String.fromCharCode(65 + question.correct)}: <code>${escapeHtml(correctOptText)}</code></p>
            <p>${question.explanation || 'Detailed concept explanation for this question.'}</p>
          </div>
        `;
      } else {
        explBox.style.display = 'none';
        explBox.innerHTML = '';
      }
    }

    // Navigation and Action Buttons
    const prevBtn = document.getElementById('quiz-prev-btn');
    const nextBtn = document.getElementById('quiz-next-btn');

    if (prevBtn) {
      prevBtn.disabled = qIndex === 0;
      prevBtn.style.opacity = qIndex === 0 ? '0.5' : '1';
    }

    if (nextBtn) {
      if (quiz.isReviewMode) {
        nextBtn.innerHTML = qIndex === quiz.totalQuestions - 1 ? 'Return to Scorecard ✓' : 'Next Question →';
      } else {
        nextBtn.innerHTML = qIndex === quiz.totalQuestions - 1 ? 'Submit Quiz ✓' : 'Next →';
      }
    }

    // Mark for Review & Clear Response Buttons
    const markBtn = document.getElementById('quiz-mark-btn');
    if (markBtn) {
      if (quiz.isReviewMode) {
        markBtn.style.display = 'none';
      } else {
        markBtn.style.display = 'inline-flex';
        if (isMarked) {
          markBtn.className = 'btn-secondary btn-mark-review is-marked';
          markBtn.innerHTML = '<span>✓ Marked for Review</span>';
        } else {
          markBtn.className = 'btn-secondary btn-mark-review';
          markBtn.innerHTML = '<span>🔖 Mark for Review</span>';
        }
      }
    }

    const clearBtn = document.getElementById('quiz-clear-btn');
    if (clearBtn) {
      if (quiz.isReviewMode) {
        clearBtn.style.display = 'none';
      } else {
        clearBtn.style.display = 'inline-flex';
        clearBtn.disabled = !isAnswered;
        clearBtn.style.opacity = isAnswered ? '1' : '0.4';
      }
    }

    this.renderQuestionPalette();
  },

  selectOption(qIndex, optionIndex) {
    const state = store.getState();
    if (state.quiz.isReviewMode) return;
    store.dispatch(ACTIONS.SELECT_OPTION, { questionIndex: qIndex, optionIndex });
    this.renderQuestion();
  },

  toggleMark(idx) {
    const state = store.getState();
    if (state.quiz.isReviewMode) return;
    const qIdx = idx !== undefined ? idx : state.quiz.currentQuestionIndex;
    const willMark = !(state.quiz.marked && state.quiz.marked[qIdx]);
    store.dispatch(ACTIONS.TOGGLE_MARK_QUESTION, qIdx);
    if (willMark) {
      toast.info(`🔖 Question ${qIdx + 1} marked for review`);
    } else {
      toast.info(`Question ${qIdx + 1} unmarked`);
    }
    this.renderQuestion();
  },

  clearOption(idx) {
    const state = store.getState();
    if (state.quiz.isReviewMode) return;
    const qIdx = idx !== undefined ? idx : state.quiz.currentQuestionIndex;
    if (state.quiz.answers.hasOwnProperty(qIdx)) {
      store.dispatch(ACTIONS.CLEAR_OPTION, qIdx);
      toast.info(`Cleared selected option for Question ${qIdx + 1}`);
      this.renderQuestion();
    }
  },

  nextQuestion() {
    const state = store.getState();
    const { quiz } = state;
    if (quiz.currentQuestionIndex < quiz.totalQuestions - 1) {
      store.dispatch(ACTIONS.NEXT_QUESTION);
      this.renderQuestion();
    } else {
      if (quiz.isReviewMode) {
        router.switchView('results-view');
        toast.info('Returned to quiz results summary.');
      } else {
        this.submitQuiz();
      }
    }
  },

  prevQuestion() {
    store.dispatch(ACTIONS.PREV_QUESTION);
    this.renderQuestion();
  },

  jumpToQuestion(idx) {
    store.dispatch(ACTIONS.JUMP_QUESTION, idx);
    this.renderQuestion();
  },

  renderQuestionPalette() {
    const palette = document.getElementById('quiz-palette-grid');
    if (!palette) return;
    palette.innerHTML = '';

    const state = store.getState();
    const { quiz } = state;

    // Calculate live summary stats
    let answeredCount = 0;
    let markedCount = 0;
    for (let i = 0; i < quiz.totalQuestions; i++) {
      if (quiz.answers.hasOwnProperty(i) && quiz.answers[i] !== undefined) answeredCount++;
      if (quiz.marked && quiz.marked[i]) markedCount++;
    }

    const statsEl = document.getElementById('quiz-palette-stats');
    if (statsEl) {
      statsEl.innerHTML = `
        <span class="palette-stat-badge stat-answered">✓ ${answeredCount} Attempted</span>
        <span class="palette-stat-badge stat-marked">🔖 ${markedCount} Marked</span>
      `;
    }

    for (let i = 0; i < quiz.totalQuestions; i++) {
      const item = document.createElement('div');
      const isCurrent = i === quiz.currentQuestionIndex;
      const isAnswered = quiz.answers.hasOwnProperty(i) && quiz.answers[i] !== undefined;
      const isMarked = !!(quiz.marked && quiz.marked[i]);

      let cellClass = 'palette-cell';

      if (quiz.isReviewMode) {
        const q = quiz.questions[i];
        if (q && isAnswered) {
          cellClass += quiz.answers[i] === q.correct ? ' answered' : ' review-incorrect';
        } else {
          cellClass += ' unanswered';
        }
      } else {
        if (isAnswered && isMarked) {
          cellClass += ' answered-marked';
        } else if (isMarked) {
          cellClass += ' marked';
        } else if (isAnswered) {
          cellClass += ' answered';
        } else {
          cellClass += ' unanswered';
        }
      }

      if (isCurrent) {
        cellClass += ' current';
      }

      item.className = cellClass;
      item.textContent = i + 1;
      item.title = `Question ${i + 1}: ${isAnswered ? 'Attempted' : 'Not Attempted'}${isMarked ? ' (Marked for review)' : ''}${isCurrent ? ' - Current' : ''}`;
      item.onclick = () => this.jumpToQuestion(i);
      palette.appendChild(item);
    }
  },

  exitQuiz() {
    const state = store.getState();
    if (!state.quiz.isReviewMode) {
      const answeredCount = Object.keys(state.quiz.answers || {}).length;
      const confirmed = window.confirm(
        `Are you sure you want to go back to Dashboard? You have attempted ${answeredCount} of ${state.quiz.totalQuestions} questions. Your answers will remain saved.`
      );
      if (!confirmed) return;
    }
    this.stopTimer();
    router.switchView('dashboard-view');
  },

  submitQuiz() {
    this.stopTimer();
    toast.success('🎉 Quiz Submitted! Calculating your scorecard...');
    setTimeout(() => {
      router.switchView('results-view');
    }, 450);
  },

  startReview() {
    store.dispatch(ACTIONS.START_REVIEW);
    router.switchView('quiz-runner-view');
    toast.info('📖 Review Mode: Viewing your selected answers, correct answers & explanations.');
  },

  retakeQuiz() {
    store.dispatch(ACTIONS.RESET_QUIZ);
    router.switchView('quiz-runner-view');
    this.startTimer();
    toast.info('🚀 Quiz reset! Good luck.');
  }
};

