-- =====================================================================
-- SEED CODING PROBLEMS FOR ONLINE MCQ QUIZ & PROBLEM BANK
-- =====================================================================
USE online_mcq_quiz;

INSERT INTO coding_problems 
(title, language, topic, difficulty, description, constraints, sample_input, sample_output, explanation, starter_code)
VALUES
(
    'Two Sum: Target Pair Finder',
    'Java',
    'Arrays',
    'Beginner',
    'Given an array of integers nums and an integer target, return the indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice. You can return the answer in any order.',
    '• 2 <= nums.length <= 10^4\n• -10^9 <= nums[i] <= 10^9\n• -10^9 <= target <= 10^9\n• Only one valid answer exists.\n• Time Complexity Target: O(N) using a Hash Map or O(N^2) brute force.',
    'nums = [2, 7, 11, 15], target = 9',
    '[0, 1]',
    'Because nums[0] + nums[1] == 2 + 7 == 9, we return [0, 1].',
    'import java.util.*;\n\npublic class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Map value -> index\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            map.put(nums[i], i);\n        }\n        return new int[0];\n    }\n}'
),
(
    'Palindrome String Validator',
    'Java',
    'Strings',
    'Beginner',
    'A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.\n\nGiven a string s, return true if it is a palindrome, or false otherwise.',
    '• 1 <= s.length <= 2 * 10^5\n• s consists only of printable ASCII characters.\n• Time Complexity Target: O(N)\n• Space Complexity Target: O(1) two-pointer approach.',
    's = "A man, a plan, a canal: Panama"',
    'true',
    '"amanaplanacanalpanama" is a palindrome because it reads identical forward and backward.',
    'public class Solution {\n    public boolean isPalindrome(String s) {\n        int left = 0, right = s.length() - 1;\n        while (left < right) {\n            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {\n                left++;\n            }\n            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {\n                right--;\n            }\n            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {\n                return false;\n            }\n            left++;\n            right--;\n        }\n        return true;\n    }\n}'
),
(
    'Factorial Trailing Zeroes',
    'Java',
    'Math & Logic',
    'Beginner',
    'Given an integer n, return the number of trailing zeroes in n! (n factorial).\n\nNote that n! = n * (n - 1) * (n - 2) * ... * 3 * 2 * 1.',
    '• 0 <= n <= 10^4\n• Expected Time Complexity: O(log5(n))\n• Expected Space Complexity: O(1)',
    'n = 5',
    '1',
    '5! = 120, which has one trailing zero. Trailing zeroes are contributed by factors of 10 (2 * 5). Count how many times 5 divides into the numbers up to n.',
    'public class Solution {\n    public int trailingZeroes(int n) {\n        int count = 0;\n        while (n >= 5) {\n            count += n / 5;\n            n /= 5;\n        }\n        return count;\n    }\n}'
),
(
    'Student Grade Ledger (OOP Design)',
    'Java',
    'OOP & Classes',
    'Beginner',
    'Design a Student class that stores a student name, roll number, and a list of numeric marks (0-100). Implement methods to:\n1. addMark(int mark)\n2. calculateAverage(): double\n3. getLetterGrade(): char (''A'' for >=90, ''B'' for >=80, ''C'' for >=70, ''D'' for >=60, ''F'' otherwise)',
    '• Student name cannot be null or empty.\n• Roll number > 0.\n• Marks must be clamped or validated between 0 and 100.',
    'Student s = new Student("Alice", 101);\ns.addMark(95);\ns.addMark(85);\ns.calculateAverage();\ns.getLetterGrade();',
    'Average: 90.0, Grade: A',
    'Alice has marks 95 and 85. Total = 180, Average = 90.0. Since 90.0 >= 90, letter grade is A.',
    'import java.util.*;\n\npublic class Student {\n    private String name;\n    private int rollNo;\n    private List<Integer> marks = new ArrayList<>();\n\n    public Student(String name, int rollNo) {\n        this.name = name;\n        this.rollNo = rollNo;\n    }\n\n    public void addMark(int mark) {\n        if (mark >= 0 && mark <= 100) marks.add(mark);\n    }\n\n    public double calculateAverage() {\n        if (marks.isEmpty()) return 0.0;\n        int sum = 0;\n        for (int m : marks) sum += m;\n        return (double) sum / marks.size();\n    }\n\n    public char getLetterGrade() {\n        double avg = calculateAverage();\n        if (avg >= 90) return ''A'';\n        if (avg >= 80) return ''B'';\n        if (avg >= 70) return ''C'';\n        if (avg >= 60) return ''D'';\n        return ''F'';\n    }\n}'
),
(
    'Valid Parentheses & Brackets',
    'Java',
    'Data Structures',
    'Intermediate',
    'Given a string s containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid.\n\nAn input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket of the same type.',
    '• 1 <= s.length <= 10^4\n• s consists of parentheses only: ''()[]{}''.\n• Time Complexity Target: O(N)\n• Space Complexity Target: O(N) using Stack.',
    's = "()[]{}"',
    'true',
    'Every opened bracket is closed by the appropriate match in FIFO/LIFO stack order.',
    'import java.util.Stack;\n\npublic class Solution {\n    public boolean isValid(String s) {\n        Stack<Character> stack = new Stack<>();\n        for (char c : s.toCharArray()) {\n            if (c == ''('') stack.push('')'');\n            else if (c == ''{'') stack.push(''}'');\n            else if (c == ''['') stack.push('']'');\n            else if (stack.isEmpty() || stack.pop() != c) return false;\n        }\n        return stack.isEmpty();\n    }\n}'
),
(
    'Reverse Words in a String',
    'Java',
    'Strings',
    'Intermediate',
    'Given an input string s, reverse the order of the words.\n\nA word is defined as a sequence of non-space characters. The words in s will be separated by at least one space.\n\nReturn a string of the words in reverse order concatenated by a single space. Note that s may contain leading or trailing spaces or multiple spaces between two words.',
    '• 1 <= s.length <= 10^4\n• s contains English letters, digits, and spaces '' ''.\n• Reduce multiple spaces between words to a single space in the inverted string.',
    's = "  the sky   is blue  "',
    '"blue is sky the"',
    'Reversed word sequence is "blue is sky the" with leading, trailing, and internal multiple spaces normalized.',
    'public class Solution {\n    public String reverseWords(String s) {\n        String[] parts = s.trim().split(\"\\\\s+\");\n        StringBuilder sb = new StringBuilder();\n        for (int i = parts.length - 1; i >= 0; i--) {\n            sb.append(parts[i]);\n            if (i > 0) sb.append(\" \");\n        }\n        return sb.toString();\n    }\n}'
),
(
    'Merge Two Sorted Arrays in O(N+M)',
    'Java',
    'Arrays',
    'Intermediate',
    'You are given two integer arrays nums1 and nums2, sorted in non-decreasing order, and two integers m and n, representing the number of elements in nums1 and nums2 respectively.\n\nMerge nums1 and nums2 into a single array sorted in non-decreasing order. The final sorted array should be stored inside nums1 (nums1 has length m + n, where the last n elements are 0).',
    '• nums1.length == m + n\n• nums2.length == n\n• 0 <= m, n <= 200\n• -10^9 <= nums1[i], nums2[j] <= 10^9\n• Follow-up: O(m + n) time using 3 pointers backwards.',
    'nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3',
    'nums1 = [1,2,2,3,5,6]',
    'The arrays merged together produce [1,2,2,3,5,6] written directly into nums1 starting from the back to avoid overwriting.',
    'public class Solution {\n    public void merge(int[] nums1, int m, int[] nums2, int n) {\n        int p1 = m - 1;\n        int p2 = n - 1;\n        int p = m + n - 1;\n        while (p1 >= 0 && p2 >= 0) {\n            if (nums1[p1] > nums2[p2]) {\n                nums1[p--] = nums1[p1--];\n            } else {\n                nums1[p--] = nums2[p2--];\n            }\n        }\n        while (p2 >= 0) {\n            nums1[p--] = nums2[p2--];\n        }\n    }\n}'
),
(
    'Search in Rotated Sorted Array',
    'Java',
    'Algorithms',
    'Intermediate',
    'There is an integer array nums sorted in ascending order (with distinct values).\n\nPrior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length). Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.\n\nYou must write an algorithm with O(log n) runtime complexity.',
    '• 1 <= nums.length <= 5000\n• -10^4 <= nums[i], target <= 10^4\n• All values of nums are unique.\n• Mandatory Time Complexity: O(log n)',
    'nums = [4,5,6,7,0,1,2], target = 0',
    '4',
    '0 is located at index 4 in the rotated array.',
    'public class Solution {\n    public int search(int[] nums, int target) {\n        int left = 0, right = nums.length - 1;\n        while (left <= right) {\n            int mid = left + (right - left) / 2;\n            if (nums[mid] == target) return mid;\n            if (nums[left] <= nums[mid]) {\n                if (nums[left] <= target && target < nums[mid]) right = mid - 1;\n                else left = mid + 1;\n            } else {\n                if (nums[mid] < target && target <= nums[right]) left = mid + 1;\n                else right = mid - 1;\n            }\n        }\n        return -1;\n    }\n}'
),
(
    'Longest Substring Without Repeating Characters',
    'Java',
    'Strings',
    'Advanced',
    'Given a string s, find the length of the longest substring without repeating characters.',
    '• 0 <= s.length <= 5 * 10^4\n• s consists of English letters, digits, symbols and spaces.\n• Time Complexity Target: O(N) using sliding window and a frequency map/set.',
    's = "abcabcbb"',
    '3',
    'The answer is "abc", with the length of 3.',
    'import java.util.HashMap;\n\npublic class Solution {\n    public int lengthOfLongestSubstring(String s) {\n        int maxLen = 0;\n        HashMap<Character, Integer> map = new HashMap<>();\n        for (int left = 0, right = 0; right < s.length(); right++) {\n            char c = s.charAt(right);\n            if (map.containsKey(c)) {\n                left = Math.max(left, map.get(c) + 1);\n            }\n            map.put(c, right);\n            maxLen = Math.max(maxLen, right - left + 1);\n        }\n        return maxLen;\n    }\n}'
),
(
    'LRU Cache Implementation (Least Recently Used)',
    'Java',
    'Data Structures',
    'Advanced',
    'Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.\n\nImplement the LRUCache class:\n• LRUCache(int capacity) Initialize the LRU cache with positive size capacity.\n• int get(int key) Return the value of the key if the key exists, otherwise return -1.\n• void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.\n\nThe functions get and put must each run in O(1) average time complexity.',
    '• 1 <= capacity <= 3000\n• 0 <= key <= 10^4\n• 0 <= value <= 10^5\n• Both get and put must run in O(1) time complexity.',
    'LRUCache cache = new LRUCache(2);\ncache.put(1, 1);\ncache.put(2, 2);\ncache.get(1); // returns 1\ncache.put(3, 3); // evicts key 2\ncache.get(2); // returns -1 (not found)',
    '[null, null, null, 1, null, -1]',
    'Key 2 is evicted because key 1 was accessed via get(1), making key 2 the least recently used element.',
    'import java.util.LinkedHashMap;\nimport java.util.Map;\n\npublic class LRUCache extends LinkedHashMap<Integer, Integer> {\n    private int capacity;\n\n    public LRUCache(int capacity) {\n        super(capacity, 0.75f, true);\n        this.capacity = capacity;\n    }\n\n    public int get(int key) {\n        return super.getOrDefault(key, -1);\n    }\n\n    public void put(int key, int value) {\n        super.put(key, value);\n    }\n\n    @Override\n    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {\n        return size() > capacity;\n    }\n}'
);
