/**
 * @file questionsData.js
 * @description Question repository for MCQ assessments across multiple languages.
 */

const questionsData = [
  {
    id: 1,
    language: 'Python',
    topic: 'Python Basics',
    difficulty: 'Easy',
    text: 'What is the output of print(type([])) in Python 3?',
    options: ["<class 'tuple'>", "<class 'list'>", "<class 'dict'>", "<class 'set'>"],
    correct: 1,
    explanation: 'In Python, empty brackets `[]` create an instance of the built-in `list` class. Therefore, `type([])` returns `<class \'list\'>`.'
  },
  {
    id: 2,
    language: 'Python',
    topic: 'Variables',
    difficulty: 'Easy',
    text: 'Which keyword is used to define a function in Python?',
    options: ['def', 'function', 'fun', 'define'],
    correct: 0,
    explanation: 'The `def` keyword (short for define) is used in Python to define user-defined functions, followed by the function name and parameter list.'
  },
  {
    id: 3,
    language: 'Python',
    topic: 'Control Flow',
    difficulty: 'Medium',
    text: 'What will be the output of bool(0) in Python?',
    options: ['True', '1', 'False', 'None'],
    correct: 2,
    explanation: 'In Python, numerical zero (0, 0.0, 0j) evaluates to `False` in a boolean context. All non-zero numbers evaluate to `True`.'
  },
  {
    id: 4,
    language: 'Python',
    topic: 'Data Structures',
    difficulty: 'Easy',
    text: 'Which of the following is the correct way to create a list in Python?',
    options: [
      'A. list = (1, 2, 3)',
      'B. list = [1, 2, 3]',
      'C. list = {1, 2, 3}',
      'D. list = <1, 2, 3>'
    ],
    correct: 1,
    explanation: 'Square brackets `[...]` are used to define lists. Parentheses `(...)` denote tuples, and curly braces `{...}` denote sets or dictionaries.'
  },
  {
    id: 5,
    language: 'Python',
    topic: 'Data Structures',
    difficulty: 'Medium',
    text: 'Which method adds an element at the end of a list in Python?',
    options: ['add()', 'append()', 'insert()', 'extend()'],
    correct: 1,
    explanation: 'The `append()` method mutates a list by appending a single element to its end in O(1) amortized time. `add()` is used for sets, and `extend()` merges another iterable.'
  },
  {
    id: 6,
    language: 'Python',
    topic: 'String Manipulation',
    difficulty: 'Easy',
    text: 'What does "python"[::-1] evaluate to?',
    options: ['"python"', '"nohtyp"', '"p"', 'Error'],
    correct: 1,
    explanation: 'The slice syntax `[start:stop:step]` with a step of `-1` traverses the string backwards from the end to the start, reversing "python" to "nohtyp".'
  },
  {
    id: 7,
    language: 'Python',
    topic: 'OOP',
    difficulty: 'Medium',
    text: 'Which special method is the constructor in a Python class?',
    options: ['__init__', '__new__', '__construct__', '__create__'],
    correct: 0,
    explanation: 'The `__init__` dunder method serves as the initializer/constructor in Python classes, invoked automatically when a new object instance is created.'
  },
  {
    id: 8,
    language: 'Python',
    topic: 'Exceptions',
    difficulty: 'Medium',
    text: 'Which block is ALWAYS executed after try and except blocks in Python?',
    options: ['catch', 'finally', 'else', 'stop'],
    correct: 1,
    explanation: 'The `finally` block is guaranteed to execute regardless of whether an exception was raised, caught, or not raised at all, commonly used for cleanup.'
  },
  {
    id: 9,
    language: 'Python',
    topic: 'Comprehensions',
    difficulty: 'Hard',
    text: 'What is the result of [x for x in range(5) if x % 2 == 0]?',
    options: ['[0, 2, 4]', '[1, 3]', '[2, 4]', '[0, 1, 2, 3, 4]'],
    correct: 0,
    explanation: '`range(5)` yields numbers 0, 1, 2, 3, 4. The predicate `x % 2 == 0` filters for even numbers: 0, 2, and 4, producing `[0, 2, 4]`.'
  },
  {
    id: 10,
    language: 'Python',
    topic: 'Built-ins',
    difficulty: 'Hard',
    text: 'What is the time complexity of searching a key in a Python dict on average?',
    options: ['O(n)', 'O(log n)', 'O(1)', 'O(n log n)'],
    correct: 2,
    explanation: 'Python dictionaries are implemented as optimized hash tables with open addressing. Key lookup runs in O(1) average constant time.'
  },
  {
    id: 11,
    language: 'Java',
    topic: 'OOP',
    difficulty: 'Easy',
    text: 'Which principle of OOP allows a subclass to provide a specific implementation of a method defined in its superclass?',
    options: ['Encapsulation', 'Polymorphism (Overriding)', 'Abstraction', 'Inheritance only'],
    correct: 1,
    explanation: 'Method overriding is a run-time manifestation of polymorphism where a subclass provides its own implementation of a superclass method.'
  },
  {
    id: 12,
    language: 'Java',
    topic: 'Memory',
    difficulty: 'Medium',
    text: 'Where are objects allocated in Java memory architecture?',
    options: ['Call Stack', 'Heap Memory', 'Metaspace', 'Program Counter Register'],
    correct: 1,
    explanation: 'All object instances and arrays in Java are dynamically allocated on the Garbage Collected Heap.'
  },
  {
    id: 13,
    language: 'C++',
    topic: 'Pointers',
    difficulty: 'Medium',
    text: 'What is the size of a pointer variable on a standard 64-bit operating system architecture?',
    options: ['4 bytes', '8 bytes', '16 bytes', 'Architecture dependent (usually 2 bytes)'],
    correct: 1,
    explanation: 'On a 64-bit architecture, memory addresses are 64 bits wide, which corresponds exactly to 8 bytes.'
  },
  {
    id: 14,
    language: 'JavaScript',
    topic: 'Asynchronous',
    difficulty: 'Medium',
    text: 'Which event loop component executes Microtasks (such as Promise.then callbacks) in modern JavaScript engines?',
    options: ['Task Queue (Macrotasks)', 'Microtask Queue', 'Call Stack directly', 'Web APIs thread pool'],
    correct: 1,
    explanation: 'Microtasks (Promises, queueMicrotask) are queued into the Microtask Queue and executed immediately after the current call stack clears, before next rendering or macrotask.'
  }
];

module.exports = questionsData;
