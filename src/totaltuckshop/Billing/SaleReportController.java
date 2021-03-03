 
package totaltuckshop.Billing;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Connection.Database_Returns;
import totaltuckshop.Connection.Validation;

 
public class SaleReportController implements Initializable {

    
    
    @FXML    private Label label111;
    @FXML    private DatePicker fromDate;
    @FXML    private DatePicker toDate;
    @FXML    private Button showButton;
    @FXML    private TableView<TableClass> saleTable;
    @FXML    private TableColumn<TableClass, String> code_col;
    @FXML    private TableColumn<TableClass, String> name_col;
    @FXML    private TableColumn<TableClass, String> date_col;
    @FXML    private TableColumn<TableClass, Double> salePrice_col;
    @FXML    private TableColumn<TableClass, Double> purchase_col;
    @FXML    private TableColumn<TableClass, Integer> items_col;
    @FXML    private TableColumn<TableClass, Integer> cashDis_col;
    @FXML    private TableColumn<TableClass, Double> percent_col;
    @FXML    private TableColumn<TableClass, Double> total_col;
    @FXML    private TableColumn<TableClass, String> invoice_col;
    @FXML    private Label grandTotalCountLabel;
    @FXML    private Label entryCountLabel;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         toDate.setValue(Validation.Default_Date((Validation.today_Date())));
         fromDate.setValue(Validation.Default_Date((Validation.today_Date())));
         events();
     }    
    
    public void events(){
        showButton.setOnAction(evt->{
            populatingTable();
        });
    }
    
 
    private void populatingTable(){
        code_col.setCellValueFactory(new PropertyValueFactory<TableClass,String>("code"));
        name_col.setCellValueFactory(new PropertyValueFactory<TableClass,String>("name"));
        date_col.setCellValueFactory(new PropertyValueFactory<TableClass,String>("date"));
        items_col.setCellValueFactory(new PropertyValueFactory<TableClass,Integer>("items"));
        cashDis_col.setCellValueFactory(new PropertyValueFactory<TableClass,Integer>("cashDic"));
        salePrice_col.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("salePrice"));
        purchase_col.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("purchasePrice"));
        percent_col.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("percentDic"));
        total_col.setCellValueFactory(new PropertyValueFactory<TableClass,Double>("total"));
        invoice_col.setCellValueFactory(new PropertyValueFactory<TableClass,String>("innvoice"));
         
        
        ObservableList list  = FXCollections.observableArrayList();
        
        String to = toDate.getValue().toString();
        String from =fromDate.getValue().toString();
        String query   = "select * from saleInvoiceDetail ";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs =st.executeQuery(query);
            list.clear();
            double gTotal =0;
            int count=0;
            while(rs.next()){
                String date = rs.getString("date");
                String dDate = date;
                date =date_revers(date);
                to = to.replaceAll("-", "");
                from = from.replaceAll("-", "");
                int dbDate=  Integer.parseInt(date);
                int toDate = Integer.parseInt(to);
                int fromDate = Integer.parseInt(from);
                
                if(dbDate >= fromDate && dbDate <= toDate){
                String code = rs.getString("pcode");
                String name = Database_Returns.Code_Return("products", "product_code", code, "product_name");
                int items = rs.getInt("items");
                int cashDis = rs.getInt("cashDiscount");
                double percentDis  = rs.getDouble("percentDiscount");
                double total = rs.getDouble("total");
                String invoiceno = rs.getString("invoice_number");
                double sPrice = rs.getDouble("sPrice");
                double pPrice = rs.getDouble("pPrice");
                gTotal +=total;
                count++;
                list.add(new TableClass(code, name, dDate, items, cashDis, sPrice, pPrice, percentDis, total, invoice_maker(invoiceno)));
                }
                
            }
            saleTable.setItems(list);
            grandTotalCountLabel.setText("Grand Total Amount "+gTotal+"/-");
            entryCountLabel.setText("Total Entry "+count);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
    public class TableClass{
        String code,name,date;
        int items , cashDic;
        double salePrice, purchasePrice,percentDic;
        double total;
        String innvoice;

        public TableClass(String code, String name, String date, int items, int cashDic, double salePrice, double purchasePrice, double percentDic, double total, String innvoice) {
            this.code = code;
            this.name = name;
            this.date = date;
            this.items = items;
            this.cashDic = cashDic;
            this.salePrice = salePrice;
            this.purchasePrice = purchasePrice;
            this.percentDic = percentDic;
            this.total = total;
            this.innvoice = innvoice;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getItems() {
            return items;
        }

        public void setItems(int items) {
            this.items = items;
        }

        public int getCashDic() {
            return cashDic;
        }

        public void setCashDic(int cashDic) {
            this.cashDic = cashDic;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public void setPurchasePrice(double purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

        public double getPercentDic() {
            return percentDic;
        }

        public void setPercentDic(double percentDic) {
            this.percentDic = percentDic;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getInnvoice() {
            return innvoice;
        }

        public void setInnvoice(String innvoice) {
            this.innvoice = innvoice;
        }
        
        
    }
    
    
    private String date_revers(String date){
       String parts[] = date.split("-");
       String newDate="";
        for (int i = parts.length-1; i >=0 ; i--) {
            newDate +=parts[i];
        }
        return newDate;
    }
    
    
    private String invoice_maker(String invoice){
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
        return invoice;
    }
}
