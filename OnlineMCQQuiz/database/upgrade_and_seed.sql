-- =====================================================================
-- UPGRADE AND SEED SCRIPT FOR ONLINE MCQ QUIZ SYSTEM
-- Adds topics, difficulties, marks, explanations, student records,
-- and quiz attempts for analytics visualization.
-- =====================================================================
USE online_mcq_quiz;

-- 1. Update Existing Java Questions (1 - 10)
UPDATE questions SET 
    topic = 'OOP', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "extends" keyword establishes class inheritance in Java. "implements" is reserved for implementing interface contracts.'
WHERE id = 1;

UPDATE questions SET 
    topic = 'Basics & Data Types', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'In Java, the default value for uninitialized boolean instance and class fields is false.'
WHERE id = 2;

UPDATE questions SET 
    topic = 'Basics & Data Types', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'String is a reference type (class in java.lang), whereas int, float, and char are primitive types.'
WHERE id = 3;

UPDATE questions SET 
    topic = 'Basics & Data Types', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'In Java, an int is a 32-bit signed two''s complement integer regardless of CPU architecture.'
WHERE id = 4;

UPDATE questions SET 
    topic = 'JVM Architecture', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'The Java Virtual Machine (JVM) interprets and executes compiled bytecode (.class files) on the host operating system.'
WHERE id = 5;

UPDATE questions SET 
    topic = 'Basics & Syntax', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The method "public static void main(String[] args)" is the entry point defined by the Java language specification.'
WHERE id = 6;

UPDATE questions SET 
    topic = 'OOP', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "private" modifier encapsulates class members so that only methods within the same class can access them.'
WHERE id = 7;

UPDATE questions SET 
    topic = 'Collections', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'HashSet implements the Set interface, which models the mathematical set abstraction and does not allow duplicate entries.'
WHERE id = 8;

UPDATE questions SET 
    topic = 'Exception Handling', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'ArrayIndexOutOfBoundsException is an unchecked runtime exception thrown when code attempts to access an array index < 0 or >= array.length.'
WHERE id = 9;

UPDATE questions SET 
    topic = 'OOP', 
    difficulty = 'Advanced', 
    marks = 3, 
    explanation = 'The "final" keyword on a method prevents subclasses from overriding it, ensuring critical behavior remains unmodified.'
WHERE id = 10;

-- 2. Update Existing HTML Questions (11 - 20)
UPDATE questions SET 
    topic = 'HTML Basics', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'HTML stands for HyperText Markup Language, the standard authoring language used for documents designed to be displayed in a web browser.'
WHERE id = 11;

UPDATE questions SET 
    topic = 'HTML Basics', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The <style> tag defines embedded CSS rules directly within the HTML document (typically inside the <head> element).'
WHERE id = 12;

UPDATE questions SET 
    topic = 'Semantic Elements', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'Heading tags range from <h1> (highest importance, largest visual weight) down to <h6> (lowest importance).'
WHERE id = 13;

UPDATE questions SET 
    topic = 'HTML Basics', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The <br> tag inserts a line break and is an empty/void element without a separate closing tag in HTML5.'
WHERE id = 14;

UPDATE questions SET 
    topic = 'Semantic Elements', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "alt" attribute provides accessible descriptive text for screen readers or when an image fails to load.'
WHERE id = 15;

UPDATE questions SET 
    topic = 'Semantic Elements', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The <ul> tag stands for Unordered List, rendering bullet points by default; <ol> creates numbered lists.'
WHERE id = 16;

UPDATE questions SET 
    topic = 'HTML Basics', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The <a> (anchor) tag with the "href" attribute creates hyperlinks linking pages, documents, or bookmark anchors.'
WHERE id = 17;

UPDATE questions SET 
    topic = 'Semantic Elements', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The <strong> tag gives text strong semantic importance; browsers typically render it in bold typeface.'
WHERE id = 18;

UPDATE questions SET 
    topic = 'Semantic Elements', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'The <footer> semantic tag in HTML5 designates metadata, author information, or copyright text for an article or page.'
WHERE id = 19;

UPDATE questions SET 
    topic = 'HTML Forms', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'The "target" attribute specifies the browsing context (such as target="_blank" to open in a new tab/window).'
WHERE id = 20;

-- 3. Update Existing CSS Questions (21 - 30)
UPDATE questions SET 
    topic = 'CSS Basics', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'CSS stands for Cascading Style Sheets, used to format presentation and layout of structured documents.'
WHERE id = 21;

UPDATE questions SET 
    topic = 'Colors & Backgrounds', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "background-color" property configures the background fill color of any block or inline HTML element.'
WHERE id = 22;

UPDATE questions SET 
    topic = 'Colors & Backgrounds', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "color" property sets the foreground text color of an element.'
WHERE id = 23;

UPDATE questions SET 
    topic = 'CSS Selectors', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'In CSS, the hash symbol (#) prefixes unique ID selectors, such as #header targeting <element id="header">.'
WHERE id = 24;

UPDATE questions SET 
    topic = 'CSS Selectors', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The period (.) symbol introduces class selectors in CSS, matching all elements with the designated class attribute.'
WHERE id = 25;

UPDATE questions SET 
    topic = 'Typography', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "font-size" property specifies the rendered size of typographic characters using units like px, em, or rem.'
WHERE id = 26;

UPDATE questions SET 
    topic = 'Box Model', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'The "margin" property creates space outside the element border, whereas "padding" clears area inside the border.'
WHERE id = 27;

UPDATE questions SET 
    topic = 'Positioning & Layout', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'Static positioning is the browser default for all elements; they flow according to normal document geometry.'
WHERE id = 28;

UPDATE questions SET 
    topic = 'Positioning & Layout', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = '"visibility: hidden" conceals the element visually while preserving its layout dimensions; "display: none" removes it from layout flow completely.'
WHERE id = 29;

UPDATE questions SET 
    topic = 'Box Model', 
    difficulty = 'Advanced', 
    marks = 3, 
    explanation = '"box-sizing: border-box" ensures the declared width and height encompass content, padding, and border.'
WHERE id = 30;

-- 4. Update Existing JavaScript Questions (31 - 40)
UPDATE questions SET 
    topic = 'ES6 Features', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "let" keyword declared in ES6 produces variables bound to the enclosing block statement ({...}), unlike function-scoped "var".'
WHERE id = 31;

UPDATE questions SET 
    topic = 'Data Types', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'Due to an early implementation quirk in JavaScript, typeof null returns "object" for legacy compatibility.'
WHERE id = 32;

UPDATE questions SET 
    topic = 'Operators & Expressions', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The strict equality operator (===) compares both value and datatype without implicit type coercion.'
WHERE id = 33;

UPDATE questions SET 
    topic = 'Functions & Built-ins', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'parseInt() parses string input until an invalid numeric character is met, returning the integer value in the given radix.'
WHERE id = 34;

UPDATE questions SET 
    topic = 'Basics & Syntax', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'Double slashes (//) indicate a single-line comment in JavaScript, C, Java, and C++.'
WHERE id = 35;

UPDATE questions SET 
    topic = 'Collections', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'In JavaScript, arrays possess a .length property (not a function) that returns the total count of slots/elements.'
WHERE id = 36;

UPDATE questions SET 
    topic = 'Collections', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'pop() removes the final element from an array and returns it; shift() removes the first element.'
WHERE id = 37;

UPDATE questions SET 
    topic = 'DOM & Events', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'The "click" event (handled via onclick in HTML or addEventListener("click", ...)) fires on mouse clicks.'
WHERE id = 38;

UPDATE questions SET 
    topic = 'Data Types', 
    difficulty = 'Beginner', 
    marks = 1, 
    explanation = 'NaN stands for "Not a Number", indicating an unrepresentable or undefined mathematical result in IEEE 754 floating-point arithmetic.'
WHERE id = 39;

UPDATE questions SET 
    topic = 'JSON & Storage', 
    difficulty = 'Intermediate', 
    marks = 2, 
    explanation = 'JSON.stringify() serializes a JavaScript value or object into a standard UTF-8 JSON text string.'
WHERE id = 40;

-- 5. Insert Additional Questions for Deep Topic & Difficulty Coverage
-- Topics: OOP, Collections, Exception Handling, HTML Forms, CSS Selectors, ES6 Features, Advanced OOP
INSERT INTO questions (language, topic, difficulty, marks, question, option_a, option_b, option_c, option_d, correct_answer, explanation) VALUES
('Java', 'OOP', 'Intermediate', 2, 'Which OOP concept binds code and data together and keeps both safe from outside interference?', 'Inheritance', 'Polymorphism', 'Encapsulation', 'Abstraction', 'C', 'Encapsulation wraps state and behavior into a cohesive class unit while restricting direct external access via access modifiers.'),
('Java', 'OOP', 'Advanced', 3, 'What is the purpose of the super() constructor call in a Java subclass?', 'It terminates the subclass execution', 'It invokes the constructor of the direct parent class', 'It instantiates an abstract class directly', 'It resets instance variables to defaults', 'B', 'super() invokes the direct superclass constructor, ensuring base class initialization executes before subclass logic.'),
('Java', 'Collections', 'Advanced', 3, 'Which Map implementation guarantees that keys are sorted in natural or custom Comparator order?', 'HashMap', 'LinkedHashMap', 'TreeMap', 'Hashtable', 'C', 'TreeMap is a NavigableMap backed by a Red-Black tree structure that guarantees sorted key ordering in O(log n) time.'),
('Java', 'Exception Handling', 'Beginner', 1, 'Which keyword is used to explicitly throw an exception object in Java?', 'throws', 'throw', 'catch', 'finally', 'B', 'The "throw" keyword explicitly raises an exception instance, while "throws" declares that a method may pass an exception to its caller.'),
('Java', 'Exception Handling', 'Advanced', 3, 'Under which condition does a finally block NOT execute?', 'When an uncaught RuntimeException occurs', 'When System.exit(0) is called in try/catch', 'When a return statement executes in try', 'When OutOfMemoryError is thrown', 'B', 'Calling System.exit() halts the JVM immediately, preventing any pending finally blocks from executing.'),
('HTML', 'HTML Forms', 'Beginner', 1, 'Which HTML attribute specifies the HTTP method used when submitting form data?', 'action', 'method', 'target', 'enctype', 'B', 'The "method" attribute determines the HTTP request type (GET or POST) dispatched to the server handler.'),
('HTML', 'HTML Forms', 'Intermediate', 2, 'Which input type is specifically designed for entering and validating email addresses?', 'type="text"', 'type="url"', 'type="email"', 'type="contact"', 'C', 'HTML5 introduces <input type="email"> which triggers automatic client-side format validation before form submission.'),
('HTML', 'HTML Forms', 'Advanced', 3, 'Which attribute in an HTML form is required when uploading file attachments via POST?', 'enctype="multipart/form-data"', 'enctype="text/plain"', 'enctype="application/x-www-form-urlencoded"', 'type="file-stream"', 'A', 'When sending binary file data via POST, enctype must be set to multipart/form-data so data is broken into MIME parts.'),
('CSS', 'CSS Selectors', 'Intermediate', 2, 'Which CSS selector targets only <p> elements that are DIRECT children of a <div>?', 'div p', 'div + p', 'div > p', 'div ~ p', 'C', 'The child combinator (>) selects elements that are immediate children of the parent element, excluding nested descendants.'),
('CSS', 'CSS Selectors', 'Advanced', 3, 'How do you style every even-numbered row (<tr>) in an HTML table using CSS?', 'tr:nth-child(even)', 'tr:even', 'tr:nth-of-type(2)', 'tr:nth(2n)', 'A', 'The :nth-child(even) pseudo-class selects all even-numbered sibling elements in the parent container.'),
('CSS', 'Box Model', 'Advanced', 3, 'What is the computed width of a box with width: 300px, padding: 20px, and box-sizing: content-box?', '300px', '320px', '340px', '360px', 'C', 'Under content-box, total width = content width (300px) + left/right padding (20px + 20px) = 340px.'),
('JavaScript', 'ES6 Features', 'Intermediate', 2, 'Which syntax unpacks array values or object properties into distinct variables in ES6?', 'Spread operator', 'Destructuring assignment', 'Rest parameters', 'Object cloning', 'B', 'Destructuring assignment allows extracting fields or elements into variables using concise pattern matching (e.g. const [a, b] = arr).'),
('JavaScript', 'OOP', 'Intermediate', 2, 'How do you create an inheritance relationship between two classes in modern JavaScript (ES6+)?', 'class Dog implements Animal', 'class Dog extends Animal', 'class Dog inherits Animal', 'class Dog : Animal', 'B', 'ES6 classes use the "extends" keyword for prototypal inheritance chaining, syntactically mirroring standard OOP languages.');

-- 6. Insert Default Student Alex Morgan
INSERT INTO students (id, username, password, full_name, email)
VALUES (1, 'student', 'student123', 'Alex Morgan', 'alex@college.edu')
ON DUPLICATE KEY UPDATE full_name = 'Alex Morgan';

-- 7. Insert Realistic Quiz Attempts for Performance Analytics Demo
INSERT INTO quiz_attempts (student_id, language, topic, difficulty, total_questions, correct_answers, wrong_answers, skipped, score, total_marks, percentage, feedback_message, attempt_date) VALUES
(1, 'Java', 'OOP', 'Beginner', 5, 3, 2, 0, 3, 5, 60.00, 'Good!', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1, 'Java', 'Collections', 'Intermediate', 5, 4, 1, 0, 4, 5, 80.00, 'Excellent!', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 'HTML', 'HTML Forms', 'Beginner', 5, 5, 0, 0, 5, 5, 100.00, 'Excellent!', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1, 'CSS', 'CSS Selectors', 'Intermediate', 5, 3, 1, 1, 3, 5, 60.00, 'Good!', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 'JavaScript', 'ES6 Features', 'Intermediate', 5, 4, 1, 0, 4, 5, 80.00, 'Excellent!', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 'Java', 'Exception Handling', 'Intermediate', 5, 5, 0, 0, 5, 5, 100.00, 'Excellent!', NOW());

-- 8. Record Answers for the Latest Attempt (Attempt ID 6)
-- Connects attempt 6 to sample questions to verify quiz review feature
SET @latest_attempt = (SELECT MAX(id) FROM quiz_attempts WHERE student_id = 1);

INSERT INTO attempt_answers (attempt_id, question_id, selected_option, is_correct, marks_obtained) VALUES
(@latest_attempt, 9, 'B', 1, 2),
(@latest_attempt, 44, 'B', 1, 1),
(@latest_attempt, 45, 'B', 1, 3),
(@latest_attempt, 1, 'B', 1, 1),
(@latest_attempt, 10, 'B', 1, 3);
