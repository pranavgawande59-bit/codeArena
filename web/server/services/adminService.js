/**
 * @file adminService.js
 * @description Admin CRUD operations for questions and problems with validation.
 */

const adminQuestionsData = require('../data/adminData');

let adminQuestions = JSON.parse(JSON.stringify(adminQuestionsData));

const adminService = {
  /**
   * Retrieves admin questions with filtering.
   */
  getQuestions(filters = {}) {
    const { search = '', topic = 'All Topics', difficulty = 'All Difficulty' } = filters;
    const cleanSearch = search.toLowerCase().trim();

    return adminQuestions.filter(q => {
      const matchSearch = !cleanSearch || q.text.toLowerCase().includes(cleanSearch) || q.id.toLowerCase().includes(cleanSearch) || q.topic.toLowerCase().includes(cleanSearch);
      const matchTopic = topic === 'All Topics' || q.topic === topic;
      const matchDiff = difficulty === 'All Difficulty' || q.difficulty === difficulty;
      return matchSearch && matchTopic && matchDiff;
    });
  },

  /**
   * Creates a new question in the admin repository.
   */
  createQuestion(questionData) {
    if (!questionData || !questionData.text || !questionData.text.trim()) {
      throw new Error('Question text is required');
    }

    const nextIdNum = adminQuestions.length + 1;
    const newId = `Q00${nextIdNum}`;

    const newQuestion = {
      id: newId,
      text: questionData.text.trim(),
      topic: questionData.topic || 'General',
      difficulty: questionData.difficulty || 'Easy'
    };

    adminQuestions.unshift(newQuestion);
    return newQuestion;
  },

  /**
   * Updates an existing question.
   */
  updateQuestion(id, updates) {
    const q = adminQuestions.find(item => item.id === id);
    if (!q) throw new Error(`Question ${id} not found`);

    if (updates.text) q.text = updates.text.trim();
    if (updates.topic) q.topic = updates.topic.trim();
    if (updates.difficulty) q.difficulty = updates.difficulty.trim();

    return q;
  },

  /**
   * Deletes a question from the admin repository.
   */
  deleteQuestion(id) {
    const initialLen = adminQuestions.length;
    adminQuestions = adminQuestions.filter(item => item.id !== id);
    if (adminQuestions.length === initialLen) {
      throw new Error(`Question ${id} not found`);
    }
    return { success: true, message: `Question ${id} deleted successfully` };
  }
};

module.exports = adminService;
