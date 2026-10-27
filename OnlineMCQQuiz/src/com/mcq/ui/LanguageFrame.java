package com.mcq.ui;

import com.mcq.dao.QuestionDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * LanguageFrame.java
 * Screen where student customizes their quiz:
 * - Programming Language
 * - Topic (OOP, Collections, Exception Handling, HTML Forms, CSS Selectors, etc.)
 * - Difficulty (Beginner, Intermediate, Advanced)
 * - Quiz Length
 */
public class LanguageFrame extends JFrame {

    private final JFrame parentFrame;
    private final QuestionDAO questionDAO;

    private JComboBox<String> cmbLanguages;
    private JComboBox<String> cmbTopics;
    private JComboBox<String> cmbDifficulties;
    private JComboBox<String> cmbQuestionCount;
    private JLabel lblAvailableCount;

    public LanguageFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.questionDAO = new QuestionDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Select Quiz Options - Online MCQ Quiz");
        setSize(580, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(22, 20, 18, 20));
        headerPanel.setLayout(new BorderLayout(0, 6));

        JLabel lblTitle = new JLabel("CUSTOMIZE YOUR QUIZ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Choose your preferred language, topic, difficulty, and length", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Selection Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Language
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblLang = new JLabel("Language:");
        lblLang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        centerPanel.add(lblLang, gbc);

        gbc.gridx = 1;
        List<String> languages = new ArrayList<>();
        languages.add("All");
        languages.addAll(questionDAO.getAvailableLanguages());
        cmbLanguages = new JComboBox<>(languages.toArray(new String[0]));
        cmbLanguages.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLanguages.setPreferredSize(new Dimension(280, 34));
        cmbLanguages.setBackground(Color.WHITE);
        centerPanel.add(cmbLanguages, gbc);

        // 2. Topic
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTop = new JLabel("Topic:");
        lblTop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        centerPanel.add(lblTop, gbc);

        gbc.gridx = 1;
        cmbTopics = new JComboBox<>(new String[]{"All"});
        cmbTopics.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTopics.setPreferredSize(new Dimension(280, 34));
        cmbTopics.setBackground(Color.WHITE);
        centerPanel.add(cmbTopics, gbc);

        // 3. Difficulty Level
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDiff = new JLabel("Difficulty:");
        lblDiff.setFont(new Font("Segoe UI", Font.BOLD, 14));
        centerPanel.add(lblDiff, gbc);

        gbc.gridx = 1;
        cmbDifficulties = new JComboBox<>(new String[]{"All", "Beginner", "Intermediate", "Advanced"});
        cmbDifficulties.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbDifficulties.setPreferredSize(new Dimension(280, 34));
        cmbDifficulties.setBackground(Color.WHITE);
        centerPanel.add(cmbDifficulties, gbc);

        // 4. Question Count
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblCnt = new JLabel("Questions:");
        lblCnt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        centerPanel.add(lblCnt, gbc);

        gbc.gridx = 1;
        cmbQuestionCount = new JComboBox<>(new String[]{"5 Questions", "10 Questions", "15 Questions", "20 Questions"});
        cmbQuestionCount.setSelectedIndex(1); // default 10
        cmbQuestionCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbQuestionCount.setPreferredSize(new Dimension(280, 34));
        cmbQuestionCount.setBackground(Color.WHITE);
        centerPanel.add(cmbQuestionCount, gbc);

        // 5. Live Count indicator
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        lblAvailableCount = new JLabel("Questions matching in database: ...", SwingConstants.CENTER);
        lblAvailableCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAvailableCount.setForeground(new Color(40, 167, 69));
        centerPanel.add(lblAvailableCount, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Buttons Panel
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(235, 240, 248));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints bgbc = new GridBagConstraints();
        bgbc.insets = new Insets(0, 12, 0, 12);
        bgbc.gridy = 0;

        JButton btnStartQuiz = HomeFrame.createStyledButton("Start Quiz", new Color(13, 110, 253), Color.WHITE);
        btnStartQuiz.setPreferredSize(new Dimension(140, 42));
        bgbc.gridx = 0;
        bottomPanel.add(btnStartQuiz, bgbc);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(108, 117, 125), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(140, 42));
        bgbc.gridx = 1;
        bottomPanel.add(btnBack, bgbc);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        cmbLanguages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTopics();
                updateCounter();
            }
        });

        cmbTopics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCounter();
            }
        });

        cmbDifficulties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCounter();
            }
        });

        updateTopics();
        updateCounter();

        btnStartQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startQuiz();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LanguageFrame.this.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });
    }

    private void updateTopics() {
        String lang = (String) cmbLanguages.getSelectedItem();
        List<String> topics = questionDAO.getAvailableTopics(lang);

        cmbTopics.removeAllItems();
        cmbTopics.addItem("All");
        for (String t : topics) {
            cmbTopics.addItem(t);
        }
    }

    private void updateCounter() {
        String lang = (String) cmbLanguages.getSelectedItem();
        String topic = (String) cmbTopics.getSelectedItem();
        String diff = (String) cmbDifficulties.getSelectedItem();

        int count = questionDAO.getQuestionCount(lang, topic, diff);
        lblAvailableCount.setText("Questions Available in Database: " + count);
        if (count == 0) {
            lblAvailableCount.setForeground(new Color(220, 53, 69));
        } else {
            lblAvailableCount.setForeground(new Color(40, 167, 69));
        }
    }

    private void startQuiz() {
        String selectedLanguage = (String) cmbLanguages.getSelectedItem();
        String selectedTopic = (String) cmbTopics.getSelectedItem();
        String selectedDifficulty = (String) cmbDifficulties.getSelectedItem();

        String countStr = (String) cmbQuestionCount.getSelectedItem();
        int requestedLimit = 10;
        if (countStr != null) {
            try {
                requestedLimit = Integer.parseInt(countStr.split(" ")[0]);
            } catch (Exception ignored) {
            }
        }

        int availableCount = questionDAO.getQuestionCount(selectedLanguage, selectedTopic, selectedDifficulty);
        if (availableCount == 0) {
            JOptionPane.showMessageDialog(
                LanguageFrame.this,
                "No questions found for the selected criteria in the database!\n"
                + "Language: " + selectedLanguage + "\nTopic: " + selectedTopic + "\nDifficulty: " + selectedDifficulty,
                "No Questions Available",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        QuizFrame quizFrame = new QuizFrame(
            LanguageFrame.this,
            selectedLanguage,
            selectedTopic,
            selectedDifficulty,
            requestedLimit
        );
        quizFrame.setVisible(true);
        LanguageFrame.this.setVisible(false);
    }
}
