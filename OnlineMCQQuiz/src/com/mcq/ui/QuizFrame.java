package com.mcq.ui;

import com.mcq.dao.QuestionDAO;
import com.mcq.dao.QuizAttemptDAO;
import com.mcq.model.AttemptAnswer;
import com.mcq.model.Question;
import com.mcq.model.QuizAttempt;
import com.mcq.model.QuizResult;
import com.mcq.model.StudentSession;
import com.mcq.util.QuizTimer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * QuizFrame.java
 * Core quiz interaction screen supporting topic-based filtering, difficulty levels,
 * timed question execution, answer tracking, and automatic persistence to MySQL.
 */
public class QuizFrame extends JFrame {

    private final JFrame parentFrame;
    private final String language;
    private final String topic;
    private final String difficulty;
    private final int requestedLimit;
    private final QuestionDAO questionDAO;
    private final QuizAttemptDAO attemptDAO;

    // Quiz Questions and State
    private List<Question> questionList;
    private int currentIndex = 0;
    private static final int SECONDS_PER_QUESTION = 30;

    /**
     * Map tracking selected options:
     * Key   : Question Index (0 to questionList.size() - 1)
     * Value : Selected Option ("A", "B", "C", or "D")
     */
    private final Map<Integer, String> selectedAnswers = new HashMap<>();

    // UI Components
    private JLabel lblLanguageTitle;
    private JLabel lblQuestionProgress;
    private JLabel lblTopicDifficulty;
    private JLabel lblTimer;
    private JTextArea txtQuestion;
    private JRadioButton rbOptionA;
    private JRadioButton rbOptionB;
    private JRadioButton rbOptionC;
    private JRadioButton rbOptionD;
    private ButtonGroup optionGroup;

    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnSubmit;

    // Timer controller
    private QuizTimer quizTimer;

    /**
     * Standard language quiz constructor.
     */
    public QuizFrame(JFrame parentFrame, String language) {
        this(parentFrame, language, "All", "All", 10);
    }

    /**
     * Full topic and difficulty-based quiz constructor.
     */
    public QuizFrame(JFrame parentFrame, String language, String topic, String difficulty, int limit) {
        this.parentFrame = parentFrame;
        this.language = (language != null && !language.trim().isEmpty()) ? language.trim() : "All";
        this.topic = (topic != null && !topic.trim().isEmpty()) ? topic.trim() : "All";
        this.difficulty = (difficulty != null && !difficulty.trim().isEmpty()) ? difficulty.trim() : "All";
        this.requestedLimit = (limit > 0) ? limit : 10;
        this.questionDAO = new QuestionDAO();
        this.attemptDAO = new QuizAttemptDAO();

        loadQuestions();
        initUI();
        initTimer();
        displayCurrentQuestion();
    }

    /**
     * Fetches questions matching language, topic, and difficulty filters.
     */
    private void loadQuestions() {
        this.questionList = questionDAO.getFilteredQuestions(language, topic, difficulty, requestedLimit);
        if (questionList == null || questionList.isEmpty()) {
            // Fallback: try by language only
            this.questionList = questionDAO.getRandomQuestionsByLanguage(language, requestedLimit);
        }
        if (questionList == null || questionList.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to retrieve questions matching criteria.\nPlease verify MySQL database connectivity.",
                "Database Notice",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void initUI() {
        String titleStr = (language.equalsIgnoreCase("All") ? "General" : language) + " Quiz";
        setTitle(titleStr + " - Online MCQ Quiz System");
        setSize(840, 620);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Top Header Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(24, 43, 73));
        topPanel.setBorder(BorderFactory.createEmptyBorder(14, 25, 14, 25));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 3));
        titleBlock.setOpaque(false);

        lblLanguageTitle = new JLabel(titleStr, SwingConstants.LEFT);
        lblLanguageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLanguageTitle.setForeground(Color.WHITE);

        String subInfo = "Topic: " + topic + "  |  Level: " + difficulty;
        lblTopicDifficulty = new JLabel(subInfo, SwingConstants.LEFT);
        lblTopicDifficulty.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTopicDifficulty.setForeground(new Color(210, 225, 245));

        titleBlock.add(lblLanguageTitle);
        titleBlock.add(lblTopicDifficulty);
        topPanel.add(titleBlock, BorderLayout.WEST);

        int totalQ = (questionList != null) ? questionList.size() : 0;
        lblQuestionProgress = new JLabel("Question 1 of " + totalQ, SwingConstants.CENTER);
        lblQuestionProgress.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblQuestionProgress.setForeground(new Color(255, 193, 7)); // Amber gold
        topPanel.add(lblQuestionProgress, BorderLayout.CENTER);

        lblTimer = new JLabel("Time: " + SECONDS_PER_QUESTION + "s", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTimer.setForeground(new Color(255, 99, 71));
        topPanel.add(lblTimer, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center Content Panel
        JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Question display box
        txtQuestion = new JTextArea();
        txtQuestion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setEditable(false);
        txtQuestion.setBackground(Color.WHITE);
        txtQuestion.setForeground(new Color(30, 35, 45));
        txtQuestion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 220, 235), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        JScrollPane questionScroll = new JScrollPane(txtQuestion);
        questionScroll.setPreferredSize(new Dimension(740, 95));
        questionScroll.setBorder(null);
        centerPanel.add(questionScroll, BorderLayout.NORTH);

        // Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        optionsPanel.setOpaque(false);

        rbOptionA = createStyledRadioButton("A. ");
        rbOptionB = createStyledRadioButton("B. ");
        rbOptionC = createStyledRadioButton("C. ");
        rbOptionD = createStyledRadioButton("D. ");

        optionGroup = new ButtonGroup();
        optionGroup.add(rbOptionA);
        optionGroup.add(rbOptionB);
        optionGroup.add(rbOptionC);
        optionGroup.add(rbOptionD);

        optionsPanel.add(rbOptionA);
        optionsPanel.add(rbOptionB);
        optionsPanel.add(rbOptionC);
        optionsPanel.add(rbOptionD);

        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Navigation Bar
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(235, 240, 248));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        JPanel navButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navButtonsPanel.setOpaque(false);

        btnPrevious = HomeFrame.createStyledButton("Previous", new Color(108, 117, 125), Color.WHITE);
        btnPrevious.setPreferredSize(new Dimension(130, 42));
        navButtonsPanel.add(btnPrevious);

        btnNext = HomeFrame.createStyledButton("Next", new Color(13, 110, 253), Color.WHITE);
        btnNext.setPreferredSize(new Dimension(130, 42));
        navButtonsPanel.add(btnNext);

        bottomPanel.add(navButtonsPanel, BorderLayout.WEST);

        btnSubmit = HomeFrame.createStyledButton("Submit Quiz", new Color(40, 167, 69), Color.WHITE);
        btnSubmit.setPreferredSize(new Dimension(150, 42));
        bottomPanel.add(btnSubmit, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSelectedAnswer();
                if (currentIndex > 0) {
                    currentIndex--;
                    displayCurrentQuestion();
                    quizTimer.restart();
                }
            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSelectedAnswer();
                if (currentIndex < questionList.size() - 1) {
                    currentIndex++;
                    displayCurrentQuestion();
                    quizTimer.restart();
                }
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSelectedAnswer();
                int unattempted = questionList.size() - selectedAnswers.size();
                String confirmMsg = unattempted > 0 
                    ? "You have " + unattempted + " unanswered question(s).\nAre you sure you want to submit the quiz?"
                    : "Are you ready to submit your quiz?";

                int choice = JOptionPane.showConfirmDialog(
                    QuizFrame.this,
                    confirmMsg,
                    "Confirm Submission",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    submitQuiz();
                }
            }
        });

        // Window closing confirmation
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int choice = JOptionPane.showConfirmDialog(
                    QuizFrame.this,
                    "Exiting will terminate this quiz attempt and lose all progress.\nAre you sure you want to exit?",
                    "Abandon Quiz",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    if (quizTimer != null) {
                        quizTimer.stop();
                    }
                    QuizFrame.this.dispose();
                    if (parentFrame != null) {
                        parentFrame.setVisible(true);
                    }
                }
            }
        });
    }

    private void initTimer() {
        quizTimer = new QuizTimer(
            SECONDS_PER_QUESTION,
            new QuizTimer.TickCallback() {
                @Override
                public void onTick(int secondsRemaining) {
                    lblTimer.setText("Time: " + secondsRemaining + "s");
                    if (secondsRemaining <= 5) {
                        lblTimer.setForeground(new Color(220, 53, 69));
                    } else {
                        lblTimer.setForeground(new Color(255, 99, 71));
                    }
                }
            },
            new QuizTimer.TimeoutCallback() {
                @Override
                public void onTimeout() {
                    saveSelectedAnswer();
                    if (currentIndex < questionList.size() - 1) {
                        currentIndex++;
                        displayCurrentQuestion();
                        quizTimer.restart();
                    } else {
                        JOptionPane.showMessageDialog(
                            QuizFrame.this,
                            "Time expired on the final question! Submitting quiz automatically.",
                            "Time Up",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        submitQuiz();
                    }
                }
            }
        );

        quizTimer.start();
    }

    private void displayCurrentQuestion() {
        if (questionList == null || questionList.isEmpty()) {
            return;
        }

        Question q = questionList.get(currentIndex);

        lblQuestionProgress.setText("Question " + (currentIndex + 1) + " of " + questionList.size());
        lblTopicDifficulty.setText(
            "Topic: " + q.getTopic() + "  |  Difficulty: " + q.getDifficulty() + "  |  Marks: " + q.getMarks()
        );

        txtQuestion.setText(q.getQuestion());
        rbOptionA.setText("A.  " + q.getOptionA());
        rbOptionB.setText("B.  " + q.getOptionB());
        rbOptionC.setText("C.  " + q.getOptionC());
        rbOptionD.setText("D.  " + q.getOptionD());

        optionGroup.clearSelection();
        String savedChoice = selectedAnswers.get(currentIndex);
        if (savedChoice != null) {
            switch (savedChoice.toUpperCase()) {
                case "A": rbOptionA.setSelected(true); break;
                case "B": rbOptionB.setSelected(true); break;
                case "C": rbOptionC.setSelected(true); break;
                case "D": rbOptionD.setSelected(true); break;
            }
        }

        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < questionList.size() - 1);
    }

    private void saveSelectedAnswer() {
        if (rbOptionA.isSelected()) {
            selectedAnswers.put(currentIndex, "A");
        } else if (rbOptionB.isSelected()) {
            selectedAnswers.put(currentIndex, "B");
        } else if (rbOptionC.isSelected()) {
            selectedAnswers.put(currentIndex, "C");
        } else if (rbOptionD.isSelected()) {
            selectedAnswers.put(currentIndex, "D");
        }
    }

    private void submitQuiz() {
        if (quizTimer != null) {
            quizTimer.stop();
        }

        int totalQuestions = questionList.size();
        int correctCount = 0;
        int wrongCount = 0;
        int skippedCount = 0;
        int totalMarks = 0;
        int marksEarned = 0;

        List<AttemptAnswer> answerList = new ArrayList<>();

        for (int i = 0; i < totalQuestions; i++) {
            Question q = questionList.get(i);
            int qMarks = q.getMarks();
            totalMarks += qMarks;

            String studentAnswer = selectedAnswers.get(i);
            boolean isCorrect = false;
            int obtained = 0;

            if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
                skippedCount++;
            } else if (studentAnswer.equalsIgnoreCase(q.getCorrectAnswer().trim())) {
                correctCount++;
                isCorrect = true;
                obtained = qMarks;
                marksEarned += qMarks;
            } else {
                wrongCount++;
            }

            AttemptAnswer ans = new AttemptAnswer(0, q.getId(), studentAnswer, isCorrect, obtained);
            ans.setQuestion(q);
            answerList.add(ans);
        }

        // Construct QuizResult
        QuizResult result = new QuizResult(
            language,
            topic,
            difficulty,
            totalQuestions,
            correctCount,
            wrongCount,
            skippedCount,
            marksEarned,
            totalMarks,
            questionList,
            selectedAnswers
        );

        // Persistent data: If student is logged in, automatically save attempt to MySQL
        if (StudentSession.isLoggedIn()) {
            int studentId = StudentSession.getCurrentStudent().getId();
            QuizAttempt attempt = new QuizAttempt(
                studentId,
                language,
                topic,
                difficulty,
                totalQuestions,
                correctCount,
                wrongCount,
                skippedCount,
                marksEarned,
                totalMarks,
                result.getPercentage(),
                result.getFeedbackMessage()
            );

            int savedId = attemptDAO.saveAttempt(attempt, answerList);
            if (savedId > 0) {
                result.setSavedAttemptId(savedId);
            }
        }

        // Launch Result Screen
        ResultFrame resultFrame = new ResultFrame(language, result);
        resultFrame.setVisible(true);
        QuizFrame.this.dispose();
    }

    private JRadioButton createStyledRadioButton(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rb.setBackground(Color.WHITE);
        rb.setOpaque(true);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 240), 1),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        return rb;
    }
}
