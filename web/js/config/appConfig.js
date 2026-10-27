/**
 * @file appConfig.js
 * @description Frontend application configuration and constants.
 */

export const APP_CONFIG = Object.freeze({
  APP_NAME: 'CODE-ARENA',
  VERSION: '2.0.0',
  API_BASE_URL: '/api/v1',
  DEFAULT_LANGUAGE: 'Python',
  DEFAULT_TIMER_MINUTES: 20,
  STORAGE_KEYS: {
    AUTH_TOKEN: 'code_arena_token',
    USER_PROFILE: 'code_arena_user',
    SOLVED_PROBLEMS: 'code_arena_solved',
    APP_SETTINGS: 'code_arena_settings'
  },
  THEMES: ['dark-default', 'dark-monokai', 'dark-midnight'],
  SUPPORTED_LANGUAGES: ['Python', 'Java', 'C++', 'JavaScript']
});
