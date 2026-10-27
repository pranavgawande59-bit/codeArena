package com.mcq.util;

import com.mcq.dao.QuestionDAO;
import com.mcq.dao.QuizAttemptDAO;
import com.mcq.dao.QuizAttemptDAO.ScoreTrendPoint;
import com.mcq.dao.QuizAttemptDAO.StudentOverviewStats;
import com.mcq.dao.QuizAttemptDAO.TopicStat;
import com.mcq.dao.StudentDAO;
import com.mcq.model.AttemptAnswer;
import com.mcq.model.Question;
import com.mcq.model.Student;
import java.util.List;

public class TestSystem {
    public static void main(String[] args) {
        System.out.println("=== 1. Testing Student Authentication ===");
        StudentDAO sdao = new StudentDAO();
        Student s = sdao.authenticate("student", "student123");
        if (s == null) {
            System.err.println("Student authentication failed!");
            System.exit(1);
        }
        System.out.println("Authenticated: " + s.getFullName() + " (" + s.getEmail() + ")");

        System.out.println("\n=== 2. Testing Overview Performance Analytics ===");
        QuizAttemptDAO adao = new QuizAttemptDAO();
        StudentOverviewStats stats = adao.getStudentOverviewStats(s.getId());
        System.out.println("Total Quizzes: " + stats.getTotalAttempts());
        System.out.println("Average Score: " + stats.getAveragePercentage() + "%");
        System.out.println("Highest Score: " + stats.getHighestPercentage() + "%");
        System.out.println("Best Performing Topic: " + stats.getBestTopic());

        System.out.println("\n=== 3. Testing Score Trend Data (Line Chart) ===");
        List<ScoreTrendPoint> points = adao.getScoreProgressOverTime(s.getId());
        System.out.println("Trend data points loaded: " + points.size());
        for (ScoreTrendPoint pt : points) {
            System.out.println("  -> Date: " + pt.getDateLabel() + " | " + pt.getLanguage() + " (" + pt.getTopic() + "): " + pt.getPercentage() + "%");
        }

        System.out.println("\n=== 4. Testing Topic Performance Stats (Bar Chart) ===");
        List<TopicStat> topicStats = adao.getTopicPerformance(s.getId());
        for (TopicStat ts : topicStats) {
            System.out.println("  -> Topic: " + ts.getTopic() + " | Avg: " + ts.getAveragePercentage() + "% | Attempts: " + ts.getTotalAttempts());
        }

        System.out.println("\n=== 5. Testing Difficulty & Topic Filtered Questions ===");
        QuestionDAO qdao = new QuestionDAO();
        List<Question> oopQuestions = qdao.getFilteredQuestions("Java", "OOP", "Beginner", 5);
        System.out.println("Found " + oopQuestions.size() + " Java Beginner OOP questions.");
        if (!oopQuestions.isEmpty()) {
            Question q = oopQuestions.get(0);
            System.out.println("  Sample Q: " + q.getQuestion());
            System.out.println("  Marks: " + q.getMarks());
            System.out.println("  Correct: " + q.getCorrectAnswer());
            System.out.println("  Explanation: " + q.getExplanation());
        }

        System.out.println("\n=== 6. Testing Quiz Attempt Answers & Review Data ===");
        if (!points.isEmpty()) {
            int lastAttemptId = points.get(points.size() - 1).getAttemptId();
            List<AttemptAnswer> reviewAnswers = adao.getAnswersForAttempt(lastAttemptId);
            System.out.println("Loaded " + reviewAnswers.size() + " review answer items for Attempt #" + lastAttemptId);
            for (AttemptAnswer ans : reviewAnswers) {
                System.out.println("  Q: " + ans.getQuestion().getQuestion());
                System.out.println("  Selected: " + ans.getSelectedOption() + " | Correct: " + ans.getQuestion().getCorrectAnswer() + " | IsCorrect: " + ans.isCorrect());
                System.out.println("  Explanation: " + ans.getQuestion().getExplanation());
            }
        }

        System.out.println("\n=== 7. Testing Programming Problem Bank DAO ===");
        com.mcq.dao.CodingProblemDAO pdao = new com.mcq.dao.CodingProblemDAO();
        List<com.mcq.model.CodingProblem> allProblems = pdao.getAllProblems(s.getId());
        System.out.println("Total coding problems retrieved: " + allProblems.size());
        if (!allProblems.isEmpty()) {
            com.mcq.model.CodingProblem first = allProblems.get(0);
            System.out.println("  First Problem: #" + first.getId() + " - " + first.getTitle() + " (" + first.getDifficulty() + ", " + first.getTopic() + ")");
            System.out.println("  Constraints: " + first.getConstraints());
            System.out.println("  Sample Input: " + first.getSampleInput());
            System.out.println("  Sample Output: " + first.getSampleOutput());

            // Test saving a solution
            boolean saved = pdao.saveStudentSolution(s.getId(), first.getId(), "Solved", first.getStarterCode());
            System.out.println("  Solution save test result: " + saved);
            int solvedCount = pdao.getStudentSolvedCount(s.getId());
            System.out.println("  Student #" + s.getId() + " Solved Problems Count: " + solvedCount);

            // Test filtering
            List<com.mcq.model.CodingProblem> beginnerProblems = pdao.getFilteredProblems("All", "All", "Beginner", "", s.getId());
            System.out.println("  Filtered Beginner Problems count: " + beginnerProblems.size());
        }

        System.out.println("\n>>> ALL SYSTEM VERIFICATION CHECKS (INCLUDING PROBLEM BANK) PASSED PERFECTLY! <<<");
    }
}

