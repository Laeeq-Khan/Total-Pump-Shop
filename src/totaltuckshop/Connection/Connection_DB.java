 
package totaltuckshop.Connection;
 
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Connection_DB {
    static Connection con;
    public static void connect_with_db(){
          try {
                Class.forName("org.sqlite.JDBC"); 
                con =  DriverManager.getConnection("jdbc:sqlite:database/totalTuckShop.db");
                System.out.println("Connected");
          } catch (SQLException e) {
              System.out.println(e.getErrorCode());
            System.out.println("connection db class and functioin connection error"+e);
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Database connection error");
            a.setContentText("Your are not connected with database");
            a.showAndWait();
            
         }catch(Exception e){
             Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Database connection error");
            a.setContentText("Your are not connected with database: Error Code : "+e);
            a.showAndWait();
           
            System.out.println("connection db class and functioin connection error"+e);
          }
        
     }//end of function
    
    
   
    public static Connection db_Connection(){
        try {
           if(con.isClosed() || con == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Database connection error");
            a.setContentText("Your are not connected with database");
            a.showAndWait();
            connect_with_db();
        }
        } catch (Exception e) {
            System.out.println("Error in connection functioin class in conneciton db");
        }
        return con;
    }
    
   
}



