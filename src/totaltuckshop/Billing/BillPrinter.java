package totaltuckshop.Billing;



import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.FontMetrics;
import javafx.scene.control.TableView;

/**
 *
 * @author mic
 */
public class BillPrinter extends javax.swing.JFrame {

     
  
    public PageFormat getPageFormat(PrinterJob pj)
{
    
    PageFormat pf = pj.defaultPage();
    Paper paper = pf.getPaper();    

    double middleHeight =8.0;  
    double headerHeight = 2.0;                  
    double footerHeight = 2.0;                  
    double width = convert_CM_To_PPI(8);      //printer know only point per inch.default value is 72ppi
    double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight); 
    paper.setSize(width, height);
    paper.setImageableArea(                    
        0,
        10,
        width,            
        height - convert_CM_To_PPI(1)
    );   //define boarder size    after that print area width is about 180 points
            
    pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
    pf.setPaper(paper);    

    return pf;
}
    
protected static double convert_CM_To_PPI(double cm) {            
	        return toPPI(cm * 0.393600787);            
}
 
protected static double toPPI(double inch) {            
	        return inch * 72d;            
}






public class BillPrintable implements Printable {
     
  public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) 
  throws PrinterException {    
      
                
      int result = NO_SUCH_PAGE;    
        if (pageIndex == 0) {                    
        
            Graphics2D g2d = (Graphics2D) graphics;                    

            double width = pageFormat.getImageableWidth();                    
           
            g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 

           
            FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
     
        try{
            /*Draw Header*/
            int y=10;
            int yShift = 10;
            int headerRectHeight=15;
            int headerRectHeighta=40;
            
           
            g2d.setFont(new Font("Arial",Font.PLAIN,12));
            g2d.drawString("                 Total Tuck Shop                    ",12,y);y+=yShift;
            g2d.setFont(new Font("Arial",Font.PLAIN,9));
            g2d.drawString("-----------------------------------------------------------------",12,y);y+=headerRectHeight;
            g2d.drawString("                    Invoice No : "+invoiceNo+"                    ",12,y);y+=headerRectHeight;
            g2d.drawString("                    Date       : "+totaltuckshop.Connection.Validation.today_Date()+"                    ",12,y);y+=headerRectHeight;
            g2d.drawString("-----------------------------------------------------------------",10,y);y+=yShift;
             g2d.drawString("Product               Price   Items Disc      Total",10,y);y+=yShift;
             g2d.drawString("----------------------------------------------------------------",10,y);y+=headerRectHeight;
             
            System.out.println(table.getItems().size()+" This is total size is ");
            for (int i = 0; i < table.getItems().size(); i++) {
                   table.getSelectionModel().select(i);
                    Billing.TableClass row = table.getSelectionModel().getSelectedItem();
                    String name = row.getName();
                    String price = row.getSaleP().getText();
                    String items = row.getItems().getText();
                    double dis1 = Double.parseDouble(row.getPercentDis().getText());
                    double dis2  = Double.parseDouble(row.getCashDiscount().getText());
                    int itemssss = Integer.parseInt(items);
                    double pricee =Double.parseDouble(price);
                    String totalAm = row.getTotal().getText();
                    double totalDouble = pricee * itemssss;
                    double perDiscountAmount = (dis1/100)*totalDouble;
                    int totalDiscountAmount = (int)(perDiscountAmount + dis2);
                    double payableAmount =totalDouble- totalDiscountAmount;
                   
             g2d.drawString(name,8,y);
             g2d.drawString(price,75,y);
             g2d.drawString(items,115,y);
             g2d.drawString(String.valueOf(totalDiscountAmount),130,y);
             g2d.drawString(String.valueOf(totalAm),160,y);y+=yShift;
             }//end loop
            g2d.drawString("----------------------------------------------------------------",8,y);y+=yShift;
            g2d.drawString(" Total Items      : "+items+"",8,y);y+=yShift;
            g2d.setFont(new Font("Arial",Font.BOLD,9));
            g2d.drawString(" Total amount : "+total+"/-",8,y);y+=yShift;
            g2d.setFont(new Font("Arial",Font.PLAIN,9));
            g2d.drawString("----------------------------------------------------------------",8,y);y+=yShift;
            g2d.drawString("                                       ",8,y);y+=yShift;
            g2d.drawString("****************************************************",8,y);y+=yShift;
            g2d.drawString("      THANKS TO VISIT TOTAL TUCK SHOP          ",8,y);y+=yShift;
            g2d.drawString("****************************************************",8,y);y+=yShift;
            g2d.drawString(" ",10,y);y+=yShift;
            g2d.drawString(" ",10,y);y+=yShift;
            
            g2d.drawString("Software Developer Laeeq Khan, 0305-5466154",6,y);y+=yShift;
            g2d.drawString(" ",10,y);y+=yShift;   
           
             
           
            
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
          

    }
    catch(Exception r){
    r.printStackTrace();
    }

              result = PAGE_EXISTS;    
          }    
          return result;    
      }
   }
  
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println("Printing");
     
    }
    
    String total , items;
    TableView<Billing.TableClass> table;
    String invoiceNo;
    public void printer(TableView table, String total , String items , String invoice ){
        this.table = table;
        this.total=total;
        this.items  = items;
        this.invoiceNo = invoice;
        
         try {
             PrinterJob pj = PrinterJob.getPrinterJob();        
             pj.setPrintable(new BillPrintable(),getPageFormat(pj));
        try {
             pj.print();
             
          
        }
         catch (PrinterException ex) {
                 ex.printStackTrace();
        }
        } catch (Exception e) {
             System.out.println(e);
        }
    }
 
}
