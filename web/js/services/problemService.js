/**
 * @file problemService.js
 * @description Frontend algorithmic code validation and test runner simulation.
 */

export const ProblemService = {
  /**
   * Analyzes written solution code heuristics to verify implementation.
   */
  evaluateSolution(problem, lang, code) {
    if (!code || code.trim().length < 15) {
      return { status: 'EMPTY', isCorrect: false, errorType: 'Compilation Error' };
    }

    const clean = code.trim().toLowerCase();
    const starter = (problem.starterCode && problem.starterCode[lang]) 
      ? problem.starterCode[lang].trim().toLowerCase() 
      : '';

    if (clean === starter) {
      return { status: 'STARTER_CODE', isCorrect: false, errorType: 'Starter Template' };
    }

    if (clean.includes('pass') && !clean.includes('for ') && !clean.includes('while ') && !clean.includes('return ')) {
      return { status: 'UNIMPLEMENTED', isCorrect: false, errorType: 'Unimplemented Code' };
    }

    let hasRealLogic = false;
    switch (problem.id) {
      case 1: // Two Sum
        hasRealLogic = clean.includes('map') || clean.includes('dict') || clean.includes('enumerate') || clean.includes('target -') || clean.includes('target-') || (clean.includes('for') && clean.includes('in range'));
        break;
      case 2: // Reverse String
        hasRealLogic = clean.includes('reverse') || clean.includes('swap') || clean.includes('left') || clean.includes('[::-1]') || (clean.includes('while') && clean.includes('<'));
        break;
      case 3: // Valid Parentheses
        hasRealLogic = clean.includes('stack') || clean.includes('pop') || clean.includes('push') || clean.includes('replace(');
        break;
      case 4: // Longest Substring
        hasRealLogic = clean.includes('set') || clean.includes('seen') || clean.includes('max') || clean.includes('window') || clean.includes('left');
        break;
      case 5: // Binary Search
        hasRealLogic = clean.includes('mid') || (clean.includes('low') && clean.includes('high')) || clean.includes('bisect');
        break;
      case 6: // Merge Two Lists
        hasRealLogic = clean.includes('dummy') || clean.includes('next') || (clean.includes('while') && (clean.includes('list1') || clean.includes('list2')));
        break;
      case 7: // Factorial Trailing Zeroes
        hasRealLogic = clean.includes('5') && (clean.includes('//') || clean.includes('/') || clean.includes('/='));
        break;
      case 8: // Rotated Sorted Array
        hasRealLogic = clean.includes('mid') && (clean.includes('low') || clean.includes('high'));
        break;
      case 9: // LRU Cache
        hasRealLogic = clean.includes('ordereddict') || clean.includes('linkedhashmap') || (clean.includes('capacity') && (clean.includes('cache') || clean.includes('map') || clean.includes('key')));
        break;
      default:
        hasRealLogic = clean.length > 50 && (clean.includes('for') || clean.includes('while') || clean.includes('if'));
    }

    return {
      status: hasRealLogic ? 'ACCEPTED' : 'WRONG_ANSWER',
      isCorrect: hasRealLogic
    };
  }
};
