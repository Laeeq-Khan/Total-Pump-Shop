/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totaltuckshop.Stock;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Connection.Database_Returns;
import totaltuckshop.Connection.Validation;

 
public class AddStockController implements Initializable {

    @FXML    private TableView<Table_Class> product_table;
    @FXML    private TableColumn<Table_Class,String>code_column,name_column ;
    @FXML    private TableColumn<Table_Class,Double> purchase_column,sale_column;
    @FXML    private JFXTextField search_field,scanField;
    @FXML    private Label details_code,details_purchase, details_stock,details_Pname, details_sale;
    @FXML    private JFXTextField invoiceNo_Field,items_Field, cashDiscount_Field, dicountPercent_Field, total_Field, gtotal_Field;
    @FXML    private JFXButton addItemButton,saveInvoiceButton;
    @FXML    private TableView<TableClass> invoice_table;
    @FXML    private TableColumn<TableClass, String> invoice_col_code, invoice_col_name;
    @FXML    private TableColumn<TableClass, Integer> invoice_col_items, invoice_col_cashDiscount;
    @FXML    private TableColumn<TableClass, Double> invoice_col_percentD;
    @FXML    private TableColumn<TableClass, Double> invoice_col_total;
    @FXML    private Label totalItems_Label,grandTotalLabel;
    @FXML    private MenuItem removeItem;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       populating_Table();
       events();
       scanField.requestFocus();
       register_invoiceTable();
    }    
    
    private void events(){
        
        
        saveInvoiceButton.setOnAction(evt->{
              if(invoice_list.size()<=0){
                 Alert a = new Alert(Alert.AlertType.ERROR);
                 a.setTitle("Warning Message");
                 a.setContentText("First Add some Products in Table then Save Invoice");
                 a.showAndWait();
                 scanField.requestFocus();
                 return;
             }
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
             alert.setTitle("Message");
             String s = "Do you want to Save this invoice? Press Ok to Save ";
             alert.setContentText(s);
             Optional<ButtonType> result = alert.showAndWait();
             if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                 saveToDatabase();
             } 
        });
        
        removeItem.setOnAction(evt->{
           if(invoice_table.getSelectionModel().getSelectedIndex() != -1){
               int index = invoice_table.getSelectionModel().getSelectedIndex();
               invoice_list.remove(index);
               total_calculator();
           }
        });
        
        search_field.setOnKeyReleased(evt->{
            if(evt.getCode() == KeyCode.ENTER){
                if(list.size()>0){
                    product_table.requestFocus();
                    product_table.getSelectionModel().select(0);
                      if(product_table.getSelectionModel().getSelectedItems()==null)return;
                      if(product_table.getSelectionModel().getSelectedIndex() == -1)return;
                    Table_Class data = product_table.getSelectionModel().getSelectedItem();
                    String code = data.getCode();
                    product_getails(code);
                }
            }
            filter_records();
        });
        
        product_table.setOnKeyReleased(evt->{
            if(product_table.getSelectionModel().getSelectedItems()==null)return;
            if(product_table.getSelectionModel().getSelectedIndex() == -1)return;
            Table_Class data = product_table.getSelectionModel().getSelectedItem();
            String code = data.getCode();
            product_getails(code);
            
            if(evt.getCode() == KeyCode.ENTER){
                if(invoiceNo_Field.getText().length()>0){
                    items_Field.requestFocus();
                }else{
                     invoiceNo_Field.requestFocus();
                }
               
            }
            total_amount_calculate();
            grandtotal_amount_calculate();
        });
        product_table.setOnMouseClicked(evt->{
           if(product_table.getSelectionModel().getSelectedItems()==null)return;
           if(product_table.getSelectionModel().getSelectedIndex() == -1)return;  
            Table_Class data = product_table.getSelectionModel().getSelectedItem();
            String code = data.getCode();
             product_getails(code);
             total_amount_calculate();
             grandtotal_amount_calculate();
        });
        
        scanField.setOnKeyReleased(evt->{
            
            
            if(evt.getCode()==KeyCode.ENTER){
                if(scanField.getText()==null || scanField.getText().length()==0)return;
                if(scanField.getText().length()>2)
                  invoiceNo_Field.requestFocus();
                 if(invoiceNo_Field.getText().length()>0){
                    items_Field.requestFocus();
                     
                }
                
                product_getails(scanField.getText());
            }
        });
        
        invoiceNo_Field.setOnKeyReleased(evt->{
             Validation.Only_Number_Color(invoiceNo_Field, 20);
            if(evt.getCode()==KeyCode.ENTER){
                items_Field.requestFocus();
            }
        });
        
         items_Field.setOnKeyReleased(evt->{
            Validation.Only_Number_Color(items_Field, 4);
            if(evt.getCode()==KeyCode.ENTER){
                cashDiscount_Field.requestFocus();
            }
            
            total_amount_calculate();
             grandtotal_amount_calculate();
        });
         
          cashDiscount_Field.setOnKeyReleased(evt->{
               Validation.Only_Number_Color(cashDiscount_Field, 6);
            if(evt.getCode()==KeyCode.ENTER){
                dicountPercent_Field.requestFocus();
            }
             grandtotal_amount_calculate();
        });
         
         dicountPercent_Field.setOnKeyReleased(evt->{
            Validation.product_Price_color(invoiceNo_Field, 4,100);
            if(evt.getCode()==KeyCode.ENTER){
                addItemButton.requestFocus();
            }
             grandtotal_amount_calculate();
        }); 
         
         addItemButton.setOnKeyReleased(evt->{
            if(evt.getCode()==KeyCode.ENTER){
                add_Item_Table();
                scanField.requestFocus();
            }
        }); 
         
        addItemButton.setOnAction(evt->{
            add_Item_Table();
        });
        
        
    }
    
    private void add_Item_Table(){
        if(validtion_before_table()==false)return;
        String productCode = details_code.getText();
        if(productCode.length()<2){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Message");
            a.setContentText("Please Scan or Select Product from Tabe");
            a.showAndWait();
            scanField.requestFocus();
            return;
        }
        String productName = details_Pname.getText();
        String Pprice = details_purchase.getText();
        String sPrice = details_sale.getText();
        String stockCurrent = details_stock.getText();
        int items = Integer.parseInt(items_Field.getText());
        int cashDis = Integer.parseInt(cashDiscount_Field.getText());
        double disPercent = Double.parseDouble(dicountPercent_Field.getText());
        double gTotal = Double.parseDouble(gtotal_Field.getText());
        double total = Double.parseDouble(total_Field.getText());
        
        if(cashDis>total){
            Alert  a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Message");
            a.setContentText("Cash Discount should be less then Total amount");
            a.showAndWait();
            cashDiscount_Field.requestFocus();
            return;
        }
        
        invoice_list.add(new TableClass(productCode, productName, cashDis, items, percent, gTotal));        
        invoice_table.setItems(invoice_list);
        total_calculator();
        clearAll();
    }
  
    
    private void total_calculator(){
        int items=0;
        double total=0;
        
        if(invoice_list.size()>0){
            for (int i = 0; i < invoice_list.size(); i++) {
                items +=invoice_list.get(i).getItems();
                total +=invoice_list.get(i).getTotal();
            }
         }
         totalItems_Label.setText("Total Items : "+items);
         grandTotalLabel.setText("G.Total Amount : "+total);
    }
    
    ObservableList<TableClass> invoice_list;
    private void register_invoiceTable(){
        
        invoice_col_code.setCellValueFactory(new PropertyValueFactory<TableClass,String>("code"));
        invoice_col_name.setCellValueFactory(new PropertyValueFactory<TableClass,String>("name"));
        invoice_col_items.setCellValueFactory(new PropertyValueFactory<TableClass,Integer>("items"));
        invoice_col_cashDiscount.setCellValueFactory(new PropertyValueFactory<TableClass,Integer>("cashDiscount"));
        invoice_col_percentD.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("percentDis"));
        invoice_col_total.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("total"));
        
        invoice_list = FXCollections.observableArrayList();
        
    }

    private void product_getails(String code) {
        
        String query  = "select * from products where product_code='"+code+"' ";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                String pName  = rs.getString("product_name");
                String pPrice = rs.getString("purchasePrice");
                String sPrice = rs.getString("salePrice");
                String stock  = Database_Returns.Code_Return("stock", "product_code", code, "items");
                
                details_code.setText(code);
                details_Pname.setText(pName);
                details_purchase.setText(pPrice);
                details_sale.setText(sPrice);
                details_stock.setText(stock);
            }else{
                 Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning Message");
                a.setContentText("This Product is not added Yet. First Creat Product Account then Add Stock. \n Select Ok with Mouse");
                a.showAndWait();
                scanField.clear();
                scanField.requestFocus();
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void total_amount_calculate() {
         if(Validation.Only_Number_Color(items_Field, 6) && details_purchase.getText()!=null){
            String it = items_Field.getText();
            String pPrice = details_purchase.getText();
            int items = Integer.parseInt(it);
            double price = Double.parseDouble(pPrice);
            
            double amount = (double)items * price;
            total_Field.setText(String.valueOf(amount));
        }
         
    }//end of function

    double  percent=0;
    private void grandtotal_amount_calculate() {
     if(Validation.Only_Number_Color(items_Field, 6) && details_purchase.getText() !=null){
         if(cashDiscount_Field.getText()==null)return;
         if(dicountPercent_Field.getText()==null)return;
         if(cashDiscount_Field.getText().length()==0)cashDiscount_Field.setText("0");
         if(dicountPercent_Field.getText().length()==0)dicountPercent_Field.setText("0");
         if(Validation.product_Price_color(dicountPercent_Field, 5, 100)==false)return;
         if(Validation.Only_Number_Color(items_Field, 5)==false)return;
         if(Validation.Only_Number_Color(cashDiscount_Field, 5)==false)return;
         
            String it = items_Field.getText();
            String pPrice = details_purchase.getText();
            int items = Integer.parseInt(it);
            double price = Double.parseDouble(pPrice);
            double amount = (double)items * price;
            double cash = Double.parseDouble(cashDiscount_Field.getText());
            double percentt = Double.parseDouble(dicountPercent_Field.getText());
            
            
            percent =(percentt/100)*amount;
            double newPrice = (double)amount-cash-percent;
             
            
            total_Field.setText(String.valueOf(amount));
            gtotal_Field.setText(String.valueOf(newPrice));
        }
    }//end of funciton

    private void saveToDatabase() {
        if(invoiceNo_Field.getText().length()<=2){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Warning Message");
            a.setContentText("Your invoice number is to short please enter complete number");
            a.show();
            return;
        }
        
       String today = Validation.today_Date();
       String invoiceNo = invoiceNo_Field.getText();
       String query = "insert into purchase_invoices(invoice_number , date)"
               + " values('"+invoiceNo+"' , '"+today+"')";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            st.executeUpdate(query);
        }catch(SQLException e){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Warning Message");
            a.setContentText("This invoice number is already exsists in database. Try other number");
            a.show();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
            
        for (int i = 0; i < invoice_list.size(); i++) {
            invoice_table.getSelectionModel().select(i);
            TableClass row = invoice_table.getSelectionModel().getSelectedItem();
            String invoice = invoiceNo_Field.getText();
            String pCode = row.getCode();
            int items = row.getItems();
            int cashDis = row.getCashDiscount();
            double percentDis = row.getPercentDis();
            double total = row.getTotal();
            String purchasePrice = Database_Returns.Code_Return("products", "product_code", pCode, "purchasePrice");
            String salePrice = Database_Returns.Code_Return("products", "product_code", pCode, "salePrice");
            String time  =java.time.LocalTime.now().toString();
            String query2 = "insert into purchase_invoice_details(pcode,date,time,item,cashDiscount,percentDiscount,invoice_number,total,sPrice,pPrice)"
                    + " values('"+pCode+"','"+today+"','"+time+"','"+items+"','"+cashDis+"','"+percentDis+"','"+invoice+"','"+total+"','"+salePrice+"','"+purchasePrice+"')";
            
            String stockQuery= "update stock set   items = items+ '"+items+"' where product_code='"+pCode+"' ";
            try {
                Statement st   = Connection_DB.db_Connection().createStatement();
                st.executeUpdate(query2);
                st.executeUpdate(stockQuery);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            
        }//end of for loop
                    totalItems_Label.setText("Total Items : 0");
                    grandTotalLabel.setText("Total Amount : 0");
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Warning Message");
                    a.setContentText("Invoice Saved Successfully");
                    a.show();
        
        clearAll();
        invoice_list.clear();
    }
    
    public class TableClass{
        String code,name;
        int cashDiscount,items;
        double percentDis, total;

        public TableClass(String code, String name, int cashDiscount, int items, double percentDis, double total) {
            this.code = code;
            this.name = name;
            this.cashDiscount = cashDiscount;
            this.items = items;
            this.percentDis = percentDis;
            this.total = total;
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

        public int getCashDiscount() {
            return cashDiscount;
        }

        public void setCashDiscount(int cashDiscount) {
            this.cashDiscount = cashDiscount;
        }

        public int getItems() {
            return items;
        }

        public void setItems(int items) {
            this.items = items;
        }

        public double getPercentDis() {
            return percentDis;
        }

        public void setPercentDis(double percentDis) {
            this.percentDis = percentDis;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
        
    }
    
    private boolean validtion_before_table(){
        if(details_code.getText()==null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Warning Message");
            a.setContentText("Your product is not selected please first scan product");
            a.showAndWait();
            scanField.requestFocus();
            return false;
        }
        
        if(invoiceNo_Field.getText()==null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Warning Message");
            a.setContentText("Invoice Number is missing.");
            a.showAndWait();
            invoiceNo_Field.requestFocus();
            return false;
        }
        
        if(details_Pname.getText()==null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Warning Message");
            a.setContentText("Product is not Select, Scan or Select from Table");
            scanField.requestFocus();
            a.show();
            return false;
        }
        
        if(Validation.Only_Number_Diaglog(items_Field, 5, "Items ") == false){
            items_Field.requestFocus();
            return false;
        }
        if(Validation.Only_Number_Diaglog(cashDiscount_Field, 6 , "Cash Discount ") == false){
            cashDiscount_Field.requestFocus();
            return false;
        }
        if(Validation.product_Price_color(dicountPercent_Field, 5 ,99) == false){
            dicountPercent_Field.requestFocus();
            return false;
        }
        
        if(items_Field.getText().equals("0") || items_Field.getText()==null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Warning Message");
            a.setContentText("Items should be more then 0.");
            a.showAndWait();
            items_Field.requestFocus();
            return false;
        }
     return true;
    }//end of method

    
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
            int count=1;
            while(rs.next()){
                String code = rs.getString("product_code");
                String name= rs.getString("product_name");
                double pPrice = rs.getDouble("purchasePrice");
                double rPrice = rs.getDouble("salePrice");
                list.add(new  Table_Class(code, name, pPrice, rPrice));
                count++;
            }
            product_table.setItems(list);
            
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
        
    }//end of class
        
         
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


        private void clearAll(){
            items_Field.setText("0");
            cashDiscount_Field.setText("0");
            dicountPercent_Field.setText("0");
            total_Field.setText("0");
            gtotal_Field.setText("0");
            details_Pname.setText(null);
            details_code.setText(null);
            details_purchase.setText(null);
            details_sale.setText(null);
            details_stock.setText(null);
        }
}
