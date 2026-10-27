package com.mcq.ui;

import com.mcq.dao.QuestionDAO;
import com.mcq.model.Question;
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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * AddQuestionFrame.java
 * Screen allowing the administrator to insert a new MCQ question into MySQL,
 * configuring topic, difficulty level, marks weightage, and learning explanation.
 */
public class AddQuestionFrame extends JFrame {

    private final JFrame parentFrame;
    private final QuestionDAO questionDAO;

    // Form inputs
    private JComboBox<String> cmbLanguage;
    private JComboBox<String> cmbTopic;
    private JComboBox<String> cmbDifficulty;
    private JComboBox<String> cmbMarks;
    private JTextArea txtQuestion;
    private JTextField txtOptionA;
    private JTextField txtOptionB;
    private JTextField txtOptionC;
    private JTextField txtOptionD;
    private JComboBox<String> cmbCorrectAnswer;
    private JTextArea txtExplanation;

    public AddQuestionFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.questionDAO = new QuestionDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Add New Question - Admin Panel");
        setSize(740, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        headerPanel.setLayout(new BorderLayout(0, 4));

        JLabel lblTitle = new JLabel("ADD NEW MCQ QUESTION", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Set language, topic, difficulty level, marks weightage, and detailed learning explanation", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Form Panel in ScrollPane
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Language
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblLang = new JLabel("Language:");
        lblLang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblLang, gbc);

        gbc.gridx = 1;
        List<String> langList = questionDAO.getAvailableLanguages();
        cmbLanguage = new JComboBox<>(langList.toArray(new String[0]));
        cmbLanguage.setEditable(true);
        cmbLanguage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbLanguage.setPreferredSize(new Dimension(420, 30));
        formPanel.add(cmbLanguage, gbc);

        // 2. Topic
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTopic = new JLabel("Topic:");
        lblTopic.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblTopic, gbc);

        gbc.gridx = 1;
        List<String> topicList = questionDAO.getAvailableTopics("All");
        cmbTopic = new JComboBox<>(topicList.toArray(new String[0]));
        cmbTopic.setEditable(true);
        cmbTopic.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTopic.setPreferredSize(new Dimension(420, 30));
        formPanel.add(cmbTopic, gbc);

        // 3. Difficulty Level & Marks (Shared Row)
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDiff = new JLabel("Difficulty & Marks:");
        lblDiff.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblDiff, gbc);

        gbc.gridx = 1;
        JPanel diffMarksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        diffMarksPanel.setOpaque(false);

        cmbDifficulty = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        cmbDifficulty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbDifficulty.setPreferredSize(new Dimension(160, 30));
        diffMarksPanel.add(cmbDifficulty);

        JLabel lblM = new JLabel("Marks:");
        lblM.setFont(new Font("Segoe UI", Font.BOLD, 13));
        diffMarksPanel.add(lblM);

        cmbMarks = new JComboBox<>(new String[]{"1", "2", "3", "5"});
        cmbMarks.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbMarks.setPreferredSize(new Dimension(80, 30));
        diffMarksPanel.add(cmbMarks);

        formPanel.add(diffMarksPanel, gbc);

        // 4. Question Text
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblQ = new JLabel("Question Text:");
        lblQ.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblQ, gbc);

        gbc.gridx = 1;
        txtQuestion = new JTextArea(3, 25);
        txtQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        JScrollPane scrollQuestion = new JScrollPane(txtQuestion);
        scrollQuestion.setPreferredSize(new Dimension(420, 65));
        formPanel.add(scrollQuestion, gbc);

        // 5. Options A, B, C, D
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Option A:"), gbc);
        gbc.gridx = 1;
        txtOptionA = new JTextField();
        txtOptionA.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtOptionA, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Option B:"), gbc);
        gbc.gridx = 1;
        txtOptionB = new JTextField();
        txtOptionB.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtOptionB, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Option C:"), gbc);
        gbc.gridx = 1;
        txtOptionC = new JTextField();
        txtOptionC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtOptionC, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Option D:"), gbc);
        gbc.gridx = 1;
        txtOptionD = new JTextField();
        txtOptionD.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtOptionD, gbc);

        // 6. Correct Answer
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel lblCorrect = new JLabel("Correct Answer:");
        lblCorrect.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblCorrect, gbc);

        gbc.gridx = 1;
        cmbCorrectAnswer = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        cmbCorrectAnswer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cmbCorrectAnswer.setPreferredSize(new Dimension(100, 30));
        formPanel.add(cmbCorrectAnswer, gbc);

        // 7. Explanation Field (Feature 3)
        gbc.gridx = 0; gbc.gridy = 9;
        JLabel lblExp = new JLabel("Explanation:");
        lblExp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblExp, gbc);

        gbc.gridx = 1;
        txtExplanation = new JTextArea(3, 25);
        txtExplanation.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtExplanation.setLineWrap(true);
        txtExplanation.setWrapStyleWord(true);
        JScrollPane scrollExp = new JScrollPane(txtExplanation);
        scrollExp.setPreferredSize(new Dimension(420, 65));
        formPanel.add(scrollExp, gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        add(formScroll, BorderLayout.CENTER);

        // Bottom Button Bar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        bottomPanel.setBackground(new Color(235, 240, 248));

        JButton btnAdd = HomeFrame.createStyledButton("Add Question", new Color(40, 167, 69), Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(140, 38));
        bottomPanel.add(btnAdd);

        JButton btnClear = HomeFrame.createStyledButton("Clear", new Color(108, 117, 125), Color.WHITE);
        btnClear.setPreferredSize(new Dimension(100, 38));
        bottomPanel.add(btnClear);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(70, 80, 95), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(100, 38));
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQuestion();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddQuestionFrame.this.dispose();
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

    private void saveQuestion() {
        Object langObj = cmbLanguage.getSelectedItem();
        String language = (langObj != null) ? langObj.toString().trim() : "";

        Object topicObj = cmbTopic.getSelectedItem();
        String topic = (topicObj != null) ? topicObj.toString().trim() : "General";

        String difficulty = (String) cmbDifficulty.getSelectedItem();
        int marks = Integer.parseInt((String) cmbMarks.getSelectedItem());

        String question = txtQuestion.getText().trim();
        String optA = txtOptionA.getText().trim();
        String optB = txtOptionB.getText().trim();
        String optC = txtOptionC.getText().trim();
        String optD = txtOptionD.getText().trim();
        String correct = (String) cmbCorrectAnswer.getSelectedItem();
        String explanation = txtExplanation.getText().trim();

        if (language.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Language field cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (topic.isEmpty()) {
            topic = "General";
        }
        if (question.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Question text cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtQuestion.requestFocus();
            return;
        }
        if (optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All four options (A, B, C, D) are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Question q = new Question(
            language,
            topic,
            difficulty,
            marks,
            question,
            optA,
            optB,
            optC,
            optD,
            correct,
            explanation
        );

        boolean success = questionDAO.addQuestion(q);
        if (success) {
            JOptionPane.showMessageDialog(this, "Question added successfully to database!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to insert question into MySQL database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtQuestion.setText("");
        txtOptionA.setText("");
        txtOptionB.setText("");
        txtOptionC.setText("");
        txtOptionD.setText("");
        txtExplanation.setText("");
        cmbCorrectAnswer.setSelectedIndex(0);
        txtQuestion.requestFocus();
    }
}
