# Online MCQ Quiz System

A complete, standalone Java desktop application built using **Java Swing**, **JDBC (Java Database Connectivity)**, and **MySQL Database**. Designed specifically as a college mini-project with clean object-oriented architecture, modular DAO layers, robust input validation, and a modern, user-friendly interface.

---

## 1. Project Overview

The **Online MCQ Quiz System** is designed with two distinct modules:

1. **Student Module**:
   - Zero registration or login required.
   - Immediate access upon opening the application.
   - Selection of programming languages: **Java**, **HTML**, **CSS**, **JavaScript** (dynamically expandable).
   - 10 randomized multiple-choice questions per quiz session.
   - Interactive 30-second countdown timer per question using `javax.swing.Timer`.
   - Previous and Next navigation with state preservation of selected answers.
   - Automatic question progression on timeout and automatic submission on the final question.
   - Detailed result calculation showing total questions, correct answers, wrong answers, skipped count, final score, percentage, and performance feedback.

2. **Admin Module**:
   - Secure authentication with username and password.
   - Administrative dashboard for managing question banks.
   - **CRUD Operations**:
     - **Add Question**: Form with validation for question text, options A-D, language, and correct answer.
     - **View Questions**: Sortable, filterable `JTable` with language category filter.
     - **Update Question**: Load question by ID or table selection, edit attributes, and save changes.
     - **Delete Question**: Delete question with a confirmation dialog.
   - Secure logout returning to the home screen.

---

## 2. Technology Stack & Constraints

| Component | Technology |
|---|---|
| **Programming Language** | Java (JDK 17 or later, compatible with Java 21) |
| **GUI Framework** | Java Swing & AWT (`JFrame`, `JPanel`, `JRadioButton`, `JTable`, `Timer`) |
| **Database** | MySQL Server 8.0+ |
| **Connectivity** | JDBC (`java.sql.*`, `PreparedStatement`) |
| **Driver** | MySQL Connector/J 8.3.0 (`lib/mysql-connector-j-8.3.0.jar`) |
| **IDE** | Eclipse IDE for Java Developers |

> **No Frameworks Used**: Pure standard Java SE. No Spring Boot, Hibernate, Servlets, JSP, or Web APIs are used, maintaining full compliance with standalone Java Swing desktop project requirements.

---

## 3. Architecture

```text
+-------------------------------------------------------+
|                 Java Swing Presentation UI            |
|  HomeFrame | LanguageFrame | QuizFrame | ResultFrame  |
|  AdminLoginFrame | AdminDashboardFrame | Add/View/Upd |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|                    Business & Util                    |
|       Question Model | QuizResult | QuizTimer         |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|                       DAO Layer                       |
|           QuestionDAO       |       AdminDAO          |
|      (PreparedStatement)    |  (PreparedStatement)   |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|                    Database Layer                     |
|                  DBConnection.java                    |
|                     (JDBC API)                        |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|                     MySQL Database                    |
|                   `online_mcq_quiz`                   |
|                Tables: `admin`, `questions`           |
+-------------------------------------------------------+
```

---

## 4. Project Directory Structure

```text
OnlineMCQQuiz/
├── .classpath                          # Eclipse build path configuration
├── .project                            # Eclipse project descriptor
├── build.bat                           # Windows batch compiler
├── run.bat                             # Windows batch launcher
├── README.md                           # Documentation & manual
├── database/
│   └── schema.sql                      # Database creation + 40 sample questions
├── lib/
│   └── mysql-connector-j-8.3.0.jar     # Official MySQL JDBC Driver
└── src/
    └── com/mcq/
        ├── Main.java                   # Application entry point
        ├── database/
        │   └── DBConnection.java       # Centralized JDBC connection manager
        ├── model/
        │   ├── Question.java           # MCQ question entity
        │   └── QuizResult.java         # Quiz evaluation entity
        ├── dao/
        │   ├── AdminDAO.java           # Admin authentication DAO
        │   └── QuestionDAO.java        # Question CRUD DAO
        ├── ui/
        │   ├── HomeFrame.java          # Home landing screen
        │   ├── LanguageFrame.java      # Language chooser
        │   ├── QuizFrame.java          # Timed MCQ quiz screen
        │   ├── ResultFrame.java        # Score & evaluation screen
        │   ├── AdminLoginFrame.java    # Administrator login portal
        │   ├── AdminDashboardFrame.java# Admin navigation hub
        │   ├── AddQuestionFrame.java   # Question creation form
        │   ├── ViewQuestionFrame.java  # Questions table & filter
        │   └── UpdateQuestionFrame.java# Question modification form
        └── util/
            └── QuizTimer.java          # 30-second swing timer wrapper
```

---

## 5. Setup & Installation Guide

### Step 1: Install Required Software
1. **Java JDK 17 or higher** (e.g., OpenJDK 17, 21):
   - Verify by running `java -version` and `javac -version` in your terminal.
2. **MySQL Server** (Community Edition 8.0+):
   - Ensure the MySQL service is started.
3. **Eclipse IDE for Java Developers**:
   - Download and install from [eclipse.org](https://www.eclipse.org/).

### Step 2: Set Up MySQL Database
1. Open MySQL Command Line Client or MySQL Workbench.
2. Log in with your `root` user and password.
3. Run the provided database script:
   ```sql
   source /path/to/OnlineMCQQuiz/database/schema.sql;
   ```
   Or copy and paste the contents of `database/schema.sql` into MySQL Workbench and execute.
4. This creates:
   - Database: `online_mcq_quiz`
   - Table: `admin` with default credentials (`admin` / `admin123`)
   - Table: `questions` with 40 questions (10 Java, 10 HTML, 10 CSS, 10 JavaScript)

### Step 3: Configure Database Password in Java
Open `src/com/mcq/database/DBConnection.java`:
```java
// Line 27:
private static final String PASSWORD = "your_password";
```
Change `"your_password"` to your actual MySQL root password (e.g., `"root"`, `"admin123"`, or `""`).

---

## 6. How to Import and Run in Eclipse IDE

1. Open **Eclipse IDE**.
2. Go to **File** -> **Import...**
3. Select **General** -> **Existing Projects into Workspace** and click **Next**.
4. Choose **Select root directory** and browse to `D:\CODE-ARENA\OnlineMCQQuiz`.
5. Ensure `OnlineMCQQuiz` is checked, and click **Finish**.
6. **Verify Build Path & MySQL Connector**:
   - Right-click project `OnlineMCQQuiz` -> **Build Path** -> **Configure Build Path...**
   - Under the **Libraries** tab, verify `mysql-connector-j-8.3.0.jar` is present under `lib/`.
7. **Run the Application**:
   - In Project Explorer, expand `src` -> `com.mcq`.
   - Right-click `Main.java` -> **Run As** -> **Java Application**.

---

## 7. How to Compile and Run from Command Prompt

If you prefer running without Eclipse:

1. Open PowerShell or Command Prompt in the `OnlineMCQQuiz` folder:
   ```cmd
   cd D:\CODE-ARENA\OnlineMCQQuiz
   ```
2. Build the project:
   ```cmd
   build.bat
   ```
3. Run the application:
   ```cmd
   run.bat
   ```

---

## 8. Default Credentials

- **Admin Username**: `admin`
- **Admin Password**: `admin123`
- **Student Access**: No credentials needed. Direct click on **Start Quiz**.
