/**
 * @file authService.js
 * @description Authentication and user profile service for student and admin sessions.
 */

// In-memory user state (scalable to MySQL/MongoDB)
let currentUser = {
  id: 101,
  name: 'Pranav Gawande',
  email: 'pranav.gawande@student.edu',
  role: 'Student',
  avatarText: 'PG',
  totalQuizzes: 12,
  attempted: 8,
  correctAnswers: 142,
  accuracy: 89,
  rank: 8,
  score: 580
};

let isAuthenticated = true;

const authService = {
  /**
   * Authenticate student with username/email and password.
   */
  login(credentials) {
    const { username, password, email } = credentials || {};
    if (!username && !email) {
      throw new Error('Username or email is required');
    }
    // Demo authentication accepting standard credentials
    isAuthenticated = true;
    if (username) {
      currentUser.name = username;
      const parts = username.split(' ');
      currentUser.avatarText = parts.map(p => p[0]).join('').substring(0, 2).toUpperCase() || 'ST';
    }
    return {
      user: { ...currentUser },
      token: 'jwt_mock_token_' + Date.now()
    };
  },

  /**
   * Terminate active user session.
   */
  logout() {
    isAuthenticated = false;
    return { message: 'Logged out successfully' };
  },

  /**
   * Retrieve current authenticated user profile.
   */
  getCurrentUser() {
    return {
      isAuthenticated,
      user: { ...currentUser }
    };
  },

  /**
   * Update student profile fields.
   */
  updateProfile(profileData) {
    if (!profileData) throw new Error('No profile data provided');
    if (profileData.name && profileData.name.trim()) {
      currentUser.name = profileData.name.trim();
      const parts = currentUser.name.split(' ');
      currentUser.avatarText = parts.map(p => p[0]).join('').substring(0, 2).toUpperCase() || 'ST';
    }
    if (profileData.email) currentUser.email = profileData.email.trim();
    return { ...currentUser };
  },

  /**
   * Award experience points to active user for passing problems or quizzes.
   */
  addScore(points) {
    currentUser.score = (currentUser.score || 0) + points;
    return currentUser.score;
  }
};

module.exports = authService;
