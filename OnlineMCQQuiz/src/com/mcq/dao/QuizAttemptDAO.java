package com.mcq.dao;

import com.mcq.database.DBConnection;
import com.mcq.model.AttemptAnswer;
import com.mcq.model.Question;
import com.mcq.model.QuizAttempt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * QuizAttemptDAO.java
 * Handles transaction-safe persistence of quiz attempts, per-question answers,
 * and high-performance SQL analytics aggregation for the Student Dashboard.
 */
public class QuizAttemptDAO {

    /**
     * DTO for score trend points used by LineChartPanel.
     */
    public static class ScoreTrendPoint {
        private final int attemptId;
        private final String dateLabel;
        private final double percentage;
        private final String language;
        private final String topic;

        public ScoreTrendPoint(int attemptId, String dateLabel, double percentage, String language, String topic) {
            this.attemptId = attemptId;
            this.dateLabel = dateLabel;
            this.percentage = percentage;
            this.language = language;
            this.topic = topic;
        }

        public int getAttemptId() { return attemptId; }
        public String getDateLabel() { return dateLabel; }
        public double getPercentage() { return percentage; }
        public String getLanguage() { return language; }
        public String getTopic() { return topic; }
    }

    /**
     * DTO for topic mastery stats used by BarChartPanel.
     */
    public static class TopicStat {
        private final String topic;
        private final double averagePercentage;
        private final int totalAttempts;

        public TopicStat(String topic, double averagePercentage, int totalAttempts) {
            this.topic = topic;
            this.averagePercentage = averagePercentage;
            this.totalAttempts = totalAttempts;
        }

        public String getTopic() { return topic; }
        public double getAveragePercentage() { return averagePercentage; }
        public int getTotalAttempts() { return totalAttempts; }
    }

    /**
     * DTO for student overview KPI cards.
     */
    public static class StudentOverviewStats {
        private final int totalAttempts;
        private final double averagePercentage;
        private final double highestPercentage;
        private final int totalQuestionsAnswered;
        private final String bestTopic;

        public StudentOverviewStats(int totalAttempts, double averagePercentage, double highestPercentage,
                                    int totalQuestionsAnswered, String bestTopic) {
            this.totalAttempts = totalAttempts;
            this.averagePercentage = averagePercentage;
            this.highestPercentage = highestPercentage;
            this.totalQuestionsAnswered = totalQuestionsAnswered;
            this.bestTopic = bestTopic;
        }

        public int getTotalAttempts() { return totalAttempts; }
        public double getAveragePercentage() { return averagePercentage; }
        public double getHighestPercentage() { return highestPercentage; }
        public int getTotalQuestionsAnswered() { return totalQuestionsAnswered; }
        public String getBestTopic() { return bestTopic; }
    }

    /**
     * Saves a quiz attempt and all corresponding answer records in a single ACID transaction.
     * 
     * @param attempt QuizAttempt object
     * @param answers List of AttemptAnswer records
     * @return Generated attempt ID (>0) if successful, -1 otherwise
     */
    public int saveAttempt(QuizAttempt attempt, List<AttemptAnswer> answers) {
        String attemptQuery = "INSERT INTO quiz_attempts (student_id, language, topic, difficulty, "
                            + "total_questions, correct_answers, wrong_answers, skipped, score, "
                            + "total_marks, percentage, feedback_message) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String answerQuery = "INSERT INTO attempt_answers (attempt_id, question_id, selected_option, is_correct, marks_obtained) "
                           + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin ACID transaction

            int attemptId = -1;
            try (PreparedStatement psAttempt = conn.prepareStatement(attemptQuery, Statement.RETURN_GENERATED_KEYS)) {
                psAttempt.setInt(1, attempt.getStudentId());
                psAttempt.setString(2, attempt.getLanguage());
                psAttempt.setString(3, attempt.getTopic());
                psAttempt.setString(4, attempt.getDifficulty());
                psAttempt.setInt(5, attempt.getTotalQuestions());
                psAttempt.setInt(6, attempt.getCorrectAnswers());
                psAttempt.setInt(7, attempt.getWrongAnswers());
                psAttempt.setInt(8, attempt.getSkipped());
                psAttempt.setInt(9, attempt.getScore());
                psAttempt.setInt(10, attempt.getTotalMarks());
                psAttempt.setDouble(11, attempt.getPercentage());
                psAttempt.setString(12, attempt.getFeedbackMessage());

                psAttempt.executeUpdate();

                try (ResultSet rs = psAttempt.getGeneratedKeys()) {
                    if (rs.next()) {
                        attemptId = rs.getInt(1);
                        attempt.setId(attemptId);
                    }
                }
            }

            if (attemptId > 0 && answers != null && !answers.isEmpty()) {
                try (PreparedStatement psAnswer = conn.prepareStatement(answerQuery)) {
                    for (AttemptAnswer ans : answers) {
                        psAnswer.setInt(1, attemptId);
                        psAnswer.setInt(2, ans.getQuestionId());
                        if (ans.getSelectedOption() != null && !ans.getSelectedOption().trim().isEmpty()) {
                            psAnswer.setString(3, ans.getSelectedOption().trim().toUpperCase());
                        } else {
                            psAnswer.setNull(3, java.sql.Types.CHAR);
                        }
                        psAnswer.setBoolean(4, ans.isCorrect());
                        psAnswer.setInt(5, ans.getMarksObtained());
                        psAnswer.addBatch();
                    }
                    psAnswer.executeBatch();
                }
            }

            conn.commit(); // Transaction successful
            return attemptId;

        } catch (SQLException e) {
            System.err.println("[QuizAttemptDAO Error] saveAttempt transaction failed: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves all quiz attempts for a specific student, ordered by attempt_date DESC.
     */
    public List<QuizAttempt> getAttemptsByStudentId(int studentId) {
        List<QuizAttempt> list = new ArrayList<>();
        String query = "SELECT id, student_id, language, topic, difficulty, total_questions, correct_answers, "
                     + "wrong_answers, skipped, score, total_marks, percentage, feedback_message, attempt_date "
                     + "FROM quiz_attempts WHERE student_id = ? ORDER BY attempt_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new QuizAttempt(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getString("language"),
                        rs.getString("topic"),
                        rs.getString("difficulty"),
                        rs.getInt("total_questions"),
                        rs.getInt("correct_answers"),
                        rs.getInt("wrong_answers"),
                        rs.getInt("skipped"),
                        rs.getInt("score"),
                        rs.getInt("total_marks"),
                        rs.getDouble("percentage"),
                        rs.getString("feedback_message"),
                        rs.getTimestamp("attempt_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizAttemptDAO Error] getAttemptsByStudentId failed: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves attempt details by attempt ID.
     */
    public QuizAttempt getAttemptById(int attemptId) {
        String query = "SELECT id, student_id, language, topic, difficulty, total_questions, correct_answers, "
                     + "wrong_answers, skipped, score, total_marks, percentage, feedback_message, attempt_date "
                     + "FROM quiz_attempts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, attemptId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new QuizAttempt(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getString("language"),
                        rs.getString("topic"),
                        rs.getString("difficulty"),
                        rs.getInt("total_questions"),
                        rs.getInt("correct_answers"),
                        rs.getInt("wrong_answers"),
                        rs.getInt("skipped"),
                        rs.getInt("score"),
                        rs.getInt("total_marks"),
                        rs.getDouble("percentage"),
                        rs.getString("feedback_message"),
                        rs.getTimestamp("attempt_date")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizAttemptDAO Error] getAttemptById failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves full question and answer details for an attempt,
     * joining questions to load explanations and correct options for the Quiz Review screen.
     */
    public List<AttemptAnswer> getAnswersForAttempt(int attemptId) {
        List<AttemptAnswer> list = new ArrayList<>();
        String query = "SELECT a.id AS answer_id, a.attempt_id, a.question_id, a.selected_option, "
                     + "a.is_correct, a.marks_obtained, "
                     + "q.language, q.topic, q.difficulty, q.marks, q.question, "
                     + "q.option_a, q.option_b, q.option_c, q.option_d, q.correct_answer, q.explanation "
                     + "FROM attempt_answers a "
                     + "JOIN questions q ON a.question_id = q.id "
                     + "WHERE a.attempt_id = ? "
                     + "ORDER BY a.id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, attemptId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AttemptAnswer ans = new AttemptAnswer(
                        rs.getInt("answer_id"),
                        rs.getInt("attempt_id"),
                        rs.getInt("question_id"),
                        rs.getString("selected_option"),
                        rs.getBoolean("is_correct"),
                        rs.getInt("marks_obtained")
                    );

                    Question q = new Question(
                        rs.getInt("question_id"),
                        rs.getString("language"),
                        rs.getString("topic"),
                        rs.getString("difficulty"),
                        rs.getInt("marks"),
                        rs.getString("question"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer"),
                        rs.getString("explanation")
                    );

                    ans.setQuestion(q);
                    list.add(ans);
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizAttemptDAO Error] getAnswersForAttempt failed: " + e.getMessage());
        }
        return list;
    }

    /**
     * Aggregates high-level performance metrics for a student.
     */
    public StudentOverviewStats getStudentOverviewStats(int studentId) {
        String mainQuery = "SELECT COUNT(*) AS total_attempts, "
                         + "COALESCE(AVG(percentage), 0) AS avg_pct, "
                         + "COALESCE(MAX(percentage), 0) AS max_pct, "
                         + "COALESCE(SUM(total_questions), 0) AS total_q "
                         + "FROM quiz_attempts WHERE student_id = ?";

        String bestTopicQuery = "SELECT topic FROM quiz_attempts WHERE student_id = ? "
                              + "GROUP BY topic ORDER BY AVG(percentage) DESC, COUNT(*) DESC LIMIT 1";

        int totalAttempts = 0;
        double avgPct = 0.0;
        double maxPct = 0.0;
        int totalQ = 0;
        String bestTopic = "None";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(mainQuery)) {
                ps.setInt(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalAttempts = rs.getInt("total_attempts");
                        avgPct = rs.getDouble("avg_pct");
                        maxPct = rs.getDouble("max_pct");
                        totalQ = rs.getInt("total_q");
                    }
                }
            }

            if (totalAttempts > 0) {
                try (PreparedStatement ps = conn.prepareStatement(bestTopicQuery)) {
                    ps.setInt(1, studentId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            bestTopic = rs.getString("topic");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizAttemptDAO Error] getStudentOverviewStats failed: " + e.getMessage());
        }

        return new StudentOverviewStats(totalAttempts, avgPct, maxPct, totalQ, bestTopic);
    }

    /**
     * Retrieves chronological quiz score points for trend/line charts.
     */
    public List<ScoreTrendPoint> getScoreProgressOverTime(int studentId) {
        List<ScoreTrendPoint> points = new ArrayList<>();
        String query = "SELECT id, percentage, language, topic, attempt_date "
                     + "FROM quiz_attempts WHERE student_id = ? ORDER BY attempt_date ASC, id ASC";

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String dateStr = (rs.getTimestamp("attempt_date") != null)
                        ? sdf.format(rs.getTimestamp("attempt_date"))
                        : "Attempt #" + rs.getInt("id");

                    points.add(new ScoreTrendPoint(
                        rs.getInt("id"),
                        dateStr,
                        rs.getDouble("percentage"),
                        rs.getString("language"),
                        rs.getString("topic")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizAttemptDAO Error] getScoreProgressOverTime failed: " + e.getMessage());
        }
        return points;
    }

    /**
     * Retrieves average score percentages aggregated across distinct topics for bar charts.
     */
    public List<TopicStat> getTopicPerformance(int studentId) {
        List<TopicStat> list = new ArrayList<>();
        String query = "SELECT topic, AVG(percentage) AS avg_pct, COUNT(*) AS attempts_count "
                     + "FROM quiz_attempts WHERE student_id = ? GROUP BY topic ORDER BY avg_pct DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TopicStat(
                        rs.getString("topic"),
                        rs.getDouble("avg_pct"),
                        rs.getInt("attempts_count")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizAttemptDAO Error] getTopicPerformance failed: " + e.getMessage());
        }
        return list;
    }
}
