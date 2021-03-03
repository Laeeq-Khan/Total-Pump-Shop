/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totaltuckshop;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Connection.Validation;

/**
 *
 * @author Laeeq Khan
 */
public class HomeController implements Initializable{

    
    @FXML private Label timeLabel,label1111,label11;
      
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayTime();
        label1111.setText(Validation.today_Date());
        todaySale();
    }
    
    public void displayTime(){
             Thread t = new Thread(new Runnable() {
	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
		while (true) {
			final String date = sdf.format(new Date());
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					timeLabel.setText(date);
				}
			});
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				break;
			}//end of catch
		}//end of whle
	}//end of run method
});
t.setName("Runnable Time Updater");
t.setDaemon(true);
t.start();
         }//end of function
    
    
    private void todaySale(){
       String query   = "select SUM(total) as totalBill from saleInvoiceDetail where date='"+label1111.getText()+"' and status='true'";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                label11.setText(rs.getString("totalBill"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
}
