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
     * Complete this line of Code as disscused prior Prabhakar Shrestha
     */
    private void fillListsFromDatabase() {
        department.clear();
        project.clear();
    }

    /**
     Complete this class as disscuesed for Pratik Pokhrel
     */
    private void searchEmployees() {
        textAreaEmployee.setText(""); // Clear previous results
   }
}