-- =====================================================================
-- DATABASE SCRIPT FOR ONLINE MCQ QUIZ SYSTEM
-- Database: MySQL
-- Project: OnlineMCQQuiz
-- =====================================================================

-- Step 1: Create Database
CREATE DATABASE IF NOT EXISTS online_mcq_quiz;
USE online_mcq_quiz;

-- Step 2: Drop existing tables in dependency order if re-initializing
DROP TABLE IF EXISTS attempt_answers;
DROP TABLE IF EXISTS quiz_attempts;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS admin;

-- Step 3: Create Admin Table
CREATE TABLE admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Insert Default Admin User
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- Step 4: Create Students Table
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Default Student
INSERT INTO students (id, username, password, full_name, email)
VALUES (1, 'student', 'student123', 'Alex Morgan', 'alex@college.edu');

-- Step 5: Create Questions Table with Topic, Difficulty, Marks, and Explanation
CREATE TABLE questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    language VARCHAR(50) NOT NULL,
    topic VARCHAR(100) DEFAULT 'General',
    difficulty VARCHAR(20) DEFAULT 'Beginner',
    marks INT DEFAULT 1,
    question TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_answer CHAR(1) NOT NULL,
    explanation TEXT
);

-- Step 6: Create Quiz Attempts Table
CREATE TABLE quiz_attempts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    language VARCHAR(50) NOT NULL,
    topic VARCHAR(100) DEFAULT 'All',
    difficulty VARCHAR(20) DEFAULT 'All',
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    wrong_answers INT NOT NULL,
    skipped INT NOT NULL,
    score INT NOT NULL,
    total_marks INT NOT NULL DEFAULT 10,
    percentage DECIMAL(5,2) NOT NULL,
    feedback_message VARCHAR(50) NOT NULL,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- Step 7: Create Attempt Answers Table
CREATE TABLE attempt_answers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    attempt_id INT NOT NULL,
    question_id INT NOT NULL,
    selected_option CHAR(1),
    is_correct BOOLEAN NOT NULL,
    marks_obtained INT NOT NULL DEFAULT 0,
    FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- Step 8: Insert Sample Questions (Java, HTML, CSS, JavaScript)
INSERT INTO questions (language, topic, difficulty, marks, question, option_a, option_b, option_c, option_d, correct_answer, explanation) VALUES
('Java', 'OOP', 'Beginner', 1, 'Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B', 'The "extends" keyword establishes class inheritance in Java. "implements" is reserved for implementing interface contracts.'),
('Java', 'Basics & Data Types', 'Beginner', 1, 'What is the default value of a boolean variable in Java?', 'true', 'false', '0', 'null', 'B', 'In Java, the default value for uninitialized boolean instance and class fields is false.'),
('Java', 'Basics & Data Types', 'Beginner', 1, 'Which of the following is not a primitive data type in Java?', 'int', 'float', 'String', 'char', 'C', 'String is a reference type (class in java.lang), whereas int, float, and char are primitive types.'),
('Java', 'Basics & Data Types', 'Beginner', 1, 'What is the size of an int variable in Java?', '16 bits', '32 bits', '64 bits', '8 bits', 'B', 'In Java, an int is a 32-bit signed two''s complement integer regardless of CPU architecture.'),
('Java', 'JVM Architecture', 'Intermediate', 2, 'Which component is responsible for running the compiled Java bytecode?', 'JDK', 'JRE', 'JVM', 'JIT', 'C', 'The Java Virtual Machine (JVM) interprets and executes compiled bytecode (.class files) on the host operating system.'),
('Java', 'Basics & Syntax', 'Beginner', 1, 'Which method is the starting point of any standalone Java application?', 'start()', 'run()', 'main()', 'init()', 'C', 'The method "public static void main(String[] args)" is the entry point defined by the Java language specification.'),
('Java', 'OOP', 'Beginner', 1, 'Which access modifier makes members accessible only within the same class?', 'public', 'protected', 'default', 'private', 'D', 'The "private" modifier encapsulates class members so that only methods within the same class can access them.'),
('Java', 'Collections', 'Intermediate', 2, 'Which collection class allows unique elements only?', 'ArrayList', 'LinkedList', 'HashSet', 'Vector', 'C', 'HashSet implements the Set interface, which models the mathematical set abstraction and does not allow duplicate entries.'),
('Java', 'Exception Handling', 'Intermediate', 2, 'Which exception is thrown when an array is accessed with an illegal index?', 'NullPointerException', 'ArrayIndexOutOfBoundsException', 'ArithmeticException', 'NumberFormatException', 'B', 'ArrayIndexOutOfBoundsException is an unchecked runtime exception thrown when code attempts to access an array index < 0 or >= array.length.'),
('Java', 'OOP', 'Advanced', 3, 'Which keyword is used to prevent method overriding in Java?', 'abstract', 'final', 'static', 'const', 'B', 'The "final" keyword on a method prevents subclasses from overriding it, ensuring critical behavior remains unmodified.'),
('Java', 'OOP', 'Intermediate', 2, 'Which OOP concept binds code and data together and keeps both safe from outside interference?', 'Inheritance', 'Polymorphism', 'Encapsulation', 'Abstraction', 'C', 'Encapsulation wraps state and behavior into a cohesive class unit while restricting direct external access via access modifiers.'),
('Java', 'OOP', 'Advanced', 3, 'What is the purpose of the super() constructor call in a Java subclass?', 'It terminates the subclass execution', 'It invokes the constructor of the direct parent class', 'It instantiates an abstract class directly', 'It resets instance variables to defaults', 'B', 'super() invokes the direct superclass constructor, ensuring base class initialization executes before subclass logic.'),
('Java', 'Collections', 'Advanced', 3, 'Which Map implementation guarantees that keys are sorted in natural or custom Comparator order?', 'HashMap', 'LinkedHashMap', 'TreeMap', 'Hashtable', 'C', 'TreeMap is a NavigableMap backed by a Red-Black tree structure that guarantees sorted key ordering in O(log n) time.'),
('Java', 'Exception Handling', 'Beginner', 1, 'Which keyword is used to explicitly throw an exception object in Java?', 'throws', 'throw', 'catch', 'finally', 'B', 'The "throw" keyword explicitly raises an exception instance, while "throws" declares that a method may pass an exception to its caller.'),
('Java', 'Exception Handling', 'Advanced', 3, 'Under which condition does a finally block NOT execute?', 'When an uncaught RuntimeException occurs', 'When System.exit(0) is called in try/catch', 'When a return statement executes in try', 'When OutOfMemoryError is thrown', 'B', 'Calling System.exit() halts the JVM immediately, preventing any pending finally blocks from executing.'),

('HTML', 'HTML Basics', 'Beginner', 1, 'What does HTML stand for?', 'Hyper Text Markup Language', 'High Text Markup Language', 'Hyper Tabular Markup Language', 'Hyper Tool Multi Language', 'A', 'HTML stands for HyperText Markup Language, the standard authoring language used for documents designed to be displayed in a web browser.'),
('HTML', 'HTML Basics', 'Beginner', 1, 'Which HTML tag is used to define an internal style sheet?', '<css>', '<style>', '<script>', '<link>', 'B', 'The <style> tag defines embedded CSS rules directly within the HTML document (typically inside the <head> element).'),
('HTML', 'Semantic Elements', 'Beginner', 1, 'Which HTML tag represents the largest heading?', '<h6>', '<heading>', '<h1>', '<head>', 'C', 'Heading tags range from <h1> (highest importance, largest visual weight) down to <h6> (lowest importance).'),
('HTML', 'HTML Basics', 'Beginner', 1, 'What is the correct HTML element for inserting a line break?', '<br>', '<lb>', '<break>', '<newline>', 'A', 'The <br> tag inserts a line break and is an empty/void element without a separate closing tag in HTML5.'),
('HTML', 'Semantic Elements', 'Beginner', 1, 'Which attribute is used to provide an alternative text for an image?', 'title', 'alt', 'src', 'desc', 'B', 'The "alt" attribute provides accessible descriptive text for screen readers or when an image fails to load.'),
('HTML', 'Semantic Elements', 'Beginner', 1, 'Which HTML element is used to create an unordered bulleted list?', '<ol>', '<ul>', '<li>', '<list>', 'B', 'The <ul> tag stands for Unordered List, rendering bullet points by default; <ol> creates numbered lists.'),
('HTML', 'HTML Basics', 'Beginner', 1, 'Which tag is used to create a hyperlink in HTML?', '<link>', '<a>', '<hyperlink>', '<href>', 'B', 'The <a> (anchor) tag with the "href" attribute creates hyperlinks linking pages, documents, or bookmark anchors.'),
('HTML', 'Semantic Elements', 'Beginner', 1, 'What is the correct HTML element to define important or bold text?', '<b>', '<important>', '<strong>', '<bold>', 'C', 'The <strong> tag gives text strong semantic importance; browsers typically render it in bold typeface.'),
('HTML', 'Semantic Elements', 'Intermediate', 2, 'Which HTML5 element is used to specify a footer for a document or section?', '<bottom>', '<footer>', '<section>', '<aside>', 'B', 'The <footer> semantic tag in HTML5 designates metadata, author information, or copyright text for an article or page.'),
('HTML', 'HTML Forms', 'Intermediate', 2, 'Which attribute specifies where to open the linked document in an <a> tag?', 'target', 'window', 'open', 'href', 'A', 'The "target" attribute specifies the browsing context (such as target="_blank" to open in a new tab/window).'),
('HTML', 'HTML Forms', 'Beginner', 1, 'Which HTML attribute specifies the HTTP method used when submitting form data?', 'action', 'method', 'target', 'enctype', 'B', 'The "method" attribute determines the HTTP request type (GET or POST) dispatched to the server handler.'),
('HTML', 'HTML Forms', 'Intermediate', 2, 'Which input type is specifically designed for entering and validating email addresses?', 'type="text"', 'type="url"', 'type="email"', 'type="contact"', 'C', 'HTML5 introduces <input type="email"> which triggers automatic client-side format validation before form submission.'),
('HTML', 'HTML Forms', 'Advanced', 3, 'Which attribute in an HTML form is required when uploading file attachments via POST?', 'enctype="multipart/form-data"', 'enctype="text/plain"', 'enctype="application/x-www-form-urlencoded"', 'type="file-stream"', 'A', 'When sending binary file data via POST, enctype must be set to multipart/form-data so data is broken into MIME parts.'),

('CSS', 'CSS Basics', 'Beginner', 1, 'What does CSS stand for?', 'Creative Style Sheets', 'Cascading Style Sheets', 'Computer Style Sheets', 'Colorful Style Sheets', 'B', 'CSS stands for Cascading Style Sheets, used to format presentation and layout of structured documents.'),
('CSS', 'Colors & Backgrounds', 'Beginner', 1, 'Which CSS property is used to change the background color of an element?', 'bgcolor', 'color', 'background-color', 'surface-color', 'C', 'The "background-color" property configures the background fill color of any block or inline HTML element.'),
('CSS', 'Colors & Backgrounds', 'Beginner', 1, 'Which CSS property is used to change the text color of an element?', 'text-color', 'font-color', 'color', 'foreground', 'C', 'The "color" property sets the foreground text color of an element.'),
('CSS', 'CSS Selectors', 'Beginner', 1, 'How do you select an element with id "header" in CSS?', '#header', '.header', '*header', 'header', 'A', 'In CSS, the hash symbol (#) prefixes unique ID selectors, such as #header targeting <element id="header">.'),
('CSS', 'CSS Selectors', 'Beginner', 1, 'How do you select all elements with class "btn" in CSS?', '#btn', '.btn', 'btn', '$btn', 'B', 'The period (.) symbol introduces class selectors in CSS, matching all elements with the designated class attribute.'),
('CSS', 'Typography', 'Beginner', 1, 'Which CSS property controls the size of text?', 'text-size', 'font-size', 'font-style', 'text-style', 'B', 'The "font-size" property specifies the rendered size of typographic characters using units like px, em, or rem.'),
('CSS', 'Box Model', 'Intermediate', 2, 'Which property is used to create space around elements, outside of any defined borders?', 'padding', 'margin', 'border-spacing', 'gap', 'B', 'The "margin" property creates space outside the element border, whereas "padding" clears area inside the border.'),
('CSS', 'Positioning & Layout', 'Intermediate', 2, 'What is the default value of the position property in CSS?', 'relative', 'fixed', 'absolute', 'static', 'D', 'Static positioning is the browser default for all elements; they flow according to normal document geometry.'),
('CSS', 'Positioning & Layout', 'Intermediate', 2, 'Which property is used to hide an element while preserving its space on the page?', 'display: none', 'visibility: hidden', 'opacity: 0', 'position: hidden', 'B', '"visibility: hidden" conceals the element visually while preserving its layout dimensions; "display: none" removes it from layout flow completely.'),
('CSS', 'Box Model', 'Advanced', 3, 'Which CSS box-sizing value includes padding and border in the total width and height?', 'content-box', 'border-box', 'padding-box', 'margin-box', 'B', '"box-sizing: border-box" ensures the declared width and height encompass content, padding, and border.'),
('CSS', 'CSS Selectors', 'Intermediate', 2, 'Which CSS selector targets only <p> elements that are DIRECT children of a <div>?', 'div p', 'div + p', 'div > p', 'div ~ p', 'C', 'The child combinator (>) selects elements that are immediate children of the parent element, excluding nested descendants.'),
('CSS', 'CSS Selectors', 'Advanced', 3, 'How do you style every even-numbered row (<tr>) in an HTML table using CSS?', 'tr:nth-child(even)', 'tr:even', 'tr:nth-of-type(2)', 'tr:nth(2n)', 'A', 'The :nth-child(even) pseudo-class selects all even-numbered sibling elements in the parent container.'),
('CSS', 'Box Model', 'Advanced', 3, 'What is the computed width of a box with width: 300px, padding: 20px, and box-sizing: content-box?', '300px', '320px', '340px', '360px', 'C', 'Under content-box, total width = content width (300px) + left/right padding (20px + 20px) = 340px.'),

('JavaScript', 'ES6 Features', 'Beginner', 1, 'Which keyword is used to declare a block-scoped variable in modern JavaScript?', 'var', 'let', 'dim', 'string', 'B', 'The "let" keyword declared in ES6 produces variables bound to the enclosing block statement ({...}), unlike function-scoped "var".'),
('JavaScript', 'Data Types', 'Intermediate', 2, 'What is the output of typeof null in JavaScript?', '"null"', '"undefined"', '"object"', '"number"', 'C', 'Due to an early implementation quirk in JavaScript, typeof null returns "object" for legacy compatibility.'),
('JavaScript', 'Operators & Expressions', 'Beginner', 1, 'Which operator is used to compare both value and data type?', '==', '===', '=', '!=', 'B', 'The strict equality operator (===) compares both value and datatype without implicit type coercion.'),
('JavaScript', 'Functions & Built-ins', 'Beginner', 1, 'Which function is used to parse a string into an integer in JavaScript?', 'Integer.parse()', 'parseInt()', 'parseNumber()', 'toFloat()', 'B', 'parseInt() parses string input until an invalid numeric character is met, returning the integer value in the given radix.'),
('JavaScript', 'Basics & Syntax', 'Beginner', 1, 'How do you write a comment on a single line in JavaScript?', '<!-- comment -->', '# comment', '/* comment */', '// comment', 'D', 'Double slashes (//) indicate a single-line comment in JavaScript, C, Java, and C++.'),
('JavaScript', 'Collections', 'Beginner', 1, 'Which built-in method returns the number of elements in an array?', 'size()', 'count()', 'length', 'index()', 'C', 'In JavaScript, arrays possess a .length property (not a function) that returns the total count of slots/elements.'),
('JavaScript', 'Collections', 'Beginner', 1, 'Which method is used to remove the last element from an array?', 'shift()', 'pop()', 'push()', 'splice()', 'B', 'pop() removes the final element from an array and returns it; shift() removes the first element.'),
('JavaScript', 'DOM & Events', 'Beginner', 1, 'Which event occurs when a user clicks on an HTML element?', 'onchange', 'onmouseclick', 'onclick', 'onpress', 'C', 'The "click" event (handled via onclick in HTML or addEventListener("click", ...)) fires on mouse clicks.'),
('JavaScript', 'Data Types', 'Beginner', 1, 'What does NaN stand for in JavaScript?', 'Not a Name', 'Number and Null', 'Not a Number', 'Null and Nil', 'C', 'NaN stands for "Not a Number", indicating an unrepresentable or undefined mathematical result in IEEE 754 floating-point arithmetic.'),
('JavaScript', 'JSON & Storage', 'Intermediate', 2, 'Which method converts a JavaScript object into a JSON string?', 'JSON.parse()', 'JSON.toString()', 'JSON.stringify()', 'JSON.convert()', 'C', 'JSON.stringify() serializes a JavaScript value or object into a standard UTF-8 JSON text string.'),
('JavaScript', 'ES6 Features', 'Intermediate', 2, 'Which syntax unpacks array values or object properties into distinct variables in ES6?', 'Spread operator', 'Destructuring assignment', 'Rest parameters', 'Object cloning', 'B', 'Destructuring assignment allows extracting fields or elements into variables using concise pattern matching (e.g. const [a, b] = arr).'),
('JavaScript', 'OOP', 'Intermediate', 2, 'How do you create an inheritance relationship between two classes in modern JavaScript (ES6+)?', 'class Dog implements Animal', 'class Dog extends Animal', 'class Dog inherits Animal', 'class Dog : Animal', 'B', 'ES6 classes use the "extends" keyword for prototypal inheritance chaining, syntactically mirroring standard OOP languages.');

-- Step 9: Seed Initial Quiz Attempts for Student Alex Morgan (Demonstration of Analytics)
INSERT INTO quiz_attempts (student_id, language, topic, difficulty, total_questions, correct_answers, wrong_answers, skipped, score, total_marks, percentage, feedback_message, attempt_date) VALUES
(1, 'Java', 'OOP', 'Beginner', 5, 3, 2, 0, 3, 5, 60.00, 'Good!', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1, 'Java', 'Collections', 'Intermediate', 5, 4, 1, 0, 4, 5, 80.00, 'Excellent!', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 'HTML', 'HTML Forms', 'Beginner', 5, 5, 0, 0, 5, 5, 100.00, 'Excellent!', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1, 'CSS', 'CSS Selectors', 'Intermediate', 5, 3, 1, 1, 3, 5, 60.00, 'Good!', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 'JavaScript', 'ES6 Features', 'Intermediate', 5, 4, 1, 0, 4, 5, 80.00, 'Excellent!', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 'Java', 'Exception Handling', 'Intermediate', 5, 5, 0, 0, 5, 5, 100.00, 'Excellent!', NOW());

-- Step 11: Create Programming Problem Bank Tables
CREATE TABLE IF NOT EXISTS coding_problems (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    language VARCHAR(50) NOT NULL DEFAULT 'Java',
    topic VARCHAR(100) NOT NULL DEFAULT 'Arrays',
    difficulty VARCHAR(20) NOT NULL DEFAULT 'Beginner',
    description TEXT NOT NULL,
    constraints TEXT NOT NULL,
    sample_input TEXT NOT NULL,
    sample_output TEXT NOT NULL,
    explanation TEXT,
    starter_code TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS student_problem_solutions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    problem_id INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Attempted',
    code_submission TEXT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (problem_id) REFERENCES coding_problems(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_problem (student_id, problem_id)
);

-- Step 12: Insert Seed Coding Problems into Problem Bank
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
    'Design a Student class that stores a student name, roll number, and a list of numeric marks (0-100). Implement methods to:\n1. addMark(int mark)\n2. calculateAverage(): double\n3. getLetterGrade(): char (\'A\' for >=90, \'B\' for >=80, \'C\' for >=70, \'D\' for >=60, \'F\' otherwise)',
    '• Student name cannot be null or empty.\n• Roll number > 0.\n• Marks must be clamped or validated between 0 and 100.',
    'Student s = new Student("Alice", 101);\ns.addMark(95);\ns.addMark(85);\ns.calculateAverage();\ns.getLetterGrade();',
    'Average: 90.0, Grade: A',
    'Alice has marks 95 and 85. Total = 180, Average = 90.0. Since 90.0 >= 90, letter grade is A.',
    'import java.util.*;\n\npublic class Student {\n    private String name;\n    private int rollNo;\n    private List<Integer> marks = new ArrayList<>();\n\n    public Student(String name, int rollNo) {\n        this.name = name;\n        this.rollNo = rollNo;\n    }\n\n    public void addMark(int mark) {\n        if (mark >= 0 && mark <= 100) marks.add(mark);\n    }\n\n    public double calculateAverage() {\n        if (marks.isEmpty()) return 0.0;\n        int sum = 0;\n        for (int m : marks) sum += m;\n        return (double) sum / marks.size();\n    }\n\n    public char getLetterGrade() {\n        double avg = calculateAverage();\n        if (avg >= 90) return \'A\';\n        if (avg >= 80) return \'B\';\n        if (avg >= 70) return \'C\';\n        if (avg >= 60) return \'D\';\n        return \'F\';\n    }\n}'
),
(
    'Valid Parentheses & Brackets',
    'Java',
    'Data Structures',
    'Intermediate',
    'Given a string s containing just the characters \'(\', \')\', \'{\', \'}\', \'[\' and \']\', determine if the input string is valid.\n\nAn input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket of the same type.',
    '• 1 <= s.length <= 10^4\n• s consists of parentheses only: \'()[]{}\'.\n• Time Complexity Target: O(N)\n• Space Complexity Target: O(N) using Stack.',
    's = "()[]{}"',
    'true',
    'Every opened bracket is closed by the appropriate match in FIFO/LIFO stack order.',
    'import java.util.Stack;\n\npublic class Solution {\n    public boolean isValid(String s) {\n        Stack<Character> stack = new Stack<>();\n        for (char c : s.toCharArray()) {\n            if (c == \'(\') stack.push(\')\');\n            else if (c == \'{\') stack.push(\'}\');\n            else if (c == \'[\') stack.push(\']\');\n            else if (stack.isEmpty() || stack.pop() != c) return false;\n        }\n        return stack.isEmpty();\n    }\n}'
),
(
    'Reverse Words in a String',
    'Java',
    'Strings',
    'Intermediate',
    'Given an input string s, reverse the order of the words.\n\nA word is defined as a sequence of non-space characters. The words in s will be separated by at least one space.\n\nReturn a string of the words in reverse order concatenated by a single space. Note that s may contain leading or trailing spaces or multiple spaces between two words.',
    '• 1 <= s.length <= 10^4\n• s contains English letters, digits, and spaces \' \'.\n• Reduce multiple spaces between words to a single space in the inverted string.',
    's = "  the sky   is blue  "',
    '"blue is sky the"',
    'Reversed word sequence is "blue is sky the" with leading, trailing, and internal multiple spaces normalized.',
    'public class Solution {\n    public String reverseWords(String s) {\n        String[] parts = s.trim().split("\\\\s+");\n        StringBuilder sb = new StringBuilder();\n        for (int i = parts.length - 1; i >= 0; i--) {\n            sb.append(parts[i]);\n            if (i > 0) sb.append(" ");\n        }\n        return sb.toString();\n    }\n}'
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
    'Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.\n\nImplement the LRUCache class:\n• LRUCache(int capacity) Initialize the LRU cache with positive size capacity.\n• int get(int key) Return the value of the key if the key exists, otherwise return -1.\n• void put(int key, int value) Update the value of the key if that key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.\n\nThe functions get and put must each run in O(1) average time complexity.',
    '• 1 <= capacity <= 3000\n• 0 <= key <= 10^4\n• 0 <= value <= 10^5\n• Both get and put must run in O(1) time complexity.',
    'LRUCache cache = new LRUCache(2);\ncache.put(1, 1);\ncache.put(2, 2);\ncache.get(1); // returns 1\ncache.put(3, 3); // evicts key 2\ncache.get(2); // returns -1 (not found)',
    '[null, null, null, 1, null, -1]',
    'Key 2 is evicted because key 1 was accessed via get(1), making key 2 the least recently used element.',
    'import java.util.LinkedHashMap;\nimport java.util.Map;\n\npublic class LRUCache extends LinkedHashMap<Integer, Integer> {\n    private int capacity;\n\n    public LRUCache(int capacity) {\n        super(capacity, 0.75f, true);\n        this.capacity = capacity;\n    }\n\n    public int get(int key) {\n        return super.getOrDefault(key, -1);\n    }\n\n    public void put(int key, int value) {\n        super.put(key, value);\n    }\n\n    @Override\n    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {\n        return size() > capacity;\n    }\n}'
);

-- Step 13: Verification
SELECT 'Setup complete! Tables, student records, categorized questions, and coding problem bank initialized.' AS status;

