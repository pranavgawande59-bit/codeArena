package com.mcq.ui;

import com.mcq.dao.QuestionDAO;
import com.mcq.model.Question;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * AdminDashboardFrame.java
 * Central management hub for administrative MCQ operations:
 * Add, View, Update, Delete questions, Question/Topic Distribution Stats, and Logout.
 */
public class AdminDashboardFrame extends JFrame {

    private final QuestionDAO questionDAO;

    public AdminDashboardFrame() {
        this.questionDAO = new QuestionDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Admin Dashboard - Online MCQ Quiz");
        setSize(620, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(22, 20, 20, 20));
        headerPanel.setLayout(new BorderLayout(0, 5));

        JLabel lblTitle = new JLabel("ADMINISTRATIVE DASHBOARD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Manage questions, categories, topics, difficulty distribution, and explanations", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Admin Action Buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 30, 8, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Button 1: Add Question
        JButton btnAdd = HomeFrame.createStyledButton("Add New Question", new Color(13, 110, 253), Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 0;
        centerPanel.add(btnAdd, gbc);

        // Button 2: View Questions Directory
        JButton btnView = HomeFrame.createStyledButton("View & Filter Questions", new Color(23, 162, 184), Color.WHITE);
        btnView.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 1;
        centerPanel.add(btnView, gbc);

        // Button 3: Update Question
        JButton btnUpdate = HomeFrame.createStyledButton("Update Question by ID", new Color(255, 153, 0), Color.WHITE);
        btnUpdate.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 2;
        centerPanel.add(btnUpdate, gbc);

        // Button 4: Manage Programming Problem Bank
        JButton btnManageProblems = HomeFrame.createStyledButton("Manage Problem Bank", new Color(0, 128, 128), Color.WHITE);
        btnManageProblems.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 3;
        centerPanel.add(btnManageProblems, gbc);

        // Button 5: Topic & Difficulty Distribution Stats
        JButton btnStats = HomeFrame.createStyledButton("Topic & Difficulty Distribution", new Color(111, 66, 193), Color.WHITE);
        btnStats.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 4;
        centerPanel.add(btnStats, gbc);

        // Button 6: Delete Question
        JButton btnDelete = HomeFrame.createStyledButton("Delete Question", new Color(220, 53, 69), Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 5;
        centerPanel.add(btnDelete, gbc);

        // Button 7: Logout
        JButton btnLogout = HomeFrame.createStyledButton("Logout", new Color(108, 117, 125), Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 6;
        centerPanel.add(btnLogout, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- Action Listeners ---
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddQuestionFrame(AdminDashboardFrame.this).setVisible(true);
                AdminDashboardFrame.this.setVisible(false);
            }
        });

        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewQuestionFrame(AdminDashboardFrame.this).setVisible(true);
                AdminDashboardFrame.this.setVisible(false);
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateQuestionFrame(AdminDashboardFrame.this).setVisible(true);
                AdminDashboardFrame.this.setVisible(false);
            }
        });

        btnManageProblems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminProblemManagementFrame(AdminDashboardFrame.this).setVisible(true);
                AdminDashboardFrame.this.setVisible(false);
            }
        });

        btnStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestionDistributionStats();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptDeleteQuestion();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    AdminDashboardFrame.this,
                    "Are you sure you want to log out of the Admin panel?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    AdminDashboardFrame.this.dispose();
                    new HomeFrame().setVisible(true);
                }
            }
        });
    }

    private void showQuestionDistributionStats() {
        int total = questionDAO.getQuestionCount("All", "All", "All");
        int beginner = questionDAO.getQuestionCount("All", "All", "Beginner");
        int intermediate = questionDAO.getQuestionCount("All", "All", "Intermediate");
        int advanced = questionDAO.getQuestionCount("All", "All", "Advanced");

        List<String> topics = questionDAO.getAvailableTopics("All");
        StringBuilder topicListStr = new StringBuilder();
        int shown = 0;
        for (String t : topics) {
            int c = questionDAO.getQuestionCount("All", t, "All");
            topicListStr.append("  • ").append(t).append(": ").append(c).append(" questions\n");
            shown++;
            if (shown >= 8) {
                int remaining = topics.size() - shown;
                if (remaining > 0) {
                    topicListStr.append("  ... and ").append(remaining).append(" more topics\n");
                }
                break;
            }
        }

        String msg = "====================================\n"
                   + "  MCQ REPOSITORY METRICS & HEALTH\n"
                   + "====================================\n\n"
                   + "Total Questions in Database: " + total + "\n\n"
                   + "--- Difficulty Breakdown ---\n"
                   + "  • Beginner     : " + beginner + " questions\n"
                   + "  • Intermediate : " + intermediate + " questions\n"
                   + "  • Advanced     : " + advanced + " questions\n\n"
                   + "--- Top Topics ---\n"
                   + topicListStr
                   + "\nAll questions include verified correct answers & learning explanations.";

        JOptionPane.showMessageDialog(this, msg, "Repository Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void promptDeleteQuestion() {
        String input = JOptionPane.showInputDialog(
            this,
            "Enter the ID of the Question you want to delete\n(or click 'View & Filter Questions' to browse all IDs):",
            "Delete Question",
            JOptionPane.QUESTION_MESSAGE
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int qId = Integer.parseInt(input.trim());
            Question q = questionDAO.getQuestionById(qId);
            if (q == null) {
                JOptionPane.showMessageDialog(this, "No question found with ID: " + qId, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this question?\n\n"
                + "ID: " + q.getId() + "\n"
                + "Language: " + q.getLanguage() + "\n"
                + "Topic: " + q.getTopic() + "\n"
                + "Question: " + q.getQuestion(),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = questionDAO.deleteQuestion(qId);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Question #" + qId + " successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete question.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Question ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}
