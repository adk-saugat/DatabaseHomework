/**
 * Author: Lon Smith, Ph.D.
 * Description: This is the framework for the database program. Additional requirements and functionality
 *    are to be built by you and your group.
 * 
 * Group Members: [Add your group members' names here
 */
// checking github
 import java.awt.EventQueue;

 //Checking SQL Database Name
 import java.sql.SQLException;

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
 import java.sql.Connection;
 import java.awt.event.ActionEvent;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JOptionPane;
 
 public class EmployeeSearchFrame extends JFrame {
  
    private boolean listFilled = false;
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDatabase;
    private JList<String> lstDepartment;
    private DefaultListModel<String> department = new DefaultListModel<String>();
    private JList<String> lstProject;
    private DefaultListModel<String> project = new DefaultListModel<String>();
    private JTextArea textAreaEmployee;
    private static Connection conn;
    private JCheckBox chckbxNotDept;
    private JCheckBox chckbxNotProject;
     /**
      * Launch the application.
      */
     public static void main(String[] args) {
         EventQueue.invokeLater(new Runnable() {
             public void run() {
                 try {
                     EmployeeSearchFrame frame = new EmployeeSearchFrame();
                     frame.setVisible(true);
                     DatabaseConfig config = new DatabaseConfig();
                     conn = config.connectDatabase();

                     //No Connection Pop-Up Error Message At Start Up
                     if (conn == null)
                        JOptionPane.showMessageDialog(frame, "Could not successfully connect to the database.\nPlease check your connection settings and try again.",
                                                                "! Database Connection Error !", 
                                                                JOptionPane.ERROR_MESSAGE);
                     
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
         });
     }
 
     /**
     * The ensureDBValid makes sure the database entered is valid
     * It also makes sure the database field is not empty
     */
     private boolean ensureDBValid() {
        if (conn == null) {
                JOptionPane.showMessageDialog(EmployeeSearchFrame.this,
                    "No database connection established.\nPlease connect to the database before filling the list.",
                    "! No Database Connection !", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
                }
        
        String enteredDBName = txtDatabase.getText().trim();

        if (enteredDBName.isEmpty()) {
            JOptionPane.showMessageDialog(EmployeeSearchFrame.this,
            "Please Enter A Database Name Before Filling",
            "! Missing Database Name !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            conn.setCatalog(enteredDBName);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(EmployeeSearchFrame.this,
                                        "The Database \"" + enteredDBName + "\" is not a valid database or cannot be accessed.\nPlease enter a valid Database Name.",
                                            "! Invalid Database Name !",
                                        JOptionPane.ERROR_MESSAGE);
        return false;
        }

        return true;
     }
     
     /**
      * Create the frame.
      */
     public EmployeeSearchFrame() {
         setTitle("Employee Search");
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setBounds(100, 100, 450, 347);
         contentPane = new JPanel();
         contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
         
         setContentPane(contentPane);
         contentPane.setLayout(null);
         
         JLabel lblNewLabel = new JLabel("Database:");
         lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
         lblNewLabel.setBounds(21, 23, 59, 14);
         contentPane.add(lblNewLabel);
         
         txtDatabase = new JTextField();
         txtDatabase.setBounds(90, 20, 193, 20);
         contentPane.add(txtDatabase);
         txtDatabase.setColumns(10);

         
        JButton btnDBFill = new JButton("Fill");
        /**
         * The btnDBFill should fill the department and project JList with the 
         * departments and projects from your entered database name.
         */
        btnDBFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Error Message Pop-Up if Database Is Not Valid
                if (!ensureDBValid()) {
                    department.clear();
                    project.clear();
                    listFilled = false;
                    return;
                }
                
                DatabaseQueries queries = new DatabaseQueries(conn);

                // Clear existing items before adding new ones
                department.clear();
                project.clear();

                String[] dept = queries.LoadDepartment();	
                for(int i = 0; i < dept.length; i++) {
                    department.addElement(dept[i]);
                }
                
                String[] prj = queries.LoadProject();
                for(int j = 0; j < prj.length; j++) {
                    project.addElement(prj[j]);
                }

                listFilled = true;
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
         
        lstProject = new JList<String>(new DefaultListModel<String>());
        lstProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstProject.setModel(project);
        lstProject.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPaneProject = new JScrollPane(lstProject);
        scrollPaneProject.setBounds(225, 84, 150, 42);
        contentPane.add(scrollPaneProject);
         
        chckbxNotDept = new JCheckBox("Not");
        chckbxNotDept.setBounds(71, 133, 59, 23);
        contentPane.add(chckbxNotDept);
        
        chckbxNotProject = new JCheckBox("Not");
        chckbxNotProject.setBounds(270, 133, 59, 23);
        contentPane.add(chckbxNotProject);
         
        lstDepartment = new JList<String>(new DefaultListModel<String>());
        lstDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstDepartment.setModel(department);
        lstDepartment.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPaneDepartment = new JScrollPane(lstDepartment);
        scrollPaneDepartment.setBounds(36, 84, 172, 40);
        contentPane.add(scrollPaneDepartment);
         
         JLabel lblEmployee = new JLabel("Employee");
         lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
         lblEmployee.setBounds(52, 179, 89, 14);
         contentPane.add(lblEmployee);
         
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Error Message Pop-Up If Database Is Not Valid
                if (!ensureDBValid()) {
                    department.clear();
                    project.clear();
                    return;
                }

                if (!listFilled) {
                    JOptionPane.showMessageDialog(EmployeeSearchFrame.this,"Please click the \"Fill\" button to load the Departments and Projects before searching for Employees\n",
                    "! Lists Not Loaded !", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // Get selected departments
                java.util.List<String> selectedDeptList = lstDepartment.getSelectedValuesList();
                String[] selectedDepartments = selectedDeptList.toArray(new String[0]);
                
                // Get selected projects
                java.util.List<String> selectedProjList = lstProject.getSelectedValuesList();
                String[] selectedProjects = selectedProjList.toArray(new String[0]);
                
                // Get checkbox states
                boolean notDept = chckbxNotDept.isSelected();
                boolean notProject = chckbxNotProject.isSelected();
                
                // Perform search
                DatabaseQueries queries = new DatabaseQueries(conn);
                String[] employees = queries.SearchEmployees(selectedDepartments, selectedProjects, notDept, notProject);
                
                // Display results
                StringBuilder resultText = new StringBuilder();
                for (String employee : employees) {
                    resultText.append(employee).append("\n");
                }
                textAreaEmployee.setText(resultText.toString());
            }
        });
         btnSearch.setBounds(80, 276, 89, 23);
         contentPane.add(btnSearch);
         
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaEmployee.setText("");
                txtDatabase.setText("");
                department.clear();
                project.clear();
                lstDepartment.clearSelection();
                lstProject.clearSelection();
                chckbxNotDept.setSelected(false);
                chckbxNotProject.setSelected(false);
                listFilled = false;
            }
        });
         btnClear.setBounds(236, 276, 89, 23);
         contentPane.add(btnClear);
         
        textAreaEmployee = new JTextArea();
        textAreaEmployee.setBounds(36, 197, 339, 68);
        JScrollPane scrollPaneEmployee = new JScrollPane(textAreaEmployee);
        scrollPaneEmployee.setBounds(36, 197, 339, 68);
        contentPane.add(scrollPaneEmployee);
     }
 }