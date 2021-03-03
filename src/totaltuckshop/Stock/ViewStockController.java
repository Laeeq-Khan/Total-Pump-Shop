
package totaltuckshop.Stock;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Connection.Database_Returns;

 
public class ViewStockController implements Initializable {
    @FXML    private Label label111;
    @FXML    private TableView<StockTable> stock_code;
    @FXML    private TableColumn<StockTable, Integer> stockSr;
    @FXML    private TableColumn<StockTable, String> details_col_code;
    @FXML    private TableColumn<StockTable, String> stockPname;
    @FXML    private TableColumn<StockTable, Integer> currentStock;
    @FXML    private JFXTextField searchField;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTable();
        
        searchField.setOnKeyReleased(evt->{
            filter_records();
        });
     }    
 
    ObservableList<StockTable> stocklist;
    public void populateTable(){
        stockSr.setCellValueFactory(new PropertyValueFactory<StockTable,Integer>("sr"));
        details_col_code.setCellValueFactory(new PropertyValueFactory<StockTable,String>("code"));
        stockPname.setCellValueFactory(new PropertyValueFactory<StockTable,String>("name"));
        currentStock.setCellValueFactory(new PropertyValueFactory<StockTable,Integer>("stock"));
        
        stocklist = FXCollections.observableArrayList();
        
        String query  = "select * from stock";
        try {
            Statement st = Connection_DB.db_Connection().createStatement();
            ResultSet rs = st.executeQuery(query);
            int srr = 1;
            while(rs.next()){
                
                String pcode = rs.getString("product_code");
                int items = rs.getInt("items");
                String pname =  Database_Returns.Code_Return("products", "product_code", pcode, "product_name");
                srr++;
                
                stocklist.add(new StockTable(srr, pcode, pname, items));
                
            }
            stock_code.setItems(stocklist);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        
    }
    
    public class StockTable{
        int sr;
        String code , name;
        int stock;

        public StockTable(int sr, String code, String name, int stock) {
            this.sr = sr;
            this.code = code;
            this.name = name;
            this.stock = stock;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
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

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
        
    }

 public void filter_records() {
  if(searchField.getText().length()==0){
     stock_code.setItems(stocklist);
     return;
     }
     ObservableList<StockTable> list2=FXCollections.observableArrayList();
     ObservableList<TableColumn<StockTable,?>> cols=stock_code.getColumns();
        for (int i = 0; i < stocklist.size(); i++) {
            for (int j = 0; j <cols.size(); j++) {
                TableColumn ab=cols.get(j);
                String value=ab.getCellData(stocklist.get(i)).toString().toLowerCase();
                String text = new String (searchField.getText().toString().toLowerCase());
                if (value.contains(text))
                {
                  list2.add(stocklist.get(i));
                
                } else {
                }
                 }         
                }
         stock_code.setItems(list2);
    
}//end of filter method
    
}
