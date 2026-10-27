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
 * UpdateQuestionFrame.java
 * Form for modifying an existing question record in MySQL,
 * including topic, difficulty level, marks weightage, and answer explanation.
 */
public class UpdateQuestionFrame extends JFrame {

    private final JFrame parentFrame;
    private final QuestionDAO questionDAO;
    private Question currentQuestion;

    // Form inputs
    private JTextField txtId;
    private JButton btnSearch;
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

    public UpdateQuestionFrame(JFrame parentFrame) {
        this(parentFrame, null);
    }

    public UpdateQuestionFrame(JFrame parentFrame, Question preLoadedQuestion) {
        this.parentFrame = parentFrame;
        this.questionDAO = new QuestionDAO();
        this.currentQuestion = preLoadedQuestion;
        initUI();
        if (preLoadedQuestion != null) {
            populateFields(preLoadedQuestion);
        }
    }

    private void initUI() {
        setTitle("Update Question - Admin Panel");
        setSize(740, 740);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        headerPanel.setLayout(new BorderLayout(0, 4));

        JLabel lblTitle = new JLabel("UPDATE MCQ QUESTION", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Modify question content, topic, difficulty level, marks, or learning explanation", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 0. Search / ID Row
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("Question ID:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblId, gbc);

        gbc.gridx = 1;
        JPanel idSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        idSearchPanel.setOpaque(false);
        txtId = new JTextField(8);
        txtId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtId.setPreferredSize(new Dimension(100, 30));
        idSearchPanel.add(txtId);

        btnSearch = HomeFrame.createStyledButton("Search ID", new Color(13, 110, 253), Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(110, 30));
        idSearchPanel.add(btnSearch);
        formPanel.add(idSearchPanel, gbc);

        // 1. Language
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Language:"), gbc);
        gbc.gridx = 1;
        List<String> langList = questionDAO.getAvailableLanguages();
        cmbLanguage = new JComboBox<>(langList.toArray(new String[0]));
        cmbLanguage.setEditable(true);
        cmbLanguage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbLanguage.setPreferredSize(new Dimension(420, 30));
        formPanel.add(cmbLanguage, gbc);

        // 2. Topic
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Topic:"), gbc);
        gbc.gridx = 1;
        List<String> topicList = questionDAO.getAvailableTopics("All");
        cmbTopic = new JComboBox<>(topicList.toArray(new String[0]));
        cmbTopic.setEditable(true);
        cmbTopic.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTopic.setPreferredSize(new Dimension(420, 30));
        formPanel.add(cmbTopic, gbc);

        // 3. Difficulty Level & Marks
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Difficulty & Marks:"), gbc);
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
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Question Text:"), gbc);
        gbc.gridx = 1;
        txtQuestion = new JTextArea(3, 25);
        txtQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        JScrollPane scrollQuestion = new JScrollPane(txtQuestion);
        scrollQuestion.setPreferredSize(new Dimension(420, 65));
        formPanel.add(scrollQuestion, gbc);

        // 5. Options
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Option A:"), gbc);
        gbc.gridx = 1;
        txtOptionA = new JTextField();
        formPanel.add(txtOptionA, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Option B:"), gbc);
        gbc.gridx = 1;
        txtOptionB = new JTextField();
        formPanel.add(txtOptionB, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Option C:"), gbc);
        gbc.gridx = 1;
        txtOptionC = new JTextField();
        formPanel.add(txtOptionC, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Option D:"), gbc);
        gbc.gridx = 1;
        txtOptionD = new JTextField();
        formPanel.add(txtOptionD, gbc);

        // 6. Correct Answer
        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("Correct Answer:"), gbc);
        gbc.gridx = 1;
        cmbCorrectAnswer = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        cmbCorrectAnswer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cmbCorrectAnswer.setPreferredSize(new Dimension(100, 30));
        formPanel.add(cmbCorrectAnswer, gbc);

        // 7. Explanation
        gbc.gridx = 0; gbc.gridy = 10;
        formPanel.add(new JLabel("Explanation:"), gbc);
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

        // Bottom Actions Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        bottomPanel.setBackground(new Color(235, 240, 248));

        JButton btnUpdate = HomeFrame.createStyledButton("Update", new Color(255, 153, 0), Color.WHITE);
        btnUpdate.setPreferredSize(new Dimension(120, 38));
        bottomPanel.add(btnUpdate);

        JButton btnClear = HomeFrame.createStyledButton("Clear", new Color(108, 117, 125), Color.WHITE);
        btnClear.setPreferredSize(new Dimension(100, 38));
        bottomPanel.add(btnClear);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(70, 80, 95), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(100, 38));
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchQuestionById();
            }
        });

        txtId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchQuestionById();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performUpdate();
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
                UpdateQuestionFrame.this.dispose();
                if (parentFrame != null) {
                    if (parentFrame instanceof ViewQuestionFrame) {
                        ((ViewQuestionFrame) parentFrame).loadTableData("All");
                    }
                    parentFrame.setVisible(true);
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (parentFrame != null) {
                    if (parentFrame instanceof ViewQuestionFrame) {
                        ((ViewQuestionFrame) parentFrame).loadTableData("All");
                    }
                    parentFrame.setVisible(true);
                }
            }
        });
    }

    private void searchQuestionById() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Question ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int qId = Integer.parseInt(idStr);
            Question q = questionDAO.getQuestionById(qId);
            if (q != null) {
                populateFields(q);
            } else {
                JOptionPane.showMessageDialog(this, "No question found with ID: " + qId, "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Question ID.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFields(Question q) {
        this.currentQuestion = q;
        txtId.setText(String.valueOf(q.getId()));
        txtId.setEditable(false);
        btnSearch.setEnabled(false);

        cmbLanguage.setSelectedItem(q.getLanguage());
        cmbTopic.setSelectedItem(q.getTopic());
        cmbDifficulty.setSelectedItem(q.getDifficulty());
        cmbMarks.setSelectedItem(String.valueOf(q.getMarks()));

        txtQuestion.setText(q.getQuestion());
        txtOptionA.setText(q.getOptionA());
        txtOptionB.setText(q.getOptionB());
        txtOptionC.setText(q.getOptionC());
        txtOptionD.setText(q.getOptionD());
        cmbCorrectAnswer.setSelectedItem(q.getCorrectAnswer());
        txtExplanation.setText(q.getExplanation());
    }

    private void performUpdate() {
        if (currentQuestion == null) {
            JOptionPane.showMessageDialog(this, "Please first search or select a question to update.", "No Question Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        if (language.isEmpty() || question.isEmpty() || optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All required fields must be filled out!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentQuestion.setLanguage(language);
        currentQuestion.setTopic(topic);
        currentQuestion.setDifficulty(difficulty);
        currentQuestion.setMarks(marks);
        currentQuestion.setQuestion(question);
        currentQuestion.setOptionA(optA);
        currentQuestion.setOptionB(optB);
        currentQuestion.setOptionC(optC);
        currentQuestion.setOptionD(optD);
        currentQuestion.setCorrectAnswer(correct);
        currentQuestion.setExplanation(explanation);

        boolean updated = questionDAO.updateQuestion(currentQuestion);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Question #" + currentQuestion.getId() + " successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update question in MySQL database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        currentQuestion = null;
        txtId.setText("");
        txtId.setEditable(true);
        btnSearch.setEnabled(true);
        txtQuestion.setText("");
        txtOptionA.setText("");
        txtOptionB.setText("");
        txtOptionC.setText("");
        txtOptionD.setText("");
        txtExplanation.setText("");
        cmbCorrectAnswer.setSelectedIndex(0);
        txtId.requestFocus();
    }
}
