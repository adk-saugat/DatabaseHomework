// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConfig {
   public DatabaseConfig() {
   }

   public Connection connectDatabase() {
      try{
         FileReader reader = new FileReader("database.prop");
         Properties p = new Properties();
         p.load(reader);
      
         String dbdriver = p.getProperty("db.driver");
         String dbuser = p.getProperty("db.user");
         String dbpassword = p.getProperty("db.password");
         String dburl = p.getProperty("db.url");
      
         Class.forName(dbdriver);
         Connection connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
         System.out.println("Database connection successful!");
         return connection;
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("Connection not made!");
      }
      return null;
   }
}

