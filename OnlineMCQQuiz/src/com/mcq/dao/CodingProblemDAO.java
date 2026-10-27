package com.mcq.dao;

import com.mcq.database.DBConnection;
import com.mcq.model.CodingProblem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CodingProblemDAO.java
 * Data Access Object for managing programming problems in the Problem Bank
 * and persisting student coding progress and submissions.
 */
public class CodingProblemDAO {

    /**
     * Inserts a new coding problem into the database.
     */
    public boolean addProblem(CodingProblem p) {
        String query = "INSERT INTO coding_problems (title, language, topic, difficulty, description, constraints, sample_input, sample_output, explanation, starter_code) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getTitle().trim());
            ps.setString(2, p.getLanguage().trim());
            ps.setString(3, p.getTopic().trim());
            ps.setString(4, p.getDifficulty().trim());
            ps.setString(5, p.getDescription().trim());
            ps.setString(6, p.getConstraints().trim());
            ps.setString(7, p.getSampleInput().trim());
            ps.setString(8, p.getSampleOutput().trim());
            ps.setString(9, (p.getExplanation() != null) ? p.getExplanation().trim() : "");
            ps.setString(10, (p.getStarterCode() != null) ? p.getStarterCode().trim() : "");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] addProblem failed: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing coding problem.
     */
    public boolean updateProblem(CodingProblem p) {
        String query = "UPDATE coding_problems SET title = ?, language = ?, topic = ?, difficulty = ?, "
                     + "description = ?, constraints = ?, sample_input = ?, sample_output = ?, "
                     + "explanation = ?, starter_code = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, p.getTitle().trim());
            ps.setString(2, p.getLanguage().trim());
            ps.setString(3, p.getTopic().trim());
            ps.setString(4, p.getDifficulty().trim());
            ps.setString(5, p.getDescription().trim());
            ps.setString(6, p.getConstraints().trim());
            ps.setString(7, p.getSampleInput().trim());
            ps.setString(8, p.getSampleOutput().trim());
            ps.setString(9, (p.getExplanation() != null) ? p.getExplanation().trim() : "");
            ps.setString(10, (p.getStarterCode() != null) ? p.getStarterCode().trim() : "");
            ps.setInt(11, p.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] updateProblem failed: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a coding problem by ID.
     */
    public boolean deleteProblem(int id) {
        String query = "DELETE FROM coding_problems WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] deleteProblem failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves a problem by ID.
     */
    public CodingProblem getProblemById(int id) {
        String query = "SELECT id, title, language, topic, difficulty, description, constraints, sample_input, sample_output, explanation, starter_code, created_at "
                     + "FROM coding_problems WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProblem(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] getProblemById failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a problem by ID including student progress and saved code.
     */
    public CodingProblem getProblemById(int id, int studentId) {
        String query = "SELECT p.id, p.title, p.language, p.topic, p.difficulty, p.description, p.constraints, "
                     + "p.sample_input, p.sample_output, p.explanation, p.starter_code, p.created_at, "
                     + "s.status, s.code_submission "
                     + "FROM coding_problems p "
                     + "LEFT JOIN student_problem_solutions s ON p.id = s.problem_id AND s.student_id = ? "
                     + "WHERE p.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ps.setInt(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CodingProblem p = mapResultSetToProblem(rs);
                    String status = rs.getString("status");
                    if (status != null && !status.trim().isEmpty()) {
                        p.setStudentStatus(status);
                    } else {
                        p.setStudentStatus("Unsolved");
                    }
                    p.setSavedUserCode(rs.getString("code_submission"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] getProblemById with studentId failed: " + e.getMessage());
        }

        CodingProblem fallback = getFallbackProblemById(id);
        if (fallback != null) {
            return fallback;
        }

        return null;
    }

    /**
     * Retrieves all coding problems without student-specific progress.
     */
    public List<CodingProblem> getAllProblems() {
        return getFilteredProblems("All", "All", "All", "", -1);
    }

    /**
     * Retrieves all coding problems with student progress.
     */
    public List<CodingProblem> getAllProblems(int studentId) {
        return getFilteredProblems("All", "All", "All", "", studentId);
    }

    /**
     * Retrieves filtered coding problems based on criteria, including student progress status.
     */
    public List<CodingProblem> getFilteredProblems(String language, String topic, String difficulty, String searchQuery, int studentId) {
        List<CodingProblem> list = new ArrayList<>();

        StringBuilder query = new StringBuilder(
            "SELECT p.id, p.title, p.language, p.topic, p.difficulty, p.description, p.constraints, "
            + "p.sample_input, p.sample_output, p.explanation, p.starter_code, p.created_at, "
            + "COALESCE(s.status, 'Unsolved') AS status, s.code_submission "
            + "FROM coding_problems p "
            + "LEFT JOIN student_problem_solutions s ON p.id = s.problem_id AND s.student_id = ? "
            + "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        params.add(studentId);

        if (language != null && !language.trim().isEmpty() && !language.equalsIgnoreCase("All")) {
            query.append(" AND p.language = ?");
            params.add(language.trim());
        }
        if (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("All")) {
            query.append(" AND p.topic = ?");
            params.add(topic.trim());
        }
        if (difficulty != null && !difficulty.trim().isEmpty() && !difficulty.equalsIgnoreCase("All")) {
            query.append(" AND p.difficulty = ?");
            params.add(difficulty.trim());
        }
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            query.append(" AND (p.title LIKE ? OR p.description LIKE ?)");
            String likePattern = "%" + searchQuery.trim() + "%";
            params.add(likePattern);
            params.add(likePattern);
        }

        query.append(" ORDER BY p.id ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CodingProblem p = mapResultSetToProblem(rs);
                    p.setStudentStatus(rs.getString("status"));
                    p.setSavedUserCode(rs.getString("code_submission"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] getFilteredProblems failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (list.isEmpty()) {
            list = getFallbackProblems(language, topic, difficulty, searchQuery);
        }

        return list;
    }

    /**
     * Saves or updates a student's solution or progress for a problem.
     */
    public boolean saveStudentSolution(int studentId, int problemId, String status, String code) {
        String query = "INSERT INTO student_problem_solutions (student_id, problem_id, status, code_submission) "
                     + "VALUES (?, ?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE status = VALUES(status), code_submission = VALUES(code_submission), last_updated = CURRENT_TIMESTAMP";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ps.setInt(2, problemId);
            ps.setString(3, (status != null && !status.trim().isEmpty()) ? status.trim() : "Attempted");
            ps.setString(4, code != null ? code : "");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] saveStudentSolution failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Counts how many problems a student has solved.
     */
    public int getStudentSolvedCount(int studentId) {
        String query = "SELECT COUNT(*) FROM student_problem_solutions WHERE student_id = ? AND status = 'Solved'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] getStudentSolvedCount failed: " + e.getMessage());
        }
        return 0;
    }

    public List<String> getAvailableLanguages() {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT language FROM coding_problems ORDER BY language ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(rs.getString("language"));
            }
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] getAvailableLanguages failed: " + e.getMessage());
        }
        if (list.isEmpty()) {
            list.addAll(Arrays.asList("Java", "Python", "C++", "JavaScript"));
        }
        return list;
    }

    public List<String> getAvailableTopics() {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT topic FROM coding_problems ORDER BY topic ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(rs.getString("topic"));
            }
        } catch (SQLException e) {
            System.err.println("[CodingProblemDAO Error] getAvailableTopics failed: " + e.getMessage());
        }
        return list;
    }

    public List<String> getAvailableDifficulties() {
        return Arrays.asList("Beginner", "Intermediate", "Advanced");
    }

    private CodingProblem mapResultSetToProblem(ResultSet rs) throws SQLException {
        return new CodingProblem(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("language"),
            rs.getString("topic"),
            rs.getString("difficulty"),
            rs.getString("description"),
            rs.getString("constraints"),
            rs.getString("sample_input"),
            rs.getString("sample_output"),
            rs.getString("explanation"),
            rs.getString("starter_code"),
            rs.getTimestamp("created_at")
        );
    }

    private List<CodingProblem> getFallbackProblems(String language, String topic, String difficulty, String searchQuery) {
        List<CodingProblem> all = createDefaultProblemsList();
        List<CodingProblem> filtered = new ArrayList<>();

        String search = (searchQuery != null) ? searchQuery.trim().toLowerCase() : "";

        for (CodingProblem p : all) {
            boolean matchLang = (language == null || language.equalsIgnoreCase("All") || p.getLanguage().equalsIgnoreCase(language));
            boolean matchTopic = (topic == null || topic.equalsIgnoreCase("All") || p.getTopic().equalsIgnoreCase(topic));
            boolean matchDiff = (difficulty == null || difficulty.equalsIgnoreCase("All") || p.getDifficulty().equalsIgnoreCase(difficulty));
            boolean matchSearch = search.isEmpty() || p.getTitle().toLowerCase().contains(search) || p.getDescription().toLowerCase().contains(search);

            if (matchLang && matchTopic && matchDiff && matchSearch) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    private CodingProblem getFallbackProblemById(int id) {
        List<CodingProblem> all = createDefaultProblemsList();
        for (CodingProblem p : all) {
            if (p.getId() == id) {
                return p;
            }
        }
        return all.isEmpty() ? null : all.get(0);
    }

    private List<CodingProblem> createDefaultProblemsList() {
        List<CodingProblem> list = new ArrayList<>();

        CodingProblem p1 = new CodingProblem(
            1, "Two Sum: Target Pair Finder", "Java", "Arrays", "Beginner",
            "Given an array of integers nums and an integer target, return the indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.",
            "• 2 <= nums.length <= 10^4\n• -10^9 <= nums[i] <= 10^9\n• Only one valid answer exists.",
            "nums = [2, 7, 11, 15], target = 9",
            "[0, 1]",
            "Because nums[0] + nums[1] == 2 + 7 == 9, we return [0, 1].",
            "public class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Write your solution here\n        return new int[0];\n    }\n}",
            new java.sql.Timestamp(System.currentTimeMillis())
        );
        p1.setStudentStatus("Unsolved");
        list.add(p1);

        CodingProblem p2 = new CodingProblem(
            2, "Palindrome String Validator", "Java", "Strings", "Beginner",
            "A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.",
            "• 1 <= s.length <= 2 * 10^5\n• Printable ASCII characters.",
            "s = \"A man, a plan, a canal: Panama\"",
            "true",
            "Reads identical forward and backward.",
            "public class Solution {\n    public boolean isPalindrome(String s) {\n        // Write your solution here\n        return false;\n    }\n}",
            new java.sql.Timestamp(System.currentTimeMillis())
        );
        p2.setStudentStatus("Unsolved");
        list.add(p2);

        CodingProblem p3 = new CodingProblem(
            3, "Valid Parentheses & Brackets", "Java", "Data Structures", "Intermediate",
            "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.",
            "• 1 <= s.length <= 10^4\n• Use Stack data structure.",
            "s = \"()[]{}\"",
            "true",
            "Every bracket is matched in LIFO order.",
            "public class Solution {\n    public boolean isValid(String s) {\n        // Write your solution here\n        return false;\n    }\n}",
            new java.sql.Timestamp(System.currentTimeMillis())
        );
        p3.setStudentStatus("Unsolved");
        list.add(p3);

        CodingProblem p4 = new CodingProblem(
            4, "Binary Search (Sorted Array)", "Java", "Searching", "Beginner",
            "Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums in O(log n) time.",
            "• 1 <= nums.length <= 10^4\n• Time Complexity Target: O(log n)",
            "nums = [-1,0,3,5,9,12], target = 9",
            "4",
            "Target 9 is present at index 4.",
            "public class Solution {\n    public int search(int[] nums, int target) {\n        // Write your solution here\n        return -1;\n    }\n}",
            new java.sql.Timestamp(System.currentTimeMillis())
        );
        p4.setStudentStatus("Unsolved");
        list.add(p4);

        CodingProblem p5 = new CodingProblem(
            5, "Factorial Trailing Zeroes", "Java", "Math & Logic", "Beginner",
            "Given an integer n, return the number of trailing zeroes in n! (n factorial) in logarithmic time.",
            "• 0 <= n <= 10^4\n• Time Complexity: O(log_5 n)",
            "n = 5",
            "1",
            "5! = 120, which has one trailing zero contributed by 5.",
            "public class Solution {\n    public int trailingZeroes(int n) {\n        // Write your solution here\n        return 0;\n    }\n}",
            new java.sql.Timestamp(System.currentTimeMillis())
        );
        p5.setStudentStatus("Unsolved");
        list.add(p5);

        CodingProblem p6 = new CodingProblem(
            6, "LRU Cache Implementation", "Java", "Data Structures", "Advanced",
            "Design a data structure that follows the constraints of a Least Recently Used (LRU) cache with O(1) get and put operations.",
            "• 1 <= capacity <= 3000\n• Both get and put in O(1) time.",
            "cache = new LRUCache(2); cache.put(1, 1); cache.get(1);",
            "[1]",
            "Key 1 accessed and marked most recently used.",
            "public class LRUCache {\n    public LRUCache(int capacity) {\n        // Initialize your data structure here\n    }\n    public int get(int key) {\n        // Write your solution here\n        return -1;\n    }\n    public void put(int key, int value) {\n        // Write your solution here\n    }\n}",
            new java.sql.Timestamp(System.currentTimeMillis())
        );
        p6.setStudentStatus("Unsolved");
        list.add(p6);

        return list;
    }
}
