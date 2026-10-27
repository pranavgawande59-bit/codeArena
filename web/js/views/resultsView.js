/**
 * @file resultsView.js
 * @description UI View controller for Quiz Results & Scorecard (Screen 7).
 */

import { store } from '../core/store.js';
import { QuizService } from '../services/quizService.js';

export const ResultsView = {
  render() {
    const state = store.getState();
    const { quiz } = state;
    const summary = QuizService.calculateScore(quiz.questions, quiz.answers);

    const scoreEl = document.getElementById('results-score-display');
    if (scoreEl) scoreEl.textContent = summary.scoreDisplay;

    const accEl = document.getElementById('results-accuracy-pct');
    if (accEl) accEl.textContent = `${summary.accuracyPct}% Accuracy`;

    // SVG donut progress circle (strokeDashoffset based on circumference 440)
    const donutProgress = document.getElementById('donut-progress-circle');
    if (donutProgress) {
      const offset = 440 - (440 * summary.accuracyPct) / 100;
      donutProgress.style.strokeDashoffset = offset;
    }

    // Update pill counters
    const pCorrect = document.getElementById('pill-correct-count');
    const pIncorrect = document.getElementById('pill-incorrect-count');
    const pSkipped = document.getElementById('pill-skipped-count');

    if (pCorrect) pCorrect.textContent = summary.correctCount;
    if (pIncorrect) pIncorrect.textContent = summary.incorrectCount;
    if (pSkipped) pSkipped.textContent = summary.skippedCount;
  }
};
