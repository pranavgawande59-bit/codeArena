package com.mcq.ui;

import com.mcq.dao.QuestionDAO;
import com.mcq.model.Question;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * ViewQuestionFrame.java
 * Displays questions in a sortable, filterable JTable with Topic, Difficulty, Marks,
 * and Explanations, alongside Edit, Delete, and multi-filter capabilities.
 */
public class ViewQuestionFrame extends JFrame {

    private final JFrame parentFrame;
    private final QuestionDAO questionDAO;

    private JComboBox<String> cmbLanguageFilter;
    private JComboBox<String> cmbTopicFilter;
    private JComboBox<String> cmbDifficultyFilter;
    private JTable questionTable;
    private DefaultTableModel tableModel;

    private final String[] columnNames = {
        "ID", "Language", "Topic", "Difficulty", "Marks", "Question",
        "Option A", "Option B", "Option C", "Option D", "Correct", "Explanation"
    };

    public ViewQuestionFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.questionDAO = new QuestionDAO();
        initUI();
        loadTableData();
    }

    private void initUI() {
        setTitle("View & Manage Questions - Admin Panel");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("QUESTIONS DIRECTORY & REPOSITORY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.NORTH);

        JLabel lblSub = new JLabel("Filter and manage questions across topics, difficulty levels, and learning explanations", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(210, 225, 245));
        headerPanel.add(lblSub, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Filter Bar (Top of Center Panel)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterPanel.setBackground(new Color(245, 247, 250));
        filterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 225, 235)));

        // Language Filter
        filterPanel.add(new JLabel("Language:"));
        List<String> langList = new ArrayList<>();
        langList.add("All");
        langList.addAll(questionDAO.getAvailableLanguages());
        cmbLanguageFilter = new JComboBox<>(langList.toArray(new String[0]));
        cmbLanguageFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbLanguageFilter.setPreferredSize(new Dimension(130, 30));
        filterPanel.add(cmbLanguageFilter);

        // Topic Filter
        filterPanel.add(new JLabel("Topic:"));
        cmbTopicFilter = new JComboBox<>(new String[]{"All"});
        cmbTopicFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTopicFilter.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(cmbTopicFilter);

        // Difficulty Filter
        filterPanel.add(new JLabel("Difficulty:"));
        cmbDifficultyFilter = new JComboBox<>(new String[]{"All", "Beginner", "Intermediate", "Advanced"});
        cmbDifficultyFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbDifficultyFilter.setPreferredSize(new Dimension(130, 30));
        filterPanel.add(cmbDifficultyFilter);

        JButton btnFilterRefresh = HomeFrame.createStyledButton("Apply Filter", new Color(13, 110, 253), Color.WHITE);
        btnFilterRefresh.setPreferredSize(new Dimension(110, 30));
        filterPanel.add(btnFilterRefresh);

        // Center Table Setup
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        questionTable = new JTable(tableModel);
        questionTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        questionTable.setRowHeight(26);
        questionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        questionTable.getTableHeader().setBackground(new Color(230, 235, 245));
        questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Column widths
        questionTable.getColumnModel().getColumn(0).setPreferredWidth(45);  // ID
        questionTable.getColumnModel().getColumn(1).setPreferredWidth(85);  // Language
        questionTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Topic
        questionTable.getColumnModel().getColumn(3).setPreferredWidth(95);  // Difficulty
        questionTable.getColumnModel().getColumn(4).setPreferredWidth(50);  // Marks
        questionTable.getColumnModel().getColumn(5).setPreferredWidth(260); // Question
        questionTable.getColumnModel().getColumn(6).setPreferredWidth(110); // Option A
        questionTable.getColumnModel().getColumn(7).setPreferredWidth(110); // Option B
        questionTable.getColumnModel().getColumn(8).setPreferredWidth(110); // Option C
        questionTable.getColumnModel().getColumn(9).setPreferredWidth(110); // Option D
        questionTable.getColumnModel().getColumn(10).setPreferredWidth(55); // Correct
        questionTable.getColumnModel().getColumn(11).setPreferredWidth(260);// Explanation

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        questionTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        questionTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        questionTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        questionTable.getColumnModel().getColumn(10).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(questionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(filterPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        bottomPanel.setBackground(new Color(235, 240, 248));

        JButton btnEdit = HomeFrame.createStyledButton("Edit Selected", new Color(255, 153, 0), Color.WHITE);
        btnEdit.setPreferredSize(new Dimension(130, 38));
        bottomPanel.add(btnEdit);

        JButton btnDelete = HomeFrame.createStyledButton("Delete Selected", new Color(220, 53, 69), Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(140, 38));
        bottomPanel.add(btnDelete);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(108, 117, 125), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(100, 38));
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        cmbLanguageFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTopicsDropdown();
                loadTableData();
            }
        });

        cmbTopicFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTableData();
            }
        });

        cmbDifficultyFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTableData();
            }
        });

        btnFilterRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTableData();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = questionTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(ViewQuestionFrame.this, "Please select a question from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int questionId = (int) tableModel.getValueAt(selectedRow, 0);
                Question q = questionDAO.getQuestionById(questionId);
                if (q != null) {
                    UpdateQuestionFrame updateFrame = new UpdateQuestionFrame(ViewQuestionFrame.this, q);
                    updateFrame.setVisible(true);
                    ViewQuestionFrame.this.setVisible(false);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = questionTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(ViewQuestionFrame.this, "Please select a question to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int questionId = (int) tableModel.getValueAt(selectedRow, 0);
                String qText = (String) tableModel.getValueAt(selectedRow, 5);

                int confirm = JOptionPane.showConfirmDialog(
                    ViewQuestionFrame.this,
                    "Are you sure you want to delete this question?\n\nID: " + questionId + "\nQuestion: " + qText,
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    if (questionDAO.deleteQuestion(questionId)) {
                        JOptionPane.showMessageDialog(ViewQuestionFrame.this, "Question deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                        loadTableData();
                    } else {
                        JOptionPane.showMessageDialog(ViewQuestionFrame.this, "Failed to delete question.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewQuestionFrame.this.dispose();
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

        updateTopicsDropdown();
    }

    private void updateTopicsDropdown() {
        String lang = (String) cmbLanguageFilter.getSelectedItem();
        List<String> topics = questionDAO.getAvailableTopics(lang);

        cmbTopicFilter.removeAllItems();
        cmbTopicFilter.addItem("All");
        for (String t : topics) {
            cmbTopicFilter.addItem(t);
        }
    }

    public void loadTableData() {
        tableModel.setRowCount(0);

        String lang = (String) cmbLanguageFilter.getSelectedItem();
        String topic = (String) cmbTopicFilter.getSelectedItem();
        String diff = (String) cmbDifficultyFilter.getSelectedItem();

        List<Question> questions = questionDAO.getFilteredQuestions(lang, topic, diff, 0); // 0 = no limit

        for (Question q : questions) {
            tableModel.addRow(new Object[]{
                q.getId(),
                q.getLanguage(),
                q.getTopic(),
                q.getDifficulty(),
                q.getMarks(),
                q.getQuestion(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD(),
                q.getCorrectAnswer(),
                q.getExplanation()
            });
        }
    }

    public void loadTableData(String language) {
        if (cmbLanguageFilter != null) {
            cmbLanguageFilter.setSelectedItem(language);
        }
        loadTableData();
    }
}
