/**
 * @file problemsData.js
 * @description Curated Algorithmic Problem Bank data.
 */

const problemsData = [
  {
    id: 1,
    title: 'Two Sum',
    topic: 'Array',
    difficulty: 'Easy',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'Given an array of integers <code>nums</code> and an integer <code>target</code>, return indices of the two numbers such that they add up to <code>target</code>.<br><br>You may assume that each input would have <strong>exactly one solution</strong>, and you may not use the same element twice. You can return the answer in any order.',
    examples: [
      {
        input: 'nums = [2,7,11,15], target = 9',
        output: '[0,1]',
        explanation: 'Because nums[0] + nums[1] == 2 + 7 == 9, we return [0, 1].'
      },
      {
        input: 'nums = [3,2,4], target = 6',
        output: '[1,2]',
        explanation: 'Because nums[1] + nums[2] == 2 + 4 == 6, we return [1, 2].'
      }
    ],
    constraints: [
      '2 <= nums.length <= 10^4',
      '-10^9 <= nums[i] <= 10^9',
      '-10^9 <= target <= 10^9',
      'Only one valid answer exists.',
      'Follow-up: Can you come up with an algorithm with O(n) runtime complexity?'
    ],
    starterCode: {
      python: `class Solution:
    def twoSum(self, nums: list[int], target: int) -> list[int]:
        # Write your code here
        pass`,
      java: `class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Write your solution here
        return new int[]{};
    }
}`,
      cpp: `#include <vector>
using namespace std;

class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        // Write your solution here
        return {};
    }
};`
    },
    testCases: [
      { input: 'nums = [2, 7, 11, 15], target = 9', expected: '[0, 1]', unsolvedOutput: '[]' },
      { input: 'nums = [3, 2, 4], target = 6', expected: '[1, 2]', unsolvedOutput: '[]' }
    ],
    explanation: {
      approach: 'One-Pass Hash Map (Dictionary)',
      description: 'We iterate through the array once. For each element `nums[i]`, we calculate its complement `target - nums[i]`. If the complement is already in our hash map, we have found the two numbers and return their indices. Otherwise, we record the current number and index into the map.',
      whyCorrect: 'Because each input has exactly one solution and hash map lookups take O(1) time, checking for the complement before storing the current element ensures we find the matching pair without reusing the same element.',
      timeComplexity: 'O(n) - Single pass through the array with n elements.',
      spaceComplexity: 'O(n) - At most n elements stored in the hash map.',
      edgeCases: 'Handles negative numbers, duplicate values, and zero target seamlessly.'
    }
  },
  {
    id: 2,
    title: 'Reverse a String',
    topic: 'String',
    difficulty: 'Easy',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'Write a function that reverses a string. The input string is given as an array of characters <code>s</code>.<br><br>You must do this by modifying the input array <strong>in-place</strong> with <code>O(1)</code> extra memory.',
    examples: [
      {
        input: 's = ["h","e","l","l","o"]',
        output: '["o","l","l","e","h"]',
        explanation: 'The array of characters is inverted in place from end to beginning.'
      },
      {
        input: 's = ["H","a","n","n","a","h"]',
        output: '["h","a","n","n","a","H"]',
        explanation: 'The entire sequence is reversed.'
      }
    ],
    constraints: [
      '1 <= s.length <= 10^5',
      's[i] is a printable ASCII character.',
      'Must modify the input array in-place with O(1) space.'
    ],
    starterCode: {
      python: `class Solution:
    def reverseString(self, s: list[str]) -> None:
        """
        Do not return anything, modify s in-place instead.
        """
        # Write your code here
        pass`,
      java: `class Solution {
    public void reverseString(char[] s) {
        // Write your solution here
    }
}`,
      cpp: `#include <vector>
using namespace std;

class Solution {
public:
    void reverseString(vector<char>& s) {
        // Write your solution here
    }
};`
    },
    testCases: [
      { input: 's = ["h","e","l","l","o"]', expected: '["o","l","l","e","h"]', unsolvedOutput: '["h","e","l","l","o"]' },
      { input: 's = ["H","a","n","n","a","h"]', expected: '["h","a","n","n","a","H"]', unsolvedOutput: '["H","a","n","n","a","h"]' }
    ],
    explanation: {
      approach: 'Two Pointers (Left and Right Converging)',
      description: 'Set two pointers: `left = 0` at the start of the sequence and `right = len(s) - 1` at the end. While `left < right`, swap `s[left]` and `s[right]`, then increment `left` and decrement `right`.',
      whyCorrect: 'Symmetric swapping inward mirrors each character across the center of the array until both pointers cross, completing reversal in n/2 operations.',
      timeComplexity: 'O(n) - Exactly n / 2 element swaps performed.',
      spaceComplexity: 'O(1) - Pure in-place modification requiring constant additional pointer storage.',
      edgeCases: 'Single character string, even-length string, odd-length string with stationary middle element.'
    }
  },
  {
    id: 3,
    title: 'Valid Parentheses',
    topic: 'Stack',
    difficulty: 'Easy',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: "Given a string <code>s</code> containing just the characters <code>'('</code>, <code>')'</code>, <code>'{'</code>, <code>'}'</code>, <code>'['</code> and <code>']'</code>, determine if the input string is valid.<br><br>An input string is valid if:<br>1. Open brackets must be closed by the same type of brackets.<br>2. Open brackets must be closed in the correct order.<br>3. Every close bracket has a corresponding open bracket of the same type.",
    examples: [
      { input: 's = "()"', output: 'true', explanation: 'Matching pair of parentheses.' },
      { input: 's = "()[]{}"', output: 'true', explanation: 'All pairs open and close symmetrically.' },
      { input: 's = "(]"', output: 'false', explanation: 'Mismatched closing bracket.' }
    ],
    constraints: [
      '1 <= s.length <= 10^4',
      "s consists of parentheses only '()[]{}'."
    ],
    starterCode: {
      python: `class Solution:
    def isValid(self, s: str) -> bool:
        # Write your code here
        return False`,
      java: `class Solution {
    public boolean isValid(String s) {
        // Write your solution here
        return false;
    }
}`,
      cpp: `#include <string>
using namespace std;

class Solution {
public:
    bool isValid(string s) {
        // Write your solution here
        return false;
    }
};`
    },
    testCases: [
      { input: 's = "()[]{}"', expected: 'true', unsolvedOutput: 'false' },
      { input: 's = "([)]"', expected: 'false', unsolvedOutput: 'false' }
    ],
    explanation: {
      approach: 'LIFO Stack Data Structure',
      description: 'Use a stack to keep track of opening brackets. When a closing bracket is encountered, verify if the top of the stack matches its expected opening counterpart. If matched, pop from stack; otherwise, return `false`. After the full scan, the string is valid if and only if the stack is completely empty.',
      whyCorrect: 'Parentheses exhibit nested Last-In, First-Out symmetry. The most recently opened bracket must be the first one closed.',
      timeComplexity: 'O(n) - Single traversal through string characters with O(1) push and pop operations.',
      spaceComplexity: 'O(n) - Worst case all open brackets pushed onto stack before closing.',
      edgeCases: 'Odd length strings (immediately false), starts with closing bracket, unclosed open brackets at end.'
    }
  },
  {
    id: 4,
    title: 'Longest Substring Without Repeating Characters',
    topic: 'String',
    difficulty: 'Medium',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'Given a string <code>s</code>, find the length of the <strong>longest substring</strong> without duplicate characters.',
    examples: [
      { input: 's = "abcabcbb"', output: '3', explanation: 'The answer is "abc", with the length of 3.' },
      { input: 's = "bbbbb"', output: '1', explanation: 'The answer is "b", with the length of 1.' }
    ],
    constraints: [
      '0 <= s.length <= 5 * 10^4',
      's consists of English letters, digits, symbols and spaces.'
    ],
    starterCode: {
      python: `class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        # Write your code here
        return 0`,
      java: `class Solution {
    public int lengthOfLongestSubstring(String s) {
        // Write your solution here
        return 0;
    }
}`,
      cpp: `#include <string>
using namespace std;

class Solution {
public:
    int lengthOfLongestSubstring(string s) {
        // Write your solution here
        return 0;
    }
};`
    },
    testCases: [
      { input: 's = "abcabcbb"', expected: '3', unsolvedOutput: '0' },
      { input: 's = "pwwkew"', expected: '3', unsolvedOutput: '0' }
    ],
    explanation: {
      approach: 'Sliding Window with Hash Map / Set',
      description: 'Maintain a window `[left, right]` where all elements inside are distinct. When a duplicate is encountered at `s[right]`, slide `left` forward past the previous occurrence of that character. At each step, record `maxLen = max(maxLen, right - left + 1)`.',
      whyCorrect: 'Instead of re-evaluating quadratic substrings, sliding window dynamically expands and shrinks boundaries in amortized linear time.',
      timeComplexity: 'O(n) - Both left and right pointers traverse the string at most once.',
      spaceComplexity: 'O(min(n, m)) - Space bounded by character set alphabet size m (e.g. 128 ASCII).',
      edgeCases: 'Empty string, string with all identical characters, string with all unique characters.'
    }
  },
  {
    id: 5,
    title: 'Binary Search',
    topic: 'Binary Search',
    difficulty: 'Easy',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'Given an array of integers <code>nums</code> which is sorted in ascending order, and an integer <code>target</code>, write a function to search <code>target</code> in <code>nums</code>. If <code>target</code> exists, then return its index. Otherwise, return <code>-1</code>.<br><br>You must write an algorithm with <code>O(log n)</code> runtime complexity.',
    examples: [
      { input: 'nums = [-1,0,3,5,9,12], target = 9', output: '4', explanation: '9 exists in nums and its index is 4.' },
      { input: 'nums = [-1,0,3,5,9,12], target = 2', output: '-1', explanation: '2 does not exist in nums so return -1.' }
    ],
    constraints: [
      '1 <= nums.length <= 10^4',
      '-10^4 < nums[i], target < 10^4',
      'All the integers in nums are unique.',
      'nums is sorted in ascending order.'
    ],
    starterCode: {
      python: `class Solution:
    def search(self, nums: list[int], target: int) -> int:
        # Write your code here
        return -1`,
      java: `class Solution {
    public int search(int[] nums, int target) {
        // Write your solution here
        return -1;
    }
}`,
      cpp: `#include <vector>
using namespace std;

class Solution {
public:
    int search(vector<int>& nums, int target) {
        // Write your solution here
        return -1;
    }
};`
    },
    testCases: [
      { input: 'nums = [-1,0,3,5,9,12], target = 9', expected: '4', unsolvedOutput: '-1' },
      { input: 'nums = [-1,0,3,5,9,12], target = 2', expected: '-1', unsolvedOutput: '-1' }
    ],
    explanation: {
      approach: 'Divide and Conquer Iterative Binary Search',
      description: 'Set `low = 0` and `high = len(nums) - 1`. While `low <= high`, calculate `mid = low + (high - low) // 2`. If `nums[mid] == target`, return `mid`. If `nums[mid] < target`, discard the left half (`low = mid + 1`). Otherwise, discard the right half (`high = mid - 1`).',
      whyCorrect: 'Because the array is monotonically sorted, halving the search space at each iteration guarantees finding the target in at most log2(n) steps.',
      timeComplexity: 'O(log n) - Search space divides by two on every step.',
      spaceComplexity: 'O(1) - Constant auxiliary space used for boundary pointers.',
      edgeCases: 'Single element array, target at first or last index, target smaller than min or larger than max.'
    }
  },
  {
    id: 6,
    title: 'Merge Two Sorted Lists',
    topic: 'Linked List',
    difficulty: 'Easy',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'You are given the heads of two sorted linked lists <code>list1</code> and <code>list2</code>.<br><br>Merge the two lists into one <strong>sorted</strong> list. The list should be made by splicing together the nodes of the first two lists.<br><br>Return the head of the merged linked list.',
    examples: [
      { input: 'list1 = [1,2,4], list2 = [1,3,4]', output: '[1,1,2,3,4,4]', explanation: 'Merged sorted sequence.' },
      { input: 'list1 = [], list2 = []', output: '[]', explanation: 'Both empty lists result in empty list.' }
    ],
    constraints: [
      'The number of nodes in both lists is in the range [0, 50].',
      '-100 <= Node.val <= 100',
      'Both list1 and list2 are sorted in non-decreasing order.'
    ],
    starterCode: {
      python: `class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    def mergeTwoLists(self, list1: ListNode, list2: ListNode) -> ListNode:
        # Write your code here
        return None`,
      java: `class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Write your solution here
        return null;
    }
}`,
      cpp: `class Solution {
public:
    ListNode* mergeTwoLists(ListNode* list1, ListNode* list2) {
        // Write your solution here
        return nullptr;
    }
};`
    },
    testCases: [
      { input: 'list1 = [1,2,4], list2 = [1,3,4]', expected: '[1,1,2,3,4,4]', unsolvedOutput: 'null' },
      { input: 'list1 = [], list2 = [0]', expected: '[0]', unsolvedOutput: 'null' }
    ],
    explanation: {
      approach: 'Iterative Two Pointers with Dummy Sentinel Node',
      description: 'Initialize a `dummy` node to anchor the result and a `tail` pointer. While both input lists have remaining nodes, attach the node with smaller value to `tail.next` and advance that list. Once one list is exhausted, splice the remainder of the other list directly to `tail.next`.',
      whyCorrect: 'Because both lists are already sorted, taking the smaller available node at each step maintains the monotonic non-decreasing order.',
      timeComplexity: 'O(n + m) - Each node in list1 and list2 is visited once.',
      spaceComplexity: 'O(1) - In-place pointer manipulation without allocating new node structures.',
      edgeCases: 'One or both lists empty, lists of disparate lengths, duplicate node values.'
    }
  },
  {
    id: 7,
    title: 'Factorial Trailing Zeroes',
    topic: 'Math & Logic',
    difficulty: 'Easy',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'Given an integer <code>n</code>, return the number of trailing zeroes in <code>n!</code> (n factorial).<br><br>Note that <code>n! = n * (n - 1) * (n - 2) * ... * 3 * 2 * 1</code>.<br><br>Your solution should have logarithmic time complexity.',
    examples: [
      { input: 'n = 5', output: '1', explanation: '5! = 120, which has one trailing zero.' },
      { input: 'n = 25', output: '6', explanation: 'Factors of 5 are 5, 10, 15, 20, and 25 (which contributes two 5s), total 6.' }
    ],
    constraints: ['0 <= n <= 10^4'],
    starterCode: {
      python: `class Solution:
    def trailingZeroes(self, n: int) -> int:
        # Write your code here
        return 0`,
      java: `class Solution {
    public int trailingZeroes(int n) {
        // Write your solution here
        return 0;
    }
}`,
      cpp: `class Solution {
public:
    int trailingZeroes(int n) {
        // Write your solution here
        return 0;
    }
};`
    },
    testCases: [
      { input: 'n = 5', expected: '1', unsolvedOutput: '0' },
      { input: 'n = 25', expected: '6', unsolvedOutput: '0' }
    ],
    explanation: {
      approach: 'Legendre\'s Formula for Counting Factors of 5',
      description: 'A trailing zero is created by the factor product 2 * 5 = 10. Since factors of 2 are strictly more abundant than factors of 5 in factorials, the problem reduces to counting how many times 5 divides numbers in range 1 to n: count = floor(n/5) + floor(n/25) + floor(n/125) + ...',
      whyCorrect: 'Each multiple of 5 gives at least one factor of 5; multiples of 25 give an extra factor of 5, multiples of 125 give another, and so on.',
      timeComplexity: 'O(log5 n) - The loop divides n by 5 at each step.',
      spaceComplexity: 'O(1) - Constant auxiliary counter.',
      edgeCases: 'n = 0 returns 0 zeroes; large n up to 10^4.'
    }
  },
  {
    id: 8,
    title: 'Search in Rotated Sorted Array',
    topic: 'Binary Search',
    difficulty: 'Medium',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'Prior to being passed to your function, <code>nums</code> is possibly rotated at an unknown pivot index <code>k</code> (<code>1 <= k < nums.length</code>).<br><br>Given the array <code>nums</code> after the possible rotation and an integer <code>target</code>, return the index of <code>target</code> if it is in <code>nums</code>, or <code>-1</code> if it is not in <code>nums</code>.<br><br>You must achieve <code>O(log n)</code> runtime complexity.',
    examples: [
      { input: 'nums = [4,5,6,7,0,1,2], target = 0', output: '4', explanation: 'Target 0 is at index 4.' },
      { input: 'nums = [4,5,6,7,0,1,2], target = 3', output: '-1', explanation: 'Target 3 does not exist in array.' }
    ],
    constraints: [
      '1 <= nums.length <= 5000',
      '-10^4 <= nums[i] <= 10^4',
      'All values of nums are unique.',
      'nums is an ascending array that is possibly rotated.'
    ],
    starterCode: {
      python: `class Solution:
    def search(self, nums: list[int], target: int) -> int:
        # Write your code here
        return -1`,
      java: `class Solution {
    public int search(int[] nums, int target) {
        // Write your solution here
        return -1;
    }
}`,
      cpp: `class Solution {
public:
    int search(vector<int>& nums, int target) {
        // Write your solution here
        return -1;
    }
};`
    },
    testCases: [
      { input: 'nums = [4,5,6,7,0,1,2], target = 0', expected: '4', unsolvedOutput: '-1' },
      { input: 'nums = [4,5,6,7,0,1,2], target = 3', expected: '-1', unsolvedOutput: '-1' }
    ],
    explanation: {
      approach: 'Modified Binary Search with Sorted Half Detection',
      description: 'At any pivot point in a rotated sorted array, at least one half (either left [low, mid] or right [mid, high]) remains strictly sorted. We check which half is sorted, then determine if `target` falls inside that sorted range. If yes, search there; otherwise, search the opposite half.',
      whyCorrect: 'Because one subarray is always monotonic, a single range check determines whether target is within it or in the wrapped section, maintaining the O(log n) divide-and-conquer property.',
      timeComplexity: 'O(log n) - Search space is halved on each iteration.',
      spaceComplexity: 'O(1) - Constant auxiliary pointer space.',
      edgeCases: 'Array not rotated (k = 0), single-element array, target at rotation pivot boundary.'
    }
  },
  {
    id: 9,
    title: 'LRU Cache Design',
    topic: 'Design',
    difficulty: 'Hard',
    languages: ['Python', 'Java', 'C++'],
    status: 'Not Attempted',
    description: 'Design a data structure that follows the constraints of a <strong>Least Recently Used (LRU) cache</strong>.<br><br>Implement the <code>LRUCache</code> class:<br>• <code>LRUCache(int capacity)</code> Initialize the LRU cache with positive size capacity.<br>• <code>int get(int key)</code> Return the value of the key if the key exists, otherwise return -1.<br>• <code>void put(int key, int value)</code> Update the value of the key if that key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, <strong>evict</strong> the least recently used key.<br><br>The functions <code>get</code> and <code>put</code> must each run in <strong>O(1)</strong> average time complexity.',
    examples: [
      {
        input: '["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]\n[[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]',
        output: '[null, null, null, 1, null, -1, null, -1, 3, 4]',
        explanation: 'Operations follow standard LRU eviction policy when capacity of 2 is exceeded.'
      }
    ],
    constraints: [
      '1 <= capacity <= 3000',
      '0 <= key <= 10^4',
      '0 <= value <= 10^5',
      'At most 2 * 10^5 calls will be made to get and put.',
      'Must achieve O(1) time complexity for get and put operations.'
    ],
    starterCode: {
      python: `class LRUCache:
    def __init__(self, capacity: int):
        # Initialize your data structure here
        pass

    def get(self, key: int) -> int:
        # Write your code here
        return -1

    def put(self, key: int, value: int) -> None:
        # Write your code here
        pass`,
      java: `class LRUCache {
    public LRUCache(int capacity) {
        // Initialize your data structure here
    }
    
    public int get(int key) {
        // Write your code here
        return -1;
    }
    
    public void put(int key, int value) {
        // Write your code here
    }
}`,
      cpp: `class LRUCache {
public:
    LRUCache(int capacity) {
        // Initialize your data structure here
    }
    
    int get(int key) {
        // Write your code here
        return -1;
    }
    
    void put(int key, int value) {
        // Write your code here
    }
};`
    },
    testCases: [
      { input: 'put(1, 1), put(2, 2), get(1)', expected: '1', unsolvedOutput: '-1' },
      { input: 'put(3, 3), get(2) [evicted]', expected: '-1', unsolvedOutput: '-1' }
    ],
    explanation: {
      approach: 'Hash Map + Doubly Linked List',
      description: 'Pair a hash map with a doubly linked list. The hash map maps each key to its corresponding node in the list for O(1) lookups. The doubly linked list maintains the LRU eviction order: accessed or newly inserted nodes are spliced to the head, and least recently used nodes are evicted from the tail.',
      whyCorrect: 'Hash map provides O(1) lookup, while doubly linked list permits O(1) removal and re-insertion without memory shifts.',
      timeComplexity: 'O(1) - Average constant time for both get and put operations.',
      spaceComplexity: 'O(capacity) - Stores at most capacity nodes and map entries.',
      edgeCases: 'Capacity 1, updating an existing key without increasing size, evicting when full.'
    }
  }
];

module.exports = problemsData;
