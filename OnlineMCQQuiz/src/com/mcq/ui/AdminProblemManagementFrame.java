package com.mcq.ui;

import com.mcq.dao.CodingProblemDAO;
import com.mcq.model.CodingProblem;
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
 * AdminProblemManagementFrame.java
 * Screen allowing administrators to manage (Add, Edit, Delete, View) coding challenges in the Problem Bank.
 */
public class AdminProblemManagementFrame extends JFrame {

    private final JFrame parentFrame;
    private final CodingProblemDAO problemDAO;

    private JTable table;
    private DefaultTableModel tableModel;
    private List<CodingProblem> problemList = new ArrayList<>();

    public AdminProblemManagementFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.problemDAO = new CodingProblemDAO();
        initUI();
        loadTableData();
    }

    private void initUI() {
        setTitle("Manage Programming Problem Bank - Admin Portal");
        setSize(980, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        headerPanel.setLayout(new BorderLayout(0, 4));

        JLabel lblTitle = new JLabel("PROGRAMMING PROBLEM BANK REPOSITORY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Create, update, or remove coding challenges and algorithms", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Table
        String[] columns = {"ID", "Title", "Topic", "Difficulty", "Language", "Created"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(140);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        bottomBar.setBackground(new Color(235, 240, 248));

        JButton btnAdd = HomeFrame.createStyledButton("Add New Problem", new Color(40, 167, 69), Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(160, 38));
        bottomBar.add(btnAdd);

        JButton btnEdit = HomeFrame.createStyledButton("Edit Selected", new Color(255, 153, 0), Color.WHITE);
        btnEdit.setPreferredSize(new Dimension(130, 38));
        bottomBar.add(btnEdit);

        JButton btnDelete = HomeFrame.createStyledButton("Delete Selected", new Color(220, 53, 69), Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(140, 38));
        bottomBar.add(btnDelete);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(108, 117, 125), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(100, 38));
        bottomBar.add(btnBack);

        add(bottomBar, BorderLayout.SOUTH);

        // Action Listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEditProblemDialog dialog = new AddEditProblemDialog(AdminProblemManagementFrame.this, null);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    loadTableData();
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(AdminProblemManagementFrame.this, "Please select a problem from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                CodingProblem p = problemList.get(row);
                AddEditProblemDialog dialog = new AddEditProblemDialog(AdminProblemManagementFrame.this, p);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    loadTableData();
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(AdminProblemManagementFrame.this, "Please select a problem from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                CodingProblem p = problemList.get(row);
                int confirm = JOptionPane.showConfirmDialog(
                    AdminProblemManagementFrame.this,
                    "Are you sure you want to delete this coding problem?\n\nID: " + p.getId() + "\nTitle: " + p.getTitle(),
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    if (problemDAO.deleteProblem(p.getId())) {
                        JOptionPane.showMessageDialog(AdminProblemManagementFrame.this, "Problem deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                        loadTableData();
                    } else {
                        JOptionPane.showMessageDialog(AdminProblemManagementFrame.this, "Failed to delete problem.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminProblemManagementFrame.this.dispose();
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

    public void loadTableData() {
        tableModel.setRowCount(0);
        problemList = problemDAO.getFilteredProblems("All", "All", "All", "", 0);

        for (CodingProblem p : problemList) {
            String dateStr = (p.getCreatedAt() != null) ? p.getCreatedAt().toString().substring(0, 16) : "N/A";
            tableModel.addRow(new Object[]{
                "#" + p.getId(),
                p.getTitle(),
                p.getTopic(),
                p.getDifficulty(),
                p.getLanguage(),
                dateStr
            });
        }
    }
}
