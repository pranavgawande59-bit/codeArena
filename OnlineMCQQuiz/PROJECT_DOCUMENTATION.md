# Comprehensive Project Report: Online MCQ Quiz System
**Course / Degree**: Bachelor of Technology (B.Tech) / Bachelor of Computer Applications (BCA) / B.Sc Computer Science  
**Subject**: Advanced Java Programming / Java Desktop Application Mini-Project  
**Architecture**: Java Swing (GUI) + JDBC + MySQL Database

---

## 1. Project Abstract
The **Online MCQ Quiz System** is a lightweight, standalone desktop application engineered in Java using Java Swing for the graphical user interface, JDBC for relational data mapping, and MySQL Server for persistent storage. The software addresses the need for an automated, interactive testing environment for evaluating students' foundational programming concepts across multiple languages (Java, HTML, CSS, JavaScript). 

By eliminating the overhead of student authentication, the system allows learners to take assessments friction-free with randomized 10-question quiz sets and per-question 30-second timers. For academic administrators, a secure administrative portal allows question bank management through full CRUD (Create, Read, Update, Delete) operations with validation and SQL injection prevention via `PreparedStatement`.

---

## 2. Project Objectives
1. **Friction-Free Student Testing**: Enable immediate access to quizzes without mandatory student registration or login overhead.
2. **Automated Assessment & Instant Feedback**: Automatically evaluate selected responses against canonical answer keys, compute percentage scores, and render immediate qualitative feedback.
3. **Randomized Question Sets**: Select 10 randomized questions per quiz session from the language question pool to minimize question repetition.
4. **Time-Constrained Testing**: Enforce a strict 30-second timer per question using `javax.swing.Timer`, auto-advancing upon expiration and auto-submitting on final timeout.
5. **Robust Administrative Management**: Provide authenticated instructors with administrative tools to Add, View, Update, and Delete questions in MySQL.
6. **Data Integrity & Security**: Use parameterized JDBC `PreparedStatement` queries to prevent SQL injection and enforce database constraints.

---

## 3. Existing System vs. Proposed System

### Existing System (Traditional / Paper-Based or Complex Web Systems)
- **Manual Overhead**: Physical question distribution, manual answer script scoring, and delayed results.
- **Cheating Vulnerability**: Static question ordering where every student receives identical sequences.
- **System Bloat**: Web-based quiz platforms often require complex web servers (Apache/Tomcat), user session databases, email verifications, and external internet connectivity.
- **High Resource Usage**: Heavy frameworks (Spring Boot, Hibernate, Node.js) that require complex local setups for simple classroom testing.

### Proposed System (Online MCQ Quiz System Desktop Application)
- **Zero Authentication for Students**: Immediate test initiation.
- **Dynamic Shuffling**: Questions are retrieved dynamically from MySQL and randomized in memory.
- **Automated Real-Time Scoring**: Zero human intervention in grading.
- **Lightweight & Standalone**: Pure Java Swing + JDBC desktop executable running independently of web servers or cloud infrastructure.
- **Graceful Error Handling**: Database connection alerts, empty-field validations, and confirmation dialogs prevent unexpected crashes.

---

## 4. Advantages & Limitations

### Advantages
1. **Portability**: Runs on any operating system equipped with Java Runtime Environment (Windows, macOS, Linux).
2. **Offline Usability**: Operates over a local MySQL instance without requiring active internet connectivity.
3. **Low Latency**: Instant GUI updates and immediate sub-second question loading.
4. **Extensible Question Bank**: New languages and questions can be added through the Admin GUI without modifying source code.
5. **Educational Clarity**: Clear separation of concerns (Presentation Layer, Data Access Layer, Model Layer, Database Layer).

### Limitations
1. **Single-Desktop Focus**: Designed as a standalone desktop app; answers are not synchronized across a distributed multi-tenant network.
2. **No Persistent Student History**: Because students do not log in, past scores are presented upon completion but not permanently archived per individual student.
3. **Admin Password Storage**: Plain-text admin password used for college demonstration simplicity (production environments should utilize BCrypt hashing).

---

## 5. Future Scope
1. **Student Score History & Export**: Ability to save and export test certificates as PDF or CSV files.
2. **Password Cryptography**: Integration of PBKDF2 or BCrypt hashing with salt for admin credentials.
3. **Negative Marking & Variable Difficulty**: Configurable point systems allowing penalties for incorrect choices.
4. **Networked Client-Server Architecture**: Migration of JDBC layer to a centralized socket or micro-REST service for campus-wide lab deployment.

---

## 6. Hardware & Software Requirements

### Hardware Requirements
- **Processor**: Intel Core i3 / AMD Ryzen 3 or higher.
- **RAM**: Minimum 2 GB (4 GB recommended).
- **Storage**: Minimum 250 MB free hard drive space (for JDK, MySQL database, and application binaries).
- **Display Resolution**: 1024 x 768 or higher.

### Software Requirements
- **Operating System**: Windows 10/11, Ubuntu 20.04+, or macOS.
- **Java Development Kit**: JDK 17, 21, or higher.
- **Database Server**: MySQL Community Server 8.0+.
- **Database Driver**: MySQL Connector/J 8.3.0.
- **Integrated Development Environment (IDE)**: Eclipse IDE for Java Developers (2022+).

---

## 7. System Analysis & Design Diagrams

### 7.1 Entity-Relationship (ER) Diagram Description
The database contains two core entities:
1. **`admin` Entity**:
   - `id` (INT, Primary Key, Auto-Increment)
   - `username` (VARCHAR(50), Unique, Not Null)
   - `password` (VARCHAR(100), Not Null)
2. **`questions` Entity**:
   - `id` (INT, Primary Key, Auto-Increment)
   - `language` (VARCHAR(50), Not Null, Indexed)
   - `question` (TEXT, Not Null)
   - `option_a` (VARCHAR(255), Not Null)
   - `option_b` (VARCHAR(255), Not Null)
   - `option_c` (VARCHAR(255), Not Null)
   - `option_d` (VARCHAR(255), Not Null)
   - `correct_answer` (CHAR(1), Not Null)

**Cardinality**: The administrator entity manages (1-to-Many) the questions repository.

```
+--------------------+            manages (CRUD)           +--------------------+
|       ADMIN        | ----------------------------------> |     QUESTIONS      |
+--------------------+ 1                                 * +--------------------+
| PK  id (INT)       |                                     | PK  id (INT)       |
|     username (UQ)  |                                     |     language       |
|     password       |                                     |     question (TEXT)|
+--------------------+                                     |     option_a       |
                                                           |     option_b       |
                                                           |     option_c       |
                                                           |     option_d       |
                                                           |     correct_answer |
                                                           +--------------------+
```

---

### 7.2 Data Flow Diagrams (DFD)

#### DFD Level 0 (Context Level Diagram)
```
                +-----------------------------------------------+
                |                                               |
                |   [Student]                                   |
                |       |                                       |
                |       |-- (1) Select Language --------------->|
                |       |-- (2) Submit Answers ---------------->|
                |       |<-- (3) Display Questions -------------|
                |       |<-- (4) View Score & Result -----------|
                |                                               |
                |             ONLINE MCQ QUIZ SYSTEM            |
                |                                               |
                |       |<-- (5) View Questions Data -----------|
                |       |-- (6) Admin Credentials ------------->|
                |       |-- (7) CRUD Question Requests -------->|
                |       |<-- (8) Operation Status --------------|
                |       |                                       |
                |   [Admin]                                     |
                |                                               |
                +-----------------------------------------------+
```

#### DFD Level 1
```
+-----------+       (1) Select Language      +-----------------------+
|  Student  | -----------------------------> | 1.0 Fetch Questions   | <---> [ DB: questions ]
+-----------+                                +-----------------------+
      |                                                  |
      | (2) Answer & Submit                              | (Questions Stream)
      v                                                  v
+-----------------------+                    +-----------------------+
| 2.0 Evaluate Score    | <----------------- | Quiz Execution Engine |
+-----------------------+                    +-----------------------+
      |
      v (Score Summary)
+-----------------------+
| 3.0 Render Result UI  |
+-----------------------+

+-----------+       (1) Credentials          +-----------------------+
|   Admin   | -----------------------------> | 4.0 Authenticate      | <---> [ DB: admin ]
+-----------+                                +-----------------------+
      |                                                  |
      | (2) Add / Update / Delete                        v (Session Granted)
      v                                      +-----------------------+
+-----------------------+                    | 5.0 Question Manager  |
| 6.0 DB CRUD Executor  | <----------------- | (Dashboard)           |
+-----------------------+                    +-----------------------+
      |
      v
[ DB: questions ]
```

---

### 7.3 Use-Case Diagram Description
- **Actors**:
  1. **Student** (Unauthenticated)
     - Use Cases: Browse Home Screen, Choose Language, Start Quiz, Attempt Question, Save Answer, Navigate Previous/Next, Monitor Timer, Submit Quiz, View Score & Feedback, Retake Quiz.
  2. **Admin** (Authenticated)
     - Use Cases: Login with Credentials, View Dashboard, Add Question, View Questions Table, Filter Questions by Language, Search Question by ID, Update Question Details, Delete Question, Logout.

---

### 7.4 Class Diagram & Package Mapping
```
                    +-----------------------+
                    |       Question        |
                    +-----------------------+
                    | - id: int             |
                    | - language: String    |
                    | - question: String    |
                    | - optionA: String     |
                    | - optionB: String     |
                    | - optionC: String     |
                    | - optionD: String     |
                    | - correctAnswer: String|
                    +-----------------------+

                    +-----------------------+
                    |      QuizResult       |
                    +-----------------------+
                    | - language: String    |
                    | - totalQuestions: int |
                    | - correctAnswers: int |
                    | - wrongAnswers: int   |
                    | - skipped: int        |
                    | - score: int          |
                    | - percentage: double  |
                    | - feedbackMsg: String |
                    +-----------------------+

        +-----------------------+       +-----------------------+
        |     QuestionDAO       |       |       AdminDAO        |
        +-----------------------+       +-----------------------+
        | + addQuestion()       |       | + validateAdmin()     |
        | + getQuestionsByLang()|       +-----------------------+
        | + getRandomQuestions()|                   |
        | + getAllQuestions()   |                   v
        | + getQuestionById()   |       +-----------------------+
        | + updateQuestion()    | ----> |     DBConnection      |
        | + deleteQuestion()    |       +-----------------------+
        +-----------------------+       | + getConnection()     |
                                        | + testConnection()    |
                                        +-----------------------+
```

---

## 8. Detailed Explanation of Modules & Methods

### 8.1 Database Layer (`DBConnection.java`)
- `getConnection()`: Loads `com.mysql.cj.jdbc.Driver` dynamically. Opens connection to `jdbc:mysql://localhost:3306/online_mcq_quiz` passing `USER` and `PASSWORD`. Returns an active `Connection` instance.
- `testConnection()`: Opens a short-lived connection and checks `conn.isClosed()`. Provides safe connection diagnostics on startup without terminating the Swing GUI.

### 8.2 Data Access Objects (DAOs)
- `AdminDAO.validateAdmin(username, password)`: Executes a parameterized `SELECT` query against the `admin` table. If `rs.next()` evaluates to true, credentials are valid.
- `QuestionDAO.getRandomQuestionsByLanguage(language, limit)`: Queries all questions matching the selected language, shuffles them in memory using `Collections.shuffle()`, and extracts a non-repeating sub-list of 10 items.
- `QuestionDAO.addQuestion(Question q)`: Uses `PreparedStatement` with 7 positional parameters to insert a new question.
- `QuestionDAO.updateQuestion(Question q)`: Updates all attributes of an existing question by matching its primary key `id`.
- `QuestionDAO.deleteQuestion(id)`: Safely deletes a question by its primary key `id`.

### 8.3 Quiz Engine & Navigation Logic
- **Answer Preservation (`Map<Integer, String> selectedAnswers`)**:
  - The UI uses a `HashMap` mapping question indices (`0..9`) to selected radio button letters (`"A"`, `"B"`, `"C"`, `"D"`).
  - When the student clicks "Next" or "Previous", `saveSelectedAnswer()` inspects radio button states and puts the letter in the map.
  - When `displayCurrentQuestion()` renders the next card, it checks `selectedAnswers.get(currentIndex)` and marks the corresponding `JRadioButton` as selected.
  - If a student navigates back and forth, their choices remain preserved.
- **Timer Engine (`QuizTimer.java`)**:
  - Encapsulates `javax.swing.Timer` with a 1000 millisecond tick interval.
  - Keeps UI updates strictly on the Swing Event Dispatch Thread (EDT).
  - When `secondsRemaining == 0`:
    - If `currentIndex < total - 1`, auto-advances to the next question.
    - If `currentIndex == total - 1`, auto-submits the quiz.
- **Score Calculation**:
  - `Correct = count where selectedAnswer == question.getCorrectAnswer()`
  - `Skipped = count where selectedAnswer == null`
  - `Wrong = total - Correct - Skipped`
  - `Score = Correct`
  - `Percentage = (Correct / Total) * 100.0`

---

## 9. Comprehensive Test Plan & Test Cases

| TC ID | Test Scenario | Input Data | Expected Output | Actual Output | Status |
|---|---|---|---|---|---|
| TC-01 | Admin login with valid credentials | User: `admin`, Pass: `admin123` | Success dialog; Admin Dashboard opens | Dashboard opens | Pass |
| TC-02 | Admin login with invalid password | User: `admin`, Pass: `wrongpass` | Error dialog: "Invalid username or password" | Error dialog shown | Pass |
| TC-03 | Admin login with empty fields | User: `""`, Pass: `""` | Warning dialog: "Username cannot be empty!" | Validation dialog shown | Pass |
| TC-04 | Add Question with valid data | Lang: `Java`, Question: `What is JRE?`, Opts: A-D, Ans: `A` | Success dialog; Record inserted into MySQL | Record inserted | Pass |
| TC-05 | Add Question with empty question | Lang: `Java`, Question: `""` | Warning dialog: "Question text cannot be empty!" | Form focus retained | Pass |
| TC-06 | View Questions table filter | Select `HTML` in filter | Table displays only HTML questions | Filtered accurately | Pass |
| TC-07 | Update Question | Modify Question ID 1 text and submit | Success dialog; Updated record in MySQL | Record updated | Pass |
| TC-08 | Delete Question with confirmation | Click Yes on confirmation | Record deleted from MySQL; Table refreshed | Record deleted | Pass |
| TC-09 | Delete Question cancel | Click No on confirmation | Deletion aborted; Record retained | Record retained | Pass |
| TC-10 | Start Quiz language selection | Select `Java` and click Start Quiz | QuizFrame loads 10 random Java questions | 10 Java questions loaded | Pass |
| TC-11 | Quiz answer preservation | Select 'B' on Q1, click Next, click Prev | Q1 displays with Option B selected | Option B checked | Pass |
| TC-12 | 30-Second Timer progression | Wait 30 seconds on Q1 | Timer reaches 0; UI auto-advances to Q2 | Advances to Q2 | Pass |
| TC-13 | Final Question Timeout | Wait 30 seconds on Q10 | Timer reaches 0; Quiz automatically submits | Auto-submits to ResultFrame | Pass |
| TC-14 | Result calculation accuracy | Answer 8 correct, 2 wrong | Score: `8 / 10`, 80.0%, "Excellent!" | Accurate metrics displayed | Pass |
| TC-15 | Database disconnected handling | Stop MySQL service | Friendly error dialog without system crash | Graceful error handled | Pass |

---

## 10. Common Errors and Troubleshooting

1. **`CommunicationsException: Communications link failure`**:
   - *Cause*: MySQL server is not running, or wrong port (3306).
   - *Fix*: Open Windows Services (`services.msc`), find **MySQL80** or **MySQL**, and click **Start**.
2. **`Access denied for user 'root'@'localhost'`**:
   - *Cause*: Incorrect MySQL password in `DBConnection.java`.
   - *Fix*: Open `src/com/mcq/database/DBConnection.java` line 27 and set `PASSWORD = "your_actual_password"`.
3. **`ClassNotFoundException: com.mysql.cj.jdbc.Driver`**:
   - *Cause*: `mysql-connector-j-8.3.0.jar` is missing from the classpath.
   - *Fix*: In Eclipse, right-click project -> **Build Path** -> **Add External Archives...** -> choose `lib/mysql-connector-j-8.3.0.jar`.
4. **`Table 'online_mcq_quiz.questions' doesn't exist`**:
   - *Cause*: Database schema script has not been executed.
   - *Fix*: Run `database/schema.sql` inside MySQL Workbench or MySQL CLI.

---

## 11. Viva Voce Questions & Model Answers

**Q1: Why did you choose Java Swing over JavaFX or web technologies?**  
*Answer*: Java Swing is part of the standard Java desktop library (found directly in `javax.swing`), requiring no external browser dependencies, servlet containers, or third-party web frameworks. It is lightweight, cross-platform, and ideal for standard desktop applications and educational mini-projects.

**Q2: What is JDBC and what are its core components?**  
*Answer*: JDBC (Java Database Connectivity) is a standard Java API that bridges Java applications with relational databases. Its key components include `DriverManager` (manages database drivers), `Connection` (session between Java and DB), `PreparedStatement` (precompiled SQL statements), and `ResultSet` (tabular database output).

**Q3: Why is `PreparedStatement` preferred over standard `Statement`?**  
*Answer*: 
1. **Security**: `PreparedStatement` parameterizes inputs, treating user input strictly as literal values and completely neutralizing SQL Injection attacks.
2. **Performance**: Pre-compiled by the database engine, reducing parsing overhead during repetitive executions.

**Q4: How does the 30-second countdown timer work in Swing?**  
*Answer*: We use `javax.swing.Timer`, which fires action events on the Swing Event Dispatch Thread (EDT) at 1000-millisecond intervals. This guarantees thread safety when modifying GUI components like `JLabel` without triggering concurrency deadlocks.

**Q5: How are student answers preserved when clicking Next and Previous?**  
*Answer*: We maintain a `Map<Integer, String> selectedAnswers` where the key is the 0-based question index and the value is the chosen option ("A", "B", "C", or "D"). Before switching questions, the selected radio button is saved to the map; upon loading a question, the map is queried to restore the radio selection.

**Q6: What is the DAO pattern and why is it used?**  
*Answer*: DAO (Data Access Object) isolates the business and presentation logic from database persistence operations. Classes like `QuestionDAO` and `AdminDAO` encapsulate all SQL statements, so if database schemas or tables change, only the DAO class needs updates without altering the UI.

---

## 12. Project Presentation Slide Points

- **Slide 1: Title & Team**
  - Project: Online MCQ Quiz System
  - Technology: Java Swing + JDBC + MySQL
- **Slide 2: Problem Statement & Motivation**
  - Need for fast, paperless, automated assessment in programming fundamentals.
  - Removing student login barriers for maximum testing efficiency.
- **Slide 3: System Architecture & Modules**
  - Student Module: Start Quiz -> Language Selection -> Timed MCQs -> Score Summary.
  - Admin Module: Login -> CRUD Dashboard (Add, View, Update, Delete questions).
- **Slide 4: Key Features & Technical Highlights**
  - 30-second timer per question with auto-progression.
  - Answer preservation across navigation.
  - Randomized 10-question pool per attempt.
  - Prevention of SQL Injection via `PreparedStatement`.
- **Slide 5: Database Design**
  - Tables: `admin` and `questions`.
  - 40 comprehensive seeded questions across Java, HTML, CSS, JavaScript.
- **Slide 6: Demonstration & Screenshots**
  - Walkthrough of Home Frame, Quiz Execution, Score Calculation, and Admin Management.
- **Slide 7: Conclusion & Future Scope**
  - Successfully satisfies all functional requirements as a complete, lightweight Java mini-project.
  - Future expansion: PDF certificate generation and network client-server integration.
