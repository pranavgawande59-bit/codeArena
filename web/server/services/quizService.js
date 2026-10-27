/**
 * @file quizService.js
 * @description Pure business logic for quiz evaluation, time constraints, and scoring.
 */

const questionsData = require('../data/questionsData');

const quizService = {
  /**
   * Retrieves quiz questions filtered by language and difficulty.
   */
  getQuestions(language = 'Python', difficulty = null, limit = 10) {
    let list = questionsData.filter(q => 
      !language || q.language.toLowerCase() === language.toLowerCase()
    );

    // If fewer than requested questions for language, fill with others
    if (list.length === 0) {
      list = questionsData.slice(0, limit);
    }

    if (difficulty && difficulty !== 'All Difficulties') {
      list = list.filter(q => q.difficulty.toLowerCase() === difficulty.toLowerCase());
    }

    return list.slice(0, limit);
  },

  /**
   * Grades a completed quiz attempt and computes accuracy metrics.
   * @param {Object} submission - { language, answers: { [index]: selectedOptionIndex } }
   */
  evaluateAttempt(submission) {
    const { language = 'Python', answers = {} } = submission || {};
    const questions = this.getQuestions(language, null, 10);
    const totalQuestions = questions.length;

    let correctCount = 0;
    let incorrectCount = 0;
    let skippedCount = 0;

    const reviewDetails = questions.map((q, idx) => {
      const selected = answers[idx];
      const isAnswered = selected !== undefined && selected !== null;
      const isCorrect = isAnswered && selected === q.correct;

      if (!isAnswered) {
        skippedCount++;
      } else if (isCorrect) {
        correctCount++;
      } else {
        incorrectCount++;
      }

      return {
        questionId: q.id,
        index: idx,
        topic: q.topic,
        difficulty: q.difficulty,
        questionText: q.text,
        options: q.options,
        correctOption: q.correct,
        selectedOption: selected,
        isCorrect,
        explanation: q.explanation
      };
    });

    const accuracyPct = totalQuestions > 0 ? Math.round((correctCount / totalQuestions) * 100) : 0;
    const scorePoints = correctCount * 10;

    return {
      language,
      totalQuestions,
      correctCount,
      incorrectCount,
      skippedCount,
      accuracyPct,
      scorePoints,
      timestamp: new Date().toISOString(),
      review: reviewDetails
    };
  }
};

module.exports = quizService;
