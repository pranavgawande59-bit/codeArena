package com.mcq.ui;

import com.mcq.dao.CodingProblemDAO;
import com.mcq.model.CodingProblem;
import com.mcq.model.StudentSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * ProblemBankFrame.java
 * Programming Problem Bank explorer screen.
 * Allows students and learners to browse, filter by topic/difficulty/language,
 * search by keyword, and open coding challenges.
 */
public class ProblemBankFrame extends JFrame {

    private final JFrame parentFrame;
    private final CodingProblemDAO problemDAO;

    private JTextField txtSearch;
    private JComboBox<String> cmbLanguage;
    private JComboBox<String> cmbTopic;
    private JComboBox<String> cmbDifficulty;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<CodingProblem> problemList = new ArrayList<>();

    public ProblemBankFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.problemDAO = new CodingProblemDAO();
        initUI();
        loadProblems();
    }

    private void initUI() {
        setTitle("Programming Problem Bank - Online MCQ Quiz System");
        setSize(1040, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        headerPanel.setLayout(new BorderLayout(0, 4));

        JLabel lblTitle = new JLabel("PROGRAMMING PROBLEM BANK", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Curated coding challenges with constraints, sample I/O, explanations, and starter code", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Filter / Search Bar (Top of Center)
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterBar.setBackground(new Color(245, 247, 250));
        filterBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 225, 235)));

        // Keyword Search
        filterBar.add(new JLabel("Search:"));
        txtSearch = new JTextField(12);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(140, 30));
        filterBar.add(txtSearch);

        // Language Filter
        filterBar.add(new JLabel("Language:"));
        List<String> langs = new ArrayList<>();
        langs.add("All");
        langs.addAll(problemDAO.getAvailableLanguages());
        cmbLanguage = new JComboBox<>(langs.toArray(new String[0]));
        cmbLanguage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbLanguage.setPreferredSize(new Dimension(110, 30));
        filterBar.add(cmbLanguage);

        // Topic Filter
        filterBar.add(new JLabel("Topic:"));
        List<String> topics = new ArrayList<>();
        topics.add("All");
        topics.addAll(problemDAO.getAvailableTopics());
        cmbTopic = new JComboBox<>(topics.toArray(new String[0]));
        cmbTopic.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTopic.setPreferredSize(new Dimension(130, 30));
        filterBar.add(cmbTopic);

        // Difficulty Filter
        filterBar.add(new JLabel("Difficulty:"));
        cmbDifficulty = new JComboBox<>(new String[]{"All", "Beginner", "Intermediate", "Advanced"});
        cmbDifficulty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbDifficulty.setPreferredSize(new Dimension(120, 30));
        filterBar.add(cmbDifficulty);

        JButton btnFilter = HomeFrame.createStyledButton("Filter", new Color(13, 110, 253), Color.WHITE);
        btnFilter.setPreferredSize(new Dimension(85, 30));
        filterBar.add(btnFilter);

        JButton btnReset = HomeFrame.createStyledButton("Reset", new Color(108, 117, 125), Color.WHITE);
        btnReset.setPreferredSize(new Dimension(85, 30));
        filterBar.add(btnReset);

        // Table
        String[] columns = {"#", "Title", "Topic", "Difficulty", "Language", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(45);  // #
        table.getColumnModel().getColumn(1).setPreferredWidth(340); // Title
        table.getColumnModel().getColumn(2).setPreferredWidth(140); // Topic
        table.getColumnModel().getColumn(3).setPreferredWidth(110); // Difficulty
        table.getColumnModel().getColumn(4).setPreferredWidth(90);  // Language
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Status

        // Center renderers
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRender);

        // Custom Difficulty renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String diff = (value != null) ? value.toString() : "";
                if ("Beginner".equalsIgnoreCase(diff)) {
                    lbl.setForeground(new Color(40, 167, 69));
                } else if ("Intermediate".equalsIgnoreCase(diff)) {
                    lbl.setForeground(new Color(255, 140, 0));
                } else {
                    lbl.setForeground(new Color(220, 53, 69));
                }
                return lbl;
            }
        });

        // Custom Status renderer
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String st = (value != null) ? value.toString() : "Unsolved";
                if ("Solved".equalsIgnoreCase(st)) {
                    lbl.setForeground(new Color(40, 167, 69));
                } else if ("Attempted".equalsIgnoreCase(st)) {
                    lbl.setForeground(new Color(13, 110, 253));
                } else {
                    lbl.setForeground(new Color(130, 140, 150));
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(filterBar, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        bottomBar.setBackground(new Color(235, 240, 248));

        JButton btnOpen = HomeFrame.createStyledButton("Solve / Open Problem", new Color(13, 110, 253), Color.WHITE);
        btnOpen.setPreferredSize(new Dimension(190, 40));
        bottomBar.add(btnOpen);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(108, 117, 125), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(100, 40));
        bottomBar.add(btnBack);

        add(bottomBar, BorderLayout.SOUTH);

        // Action Listeners
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProblems();
            }
        });

        txtSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProblems();
            }
        });

        cmbLanguage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProblems();
            }
        });

        cmbTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProblems();
            }
        });

        cmbDifficulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProblems();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText("");
                cmbLanguage.setSelectedIndex(0);
                cmbTopic.setSelectedIndex(0);
                cmbDifficulty.setSelectedIndex(0);
                loadProblems();
            }
        });

        btnOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSelectedProblem();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProblemBankFrame.this.dispose();
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

    private void openSelectedProblem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a problem from the table to solve.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CodingProblem selected = problemList.get(row);
        int studentId = StudentSession.isLoggedIn() ? StudentSession.getCurrentStudent().getId() : 0;
        CodingProblem fullProblem = problemDAO.getProblemById(selected.getId(), studentId);

        if (fullProblem != null) {
            ProblemDetailFrame detailFrame = new ProblemDetailFrame(ProblemBankFrame.this, fullProblem);
            detailFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Problem details not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadProblems() {
        tableModel.setRowCount(0);

        String lang = (String) cmbLanguage.getSelectedItem();
        String topic = (String) cmbTopic.getSelectedItem();
        String diff = (String) cmbDifficulty.getSelectedItem();
        String search = txtSearch.getText().trim();

        int studentId = StudentSession.isLoggedIn() ? StudentSession.getCurrentStudent().getId() : 0;

        problemList = problemDAO.getFilteredProblems(lang, topic, diff, search, studentId);

        for (int i = 0; i < problemList.size(); i++) {
            CodingProblem p = problemList.get(i);
            tableModel.addRow(new Object[]{
                "#" + p.getId(),
                p.getTitle(),
                p.getTopic(),
                p.getDifficulty(),
                p.getLanguage(),
                p.getStudentStatus()
            });
        }
    }
}
