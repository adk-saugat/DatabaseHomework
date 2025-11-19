import java.util.Properties;
import java.io.FileReader;
import java.sql.*;

public class DatabaseConfig{

   public DatabaseConfig(){

   }
   public Connection connectDatabase()
   {
      Connection con = null;
   
      try
      {
         FileReader reader = new FileReader("database.prop");
         Properties p = new Properties();
         p.load(reader);
      
         String dbdriver = p.getProperty("db.driver");
         String dbuser = p.getProperty("db.user");
         String dbpassword = p.getProperty("db.password");
         String dburl = p.getProperty("db.url");
      
         con = DriverManager.getConnection(dburl, dbuser, dbpassword);
         return con;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}
