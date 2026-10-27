package com.mcq.ui;

import com.mcq.dao.CodingProblemDAO;
import com.mcq.dao.QuestionDAO;
import com.mcq.dao.QuizAttemptDAO;
import com.mcq.dao.QuizAttemptDAO.ScoreTrendPoint;
import com.mcq.dao.QuizAttemptDAO.StudentOverviewStats;
import com.mcq.dao.QuizAttemptDAO.TopicStat;
import com.mcq.model.CodingProblem;
import com.mcq.model.QuizAttempt;
import com.mcq.model.Student;
import com.mcq.model.StudentSession;
import com.mcq.ui.chart.BarChartPanel;
import com.mcq.ui.chart.LineChartPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * StudentDashboardFrame.java
 * Central student command center featuring:
 * 1. Persistent performance analytics with custom Java2D line & bar charts
 * 2. Complete historical quiz attempt tracking and question review
 * 3. Topic and difficulty-filtered quiz configuration launcher
 * 4. Programming Problem Bank workspace with code solution saving
 */
public class StudentDashboardFrame extends JFrame {

    private final Student student;
    private final QuizAttemptDAO attemptDAO;
    private final QuestionDAO questionDAO;
    private final CodingProblemDAO codingProblemDAO;

    // Analytics components
    private JLabel lblTotalAttempts;
    private JLabel lblAvgScore;
    private JLabel lblMaxScore;
    private JLabel lblBestTopic;
    private JLabel lblSolvedProblems;
    private LineChartPanel lineChartPanel;
    private BarChartPanel barChartPanel;

    // Problem Bank components in dashboard
    private JTable problemTable;
    private DefaultTableModel problemTableModel;
    private List<CodingProblem> problemList = new ArrayList<>();
    private JComboBox<String> cmbProbLang;
    private JComboBox<String> cmbProbDiff;
    private JTextField txtProbSearch;

    // History components
    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    private List<QuizAttempt> attemptList = new ArrayList<>();

    // Custom Quiz Launcher components
    private JComboBox<String> cmbLanguage;
    private JComboBox<String> cmbTopic;
    private JComboBox<String> cmbDifficulty;
    private JComboBox<String> cmbQuestionCount;
    private JLabel lblAvailableCount;

    public StudentDashboardFrame() {
        this.student = StudentSession.getCurrentStudent();
        this.attemptDAO = new QuizAttemptDAO();
        this.questionDAO = new QuestionDAO();
        this.codingProblemDAO = new CodingProblemDAO();

        if (this.student == null) {
            JOptionPane.showMessageDialog(null, "No active student session. Redirecting to login.");
            new StudentLoginFrame(new HomeFrame()).setVisible(true);
            dispose();
            return;
        }

        initUI();
        refreshAllData();
    }

    private void initUI() {
        setTitle("Student Dashboard & Analytics - " + student.getFullName());
        setSize(1020, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JPanel userPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        userPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("STUDENT DASHBOARD & ANALYTICS", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("Welcome, " + student.getFullName() + " (" + student.getEmail() + ")", SwingConstants.LEFT);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(210, 225, 245));

        userPanel.add(lblTitle);
        userPanel.add(lblUser);
        headerPanel.add(userPanel, BorderLayout.WEST);

        // Header Action Buttons
        JPanel headerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerBtns.setOpaque(false);

        JButton btnHome = HomeFrame.createStyledButton("Home", new Color(13, 110, 253), Color.WHITE);
        btnHome.setPreferredSize(new Dimension(100, 36));
        headerBtns.add(btnHome);

        JButton btnLogout = HomeFrame.createStyledButton("Logout", new Color(108, 117, 125), Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(100, 36));
        headerBtns.add(btnLogout);

        headerPanel.add(headerBtns, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Main Tabbed Container
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Tab 1: Performance Analytics
        tabbedPane.addTab("  Performance Analytics  ", createAnalyticsPanel());

        // Tab 2: Quiz Attempt History
        tabbedPane.addTab("  Attempt History & Review  ", createHistoryPanel());

        // Tab 3: Take a Quiz
        tabbedPane.addTab("  Take Topic-Based Quiz  ", createTakeQuizPanel());

        // Tab 4: Coding Problem Bank
        tabbedPane.addTab("  Coding Problem Bank  ", createProblemBankPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Header listeners
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentDashboardFrame.this.dispose();
                new HomeFrame().setVisible(true);
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int c = JOptionPane.showConfirmDialog(
                    StudentDashboardFrame.this,
                    "Are you sure you want to sign out?",
                    "Confirm Sign Out",
                    JOptionPane.YES_NO_OPTION
                );
                if (c == JOptionPane.YES_OPTION) {
                    StudentSession.logout();
                    StudentDashboardFrame.this.dispose();
                    new HomeFrame().setVisible(true);
                }
            }
        });
    }

    /**
     * Tab 1: Performance Analytics Panel with KPI Cards and Charts
     */
    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 1. Top KPI Stat Cards (5 Cards)
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 85));

        lblTotalAttempts = new JLabel("0", SwingConstants.CENTER);
        lblAvgScore = new JLabel("0.0%", SwingConstants.CENTER);
        lblMaxScore = new JLabel("0.0%", SwingConstants.CENTER);
        lblBestTopic = new JLabel("-", SwingConstants.CENTER);
        lblSolvedProblems = new JLabel("0", SwingConstants.CENTER);

        statsPanel.add(createKpiCard("TOTAL QUIZZES", lblTotalAttempts, new Color(13, 110, 253)));
        statsPanel.add(createKpiCard("AVERAGE SCORE", lblAvgScore, new Color(40, 167, 69)));
        statsPanel.add(createKpiCard("HIGHEST SCORE", lblMaxScore, new Color(255, 153, 0)));
        statsPanel.add(createKpiCard("BEST TOPIC", lblBestTopic, new Color(111, 66, 193)));
        statsPanel.add(createKpiCard("PROBLEMS SOLVED", lblSolvedProblems, new Color(0, 128, 128)));

        panel.add(statsPanel, BorderLayout.NORTH);

        // 2. Charts Panel (Split horizontal: Line Chart + Bar Chart)
        JPanel chartsSplit = new JPanel(new GridLayout(2, 1, 0, 12));
        chartsSplit.setOpaque(false);

        lineChartPanel = new LineChartPanel("Score Progression Over Time (Improvement Trend)", new ArrayList<ScoreTrendPoint>());
        lineChartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        barChartPanel = new BarChartPanel("Topic Accuracy Breakdown & Topic Mastery", new ArrayList<TopicStat>());
        barChartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        chartsSplit.add(lineChartPanel);
        chartsSplit.add(barChartPanel);

        panel.add(chartsSplit, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Tab 2: Historical Quiz Attempts and Review Table
     */
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Table setup
        String[] cols = {"Attempt #", "Date", "Language", "Topic", "Difficulty", "Score", "Percentage", "Rating"};
        historyTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        historyTable = new JTable(historyTableModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyTable.setRowHeight(28);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        historyTable.getTableHeader().setBackground(new Color(230, 235, 245));
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        historyTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        historyTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        historyTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        historyTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        historyTable.getColumnModel().getColumn(7).setCellRenderer(centerRender);

        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235), 1));
        panel.add(scroll, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        bottomBar.setOpaque(false);

        JButton btnReview = HomeFrame.createStyledButton("Review Selected Attempt", new Color(13, 110, 253), Color.WHITE);
        btnReview.setPreferredSize(new Dimension(200, 40));
        bottomBar.add(btnReview);

        JButton btnRefresh = HomeFrame.createStyledButton("Refresh", new Color(108, 117, 125), Color.WHITE);
        btnRefresh.setPreferredSize(new Dimension(110, 40));
        bottomBar.add(btnRefresh);

        panel.add(bottomBar, BorderLayout.SOUTH);

        btnReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = historyTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, "Please select an attempt from the table to review.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                QuizAttempt attempt = attemptList.get(row);
                String title = attempt.getLanguage() + " - " + attempt.getTopic() + " (" + attempt.getDifficulty() + ")";
                QuizReviewFrame reviewFrame = new QuizReviewFrame(StudentDashboardFrame.this, attempt.getId(), title);
                reviewFrame.setVisible(true);
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAllData();
            }
        });

        return panel;
    }

    /**
     * Tab 3: Take Topic-Based Quiz Configuration Panel
     */
    private JPanel createTakeQuizPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 220, 235), 1),
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblHeader = new JLabel("CONFIGURE YOUR PRACTICE QUIZ", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(new Color(24, 43, 73));
        card.add(lblHeader, gbc);

        JLabel lblSub = new JLabel("<html><center>Filter questions by topic, difficulty, and quiz length to sharpen your skills.</center></html>", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(110, 120, 130));
        gbc.gridy = 1;
        card.add(lblSub, gbc);

        gbc.gridwidth = 1;

        // 1. Language
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblLang = new JLabel("Select Language:");
        lblLang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(lblLang, gbc);

        gbc.gridx = 1;
        List<String> langs = new ArrayList<>();
        langs.add("All");
        langs.addAll(questionDAO.getAvailableLanguages());
        cmbLanguage = new JComboBox<>(langs.toArray(new String[0]));
        cmbLanguage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLanguage.setPreferredSize(new Dimension(280, 36));
        card.add(cmbLanguage, gbc);

        // 2. Topic
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTop = new JLabel("Select Topic:");
        lblTop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(lblTop, gbc);

        gbc.gridx = 1;
        cmbTopic = new JComboBox<>(new String[]{"All"});
        cmbTopic.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTopic.setPreferredSize(new Dimension(280, 36));
        card.add(cmbTopic, gbc);

        // 3. Difficulty Level
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblDiff = new JLabel("Difficulty Level:");
        lblDiff.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(lblDiff, gbc);

        gbc.gridx = 1;
        cmbDifficulty = new JComboBox<>(new String[]{"All", "Beginner", "Intermediate", "Advanced"});
        cmbDifficulty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbDifficulty.setPreferredSize(new Dimension(280, 36));
        card.add(cmbDifficulty, gbc);

        // 4. Quiz Length
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblLen = new JLabel("Number of Questions:");
        lblLen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(lblLen, gbc);

        gbc.gridx = 1;
        cmbQuestionCount = new JComboBox<>(new String[]{"5 Questions", "10 Questions", "15 Questions", "20 Questions"});
        cmbQuestionCount.setSelectedIndex(1); // default 10
        cmbQuestionCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbQuestionCount.setPreferredSize(new Dimension(280, 36));
        card.add(cmbQuestionCount, gbc);

        // 5. Questions Available Live Counter
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        lblAvailableCount = new JLabel("Matching Questions Available: ...", SwingConstants.CENTER);
        lblAvailableCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAvailableCount.setForeground(new Color(40, 167, 69));
        card.add(lblAvailableCount, gbc);

        // 6. Launch Button
        gbc.gridy = 7;
        JButton btnLaunch = HomeFrame.createStyledButton("Start Customized Quiz", new Color(13, 110, 253), Color.WHITE);
        btnLaunch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLaunch.setPreferredSize(new Dimension(280, 44));
        card.add(btnLaunch, gbc);

        panel.add(card, new GridBagConstraints());

        // Update topics dynamically when language changes
        cmbLanguage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTopicsDropdown();
                updateLiveAvailabilityCounter();
            }
        });

        cmbTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLiveAvailabilityCounter();
            }
        });

        cmbDifficulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLiveAvailabilityCounter();
            }
        });

        updateTopicsDropdown();
        updateLiveAvailabilityCounter();

        // Launch Quiz Action
        btnLaunch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchCustomQuiz();
            }
        });

        return panel;
    }

    private void updateTopicsDropdown() {
        String selectedLang = (String) cmbLanguage.getSelectedItem();
        List<String> topics = questionDAO.getAvailableTopics(selectedLang);

        cmbTopic.removeAllItems();
        cmbTopic.addItem("All");
        for (String t : topics) {
            cmbTopic.addItem(t);
        }
    }

    private void updateLiveAvailabilityCounter() {
        String lang = (String) cmbLanguage.getSelectedItem();
        String topic = (String) cmbTopic.getSelectedItem();
        String diff = (String) cmbDifficulty.getSelectedItem();

        int count = questionDAO.getQuestionCount(lang, topic, diff);
        lblAvailableCount.setText("Questions Available in Database: " + count);
        if (count == 0) {
            lblAvailableCount.setForeground(new Color(220, 53, 69));
        } else {
            lblAvailableCount.setForeground(new Color(40, 167, 69));
        }
    }

    private void launchCustomQuiz() {
        String lang = (String) cmbLanguage.getSelectedItem();
        String topic = (String) cmbTopic.getSelectedItem();
        String diff = (String) cmbDifficulty.getSelectedItem();

        String countStr = (String) cmbQuestionCount.getSelectedItem();
        int requestedCount = 10;
        if (countStr != null) {
            try {
                requestedCount = Integer.parseInt(countStr.split(" ")[0]);
            } catch (Exception ex) {
                requestedCount = 10;
            }
        }

        int available = questionDAO.getQuestionCount(lang, topic, diff);
        if (available == 0) {
            JOptionPane.showMessageDialog(
                this,
                "No questions found matching your filter criteria:\n"
                + "Language: " + lang + "\nTopic: " + topic + "\nDifficulty: " + diff,
                "No Questions Available",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        QuizFrame quizFrame = new QuizFrame(
            StudentDashboardFrame.this,
            lang,
            topic,
            diff,
            requestedCount
        );
        quizFrame.setVisible(true);
        StudentDashboardFrame.this.setVisible(false);
    }

    /**
     * Tab 4: Problem Bank panel embedded directly in Student Dashboard.
     */
    private JPanel createProblemBankPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Filter / Search Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterBar.setOpaque(false);

        filterBar.add(new JLabel("Search:"));
        txtProbSearch = new JTextField(12);
        txtProbSearch.setPreferredSize(new Dimension(130, 28));
        filterBar.add(txtProbSearch);

        filterBar.add(new JLabel("Lang:"));
        List<String> langs = new ArrayList<>();
        langs.add("All");
        langs.addAll(codingProblemDAO.getAvailableLanguages());
        cmbProbLang = new JComboBox<>(langs.toArray(new String[0]));
        cmbProbLang.setPreferredSize(new Dimension(100, 28));
        filterBar.add(cmbProbLang);

        filterBar.add(new JLabel("Difficulty:"));
        cmbProbDiff = new JComboBox<>(new String[]{"All", "Beginner", "Intermediate", "Advanced"});
        cmbProbDiff.setPreferredSize(new Dimension(110, 28));
        filterBar.add(cmbProbDiff);

        JButton btnFilter = HomeFrame.createStyledButton("Filter", new Color(13, 110, 253), Color.WHITE);
        btnFilter.setPreferredSize(new Dimension(80, 28));
        filterBar.add(btnFilter);

        panel.add(filterBar, BorderLayout.NORTH);

        // Problem Table
        String[] cols = {"#", "Title", "Topic", "Difficulty", "Language", "Your Status"};
        problemTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        problemTable = new JTable(problemTableModel);
        problemTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        problemTable.setRowHeight(26);
        problemTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        problemTable.getTableHeader().setBackground(new Color(230, 235, 245));
        problemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        problemTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        problemTable.getColumnModel().getColumn(1).setPreferredWidth(320);
        problemTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        problemTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        problemTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        problemTable.getColumnModel().getColumn(5).setPreferredWidth(110);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        problemTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        problemTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);

        problemTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String diff = (value != null) ? value.toString() : "";
                if ("Beginner".equalsIgnoreCase(diff)) lbl.setForeground(new Color(40, 167, 69));
                else if ("Intermediate".equalsIgnoreCase(diff)) lbl.setForeground(new Color(255, 140, 0));
                else lbl.setForeground(new Color(220, 53, 69));
                return lbl;
            }
        });

        problemTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String st = (value != null) ? value.toString() : "Unsolved";
                if ("Solved".equalsIgnoreCase(st)) lbl.setForeground(new Color(40, 167, 69));
                else if ("Attempted".equalsIgnoreCase(st)) lbl.setForeground(new Color(13, 110, 253));
                else lbl.setForeground(new Color(130, 140, 150));
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(problemTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235), 1));
        panel.add(scroll, BorderLayout.CENTER);

        // Bottom Button
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        bottomBar.setOpaque(false);

        JButton btnOpen = HomeFrame.createStyledButton("Open Problem in Code Workspace", new Color(0, 128, 128), Color.WHITE);
        btnOpen.setPreferredSize(new Dimension(240, 36));
        bottomBar.add(btnOpen);

        JButton btnRefreshProb = HomeFrame.createStyledButton("Refresh", new Color(108, 117, 125), Color.WHITE);
        btnRefreshProb.setPreferredSize(new Dimension(90, 36));
        bottomBar.add(btnRefreshProb);

        panel.add(bottomBar, BorderLayout.SOUTH);

        // Listeners
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCodingProblems();
            }
        });

        btnOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCodingProblem();
            }
        });

        btnRefreshProb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCodingProblems();
            }
        });

        return panel;
    }

    private void openCodingProblem() {
        int row = problemTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a problem from the table to solve.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CodingProblem selected = problemList.get(row);
        CodingProblem fullProblem = codingProblemDAO.getProblemById(selected.getId(), student.getId());
        if (fullProblem != null) {
            ProblemDetailFrame detailFrame = new ProblemDetailFrame(StudentDashboardFrame.this, fullProblem);
            detailFrame.setVisible(true);
        }
    }

    private void loadCodingProblems() {
        if (problemTableModel == null) return;
        problemTableModel.setRowCount(0);

        String lang = (cmbProbLang != null) ? (String) cmbProbLang.getSelectedItem() : "All";
        String diff = (cmbProbDiff != null) ? (String) cmbProbDiff.getSelectedItem() : "All";
        String search = (txtProbSearch != null) ? txtProbSearch.getText().trim() : "";

        problemList = codingProblemDAO.getFilteredProblems(lang, "All", diff, search, student.getId());

        for (CodingProblem p : problemList) {
            problemTableModel.addRow(new Object[]{
                "#" + p.getId(),
                p.getTitle(),
                p.getTopic(),
                p.getDifficulty(),
                p.getLanguage(),
                p.getStudentStatus()
            });
        }
    }

    /**
     * Refreshes KPIs, Charts, History Table, and Problem Bank with latest MySQL data.
     */
    public void refreshAllData() {
        if (student == null) return;

        // 1. KPI Stats
        StudentOverviewStats stats = attemptDAO.getStudentOverviewStats(student.getId());
        lblTotalAttempts.setText(String.valueOf(stats.getTotalAttempts()));
        lblAvgScore.setText(String.format("%.1f%%", stats.getAveragePercentage()));
        lblMaxScore.setText(String.format("%.1f%%", stats.getHighestPercentage()));
        lblBestTopic.setText(stats.getBestTopic());

        int solvedCount = codingProblemDAO.getStudentSolvedCount(student.getId());
        if (lblSolvedProblems != null) {
            lblSolvedProblems.setText(String.valueOf(solvedCount));
        }

        // 2. Charts
        List<ScoreTrendPoint> trendPoints = attemptDAO.getScoreProgressOverTime(student.getId());
        lineChartPanel.setData(trendPoints);

        List<TopicStat> topicStats = attemptDAO.getTopicPerformance(student.getId());
        barChartPanel.setData(topicStats);

        // 3. History Table
        attemptList = attemptDAO.getAttemptsByStudentId(student.getId());
        historyTableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (QuizAttempt a : attemptList) {
            String dateStr = (a.getAttemptDate() != null) ? sdf.format(a.getAttemptDate()) : "N/A";
            historyTableModel.addRow(new Object[]{
                "#" + a.getId(),
                dateStr,
                a.getLanguage(),
                a.getTopic(),
                a.getDifficulty(),
                a.getScore() + " / " + a.getTotalMarks(),
                String.format("%.1f%%", a.getPercentage()),
                a.getFeedbackMessage()
            });
        }

        // 4. Problem Bank
        loadCodingProblems();
    }

    private JPanel createKpiCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
            )
        ));

        JLabel lblCardTitle = new JLabel(title, SwingConstants.LEFT);
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblCardTitle.setForeground(new Color(110, 120, 135));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(lblCardTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }
}
