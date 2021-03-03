/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totaltuckshop.Accounts;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import totaltuckshop.Connection.Connection_DB;

/**
 * FXML Controller class
 *
 * @author Laeeq Khan
 */
public class AccountsallProductsController implements Initializable {

    
    @FXML
    private TableView<TableClass> product_table;
    @FXML    private TableColumn<TableClass, String> code_column, name_column,detailsColumn;
    @FXML    private TableColumn<TableClass, Double> purchase_column,sale_column;
    @FXML    private TableColumn<TableClass, String> entry_date_column;
    @FXML    private JFXTextField search_field;
    @FXML    private Label total_products_label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         populating_Table();
         
         search_field.setOnKeyReleased(evt->{
             filter_records();
         });
    }    
    
    ObservableList<TableClass> list;
    private void populating_Table(){
        code_column.setCellValueFactory(new PropertyValueFactory<TableClass,String>("code"));
        name_column.setCellValueFactory(new PropertyValueFactory<TableClass,String>("name"));
        purchase_column.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("pprice"));
        sale_column.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("sprice"));
        entry_date_column.setCellValueFactory(new PropertyValueFactory<TableClass,String>("date"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<TableClass,String>("details"));
        
         list = FXCollections.observableArrayList();
         try {
            String query = "select * from products where status ='true'";
            Statement st=   Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            int count=1;
            while(rs.next()){
                String code = rs.getString("product_id");
                String name= rs.getString("product_name");
                double pPrice = rs.getDouble("purchasePrice");
                double rPrice = rs.getDouble("salePrice");
                String date = rs.getString("entryDate");
                String details = rs.getString("details");
                list.add(new  TableClass(code, date, details, name, pPrice, rPrice));
                count++;
            }
            product_table.setItems(list);
            total_products_label.setText("Total Products are : "+count);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
    public class TableClass{
        String code,date,details,name;
        double pprice,sprice;

        public TableClass(String code, String date, String details, String name, double pprice, double sprice) {
            this.code = code;
            this.date = date;
            this.details = details;
            this.name = name;
            this.pprice = pprice;
            this.sprice = sprice;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
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

        public double getSprice() {
            return sprice;
        }

        public void setSprice(double sprice) {
            this.sprice = sprice;
        }
        
    }//end of class
    
    public void filter_records() {
  if(search_field.getText().length()==0){
     product_table.setItems(list);
     return;
     }
     ObservableList<TableClass> list2=FXCollections.observableArrayList();
     ObservableList<TableColumn<TableClass,?>> cols=product_table.getColumns();
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
    
}//end of filter method
    
}
