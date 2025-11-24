import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseQueries {
    private Connection connection;
    private Statement stmt;
    private String query;
    private ResultSet result;

    public DatabaseQueries(Connection connection) {
        this.connection = connection;
    }

    /**
     * Loads all department names from the DEPARTMENT table
     * @return Array of department names, or null if error occurs
     */
    public String[] LoadDepartment() {
        try {
            if (connection != null && !connection.isClosed()) {
                stmt = connection.createStatement();
                query = "SELECT Dname FROM DEPARTMENT ORDER BY Dname";
                result = stmt.executeQuery(query);

                ArrayList<String> departmentList = new ArrayList<String>();
                while (result.next()) {
                    departmentList.add(result.getString("Dname"));
                }
                return departmentList.toArray(new String[0]);
            }
        } catch (Exception e) {
            System.out.println("Error loading departments: " + e.getMessage());
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * Loads all project names from the PROJECT table
     * @return Array of project names
     */
    public String[] LoadProject() {
        try {
            if (connection != null && !connection.isClosed()) {
                stmt = connection.createStatement();
                query = "SELECT Pname FROM PROJECT ORDER BY Pname";
                result = stmt.executeQuery(query);

                ArrayList<String> projectList = new ArrayList<String>();
                while (result.next()) {
                    projectList.add(result.getString("Pname"));
                }
                return projectList.toArray(new String[0]);
            }
        } catch (Exception e) {
            System.out.println("Error loading projects: " + e.getMessage());
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * Searches for employees based on selected criteria.
     * Logic:
     * 1. Base query selects employees.
     * 2. If departments selected: Filter by Dname IN (or NOT IN) selection.
     * 3. If projects selected: Filter by Ssn IN (or NOT IN) a subquery of employees working on those projects.
     */
    public String[] SearchEmployees(String[] selectedDepartments, String[] selectedProjects, boolean notDept, boolean notProject) {
        ArrayList<String> employees = new ArrayList<>();
        
        try {
            if (connection == null || connection.isClosed()) {
                return new String[]{"Error: No Database Connection"};
            }

            StringBuilder sql = new StringBuilder("SELECT DISTINCT e.Fname, e.Lname FROM EMPLOYEE e ");
            // We join department to filter by department name easily
            sql.append("JOIN DEPARTMENT d ON e.Dno = d.Dnumber WHERE 1=1 ");

            // --- Handle Department Filter ---
            if (selectedDepartments != null && selectedDepartments.length > 0) {
                String operator = notDept ? "NOT IN" : "IN";
                sql.append("AND d.Dname ").append(operator).append(" (");
                for (int i = 0; i < selectedDepartments.length; i++) {
                    sql.append("'").append(selectedDepartments[i]).append("'");
                    if (i < selectedDepartments.length - 1) sql.append(",");
                }
                sql.append(") ");
            }

            // --- Handle Project Filter ---
            // Using a subquery is safer for Project filtering to avoid row duplication or logic errors with Joins
            if (selectedProjects != null && selectedProjects.length > 0) {
                String operator = notProject ? "NOT IN" : "IN";
                sql.append("AND e.Ssn ").append(operator).append(" (");
                sql.append("SELECT w.Essn FROM WORKS_ON w JOIN PROJECT p ON w.Pno = p.Pnumber WHERE p.Pname IN (");
                for (int i = 0; i < selectedProjects.length; i++) {
                    sql.append("'").append(selectedProjects[i]).append("'");
                    if (i < selectedProjects.length - 1) sql.append(",");
                }
                sql.append(")) ");
            }

            // Execute Query
            System.out.println("Executing Query: " + sql.toString()); // For debugging
            stmt = connection.createStatement();
            result = stmt.executeQuery(sql.toString());

            while (result.next()) {
                employees.add(result.getString("Fname") + " " + result.getString("Lname"));
            }

            if (employees.isEmpty()) {
                employees.add("No employees found matching criteria.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            employees.add("SQL Error: " + e.getMessage());
        }

        return employees.toArray(new String[0]);
    }
}