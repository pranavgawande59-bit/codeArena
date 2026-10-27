/**
 * @file leaderboardData.js
 * @description Seed dataset for platform rankings and student performance metrics.
 */

const leaderboardData = [
  { rank: 1, name: 'Arjun Sharma', quizzes: 28, accuracy: '96%', score: 920, medal: '🥇' },
  { rank: 2, name: 'Sneha Patel', quizzes: 24, accuracy: '94%', score: 880, medal: '🥈' },
  { rank: 3, name: 'Rohit Kumar', quizzes: 22, accuracy: '91%', score: 820, medal: '🥉' },
  { rank: 4, name: 'Priya Singh', quizzes: 20, accuracy: '89%', score: 760, medal: '4' },
  { rank: 5, name: 'Karan Mehta', quizzes: 18, accuracy: '85%', score: 720, medal: '5' },
  { rank: 6, name: 'Ananya Desai', quizzes: 16, accuracy: '82%', score: 680, medal: '6' },
  { rank: 7, name: 'Vikash Yadav', quizzes: 15, accuracy: '80%', score: 640, medal: '7' },
  { rank: 8, name: 'Pranav Gawande', quizzes: 12, accuracy: '89%', score: 580, medal: '8', isCurrent: true }
];

module.exports = leaderboardData;
