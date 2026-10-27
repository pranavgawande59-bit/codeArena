/**
 * @file quizService.js
 * @description Frontend business logic for quiz evaluation, timing, and formatting.
 */

export const QuizService = {
  /**
   * Formats remaining seconds into a standard HH:MM:SS string.
   */
  formatTime(totalSeconds) {
    const s = Math.max(0, Math.floor(totalSeconds));
    const m = Math.floor(s / 60);
    const secs = s % 60;
    return `00:${String(m).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  },

  /**
   * Computes quiz score summary from state questions and user answers.
   */
  calculateScore(questions = [], answers = {}) {
    let correctCount = 0;
    let incorrectCount = 0;
    let skippedCount = 0;

    questions.forEach((q, idx) => {
      const selected = answers[idx];
      if (selected === undefined || selected === null) {
        skippedCount++;
      } else if (selected === q.correct) {
        correctCount++;
      } else {
        incorrectCount++;
      }
    });

    const totalQuestions = questions.length || 10;
    const accuracyPct = Math.round((correctCount / totalQuestions) * 100);

    return {
      totalQuestions,
      correctCount,
      incorrectCount,
      skippedCount,
      accuracyPct,
      scoreDisplay: `${correctCount}/${totalQuestions}`
    };
  }
};
