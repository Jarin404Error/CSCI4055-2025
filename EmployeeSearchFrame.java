/**
 * Author: Lon Smith, Ph.D.
 * Description: This is the framework for the database program. Additional requirements and functionality
 *    are to be built by you and your group.
 */
//CHECKING for commit
 import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Vector;
import java.util.List;


public class EmployeeSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDatabase;
    private JList<String> lstDepartment;
    private DefaultListModel<String> department = new DefaultListModel<String>();
    private JList<String> lstProject;
    private DefaultListModel<String> project = new DefaultListModel<String>();
    private JTextArea textAreaEmployee;
    private JScrollPane scrollPaneDepartment;
    private JScrollPane scrollPaneProject;
    private JScrollPane scrollPaneEmployee;
    private JCheckBox chckbxNotDept;
    private JCheckBox chckbxNotProject;


    private static final String DEFAULT_DB_NAME = "company";
    private static final String DB_URL_PREFIX = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EmployeeSearchFrame frame = new EmployeeSearchFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public EmployeeSearchFrame() {
        setTitle("Employee Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Database:");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 23, 59, 14);
        contentPane.add(lblNewLabel);

        txtDatabase = new JTextField(DEFAULT_DB_NAME); // Set default value
        txtDatabase.setBounds(90, 20, 193, 20);
        contentPane.add(txtDatabase);
        txtDatabase.setColumns(10);

        JButton btnDBFill = new JButton("Fill");

        // ** CHANGE 2: Implement JDBC Connection and Data Retrieval Logic **
        btnDBFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillListsFromDatabase();
            }
        });

        btnDBFill.setFont(new Font("Times New Roman", Font.BOLD, 12));
        btnDBFill.setBounds(307, 19, 68, 23);
        contentPane.add(btnDBFill);

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblDepartment.setBounds(52, 63, 89, 14);
        contentPane.add(lblDepartment);

        JLabel lblProject = new JLabel("Project");
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblProject.setBounds(255, 63, 47, 14);
        contentPane.add(lblProject);

        // Setup Project List (lstProject)
        lstProject = new JList<String>(project);
        lstProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstProject.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        scrollPaneProject = new JScrollPane(lstProject);
        scrollPaneProject.setBounds(225, 84, 150, 70);
        contentPane.add(scrollPaneProject);

        chckbxNotDept = new JCheckBox("Not");
        chckbxNotDept.setBounds(71, 165, 59, 23);
        contentPane.add(chckbxNotDept);

        chckbxNotProject = new JCheckBox("Not");
        chckbxNotProject.setBounds(270, 165, 59, 23);
        contentPane.add(chckbxNotProject);

        // Setup Department List (lstDepartment)
        lstDepartment = new JList<String>(department);
        lstDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstDepartment.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        scrollPaneDepartment = new JScrollPane(lstDepartment);
        scrollPaneDepartment.setBounds(36, 84, 172, 70);
        contentPane.add(scrollPaneDepartment);

        JLabel lblEmployee = new JLabel("Employee");
        lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblEmployee.setBounds(52, 209, 89, 14);
        contentPane.add(lblEmployee);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchEmployees(); // Call the new search logic method
            }
        });
        btnSearch.setBounds(80, 420, 89, 23);
        contentPane.add(btnSearch);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaEmployee.setText("");
                lstDepartment.clearSelection();
                lstProject.clearSelection();
                chckbxNotDept.setSelected(false);
                chckbxNotProject.setSelected(false);
            }
        });
        btnClear.setBounds(236, 420, 89, 23);
        contentPane.add(btnClear);

        textAreaEmployee = new JTextArea();
        textAreaEmployee.setLineWrap(true);
        textAreaEmployee.setWrapStyleWord(true);
        textAreaEmployee.setEditable(false); // Employees are displayed, not edited

        scrollPaneEmployee = new JScrollPane(textAreaEmployee);
        scrollPaneEmployee.setBounds(36, 230, 339, 180);
        contentPane.add(scrollPaneEmployee);
    }
/**
     * Attempts to establish a connection to the database.
     * @return A valid Connection object, or null if connection fails.
     */
    private Connection getConnection() throws SQLException {
        String dbName = txtDatabase.getText().trim();
        if (dbName.isEmpty()) {
            JOptionPane.showMessageDialog(contentPane, "Please enter a database name.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String url = DB_URL_PREFIX + dbName;
        return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
    } 
    /**
     * Complete this line of Code as disscused prior Prabhakar Shrestha
     */
	private void fillListsFromDatabase() {
        department.clear();
        project.clear();
        textAreaEmployee.setText("");

        try (Connection conn = getConnection()) {
            if (conn == null) return; // Connection failed and message was already shown

            // --- 1. Populate Department List ---
            Statement deptStmt = conn.createStatement();
            ResultSet deptRs = deptStmt.executeQuery("SELECT Dname FROM DEPARTMENT ORDER BY Dname");

            while(deptRs.next()) {
                department.addElement(deptRs.getString("Dname"));
            }
            deptRs.close();
            deptStmt.close();

            // --- 2. Populate Project List ---
            Statement projStmt = conn.createStatement();
            ResultSet projRs = projStmt.executeQuery("SELECT Pname FROM PROJECT ORDER BY Pname");

            while(projRs.next()) {
                project.addElement(projRs.getString("Pname"));
            }
            projRs.close();
            projStmt.close();

            JOptionPane.showMessageDialog(contentPane, "Database lists populated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(contentPane,
                    "Database connection or query failed: " + ex.getMessage() + "\nCheck database name, credentials, and server status.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        	}
	}


    /**
     Complete this class as disscuesed for Pratik Pokhrel
     */
   private void searchEmployees() {
        textAreaEmployee.setText(""); // Clear previous results

        List<String> selectedDepts = lstDepartment.getSelectedValuesList();
        List<String> selectedProjs = lstProject.getSelectedValuesList();

        boolean isDeptSelected = !selectedDepts.isEmpty();
        boolean isProjSelected = !selectedProjs.isEmpty();
        
        if (!isDeptSelected && !isProjSelected) {
            JOptionPane.showMessageDialog(contentPane, "Please select at least one Department or Project.", "Search Criteria Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            if (conn == null) return;

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT E.Fname, E.Lname, E.Ssn ");
            sql.append("FROM EMPLOYEE E ");

            // We will conditionally JOIN based on the selections
            if (isProjSelected) {
                sql.append("JOIN WORKS_ON WO ON E.Ssn = WO.Essn ");
                sql.append("JOIN PROJECT P ON WO.Pno = P.Pnumber ");
            }

            // Build the WHERE clause
            sql.append("WHERE 1=1 "); // Start with a true condition for easy AND appending

            // --- Department Filtering ---
            if (isDeptSelected) {
                // If NOT Dept is checked, we select employees whose Dno is NOT IN the selected Dnumbers
                String deptOperator = chckbxNotDept.isSelected() ? "NOT IN" : "IN";
                
                // Subquery to get Dnumbers for the selected Dnames
                sql.append("AND E.Dno ");
                sql.append(deptOperator);
                sql.append(" (SELECT Dnumber FROM DEPARTMENT WHERE Dname IN (");
                
                // Add placeholders for Dname values
                for (int i = 0; i < selectedDepts.size(); i++) {
                    sql.append("?");
                    if (i < selectedDepts.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(")) ");
            }

            // --- Project Filtering ---
            if (isProjSelected) {
                // If NOT Project is checked, we use a subquery to select employees whose Ssn is NOT IN the Essn of people working on selected projects
                if (chckbxNotProject.isSelected()) {
                    sql.append("AND E.Ssn NOT IN (");
                    sql.append("SELECT Essn FROM WORKS_ON WO2 JOIN PROJECT P2 ON WO2.Pno = P2.Pnumber WHERE P2.Pname IN (");
                } else {
                    // Simple IN condition on the Pname from the JOINed tables
                    sql.append("AND P.Pname IN (");
                }

                // Add placeholders for Pname values
                for (int i = 0; i < selectedProjs.size(); i++) {
                    sql.append("?");
                    if (i < selectedProjs.size() - 1) {
                        sql.append(", ");
                    }
                }
                
                if (chckbxNotProject.isSelected()) {
                     sql.append(")) "); // Close Pname IN and Ssn NOT IN
                } else {
                    sql.append(") "); // Close Pname IN
                }
            }
            
            sql.append("ORDER BY E.Lname, E.Fname");

            // --- Execution ---
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            
            // Set parameters (Department names first, then Project names)
            int paramIndex = 1;
            if (isDeptSelected) {
                for (String deptName : selectedDepts) {
                    pstmt.setString(paramIndex++, deptName);
                }
            }
            if (isProjSelected) {
                for (String projName : selectedProjs) {
                    pstmt.setString(paramIndex++, projName);
                }
            }

            ResultSet rs = pstmt.executeQuery();

            // --- Display Results ---
            StringBuilder results = new StringBuilder();
            results.append("SQL Query Executed. Results:\n");
            
            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                results.append("No employees match the specified criteria.");
            } else {
                while (rs.next()) {
                    String fname = rs.getString("Fname");
                    String lname = rs.getString("Lname");
                    String ssn = rs.getString("Ssn");
                    results.append(String.format("%s %s (SSN: %s)\n", fname, lname, ssn));
                }
            }
            
            textAreaEmployee.setText(results.toString());
            
            rs.close();
            pstmt.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(contentPane,
                    "SQL Error during search: " + ex.getMessage(),
                    "Database Query Error",
                    JOptionPane.ERROR_MESSAGE);
            // Optionally print the generated SQL for debugging
            // System.err.println("Generated SQL: " + sql.toString()); 
        }
    }
}