package com.mcq.ui;

import com.mcq.model.QuizResult;
import com.mcq.model.StudentSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * ResultFrame.java
 * Displays comprehensive score metrics and performance feedback after quiz submission.
 * Enables direct transition to QuizReviewFrame for question-by-question analysis with explanations,
 * and seamless navigation to the Student Dashboard.
 */
public class ResultFrame extends JFrame {

    private final String language;
    private final QuizResult result;

    public ResultFrame(String language, QuizResult result) {
        this.language = language;
        this.result = result;
        initUI();
    }

    private void initUI() {
        setTitle("Quiz Result - " + language);
        setSize(650, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 18, 20));
        headerPanel.setLayout(new BorderLayout(0, 5));

        JLabel lblTitle = new JLabel("QUIZ RESULT & SCORECARD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        String subText = "Language: " + language + "  |  Topic: " + result.getTopic() + "  |  Difficulty: " + result.getDifficulty();
        JLabel lblSub = new JLabel(subText, SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(255, 193, 7)); // Gold accent

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Content Card (Score Table / Metrics)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 25, 10, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Result Card container
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 220, 235), 1),
            BorderFactory.createEmptyBorder(18, 32, 18, 32)
        ));

        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 15, 5, 15);
        cgbc.anchor = GridBagConstraints.WEST;

        // Metric rows
        addMetricRow(cardPanel, cgbc, 0, "Total Questions :", String.valueOf(result.getTotalQuestions()), new Color(50, 50, 50));
        addMetricRow(cardPanel, cgbc, 1, "Correct Answers :", String.valueOf(result.getCorrectAnswers()), new Color(40, 167, 69));
        addMetricRow(cardPanel, cgbc, 2, "Wrong Answers   :", String.valueOf(result.getWrongAnswers()), new Color(220, 53, 69));
        addMetricRow(cardPanel, cgbc, 3, "Skipped         :", String.valueOf(result.getSkipped()), new Color(108, 117, 125));

        // Separator line
        cgbc.gridy = 4;
        cgbc.gridx = 0;
        cgbc.gridwidth = 2;
        JLabel lblSep = new JLabel("----------------------------------------------------------------", SwingConstants.CENTER);
        lblSep.setForeground(new Color(200, 205, 215));
        cardPanel.add(lblSep, cgbc);

        cgbc.gridwidth = 1;
        addMetricRow(cardPanel, cgbc, 5, "Total Marks Earned :", result.getScore() + " / " + result.getTotalMarks(), new Color(13, 110, 253));
        addMetricRow(cardPanel, cgbc, 6, "Percentage         :", String.format("%.1f%%", result.getPercentage()), new Color(13, 110, 253));

        // Feedback / Remark label
        cgbc.gridy = 7;
        cgbc.gridx = 0;
        cgbc.gridwidth = 2;
        cgbc.insets = new Insets(10, 15, 5, 15);
        JLabel lblRemark = new JLabel("Performance: " + result.getFeedbackMessage(), SwingConstants.CENTER);
        lblRemark.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblRemark.setHorizontalAlignment(SwingConstants.CENTER);

        if (result.getPercentage() >= 80.0) {
            lblRemark.setForeground(new Color(40, 167, 69));
        } else if (result.getPercentage() >= 60.0) {
            lblRemark.setForeground(new Color(13, 110, 253));
        } else if (result.getPercentage() >= 40.0) {
            lblRemark.setForeground(new Color(255, 140, 0));
        } else {
            lblRemark.setForeground(new Color(220, 53, 69));
        }
        cardPanel.add(lblRemark, cgbc);

        // Student Save status notice
        cgbc.gridy = 8;
        cgbc.insets = new Insets(6, 15, 2, 15);
        if (StudentSession.isLoggedIn()) {
            JLabel lblSaved = new JLabel("Saved to " + StudentSession.getCurrentStudent().getFullName() + "'s Analytics Profile", SwingConstants.CENTER);
            lblSaved.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblSaved.setForeground(new Color(40, 167, 69));
            lblSaved.setHorizontalAlignment(SwingConstants.CENTER);
            cardPanel.add(lblSaved, cgbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(cardPanel, gbc);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Action Buttons Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 15));
        bottomPanel.setBackground(new Color(235, 240, 248));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // 1. Review Answers & Explanations Button (Feature 3)
        JButton btnReview = HomeFrame.createStyledButton("Review Answers & Explanations", new Color(111, 66, 193), Color.WHITE);
        btnReview.setPreferredSize(new Dimension(240, 42));
        bottomPanel.add(btnReview);

        // 2. Student Dashboard button (if logged in) or Try Again
        if (StudentSession.isLoggedIn()) {
            JButton btnDashboard = HomeFrame.createStyledButton("Student Dashboard", new Color(13, 110, 253), Color.WHITE);
            btnDashboard.setPreferredSize(new Dimension(160, 42));
            bottomPanel.add(btnDashboard);

            btnDashboard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ResultFrame.this.dispose();
                    new StudentDashboardFrame().setVisible(true);
                }
            });
        }

        JButton btnTryAgain = HomeFrame.createStyledButton("Try Again", new Color(40, 167, 69), Color.WHITE);
        btnTryAgain.setPreferredSize(new Dimension(110, 42));
        bottomPanel.add(btnTryAgain);

        JButton btnHome = HomeFrame.createStyledButton("Home", new Color(108, 117, 125), Color.WHITE);
        btnHome.setPreferredSize(new Dimension(90, 42));
        bottomPanel.add(btnHome);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---

        // Review Answers & Explanations
        btnReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reviewTitle = language + " - " + result.getTopic() + " (" + result.getDifficulty() + ")";
                QuizReviewFrame reviewFrame = new QuizReviewFrame(
                    ResultFrame.this,
                    result.getQuestionList(),
                    result.getSelectedAnswers(),
                    reviewTitle
                );
                reviewFrame.setVisible(true);
            }
        });

        // Try Again -> returns to Language / Quiz Selection
        btnTryAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultFrame.this.dispose();
                HomeFrame home = new HomeFrame();
                new LanguageFrame(home).setVisible(true);
            }
        });

        // Home -> returns to Main Home Screen
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultFrame.this.dispose();
                new HomeFrame().setVisible(true);
            }
        });
    }

    private void addMetricRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value, Color valColor) {
        gbc.gridy = row;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(50, 60, 75));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 15));
        val.setForeground(valColor);
        panel.add(val, gbc);
    }
}
