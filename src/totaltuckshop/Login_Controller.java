package totaltuckshop;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import totaltuckshop.Connection.Connection_DB;

 
public class Login_Controller implements Initializable {
 
    Statement st;
    @FXML JFXTextField username_Field;
    @FXML JFXPasswordField  password_Field;
    @FXML JFXButton login_Button;
    static String username,password,userId;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    Events();   
//        try {
//            String query = "ALTER TABLE fee_pending ADD admission VARCHAR(20)  NULL AFTER pending_code ";//, ADD 'registration' VARCHAR(20) NOT NULL AFTER 'admission'";
//            st.executeUpdate(query);
//             query = "ALTER TABLE fee_pending ADD registratioin VARCHAR(20)  NULL AFTER admission ";//, ADD 'registration' VARCHAR(20) NOT NULL AFTER 'admission'";
//            st.executeUpdate(query);
//            query = "DELETE FROM fee_pending";
//            st.executeUpdate(query);
//            
//            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
//            a.setContentText("Done");
//            a.showAndWait();
//            System.exit(0);
//            System.out.println("added");
//        } catch (Exception e) {
//            System.out.println("aaa "+e);
//        }
    
    }    
    
    public void Events(){
        login_Button.setOnAction(event->{
        Login1();
        });
        login_Button.setOnKeyReleased(event->{
        if(event.getCode() == KeyCode.ENTER){
            Login1();
        }
        });
        
        password_Field.setOnKeyReleased(evt->{
        if(evt.getCode() == KeyCode.ENTER){
             Login1();
        }
        });
        
    }//end of function
    
    
    private void Login1(){
     //   updatingdatabaes();
        String username = username_Field.getText();
        String pass= password_Field.getText();
         if(validation(username, pass)==false)return;
        
        try {
            
            String query = "select * from login where username='"+username+"' and user_pass='"+pass+"'";
            Statement st =  Connection_DB.db_Connection().createStatement();
            ResultSet rs  = st.executeQuery(query);
            if(rs.next()){
                this.username= rs.getString("username");
                password  = rs.getString("user_Pass");
                userId = rs.getString("user_id");
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/mainWindow.fxml"));
               Stage stag = new Stage();
                stag.setScene(new Scene(p));
                stag.setWidth(1100);
                stag.setHeight(700);
                stag.setTitle("Total Tuck shop");
                stag.show();
                TotalTuckShop.getStage().close();
                 
            }else{
                Alert a = new Alert(Alert.AlertType.WARNING);
                 a.setTitle("Message");
                a.setContentText("Wrong username or password");
                a.showAndWait(); 
            }
    
        } catch (Exception e) {
            System.out.println("Errro i login1 funciotn login controlle class"+e);
        }
 
        
    }//end of login
    
    private boolean validation(String a1 , String b1){
        if(a1.length()==0){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Empty");
            a.setContentText("Username Field is Empty");
            a.showAndWait();
            return false;
        }
        if(b1.length()==0){
             Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Empty");
            a.setContentText("Username Field is Empty");
            a.showAndWait();
            return false;
        }
        return true;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUserId() {
        return userId;
    }
    
    
     
   
}
