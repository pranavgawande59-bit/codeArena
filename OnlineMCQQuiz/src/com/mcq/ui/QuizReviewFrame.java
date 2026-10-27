package com.mcq.ui;

import com.mcq.dao.QuizAttemptDAO;
import com.mcq.model.AttemptAnswer;
import com.mcq.model.Question;
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
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * QuizReviewFrame.java
 * Comprehensive, interactive review screen where students examine each question,
 * their selected answer, the verified correct answer, and an in-depth explanation.
 */
public class QuizReviewFrame extends JFrame {

    private final JFrame parentFrame;
    private final List<ReviewItem> reviewItems = new ArrayList<>();
    private int currentIndex = 0;
    private String currentFilter = "ALL"; // "ALL", "WRONG", "SKIPPED", "CORRECT"

    // UI Components
    private JLabel lblHeaderTitle;
    private JLabel lblQuestionNum;
    private JLabel lblTopicBadge;
    private JLabel lblDifficultyBadge;
    private JLabel lblMarksBadge;
    private JLabel lblStatusBanner;

    private JTextArea txtQuestion;
    private JPanel panelOptionA;
    private JPanel panelOptionB;
    private JPanel panelOptionC;
    private JPanel panelOptionD;

    private JLabel lblOptAText;
    private JLabel lblOptBText;
    private JLabel lblOptCText;
    private JLabel lblOptDText;

    private JLabel lblOptATag;
    private JLabel lblOptBTag;
    private JLabel lblOptCTag;
    private JLabel lblOptDTag;

    private JTextArea txtExplanation;
    private JButton btnPrev;
    private JButton btnNext;
    private JPanel questionNavButtonsPanel;

    /**
     * Internal container holding a question and its answer state for review.
     */
    public static class ReviewItem {
        public final Question question;
        public final String selectedOption; // "A", "B", "C", "D", or null
        public final boolean isCorrect;
        public final boolean isSkipped;

        public ReviewItem(Question question, String selectedOption) {
            this.question = question;
            this.selectedOption = (selectedOption != null && !selectedOption.trim().isEmpty())
                ? selectedOption.trim().toUpperCase() : null;
            this.isSkipped = (this.selectedOption == null);
            this.isCorrect = !isSkipped && this.selectedOption.equalsIgnoreCase(question.getCorrectAnswer().trim());
        }
    }

    /**
     * Constructor for immediate in-memory review after quiz submission.
     */
    public QuizReviewFrame(JFrame parentFrame, List<Question> questions, Map<Integer, String> selectedAnswers, String quizTitle) {
        this.parentFrame = parentFrame;
        if (questions != null) {
            for (int i = 0; i < questions.size(); i++) {
                String choice = (selectedAnswers != null) ? selectedAnswers.get(i) : null;
                reviewItems.add(new ReviewItem(questions.get(i), choice));
            }
        }
        initUI(quizTitle);
        displayCurrentItem();
    }

    /**
     * Constructor for historical review loaded from database by attemptId.
     */
    public QuizReviewFrame(JFrame parentFrame, int attemptId, String quizTitle) {
        this.parentFrame = parentFrame;
        QuizAttemptDAO dao = new QuizAttemptDAO();
        List<AttemptAnswer> answers = dao.getAnswersForAttempt(attemptId);
        if (answers != null) {
            for (AttemptAnswer ans : answers) {
                if (ans.getQuestion() != null) {
                    reviewItems.add(new ReviewItem(ans.getQuestion(), ans.getSelectedOption()));
                }
            }
        }
        initUI(quizTitle);
        displayCurrentItem();
    }

    private void initUI(String quizTitle) {
        setTitle("Quiz Review & Explanations - " + quizTitle);
        setSize(980, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 25, 16, 25));

        lblHeaderTitle = new JLabel("QUIZ REVIEW: " + quizTitle.toUpperCase(), SwingConstants.LEFT);
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeaderTitle.setForeground(Color.WHITE);
        headerPanel.add(lblHeaderTitle, BorderLayout.WEST);

        // Score overview badge in header
        int total = reviewItems.size();
        int correct = 0;
        int wrong = 0;
        int skipped = 0;
        for (ReviewItem it : reviewItems) {
            if (it.isSkipped) skipped++;
            else if (it.isCorrect) correct++;
            else wrong++;
        }
        JLabel lblSummary = new JLabel(
            String.format("Correct: %d  |  Incorrect: %d  |  Skipped: %d", correct, wrong, skipped),
            SwingConstants.RIGHT
        );
        lblSummary.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSummary.setForeground(new Color(255, 193, 7));
        headerPanel.add(lblSummary, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Split Container: Left Sidebar (Question list & Filters) + Right Center (Question Detail)
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 247, 250));

        // --- Left Sidebar ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(new Color(238, 242, 248));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(215, 225, 238)));

        // Filter buttons at top of sidebar
        JPanel filterPanel = new JPanel(new GridLayout(2, 2, 4, 4));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAll = createFilterButton("All (" + total + ")", "ALL");
        JButton btnWrong = createFilterButton("Wrong (" + wrong + ")", "WRONG");
        JButton btnSkipped = createFilterButton("Skipped (" + skipped + ")", "SKIPPED");
        JButton btnCorrect = createFilterButton("Correct (" + correct + ")", "CORRECT");

        filterPanel.add(btnAll);
        filterPanel.add(btnWrong);
        filterPanel.add(btnSkipped);
        filterPanel.add(btnCorrect);
        sidebar.add(filterPanel, BorderLayout.NORTH);

        // Question Navigator buttons (Scrollable)
        questionNavButtonsPanel = new JPanel();
        questionNavButtonsPanel.setLayout(new BoxLayout(questionNavButtonsPanel, BoxLayout.Y_AXIS));
        questionNavButtonsPanel.setOpaque(false);
        questionNavButtonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        buildNavigatorButtons();

        JScrollPane navScroll = new JScrollPane(questionNavButtonsPanel);
        navScroll.setBorder(null);
        navScroll.setOpaque(false);
        navScroll.getViewport().setOpaque(false);
        sidebar.add(navScroll, BorderLayout.CENTER);

        mainContent.add(sidebar, BorderLayout.WEST);

        // --- Center Detail Panel ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Question metadata row
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        metaPanel.setOpaque(false);

        lblQuestionNum = new JLabel("Question 1 of " + total);
        lblQuestionNum.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblQuestionNum.setForeground(new Color(24, 43, 73));
        metaPanel.add(lblQuestionNum);

        lblTopicBadge = createBadgeLabel("Topic: OOP", new Color(13, 110, 253));
        metaPanel.add(lblTopicBadge);

        lblDifficultyBadge = createBadgeLabel("Difficulty: Intermediate", new Color(108, 117, 125));
        metaPanel.add(lblDifficultyBadge);

        lblMarksBadge = createBadgeLabel("Marks: 1", new Color(40, 167, 69));
        metaPanel.add(lblMarksBadge);

        // Status banner (e.g. "You answered correctly" or "Incorrect")
        lblStatusBanner = new JLabel("Status banner", SwingConstants.LEFT);
        lblStatusBanner.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatusBanner.setOpaque(true);
        lblStatusBanner.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        JPanel topQuestionHeader = new JPanel(new BorderLayout(0, 8));
        topQuestionHeader.setOpaque(false);
        topQuestionHeader.add(metaPanel, BorderLayout.NORTH);
        topQuestionHeader.add(lblStatusBanner, BorderLayout.SOUTH);
        centerPanel.add(topQuestionHeader, BorderLayout.NORTH);

        // Middle Scrollable Area: Question Text + 4 Options + Explanation Card
        JPanel cardContent = new JPanel();
        cardContent.setLayout(new BoxLayout(cardContent, BoxLayout.Y_AXIS));
        cardContent.setOpaque(false);

        // 1. Question Text Area
        txtQuestion = new JTextArea();
        txtQuestion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setEditable(false);
        txtQuestion.setBackground(Color.WHITE);
        txtQuestion.setForeground(new Color(33, 40, 50));
        txtQuestion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 220, 235), 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        cardContent.add(txtQuestion);
        cardContent.add(Box.createVerticalStrut(10));

        // 2. Options A, B, C, D panels
        panelOptionA = createOptionPanel();
        panelOptionB = createOptionPanel();
        panelOptionC = createOptionPanel();
        panelOptionD = createOptionPanel();

        lblOptAText = (JLabel) panelOptionA.getClientProperty("textLabel");
        lblOptBText = (JLabel) panelOptionB.getClientProperty("textLabel");
        lblOptCText = (JLabel) panelOptionC.getClientProperty("textLabel");
        lblOptDText = (JLabel) panelOptionD.getClientProperty("textLabel");

        lblOptATag = (JLabel) panelOptionA.getClientProperty("tagLabel");
        lblOptBTag = (JLabel) panelOptionB.getClientProperty("tagLabel");
        lblOptCTag = (JLabel) panelOptionC.getClientProperty("tagLabel");
        lblOptDTag = (JLabel) panelOptionD.getClientProperty("tagLabel");

        cardContent.add(panelOptionA);
        cardContent.add(Box.createVerticalStrut(6));
        cardContent.add(panelOptionB);
        cardContent.add(Box.createVerticalStrut(6));
        cardContent.add(panelOptionC);
        cardContent.add(Box.createVerticalStrut(6));
        cardContent.add(panelOptionD);
        cardContent.add(Box.createVerticalStrut(12));

        // 3. Explanation Callout Card
        JPanel explanationCard = new JPanel(new BorderLayout(0, 6));
        explanationCard.setBackground(new Color(238, 246, 255));
        explanationCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(185, 215, 250), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblExpHeader = new JLabel("EXPLANATION & LEARNING CONCEPT", SwingConstants.LEFT);
        lblExpHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblExpHeader.setForeground(new Color(13, 110, 253));
        explanationCard.add(lblExpHeader, BorderLayout.NORTH);

        txtExplanation = new JTextArea();
        txtExplanation.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtExplanation.setLineWrap(true);
        txtExplanation.setWrapStyleWord(true);
        txtExplanation.setEditable(false);
        txtExplanation.setOpaque(false);
        txtExplanation.setForeground(new Color(40, 50, 65));
        explanationCard.add(txtExplanation, BorderLayout.CENTER);

        cardContent.add(explanationCard);

        JScrollPane centerScroll = new JScrollPane(cardContent);
        centerScroll.setBorder(null);
        centerScroll.setOpaque(false);
        centerScroll.getViewport().setOpaque(false);
        centerPanel.add(centerScroll, BorderLayout.CENTER);

        mainContent.add(centerPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        // --- Bottom Action Bar ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(235, 240, 248));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel navBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navBtns.setOpaque(false);

        btnPrev = HomeFrame.createStyledButton("Previous", new Color(108, 117, 125), Color.WHITE);
        btnPrev.setPreferredSize(new Dimension(120, 38));
        btnNext = HomeFrame.createStyledButton("Next", new Color(13, 110, 253), Color.WHITE);
        btnNext.setPreferredSize(new Dimension(120, 38));

        navBtns.add(btnPrev);
        navBtns.add(btnNext);
        bottomPanel.add(navBtns, BorderLayout.WEST);

        JButton btnClose = HomeFrame.createStyledButton("Close Review", new Color(70, 80, 95), Color.WHITE);
        btnClose.setPreferredSize(new Dimension(130, 38));
        bottomPanel.add(btnClose, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        btnPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigate(-1);
            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigate(1);
            }
        });

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizReviewFrame.this.dispose();
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

    private void navigate(int delta) {
        int target = currentIndex + delta;
        while (target >= 0 && target < reviewItems.size()) {
            if (matchesFilter(reviewItems.get(target))) {
                currentIndex = target;
                displayCurrentItem();
                return;
            }
            target += delta;
        }
    }

    private boolean matchesFilter(ReviewItem item) {
        switch (currentFilter) {
            case "WRONG": return !item.isSkipped && !item.isCorrect;
            case "SKIPPED": return item.isSkipped;
            case "CORRECT": return item.isCorrect;
            default: return true;
        }
    }

    private void buildNavigatorButtons() {
        questionNavButtonsPanel.removeAll();

        for (int i = 0; i < reviewItems.size(); i++) {
            ReviewItem it = reviewItems.get(i);
            if (!matchesFilter(it)) {
                continue;
            }

            final int index = i;
            String text = "Q" + (i + 1) + ": ";
            Color statusColor;
            if (it.isSkipped) {
                text += "Skipped";
                statusColor = new Color(130, 140, 150);
            } else if (it.isCorrect) {
                text += "Correct";
                statusColor = new Color(40, 167, 69);
            } else {
                text += "Wrong";
                statusColor = new Color(220, 53, 69);
            }

            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", (i == currentIndex) ? Font.BOLD : Font.PLAIN, 12));
            btn.setMaximumSize(new Dimension(210, 32));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setHorizontalAlignment(SwingConstants.LEFT);

            if (i == currentIndex) {
                btn.setBackground(statusColor);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(statusColor);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 4, 1, 1, statusColor),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
            }

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentIndex = index;
                    displayCurrentItem();
                }
            });

            questionNavButtonsPanel.add(btn);
            questionNavButtonsPanel.add(Box.createVerticalStrut(4));
        }

        questionNavButtonsPanel.revalidate();
        questionNavButtonsPanel.repaint();
    }

    private void displayCurrentItem() {
        if (reviewItems.isEmpty()) {
            txtQuestion.setText("No questions available for review.");
            return;
        }

        ReviewItem item = reviewItems.get(currentIndex);
        Question q = item.question;

        lblQuestionNum.setText("Question " + (currentIndex + 1) + " of " + reviewItems.size());
        lblTopicBadge.setText("Topic: " + q.getTopic());
        lblDifficultyBadge.setText("Difficulty: " + q.getDifficulty());
        lblMarksBadge.setText("Marks: " + q.getMarks());

        txtQuestion.setText(q.getQuestion());

        String correctChoice = (q.getCorrectAnswer() != null) ? q.getCorrectAnswer().trim().toUpperCase() : "A";
        String studentChoice = item.selectedOption;

        // Status banner styling
        if (item.isSkipped) {
            lblStatusBanner.setText("Status: Skipped (0 marks)  |  Correct Answer is " + correctChoice);
            lblStatusBanner.setBackground(new Color(245, 245, 245));
            lblStatusBanner.setForeground(new Color(100, 110, 120));
        } else if (item.isCorrect) {
            lblStatusBanner.setText("Status: Correct! (+" + q.getMarks() + " marks)  |  You selected option " + studentChoice);
            lblStatusBanner.setBackground(new Color(212, 237, 218));
            lblStatusBanner.setForeground(new Color(21, 87, 36));
        } else {
            lblStatusBanner.setText("Status: Incorrect (0 marks)  |  You selected option " + studentChoice + ", Correct answer is " + correctChoice);
            lblStatusBanner.setBackground(new Color(248, 215, 218));
            lblStatusBanner.setForeground(new Color(114, 28, 36));
        }

        // Format 4 options
        formatOptionRow(panelOptionA, lblOptAText, lblOptATag, "A", q.getOptionA(), studentChoice, correctChoice);
        formatOptionRow(panelOptionB, lblOptBText, lblOptBTag, "B", q.getOptionB(), studentChoice, correctChoice);
        formatOptionRow(panelOptionC, lblOptCText, lblOptCTag, "C", q.getOptionC(), studentChoice, correctChoice);
        formatOptionRow(panelOptionD, lblOptDText, lblOptDTag, "D", q.getOptionD(), studentChoice, correctChoice);

        // Explanation text
        String exp = q.getExplanation();
        if (exp != null && !exp.trim().isEmpty()) {
            txtExplanation.setText(exp);
        } else {
            txtExplanation.setText("No additional explanation was provided for this question.");
        }

        btnPrev.setEnabled(hasPreviousMatch());
        btnNext.setEnabled(hasNextMatch());

        buildNavigatorButtons();
    }

    private boolean hasPreviousMatch() {
        for (int i = currentIndex - 1; i >= 0; i--) {
            if (matchesFilter(reviewItems.get(i))) return true;
        }
        return false;
    }

    private boolean hasNextMatch() {
        for (int i = currentIndex + 1; i < reviewItems.size(); i++) {
            if (matchesFilter(reviewItems.get(i))) return true;
        }
        return false;
    }

    private void formatOptionRow(JPanel panel, JLabel lblText, JLabel lblTag, String optLetter, String optContent,
                                 String studentChoice, String correctChoice) {
        lblText.setText(optLetter + ".  " + optContent);

        boolean isStudent = optLetter.equalsIgnoreCase(studentChoice);
        boolean isCorrect = optLetter.equalsIgnoreCase(correctChoice);

        if (isCorrect) {
            panel.setBackground(new Color(230, 245, 233));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 167, 69), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            lblTag.setText(isStudent ? "YOUR CORRECT CHOICE" : "CORRECT ANSWER");
            lblTag.setForeground(new Color(30, 126, 52));
            lblTag.setVisible(true);
        } else if (isStudent) {
            panel.setBackground(new Color(253, 237, 237));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 53, 69), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            lblTag.setText("YOUR CHOICE (INCORRECT)");
            lblTag.setForeground(new Color(180, 40, 40));
            lblTag.setVisible(true);
        } else {
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            lblTag.setVisible(false);
        }
    }

    private JPanel createOptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel lblText = new JLabel("Option");
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblText.setForeground(new Color(35, 45, 60));

        JLabel lblTag = new JLabel("TAG", SwingConstants.RIGHT);
        lblTag.setFont(new Font("Segoe UI", Font.BOLD, 11));

        panel.add(lblText, BorderLayout.CENTER);
        panel.add(lblTag, BorderLayout.EAST);

        panel.putClientProperty("textLabel", lblText);
        panel.putClientProperty("tagLabel", lblTag);

        return panel;
    }

    private JLabel createBadgeLabel(String text, Color bg) {
        JLabel badge = new JLabel(text);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(Color.WHITE);
        badge.setBackground(bg);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        return badge;
    }

    private JButton createFilterButton(String text, final String filterVal) {
        final JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(Color.WHITE);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFilter = filterVal;
                // find first item matching filter
                for (int i = 0; i < reviewItems.size(); i++) {
                    if (matchesFilter(reviewItems.get(i))) {
                        currentIndex = i;
                        break;
                    }
                }
                displayCurrentItem();
            }
        });
        return btn;
    }
}
