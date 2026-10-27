/**
 * @file store.js
 * @description Centralized, reactive state store with Redux-like dispatch and Pub/Sub subscriptions.
 */

import { ACTIONS } from './events.js';
import { StorageService } from '../services/storageService.js';
import { APP_CONFIG } from '../config/appConfig.js';

// Default initial state
const defaultState = {
  currentView: 'landing-view',
  isAuthenticated: false,
  currentUser: {
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
  },
  quiz: {
    language: 'Python',
    currentQuestionIndex: 3,
    totalQuestions: 10,
    timerSeconds: 18 * 60 + 45,
    timerInterval: null,
    isReviewMode: false,
    marked: {},
    answers: {
      0: 1,
      1: 0,
      2: 2,
      3: 1
    },
    questions: []
  },
  activeProblemId: 1,
  activeLanguage: 'python',
  problems: [],
  leaderboard: [],
  adminQuestions: [],
  settings: {
    quizTimerMinutes: 20,
    soundFx: true,
    theme: 'dark-default',
    fontSize: '14px'
  }
};

class Store {
  constructor(initialState = defaultState) {
    this._state = this._hydrate(initialState);
    this._listeners = new Set();
  }

  /**
   * Restores persisted state from local storage.
   */
  _hydrate(state) {
    const savedUser = StorageService.get(APP_CONFIG.STORAGE_KEYS.USER_PROFILE);
    const savedToken = StorageService.get(APP_CONFIG.STORAGE_KEYS.AUTH_TOKEN);
    const savedSettings = StorageService.get(APP_CONFIG.STORAGE_KEYS.APP_SETTINGS);

    const merged = { ...state };
    if (savedUser && savedToken) {
      merged.isAuthenticated = true;
      merged.currentUser = { ...merged.currentUser, ...savedUser };
    }
    if (savedSettings) {
      merged.settings = { ...merged.settings, ...savedSettings };
    }
    return merged;
  }

  /**
   * Retrieves an immutable snapshot of current application state.
   */
  getState() {
    return this._state;
  }

  /**
   * Subscribes a listener to receive state change notifications.
   * @param {Function} listener - Callback taking (newState, action)
   * @returns {Function} Unsubscribe handle
   */
  subscribe(listener) {
    this._listeners.add(listener);
    return () => this._listeners.delete(listener);
  }

  /**
   * Emits state update to all active subscribers.
   */
  _notify(action) {
    this._listeners.forEach(listener => {
      try {
        listener(this._state, action);
      } catch (err) {
        console.error('[Store Listener Error]', err);
      }
    });
  }

  /**
   * Dispatches an action and executes pure state transformations.
   */
  dispatch(actionType, payload) {
    const action = typeof actionType === 'object' ? actionType : { type: actionType, payload };
    const prevState = this._state;

    switch (action.type) {
      case ACTIONS.LOGIN_SUCCESS: {
        const user = action.payload?.user || prevState.currentUser;
        this._state = {
          ...prevState,
          isAuthenticated: true,
          currentUser: { ...prevState.currentUser, ...user }
        };
        StorageService.set(APP_CONFIG.STORAGE_KEYS.AUTH_TOKEN, 'token_active_' + Date.now());
        StorageService.set(APP_CONFIG.STORAGE_KEYS.USER_PROFILE, this._state.currentUser);
        break;
      }

      case ACTIONS.LOGOUT: {
        this._state = {
          ...prevState,
          isAuthenticated: false
        };
        StorageService.remove(APP_CONFIG.STORAGE_KEYS.AUTH_TOKEN);
        break;
      }

      case ACTIONS.UPDATE_PROFILE: {
        const updatedUser = { ...prevState.currentUser, ...action.payload };
        this._state = {
          ...prevState,
          currentUser: updatedUser
        };
        StorageService.set(APP_CONFIG.STORAGE_KEYS.USER_PROFILE, updatedUser);
        break;
      }

      case ACTIONS.NAVIGATE_TO: {
        this._state = {
          ...prevState,
          currentView: action.payload
        };
        break;
      }

      case ACTIONS.START_QUIZ: {
        const { language = 'Python', questions = [] } = action.payload || {};
        this._state = {
          ...prevState,
          quiz: {
            ...prevState.quiz,
            language,
            questions: questions.length ? questions : prevState.quiz.questions,
            totalQuestions: questions.length || prevState.quiz.totalQuestions,
            currentQuestionIndex: 0,
            answers: {},
            marked: {},
            isReviewMode: false,
            timerSeconds: prevState.settings.quizTimerMinutes * 60
          }
        };
        break;
      }

      case ACTIONS.SELECT_OPTION: {
        const { questionIndex, optionIndex } = action.payload;
        this._state = {
          ...prevState,
          quiz: {
            ...prevState.quiz,
            answers: {
              ...prevState.quiz.answers,
              [questionIndex]: optionIndex
            }
          }
        };
        break;
      }

      case ACTIONS.TOGGLE_MARK_QUESTION: {
        const qIdx = action.payload !== undefined ? action.payload : prevState.quiz.currentQuestionIndex;
        const currentMarked = { ...(prevState.quiz.marked || {}) };
        if (currentMarked[qIdx]) {
          delete currentMarked[qIdx];
        } else {
          currentMarked[qIdx] = true;
        }
        this._state = {
          ...prevState,
          quiz: {
            ...prevState.quiz,
            marked: currentMarked
          }
        };
        break;
      }

      case ACTIONS.CLEAR_OPTION: {
        const qIdx = action.payload !== undefined ? action.payload : prevState.quiz.currentQuestionIndex;
        const currentAnswers = { ...prevState.quiz.answers };
        delete currentAnswers[qIdx];
        this._state = {
          ...prevState,
          quiz: {
            ...prevState.quiz,
            answers: currentAnswers
          }
        };
        break;
      }

      case ACTIONS.NEXT_QUESTION: {
        if (prevState.quiz.currentQuestionIndex < prevState.quiz.totalQuestions - 1) {
          this._state = {
            ...prevState,
            quiz: {
              ...prevState.quiz,
              currentQuestionIndex: prevState.quiz.currentQuestionIndex + 1
            }
          };
        }
        break;
      }

      case ACTIONS.PREV_QUESTION: {
        if (prevState.quiz.currentQuestionIndex > 0) {
          this._state = {
            ...prevState,
            quiz: {
              ...prevState.quiz,
              currentQuestionIndex: prevState.quiz.currentQuestionIndex - 1
            }
          };
        }
        break;
      }

      case ACTIONS.JUMP_QUESTION: {
        this._state = {
          ...prevState,
          quiz: {
            ...prevState.quiz,
            currentQuestionIndex: action.payload
          }
        };
        break;
      }

      case ACTIONS.TICK_TIMER: {
        if (prevState.quiz.timerSeconds > 0) {
          this._state = {
            ...prevState,
            quiz: {
              ...prevState.quiz,
              timerSeconds: prevState.quiz.timerSeconds - 1
            }
          };
        }
        break;
      }

      case ACTIONS.START_REVIEW: {
        this._state = {
          ...prevState,
          quiz: {
            ...prevState.quiz,
            isReviewMode: true,
            currentQuestionIndex: 0
          }
        };
        break;
      }

      case ACTIONS.RESET_QUIZ: {
        this._state = {
          ...prevState,
          quiz: {
            ...prevState.quiz,
            isReviewMode: false,
            currentQuestionIndex: 0,
            answers: {},
            marked: {},
            timerSeconds: prevState.settings.quizTimerMinutes * 60
          }
        };
        break;
      }

      case ACTIONS.SET_ACTIVE_PROBLEM: {
        this._state = {
          ...prevState,
          activeProblemId: action.payload
        };
        break;
      }

      case ACTIONS.SET_CODE_LANGUAGE: {
        this._state = {
          ...prevState,
          activeLanguage: action.payload
        };
        break;
      }

      case ACTIONS.MARK_PROBLEM_SOLVED: {
        const problemId = action.payload;
        const updatedProblems = prevState.problems.map(p => 
          p.id === problemId ? { ...p, status: 'Solved' } : p
        );
        this._state = {
          ...prevState,
          problems: updatedProblems,
          currentUser: {
            ...prevState.currentUser,
            score: (prevState.currentUser.score || 580) + 50
          }
        };
        break;
      }

      case ACTIONS.UPDATE_SETTINGS: {
        const updatedSettings = { ...prevState.settings, ...action.payload };
        this._state = {
          ...prevState,
          settings: updatedSettings
        };
        StorageService.set(APP_CONFIG.STORAGE_KEYS.APP_SETTINGS, updatedSettings);
        break;
      }

      default:
        // Generic direct state updates for datasets
        if (action.payload && typeof action.payload === 'object') {
          this._state = { ...prevState, ...action.payload };
        }
        break;
    }

    this._notify(action);
    return this._state;
  }
}

export const store = new Store();
