/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totaltuckshop;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Custom.About_Shop;

/**
 *
 * @author Laeeq Khan
 */
public class mainWindow implements Initializable {
     @FXML    private MenuItem saleReturn,purchaseReturn,billingHistory,menu_create_product,billing,viewAllProducts,addStock,purchaseHistory,viewAllStock;
     @FXML    private AnchorPane main_Container;
     @FXML    private AnchorPane homeAnchor;
     @FXML    private MenuItem saleReport ,purchaseReport, changePassword,home;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetching_shop_info();
        events();
         try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/Home.fxml"));
                  main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
    }   
    
    
    
    public void events(){
        
        purchaseReport.setOnAction(evt->{
        try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/purchaseReport.fxml"));
                 main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        saleReport.setOnAction(evt->{
        try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/saleReport.fxml"));
                 main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        changePassword.setOnAction(evt->{
            try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/changePassword.fxml"));
                 main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
    
        
        home.setOnAction(evt->{
         try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/Home.fxml"));
                 main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        
        
          purchaseReturn.setOnAction(evt->{
         try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/purchaseReturn.fxml"));
                 main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
          
          
            saleReturn.setOnAction(evt->{
         try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/saleReturn.fxml"));
                 main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        billingHistory.setOnAction(evt->{
         try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/viewSaleHistory.fxml"));
                 main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        
        billing.setOnAction(evt->{
          try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/billing.fxml"));
                  main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        viewAllStock.setOnAction(evt->{
           try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/viewStock.fxml"));
                  main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        menu_create_product.setOnAction(evt->{
            try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/create_product.fxml"));
                  main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        viewAllProducts.setOnAction(evt->{
        try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/accountsallProducts.fxml"));
                  main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("added");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        addStock.setOnAction(evt->{
            try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/addStock.fxml"));
                  main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("added");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        
        purchaseHistory.setOnAction(evt->{
             try { 
                 AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/viewPurchaseHistory.fxml"));
                  main_Container.getChildren().clear();
                 main_Container.getChildren().add(p);
                 System.out.println("added");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
    }
    
    
    
    
    public void fetching_shop_info(){
        String query = "select  * from about where id ='1' ";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                String storeName = rs.getString("store_name");
                String username = "Laeeq";
                About_Shop shop = About_Shop.getInstance();
                shop.setStoreName(storeName);
                shop.setUsername(username);
                shop.setId("1");
                System.out.println(storeName);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Class Mainwindow "+e);
        }
    }
    
}
