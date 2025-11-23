import java.sql.*;
import java.util.ArrayList;

public class DatabaseQueries{
    private Connection connection;
    private Statement stmt;
    private String query;
    private ResultSet result;

    public DatabaseQueries(Connection connection){
        this.connection = connection;
    }

    /**
     * Loads all department names from the DEPARTMENT table
     * @return Array of department names, or null if error occurs
     */
    public String[] LoadDepartment(){
        try{
            if(connection != null && !connection.isClosed()){
                stmt = connection.createStatement();
                query = "SELECT Dname FROM DEPARTMENT";
                result = stmt.executeQuery(query);

                ArrayList<String> departmentList = new ArrayList<String>();
                while (result.next()){
                    departmentList.add(result.getString("Dname"));
                }
                return departmentList.toArray(new String[0]);
            } else {
                System.out.println("Connection is null or closed!");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public String[] LoadProject(){
        return null;
    }
    
    public String[] SearchEmployees(String[] selectedDepartments, String[] selectedProjects, boolean notDept, boolean notProject) {
        return null;
    }
}