/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totaltuckshop.Accounts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.util.Collections.list;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Connection.Validation;

public class Create_productController implements Initializable {

    
    
    @FXML private JFXTextField product_code_field,product_name_field,purchase_price_field,productsDetails_field,
            sale_price_field;
    @FXML     private JFXButton save_button,clear_button;
    @FXML    private TableView<Table_Class> product_table;
    @FXML    private TableColumn<Table_Class,String>code_column,name_column ;
    @FXML    private TableColumn<Table_Class,Double> purchase_column,sale_column;
    @FXML    private JFXTextField search_field;
    @FXML    private Label total_product_label;
    @FXML    private MenuItem edit_record, delete_record;

    String edit_id="";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        events();
        populating_Table();
    }    
    
    private void events(){
        live_validations();
        
        save_button.setOnKeyReleased(evt->{
            if(evt.getCode()==KeyCode.ENTER){
                save();
            }
        });
        save_button.setOnAction(evt->{
            save();
        });
        
        clear_button.setOnKeyReleased(evt->{
            if(evt.getCode()==KeyCode.ENTER){
               clearAll();
            }
        });
        clear_button.setOnAction(evt->{
            clearAll();
        });
        
        edit_record.setOnAction(evt->{
            product_code_field.setDisable(true);
            save_button.setText("Update");
            table_to_field();
            
        });
        
        delete_record.setOnAction(evt->{
            delete();
        });
        
        search_field.setOnKeyReleased(evt->{
            filter_records();
             if(evt.getCode()==KeyCode.ENTER){
               product_table.requestFocus();
               if(list.size()>0){
                   product_table.getSelectionModel().select(0);
               }
            }
        });
        
    }//end of method
    
    
    
    
    private void table_to_field(){
      if(product_table.getSelectionModel().getSelectedItem() == null) return; 
      Table_Class row  = product_table.getSelectionModel().getSelectedItem();
      String id = row.getCode();
      edit_id = id;
      String query = "select * from products where product_id='"+id+"'";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                String barcode = rs.getString("product_code");
                String name = rs.getString("product_name");
                String purchase = rs.getString("purchasePrice");
                String salePrice = rs.getString("salePrice");
                String details = rs.getString("details");
                product_code_field.setText(barcode);
                product_name_field.setText(name);
                purchase_price_field.setText(purchase);
                sale_price_field.setText(salePrice);
                productsDetails_field.setText(details);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
      
    }
    
    private void live_validations(){
        product_name_field.setOnKeyReleased(evt->{
             if(evt.getCode() == KeyCode.ENTER){
                purchase_price_field.requestFocus();
            }
            Validation.Only_Text_Color(search_field, 15);
        });
        purchase_price_field.setOnKeyReleased(evt->{
             if(evt.getCode() == KeyCode.ENTER){
                sale_price_field.requestFocus();
            }
            Validation.product_Price_color(purchase_price_field, 6, 100000);
        });
        
        sale_price_field.setOnKeyReleased(evt->{
            if(evt.getCode() == KeyCode.ENTER){
                productsDetails_field.requestFocus();
            }
         Validation.product_Price_color(sale_price_field, 6, 100000);
        });
        
       product_code_field.setOnKeyReleased(evt->{
            if(evt.getCode()==KeyCode.ENTER && product_code_field.getText().length()>2){
               product_name_field.requestFocus();
            }
       });
       
     productsDetails_field.setOnKeyReleased(evt->{
            if(evt.getCode()==KeyCode.ENTER){
               save_button.requestFocus();
            }
       });
        
    }
    
    private boolean validation_befor_save(){
       if(product_code_field.getText().length() <4 || product_code_field.getText().length()>20){
           Alert a =  new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Message");
            a.setContentText("Scan your product BarCode");
            a.show();
            product_code_field.setText("");
            product_code_field.requestFocus();
            return false;
       }
        
       //if( Validation.Only_Text_Diaglog(product_name_field, 20, "Product Name")==false)return false;
       if( Validation.product_price_dialog(purchase_price_field, 6, 100000,"Purchase Product Price")==false)return false;
       if(Validation.product_price_dialog(sale_price_field, 6, 100000,"Re-Tail Price")==false)return false;
        
       
       double purchase = Double.parseDouble(purchase_price_field.getText());
       double sale = Double.parseDouble(sale_price_field.getText());
       if(purchase>sale){
           Alert a =  new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Message");
            a.setContentText("Re-Tail Price should be more then Purchase Price");
            a.show();
            purchase_price_field.requestFocus();
            return false;
       }
       
        return true;
    }
    
    
    private void save(){
        if(save_button.getText().equalsIgnoreCase("Update")){
            update();
            return;
        }
        if(validation_befor_save()==false)return;
        
        String code  = product_code_field.getText();
        String name  = product_name_field.getText().toUpperCase();
        String purchasePrice = purchase_price_field.getText();
        String salePrice = sale_price_field.getText();
        String details  = productsDetails_field.getText();
        String date = Validation.today_Date();
        
        String query = "INSERT INTO products(product_code,product_name,purchasePrice,salePrice,details,entryDate)"
                + " values('"+code+"', '"+name+"', '"+purchasePrice+"' , '"+salePrice+"', '"+details+"', '"+date+"'  )";
        
        String query_stock = "INSERT INTO stock(product_code,items) values('"+code+"','0')";
        try {
            Statement st= Connection_DB.db_Connection().createStatement();
            st.executeUpdate(query);
            st.executeUpdate(query_stock);
            
            Alert a =  new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Message");
            a.setContentText("Your Product has been saved succfully");
            a.show();
            clearAll();
            product_code_field.requestFocus();
            populating_Table();
        } catch(SQLException ex){
            Alert a =  new Alert(Alert.AlertType.ERROR);
            a.setTitle("Message");
            a.setContentText("Product Code"+product_code_field.getText()+" is Already Exists"+ex);
            a.show();
            product_code_field.requestFocus();
        }catch (Exception e) {
            Alert a =  new Alert(Alert.AlertType.ERROR);
            a.setTitle("Message");
            a.setContentText("Your Product is not saved "+e);
            a.show();
            e.printStackTrace();
            System.out.println(e);
        }
        }//end of save method   
    
    
    private void clearAll(){
        product_code_field.clear();
        product_name_field.clear();
        purchase_price_field.clear();
        sale_price_field.clear();
        productsDetails_field.clear();
        save_button.setText("Save");
        edit_id="";    
        product_code_field.setDisable(false);
        product_code_field.requestFocus();
     }
    
    
    ObservableList<Table_Class> list;
    private void populating_Table(){
        code_column.setCellValueFactory(new PropertyValueFactory<Table_Class,String>("code"));
        name_column.setCellValueFactory(new PropertyValueFactory<Table_Class,String>("name"));
        purchase_column.setCellValueFactory(new PropertyValueFactory<Table_Class,Double>("pprice"));
        sale_column.setCellValueFactory(new PropertyValueFactory<Table_Class,Double>("rprice"));
        
         list = FXCollections.observableArrayList();
        
        try {
            String query = "select * from products where status ='true'";
            Statement st=   Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            int count=0;
            while(rs.next()){
                String code = rs.getString("product_id");
                String name= rs.getString("product_name");
                double pPrice = rs.getDouble("purchasePrice");
                double rPrice = rs.getDouble("salePrice");
                list.add(new  Table_Class(code, name, pPrice, rPrice));
                count++;
            }
            product_table.setItems(list);
            total_product_label.setText("Total Products are : "+count);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        
    }

    private void update() {
        if(validation_befor_save()==false)return;
        
        String code  = product_code_field.getText();
        String name  = product_name_field.getText().toUpperCase();
        String purchasePrice = purchase_price_field.getText();
        String salePrice = sale_price_field.getText();
        String details  = productsDetails_field.getText();
        String date = Validation.today_Date();
        
        String query = "UPDATE products SET product_code='"+code+"' , product_name='"+name+"' , purchasePrice='"+purchasePrice+"' , salePrice='"+salePrice+"', details='"+details+"' "
                + " where product_id='"+edit_id+"'";
        try {
            Statement st= Connection_DB.db_Connection().createStatement();
            st.executeUpdate(query);
            
            Alert a =  new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Message");
            a.setContentText("Your Product has been updated succfully");
            a.show();
            clearAll();
            product_code_field.requestFocus();
            populating_Table();
        } catch (Exception e) {
            Alert a =  new Alert(Alert.AlertType.ERROR);
            a.setTitle("Message");
            a.setContentText("Your Product is not updated "+e);
            a.show();
            e.printStackTrace();
            System.out.println(e);
        }
        clearAll();
    }

    private void delete() {
        if(product_table.getSelectionModel().getSelectedItem() == null) return; 
              Table_Class row  = product_table.getSelectionModel().getSelectedItem();
              String id = row.getCode();
         String query = "Delete from products where product_id ='"+id+"' ";
         try {
           
            Alert alert = new Alert(AlertType.CONFIRMATION);
             alert.setTitle("Message");
             String s = "Do you Really want to delete "+row.getName()+" Product";
             alert.setContentText(s);
             Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                Statement st = Connection_DB.db_Connection().createStatement();
                st.executeUpdate(query);
             } 
            populating_Table();
        } catch (Exception e) {
            e.printStackTrace();
             System.out.println(e);
        }
    }
    
    public class Table_Class{
        String code, name;
        double pprice,rprice;

        public Table_Class(String code, String name, double pprice, double rprice) {
            this.code = code;
            this.name = name;
            this.pprice = pprice;
            this.rprice = rprice;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPprice() {
            return pprice;
        }

        public void setPprice(double pprice) {
            this.pprice = pprice;
        }

        public double getRprice() {
            return rprice;
        }

        public void setRprice(double rprice) {
            this.rprice = rprice;
        }
        
    }
    
    
    
    
public void filter_records() {
  if(search_field.getText().length()==0){
     product_table.setItems(list);
     return;
     }
     ObservableList<Table_Class> list2=FXCollections.observableArrayList();
     ObservableList<TableColumn<Table_Class,?>> cols=product_table.getColumns();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j <cols.size(); j++) {
                TableColumn ab=cols.get(j);
                String value=ab.getCellData(list.get(i)).toString().toLowerCase();
                String text = new String (search_field.getText().toString().toLowerCase());
                if (value.contains(text))
                {
                  list2.add(list.get(i));
                
                } else {
                }
            }         
                }
         product_table.setItems(list2);
    
}
}
