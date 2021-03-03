/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totaltuckshop.Billing;

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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Connection.Database_Returns;
import totaltuckshop.Connection.Validation;

 
public class Billing implements Initializable {

    @FXML    private JFXTextField scanField;
    @FXML    private Label label111;
    @FXML    private TableView<Table_Class> product_table;
    @FXML    private TableColumn<Table_Class, String> code_column;
    @FXML    private TableColumn<Table_Class, String> name_column;
    @FXML    private TableColumn<Table_Class, Double> purchase_column;
    @FXML    private TableColumn<Table_Class, Double> sale_column;
    @FXML    private TableColumn<Table_Class, Integer> stockItems;
    @FXML    private JFXTextField search_field;
    @FXML    private Label details_code;
    @FXML    private Label details_purchase;
    @FXML    private Label details_stock;
    @FXML    private Label details_Pname;
    @FXML    private Label details_sale;
    @FXML    private JFXTextField invoiceNo_Field;
    @FXML    private TableView<TableClass> invoice_table;
    @FXML    private TableColumn<TableClass, JFXTextField> invoice_col_code;
    @FXML    private TableColumn<TableClass, String> invoice_col_name;
    @FXML    private TableColumn<TableClass, JFXTextField> invoice_col_items,invoice_col_sale;;
    @FXML    private TableColumn<TableClass, JFXTextField> invoice_col_cashDiscount;
    @FXML    private TableColumn<TableClass, JFXTextField> invoice_col_percentD;
    @FXML    private TableColumn<TableClass, JFXTextField> invoice_col_total;
    @FXML    private MenuItem removeItem;
    @FXML    private JFXButton billPrintButton;
    @FXML    private Label totalItems_Label;
    @FXML    private Label grandTotalLabel;
    @FXML    private Label grandTotalLabel1;
    @FXML    private Label grandTotalLabel11;
    @FXML    private JFXButton billsaveButton;
    BillPrinter p ;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        p =new BillPrinter();
       populating_Table();
       events();
       scanField.requestFocus();
      register_invoiceTable();
        getUniqueInvoice();
    }    
    
    private void events(){
        
        
        billsaveButton.setOnAction(evt->{
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
                 saveToDatabase(0);
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
            if(evt.getCode() ==KeyCode.ENTER){
                 if(product_table.getSelectionModel().getSelectedIndex() != -1){
                     add_Item_Table(product_table.getSelectionModel().getSelectedItem().getCode());
                 }
            }
        });
        product_table.setOnMouseClicked(evt->{
            if(product_table.getSelectionModel().getSelectedIndex() != -1){
                   product_getails(product_table.getSelectionModel().getSelectedItem().getCode());
             }
        });
        
        
      
        scanField.setOnKeyReleased(evt->{
            
            if(evt.isControlDown() && evt.getCode() == KeyCode.P){
                 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Message");
                 String s = "Do you want to print recipt now?";
                alert.setContentText(s);
                 Optional<ButtonType> result = alert.showAndWait();
                 if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                p.printer(invoice_table, grandTotalLabel1.getText(), grandTotalLabel11.getText() , invoiceNo_Field.getText());
               saveToDatabase(1);
             } 
                
            }
            if(evt.getCode()==KeyCode.ENTER){
                if(scanField.getText()==null || scanField.getText().length()==0)return;
                if(scanField.getText().length()>2);
                  add_Item_Table(scanField.getText());
                  scanField.setText("");
            }
        });
        

       billPrintButton.setOnMouseClicked(evt->{
          p.printer(invoice_table, grandTotalLabel1.getText(), grandTotalLabel11.getText() , invoiceNo_Field.getText());
          saveToDatabase(1);
       });
        
        
    }
     
     double total=0;
    private void add_Item_Table(String code){
        product_getails(code);
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
        
        Label label = new Label();
        label.setText(code);
        JFXTextField itemsField = new JFXTextField("1");
        JFXTextField cashDiscount = new JFXTextField("0");
        JFXTextField percentDiscount = new JFXTextField("0.0");
         JFXTextField salePriceField = new JFXTextField(sPrice);
         JFXTextField codeField = new JFXTextField(code);
         
         salePriceField.setEditable(false);
         JFXTextField totalField = new JFXTextField();
         totalField.setEditable(false);
         totalField.setStyle("-fx-background-color: #fffd8c;-fx-font-weight: bolder;-fx-alignment: center ;");
         
          
        liveBillingValidatons(codeField,itemsField, cashDiscount, salePriceField, percentDiscount,totalField);
        itemsField.setOnKeyReleased(evt->{
            liveBillingValidatons(codeField,itemsField, cashDiscount, salePriceField, percentDiscount,totalField);
        });
        
        percentDiscount.setOnKeyReleased(evt->{
            liveBillingValidatons(codeField,itemsField, cashDiscount, salePriceField, percentDiscount,totalField);
        });
        
        cashDiscount.setOnKeyReleased(evt->{
            
            liveBillingValidatons(codeField,itemsField, cashDiscount, salePriceField, percentDiscount,totalField);
        });
        
        
         String stockString  = Database_Returns.Code_Return("stock", "product_code", details_code.getText()  , "items");
                int stock = Integer.parseInt(stockString);
                if(Integer.parseInt(details_stock.getText())  <=0){
                     Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Message");
                    a.setContentText("Out of Stock. You have no more items to sale it");
                    a.showAndWait();
                    return;
         }
        
        boolean flag = false;
        for (int i = 0; i < invoice_list.size(); i++) {
            if(invoice_list.get(i).getCode().getText().equals(label.getText())){
                int items = Integer.parseInt(invoice_list.get(i).getItems().getText());
                 items++;
                 stockString  = Database_Returns.Code_Return("stock", "product_code", invoice_list.get(i).getCode().getText()  , "items");
                 stock = Integer.parseInt(stockString);
                if(items  > stock){
                     Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Message");
                    a.setContentText("Out of Stock. You have no more items to sale it");
                    a.showAndWait();
                    return;
                }
                invoice_list.get(i).getItems().setText(String.valueOf(items));
                liveBillingValidatons(invoice_list.get(i).getCode(),invoice_list.get(i).getItems(), invoice_list.get(i).getCashDiscount(), invoice_list.get(i).getSaleP(), invoice_list.get(i).percentDis,invoice_list.get(i).getTotal());
                flag = true;
            }
        }
        
        if(flag==false){
            invoice_list.add(new TableClass(details_Pname.getText(), codeField, cashDiscount, itemsField, percentDiscount, salePriceField,totalField));
            invoice_table.setItems(invoice_list);
        }
        
        total_calculator();
        
     
    }
  
  
    
    public void liveBillingValidatons(JFXTextField codeField, JFXTextField itemsField , JFXTextField cashDiscount, JFXTextField salePriceField, JFXTextField percentDiscount , JFXTextField totalField){
        
        if((Validation.Only_Number_Color(itemsField, 5)==true) && (Validation.Only_Number_Color(cashDiscount, 6)==true) && (Validation.product_Price_color(percentDiscount, 6, 100))){
                int items = Integer.parseInt(itemsField.getText());
                int cash = Integer.parseInt(cashDiscount.getText());
                double percentDis = Double.parseDouble(percentDiscount.getText());
                double price = Double.parseDouble(salePriceField.getText());
                double percentAmount = ( percentDis/100);
                percentAmount = percentAmount*(price*items);
                total = (items * price)-cash-percentAmount;
                totalField.setText(String.valueOf(total));
               
                if(total<0){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Message");
                    a.setContentText("You can't give discount more then Total for this product");
                    a.show();
                    percentDiscount.setText("0");
                    cashDiscount.setText("0");
                    total = (items * price);
                    totalField.setText(String.valueOf(total));
                }
                
                String stock = Database_Returns.Code_Return("stock", "product_code", codeField.getText(), "items");
                int stk = Integer.parseInt(stock);
                if(items>stk){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Message");
                    a.setContentText("You can't give discount more then Total for this product");
                    a.show();
                    itemsField.requestFocus();
                    itemsField.setText("0");
                }
                 total_calculator();
            } 
    }
    
    private void total_calculator(){
        int items=0;
        double total=0;
        
        if(invoice_list.size()>0){
            for (int i = 0; i < invoice_list.size(); i++) {
                if(Validation.Only_Number_Color(invoice_list.get(i).getItems(), 6)==false)return;
                items +=Integer.parseInt(invoice_list.get(i).getItems().getText());
                total +=Double.parseDouble(invoice_list.get(i).getTotal().getText());
            }
         }
         grandTotalLabel1.setText(String.valueOf(total));
         grandTotalLabel11.setText(String.valueOf(items));
    }
    
    ObservableList<TableClass> invoice_list;
    private void register_invoiceTable(){
        invoice_col_code.setCellValueFactory(new PropertyValueFactory<TableClass,JFXTextField>("code"));
        invoice_col_name.setCellValueFactory(new PropertyValueFactory<TableClass,String>("name"));
        invoice_col_items.setCellValueFactory(new PropertyValueFactory<TableClass,JFXTextField>("items"));
        invoice_col_cashDiscount.setCellValueFactory(new PropertyValueFactory<TableClass,JFXTextField>("cashDiscount"));
        invoice_col_percentD.setCellValueFactory(new PropertyValueFactory<TableClass,JFXTextField>("percentDis"));
        invoice_col_total.setCellValueFactory(new PropertyValueFactory<TableClass,JFXTextField>("total"));
        invoice_col_sale.setCellValueFactory(new PropertyValueFactory<TableClass,JFXTextField>("saleP"));
        
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

  
 

    private void saveToDatabase(int cmd) {
        
       
        for (int i = 0; i < invoice_list.size(); i++) {
            boolean one = Validation.Only_Number_Color(invoice_list.get(i).getItems(), 6);
            boolean two = Validation.Only_Number_Color(invoice_list.get(i).getCashDiscount(), 6);
            boolean three = Validation.product_Price_color(invoice_list.get(i).getPercentDis(), 6,100);
            boolean flag=false;
            if(one==false){
                flag=true;
                invoice_list.get(i).getItems().requestFocus();
            }else if(two==false){
                flag= true;
                invoice_list.get(i).getCashDiscount().requestFocus();
            }else if(three==false){
                flag=true;
                invoice_list.get(i).getCashDiscount().requestFocus();
            }else if(invoice_list.get(i).getItems().getText().equals("0")){
                invoice_list.get(i).getItems().requestFocus();
                flag =true;
            }
            
            if(flag==true){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Warning Message");
                a.setContentText("Invalid Value in Bill");
                a.showAndWait();
                return;
            }
           
        }
        
       String today = Validation.today_Date();
       String invoiceNo = invoiceNo_Field.getText();
       
       String query = "insert into saleInvoice(invoiceNumber , date)"
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
            String pCode = row.getCode().getText();
            int items = Integer.parseInt(row.getItems().getText());
            int cashDis = Integer.parseInt(row.getCashDiscount().getText());
            double percentDis = Double.parseDouble(row.getPercentDis().getText());
            double total = Double.parseDouble(row.getTotal().getText());
            String purchasePrice = Database_Returns.Code_Return("products", "product_code", pCode, "purchasePrice");
            String salePrice = Database_Returns.Code_Return("products", "product_code", pCode, "salePrice");
            String time  =java.time.LocalTime.now().toString();
            String query2 = "insert into saleInvoiceDetail(pcode,date,time,items,cashDiscount,percentDiscount,invoice_number,total,sPrice,pPrice)"
                    + " values('"+pCode+"','"+today+"','"+time+"','"+items+"','"+cashDis+"','"+percentDis+"','"+invoice+"','"+total+"','"+salePrice+"','"+purchasePrice+"')";
            
            String stockQuery= "update stock set   items = items- '"+items+"' where product_code='"+pCode+"' ";
            try {
                Statement st   = Connection_DB.db_Connection().createStatement();
                st.executeUpdate(query2);
                st.executeUpdate(stockQuery);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            
        }//end of for loop'
        
        if(cmd==1){
            System.out.println("Printed");
        }else{
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Warning Message");
            a.setContentText("Invoice Saved Successfully");
            a.show();
        }
        populating_Table();
        grandTotalLabel11.setText("0");
        grandTotalLabel1.setText("0.0");
            
        
        clearAll();
        invoice_list.clear();
        getUniqueInvoice();
    }
    
    public class TableClass{
        String name;
        JFXTextField code, cashDiscount,items,percentDis,saleP;
        JFXTextField total;

        public TableClass(String name, JFXTextField code, JFXTextField cashDiscount, JFXTextField items, JFXTextField percentDis, JFXTextField saleP, JFXTextField total) {
            this.name = name;
            this.code = code;
            this.cashDiscount = cashDiscount;
            this.items = items;
            this.percentDis = percentDis;
            this.saleP = saleP;
            this.total = total;
        }

        

        public JFXTextField getTotal() {
            return total;
        }

        public void setTotal(JFXTextField total) {
            this.total = total;
        }

        

        public JFXTextField getSaleP() {
            return saleP;
        }

        public void setSaleP(JFXTextField saleP) {
            this.saleP = saleP;
        }

        public JFXTextField getCode() {
            return code;
        }

        public void setCode(JFXTextField code) {
            this.code = code;
        }


 
        

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public JFXTextField getCashDiscount() {
            return cashDiscount;
        }

        public void setCashDiscount(JFXTextField cashDiscount) {
            this.cashDiscount = cashDiscount;
        }

        public JFXTextField getItems() {
            return items;
        }

        public void setItems(JFXTextField items) {
            this.items = items;
        }

        public JFXTextField getPercentDis() {
            return percentDis;
        }

        public void setPercentDis(JFXTextField percentDis) {
            this.percentDis = percentDis;
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
        
        return true;
    }//end of method

    
    ObservableList<Table_Class> list;
    private void populating_Table(){
        code_column.setCellValueFactory(new PropertyValueFactory<Table_Class,String>("code"));
        name_column.setCellValueFactory(new PropertyValueFactory<Table_Class,String>("name"));
        purchase_column.setCellValueFactory(new PropertyValueFactory<Table_Class,Double>("pprice"));
        sale_column.setCellValueFactory(new PropertyValueFactory<Table_Class,Double>("rprice"));
        stockItems.setCellValueFactory(new PropertyValueFactory<Table_Class,Integer>("stock"));
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
                String stockss = Database_Returns.Code_Return("stock", "product_code", code, "items");
                int stock = Integer.parseInt(stockss);
                list.add(new  Table_Class(code, name, pPrice, rPrice,stock));
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
        int stock;

        public Table_Class(String code, String name, double pprice, double rprice, int stock) {
            this.code = code;
            this.name = name;
            this.pprice = pprice;
            this.rprice = rprice;
            this.stock = stock;
        }

       

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
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
             
            details_Pname.setText(null);
            details_code.setText(null);
            details_purchase.setText(null);
            details_sale.setText(null);
            details_stock.setText(null);
        }
        
        
        private void getUniqueInvoice(){
            String query = "select  Max(sr) as invoice from saleInvoice";
            String invoice="";
            try {
                Statement st = Connection_DB.db_Connection().createStatement();
                ResultSet rs = st.executeQuery(query);
                if(rs.next()){
                    int maxx = rs.getInt("invoice");
                    maxx++;
                    invoice = String.valueOf(maxx);
                }
            } catch (Exception e) {
                e.printStackTrace();
                    System.out.println(e);
            }
            
            if(invoice.length()<6){
                if(invoice.length()==1){
                    invoice ="00000"+invoice;
                }else if(invoice.length()==2){
                    invoice ="0000"+invoice;
                }else if(invoice.length()==3){
                    invoice ="000"+invoice;
                }else if(invoice.length()==4){
                    invoice ="00"+invoice;
                }else if(invoice.length()==5){
                    invoice ="0"+invoice;
                }
            }
            invoiceNo_Field.setText(invoice);
            
        }
}
