package totaltuckshop.Billing;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Connection.Database_Returns;
import totaltuckshop.Connection.Validation;


public class viewSaleHistoryController implements Initializable {

    @FXML    private Label label111;
    @FXML    private JFXTextField searchField;
    @FXML    private TableView<invoiceTableClass> invoice_col_table;
    @FXML    private TableColumn<invoiceTableClass, String> invoice_col_sr, invoice_col_no,invoice_col_date;
    @FXML    private TableView<DetailsTableClass> details_col_table;
    @FXML    private TableColumn<DetailsTableClass, String> details_col_sr,details_col_code, details_col_name;
    @FXML    private TableColumn<DetailsTableClass, Integer> details_col_item;
    @FXML    private TableColumn<DetailsTableClass, Double> details_col_pPrice, details_col_dicount, details_col_grandTotal;
    @FXML    private Label selectInvoiceLabel;
    @FXML    private Label totalItemsLabel;
    @FXML    private Label gTotalAmountLabel;
    @FXML    private Label totalInvoice_Label;
    @FXML    private DatePicker datePicker;
    @FXML    private MenuItem deletePurchasedItem;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      searchField.requestFocus();
     
      events();
    }    
    
    
    public void events(){
        
        
        deletePurchasedItem.setOnAction(evt->{
            if(details_col_table.getSelectionModel().getSelectedIndex() != -1){
                
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
             alert.setTitle("Message");
             String s = "If you delete this item then i will update your stock. Make sure before delete this";
             alert.setContentText(s);
             Optional<ButtonType> result = alert.showAndWait();
             if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                 deletePurchaseItem();
             } 
            }
        });
        
        datePicker.setOnAction(evt->{
             populating_invoices(Validation.Reversing_Date(datePicker.getValue().toString()));
        });
        
        searchField.setOnKeyReleased(evt->{
            filter_records();
        });
        
        invoice_col_table.setOnKeyReleased(evt->{
          if(invoice_col_table.getSelectionModel().getSelectedIndex() !=-1){
                invoiceTableClass row = invoice_col_table.getSelectionModel().getSelectedItem();
                selectInvoiceLabel.setText("Invoice No "+row.getInvoicenumber()+"    |   Date: "+row.getDate());
                displayInvoiceDetail(row.getInvoicenumber());
            }
        });
        
        invoice_col_table.setOnMouseClicked(evt->{
            if(invoice_col_table.getSelectionModel().getSelectedIndex() !=-1){
                invoiceTableClass row = invoice_col_table.getSelectionModel().getSelectedItem();
                selectInvoiceLabel.setText("Invoice No "+row.getInvoicenumber()+"    |    Date: "+row.getDate());
                displayInvoiceDetail(row.getInvoicenumber());
            }
        });
        
    }
    
    private void deletePurchaseItem(){
                DetailsTableClass row = details_col_table.getSelectionModel().getSelectedItem();
                int index =  details_col_table.getSelectionModel().getFocusedIndex();
                detailsList.remove(index);
                total_calculator();
                String sr = row.getSr();
                String pCode = row.getCode();
                int items = row.getItems();
                
                String query = "update saleInvoiceDetail set status='false' where sr='"+sr+"'";
                String stockQuery= "update stock set   items = items+ '"+items+"' where product_code='"+pCode+"' ";
                try {
                    Statement st = Connection_DB.db_Connection().createStatement();
                    st.executeUpdate(query);
                    st.executeUpdate(stockQuery);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
    }//end of method
    
    
     ObservableList<DetailsTableClass> detailsList;
    private void displayInvoiceDetail(String invoice){
        details_col_sr.setCellValueFactory(new PropertyValueFactory<DetailsTableClass,String>("sr"));
        details_col_code.setCellValueFactory(new PropertyValueFactory<DetailsTableClass,String>("code"));
        details_col_name.setCellValueFactory(new PropertyValueFactory<DetailsTableClass,String>("name"));
        details_col_item.setCellValueFactory(new PropertyValueFactory<DetailsTableClass,Integer>("items")); 
        details_col_pPrice.setCellValueFactory(new PropertyValueFactory<DetailsTableClass,Double>("purchase")); 
        details_col_dicount.setCellValueFactory(new PropertyValueFactory<DetailsTableClass,Double>("discount"));
        details_col_grandTotal.setCellValueFactory(new PropertyValueFactory<DetailsTableClass,Double>("total"));
        
          detailsList=FXCollections.observableArrayList();
        
        String query = "select * from saleInvoiceDetail where status='true' and invoice_number='"+invoice+"'";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                String sr = rs.getString("sr");
                String pcode = rs.getString("pcode");
                String name = Database_Returns.Code_Return("products", "product_code", pcode, "product_name");
                String date = rs.getString("date");
                int items = rs.getInt("items");
                int cashDiscount  = rs.getInt("cashDiscount");
                double percenDiscount = rs.getDouble("percentDiscount");
                double total = rs.getDouble("total");
                double pPrice = rs.getDouble("pPrice");
                double totalDicount = cashDiscount + percenDiscount;
                detailsList.add(new DetailsTableClass(sr, pcode, name, items, pPrice, totalDicount, total));
            }
            details_col_table.setItems(detailsList);
             total_calculator();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
         
        
    }
    
    
    public class DetailsTableClass{
        String sr,code,name;
        int items;
        double purchase;
        double discount;
        double total;

        public DetailsTableClass(String sr, String code, String name, int items, double purchase, double discount, double gTotal) {
            this.sr = sr;
            this.code = code;
            this.name = name;
            this.items = items;
            this.purchase = purchase;
            this.discount = discount;
            this.total = gTotal;
        }

        public String getSr() {
            return sr;
        }

        public void setSr(String sr) {
            this.sr = sr;
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

        public int getItems() {
            return items;
        }

        public void setItems(int items) {
            this.items = items;
        }

        public double getPurchase() {
            return purchase;
        }

        public void setPurchase(double purchase) {
            this.purchase = purchase;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

       
    }
    
    ObservableList<invoiceTableClass> invoice_list ;
    private void populating_invoices(String date) {
        invoice_col_sr.setCellValueFactory(new PropertyValueFactory<invoiceTableClass,String>("sr"));
        invoice_col_no.setCellValueFactory(new PropertyValueFactory<invoiceTableClass,String>("invoicenumber"));
        invoice_col_date.setCellValueFactory(new PropertyValueFactory<invoiceTableClass,String>("date"));
        
        invoice_list = FXCollections.observableArrayList();
        String query = "select * from saleInvoice where date='"+date+"'";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            int count=0;
             Statement st2;
             ResultSet rs2 ;
            while(rs.next()){
                String sr = rs.getString("sr");
                String invocie = rs.getString("invoiceNumber");
                String q  ="select * from saleInvoiceDetail where invoice_number='"+invocie+"' and status='true'";
                st2 = Connection_DB.db_Connection().createStatement();
                rs2 = st2.executeQuery(q);
               if(rs2.next()){
                String datee = rs.getString("date");
                count++;
                invoice_list.add(new invoiceTableClass(sr, invocie, datee));
               }
            }
            invoice_col_table.setItems(invoice_list);
            totalInvoice_Label.setText("Total Invoices : "+count);
           
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        
    }
    
    
    public class invoiceTableClass{
        String sr,invoicenumber,date;

        public invoiceTableClass(String sr, String invoicenumber, String date) {
            this.sr = sr;
            this.invoicenumber = invoicenumber;
            this.date = date;
        }

        public String getSr() {
            return sr;
        }

        public void setSr(String sr) {
            this.sr = sr;
        }

        public String getInvoicenumber() {
            return invoicenumber;
        }

        public void setInvoicenumber(String invoicenumber) {
            this.invoicenumber = invoicenumber;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
        
    }//end of class
    
public void filter_records() {
    if(searchField.getText().length()==0){
     invoice_col_table.setItems(invoice_list);
     return;
     }
     ObservableList<invoiceTableClass> list2=FXCollections.observableArrayList();
     ObservableList<TableColumn<invoiceTableClass,?>> cols=invoice_col_table.getColumns();
        for (int i = 0; i < invoice_list.size(); i++) {
            for (int j = 0; j <cols.size(); j++) {
                TableColumn ab=cols.get(j);
                String value=ab.getCellData(invoice_list.get(i)).toString().toLowerCase();
                String text = new String (searchField.getText().toString().toLowerCase());
                if (value.contains(text))
                {
                  list2.add(invoice_list.get(i));
                
                } else {
                }
            }         
                }
         invoice_col_table.setItems(list2);
    
}//end of method
    

  private void total_calculator(){
        int items=0;
        double total=0;
          for (int i = 0; i < detailsList.size(); i++) {
                items +=detailsList.get(i).getItems();
                total +=detailsList.get(i).getTotal();
            }
         
         totalItemsLabel.setText("Total Items : "+items);
         gTotalAmountLabel.setText("G.Total Amount : "+total);
    }
    
}
