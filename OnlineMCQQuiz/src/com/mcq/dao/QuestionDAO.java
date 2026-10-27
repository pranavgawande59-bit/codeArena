package com.mcq.dao;

import com.mcq.database.DBConnection;
import com.mcq.model.Question;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * QuestionDAO.java
 * Data Access Object for CRUD operations and filtered topic/difficulty queries on 'questions'.
 * Uses parameterized queries to ensure security and SQL injection defense.
 */
public class QuestionDAO {

    /**
     * Inserts a new Question into the database.
     */
    public boolean addQuestion(Question q) {
        String query = "INSERT INTO questions (language, topic, difficulty, marks, question, "
                     + "option_a, option_b, option_c, option_d, correct_answer, explanation) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, q.getLanguage().trim());
            ps.setString(2, (q.getTopic() != null && !q.getTopic().trim().isEmpty()) ? q.getTopic().trim() : "General");
            ps.setString(3, (q.getDifficulty() != null && !q.getDifficulty().trim().isEmpty()) ? q.getDifficulty().trim() : "Beginner");
            ps.setInt(4, (q.getMarks() > 0) ? q.getMarks() : 1);
            ps.setString(5, q.getQuestion().trim());
            ps.setString(6, q.getOptionA().trim());
            ps.setString(7, q.getOptionB().trim());
            ps.setString(8, q.getOptionC().trim());
            ps.setString(9, q.getOptionD().trim());
            ps.setString(10, q.getCorrectAnswer().trim().toUpperCase());
            ps.setString(11, (q.getExplanation() != null) ? q.getExplanation().trim() : "");

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] addQuestion failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all questions belonging to a specific programming language.
     */
    public List<Question> getQuestionsByLanguage(String language) {
        List<Question> list = new ArrayList<>();
        String query = "SELECT id, language, topic, difficulty, marks, question, option_a, option_b, option_c, option_d, correct_answer, explanation "
                     + "FROM questions WHERE language = ? ORDER BY id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, language);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToQuestion(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] getQuestionsByLanguage failed: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves a specified number of randomly selected questions for a given language.
     */
    public List<Question> getRandomQuestionsByLanguage(String language, int limit) {
        return getFilteredQuestions(language, "All", "All", limit);
    }

    /**
     * Counts how many questions exist in the database for a specific language.
     */
    public int getQuestionCountByLanguage(String language) {
        return getQuestionCount(language, "All", "All");
    }

    /**
     * Flexible multi-criteria question counter for Language, Topic, and Difficulty.
     */
    public int getQuestionCount(String language, String topic, String difficulty) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM questions WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (language != null && !language.trim().isEmpty() && !language.equalsIgnoreCase("All")) {
            query.append(" AND language = ?");
            params.add(language.trim());
        }
        if (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("All")) {
            query.append(" AND topic = ?");
            params.add(topic.trim());
        }
        if (difficulty != null && !difficulty.trim().isEmpty() && !difficulty.equalsIgnoreCase("All")) {
            query.append(" AND difficulty = ?");
            params.add(difficulty.trim());
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] getQuestionCount failed: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieves questions filtered by Language, Topic, and Difficulty,
     * randomized in Java to prevent repetitive question ordering.
     */
    public List<Question> getFilteredQuestions(String language, String topic, String difficulty, int limit) {
        List<Question> list = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT id, language, topic, difficulty, marks, question, option_a, option_b, option_c, option_d, correct_answer, explanation "
            + "FROM questions WHERE 1=1"
        );
        List<String> params = new ArrayList<>();

        if (language != null && !language.trim().isEmpty() && !language.equalsIgnoreCase("All")) {
            query.append(" AND language = ?");
            params.add(language.trim());
        }
        if (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("All")) {
            query.append(" AND topic = ?");
            params.add(topic.trim());
        }
        if (difficulty != null && !difficulty.trim().isEmpty() && !difficulty.equalsIgnoreCase("All")) {
            query.append(" AND difficulty = ?");
            params.add(difficulty.trim());
        }

        query.append(" ORDER BY id ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToQuestion(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] getFilteredQuestions failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (list.isEmpty()) {
            return list;
        }

        // Shuffle in memory for clean randomized quiz delivery
        Collections.shuffle(list);

        if (limit > 0 && list.size() > limit) {
            return new ArrayList<>(list.subList(0, limit));
        }
        return list;
    }

    /**
     * Retrieves all questions across all programming languages.
     */
    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        String query = "SELECT id, language, topic, difficulty, marks, question, option_a, option_b, option_c, option_d, correct_answer, explanation "
                     + "FROM questions ORDER BY id ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(mapResultSetToQuestion(rs));
            }

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] getAllQuestions failed: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves a single Question by its primary key ID.
     */
    public Question getQuestionById(int id) {
        String query = "SELECT id, language, topic, difficulty, marks, question, option_a, option_b, option_c, option_d, correct_answer, explanation "
                     + "FROM questions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToQuestion(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] getQuestionById failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing Question in the database.
     */
    public boolean updateQuestion(Question q) {
        String query = "UPDATE questions SET language = ?, topic = ?, difficulty = ?, marks = ?, "
                     + "question = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, "
                     + "correct_answer = ?, explanation = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, q.getLanguage().trim());
            ps.setString(2, (q.getTopic() != null && !q.getTopic().trim().isEmpty()) ? q.getTopic().trim() : "General");
            ps.setString(3, (q.getDifficulty() != null && !q.getDifficulty().trim().isEmpty()) ? q.getDifficulty().trim() : "Beginner");
            ps.setInt(4, (q.getMarks() > 0) ? q.getMarks() : 1);
            ps.setString(5, q.getQuestion().trim());
            ps.setString(6, q.getOptionA().trim());
            ps.setString(7, q.getOptionB().trim());
            ps.setString(8, q.getOptionC().trim());
            ps.setString(9, q.getOptionD().trim());
            ps.setString(10, q.getCorrectAnswer().trim().toUpperCase());
            ps.setString(11, (q.getExplanation() != null) ? q.getExplanation().trim() : "");
            ps.setInt(12, q.getId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] updateQuestion failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a Question from the database by its primary key ID.
     */
    public boolean deleteQuestion(int id) {
        String query = "DELETE FROM questions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] deleteQuestion failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves distinct languages present in questions.
     */
    public List<String> getAvailableLanguages() {
        List<String> languages = new ArrayList<>();
        String query = "SELECT DISTINCT language FROM questions ORDER BY language ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                languages.add(rs.getString("language"));
            }

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] getAvailableLanguages failed: " + e.getMessage());
        }

        if (languages.isEmpty()) {
            languages.addAll(Arrays.asList("Java", "HTML", "CSS", "JavaScript"));
        }

        return languages;
    }

    /**
     * Retrieves distinct topics, optionally filtered by language.
     */
    public List<String> getAvailableTopics(String language) {
        List<String> topics = new ArrayList<>();
        String query = (language == null || language.equalsIgnoreCase("All"))
            ? "SELECT DISTINCT topic FROM questions WHERE topic IS NOT NULL AND topic != '' ORDER BY topic ASC"
            : "SELECT DISTINCT topic FROM questions WHERE language = ? AND topic IS NOT NULL AND topic != '' ORDER BY topic ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            if (language != null && !language.equalsIgnoreCase("All")) {
                ps.setString(1, language);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    topics.add(rs.getString("topic"));
                }
            }

        } catch (SQLException e) {
            System.err.println("[QuestionDAO Error] getAvailableTopics failed: " + e.getMessage());
        }

        return topics;
    }

    /**
     * Returns standard difficulty levels.
     */
    public List<String> getAvailableDifficulties() {
        return Arrays.asList("Beginner", "Intermediate", "Advanced");
    }

    private Question mapResultSetToQuestion(ResultSet rs) throws SQLException {
        return new Question(
            rs.getInt("id"),
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
    }
}
