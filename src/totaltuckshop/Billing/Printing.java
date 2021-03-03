package totaltuckshop.Billing;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.*;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.util.Date;
import javafx.scene.control.TableView;

public class Printing implements Printable, ActionListener {

    private static Paper paper;
    TableView table1;
    int[] pageBreaks;  // array of page break line positions.
    String[] textLines;
    String invoice;
    String date;
    String total , items;
  
    private void initTextLines() {
        if (textLines == null) {
            //int numLines=table1.getItems().size();
            int numLines=20;
             
            for (int i=0;i<numLines;i++) {
                textLines[i]= "" + i;
            }
        }
    }
  public int print(Graphics g, PageFormat pf, int pageIndex)throws PrinterException {
 
        Font font = new Font("Serif", Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
  
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
 
        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
        int y = 0; 
//        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
//        int end   = (pageIndex == pageBreaks.length)
//                         ? textLines.length : pageBreaks[pageIndex];
////  
        int x1 = 100 ,  y1 = 6 ; // these two are using to totaly change the posiciton of whole document form x to y cordinates
    if(pageIndex == 0){
        
    int xx = 0;
    int yy = 5;
     
              g.setFont(new Font("default" , Font.BOLD , 8));
//            g.drawLine(270+x1, yy+55+y1, 270+x1, yy+70+y1);
//            g.drawString("Rate P", 222+x1, 70+y1);
//        
       
              g.drawString("1", 0+x1, 70+y1);
              g.drawString("2", 30+x1, 70+y1);
              g.drawString("3", 60+x1, 70+y1);
              g.drawString("4", 90+x1, 70+y1);
              g.drawString("5", 120+x1, 70+y1);
              g.drawString("6", 150+x1, 70+y1);
              g.drawString("7", 180+x1, 70+y1);
              g.drawString("8", 210+x1, 70+y1);
              g.drawString("9", 240+x1, 70+y1);
              g.drawString("10", 270+x1, 70+y1);
              g.drawString("11", 300+x1, 70+y1);
              g.drawString("12", 330+x1, 70+y1);
              g.drawString("13", 350+x1, 70+y1);
              g.drawString("14", 380+x1, 70+y1);
              g.drawString("15", 380+x1, 70+y1);
              g.drawString("16", 410+x1, 70+y1);
              g.drawString("17", 430+x1, 70+y1);
              g.drawString("18", 460+x1, 70+y1);
              
              
        
    }
        int tot =70;
        
    for (int line = 0; line < 20; line++) {
                     y += lineHeight;
                      tot = y+70;
        g.drawString(String.valueOf("Laeeq Khan"), 1+x1, y+70+y1+tot);
//                     String name = table1.getValueAt(line, 1).toString();
//                     String cartan = table1.getValueAt(line, 2).toString();
//                     String pieces = table1.getValueAt(line, 3).toString();
//                     String free  = table1.getValueAt(line, 4).toString();
//                     String disC = table1.getValueAt(line  , 5).toString();
//                     String disP = table1.getValueAt(line, 6).toString();
//                     String rate = table1.getValueAt(line, 7).toString();
//                     String amount = table1.getValueAt(line, 8).toString();
//          
//                     if(name.length() >=16)
//        g.setFont(new Font("default" , Font.PLAIN , 6));
//                     else
//        g.setFont(new Font("default" , Font.PLAIN , 8));
//        g.drawString(name, 1+x1, y+70+y1);
//        g.setFont(new Font("default" , Font.PLAIN , 8));
//         g.drawString(cartan, 88+x1, y+70+y1);
//          g.drawString(pieces, 116+x1, y+70+y1);
//           g.drawString(free, 144+x1, y+70+y1);
//            g.drawString(disC, 167+x1, y+70+y1);
//             g.drawString(disP, 194+x1, y+70+y1);
//               g.drawString(rate, 222+x1, y+70+y1);
//               g.drawString(amount, 280+x1, y+70+y1);
       
    }
          tot = tot + 50;
    
          if(pageIndex == pageBreaks.length){
//                g.setFont(new Font("default" , Font.BOLD , 10));
//    g.drawString("Items : ", 10+x1, tot+y1);
//    g.drawString(String.valueOf(item), 50+x1, tot+y1);
//    g.drawString("G.Total : ", 120+x1,tot+y1);
//    g.drawString(String.valueOf(totalBill ), 170+x1, tot+y1);
//    g.drawString("Discount : ", 230+x1, tot+y1);
//    g.drawString(String.valueOf(discount), 280+x1, tot+y1);
//    
//    g.drawString("Signatue : ", 2+x1, tot+40+y1);
//    g.drawLine(50+x1, tot+40+y1, 130+x1, tot+40+y1);
//    g.drawString("Net Amount :", 140+x1 , tot+40+y1);
//    g.drawString(String.valueOf(totalBill - discount),  207+x1, tot+40+y1);

          }
  
   
    /* tell the caller that this page is part of the printed document */
    return NO_SUCH_PAGE;
}


public void actionPerformed(ActionEvent e) {
   
}


public Printing( ){
//    TableView table , String invoice, String total , String date , String items
//    this.table1 = table;
//    this.invoice =invoice;
//    this.date = date;
//    this.total = total;
//    this.items = items;
//    
    paper = new Paper();
    paper.setSize(420, 595);

     PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(this);
    boolean ok = job.printDialog();
    if (ok) {
        try {
            job.print();
        } catch (PrinterException ex) {
            /* The job did not successfully complete */
        }
    }
}

  

}       
 
class laiq{
      public static void main(String[] args) {
        new Printing();
    }
}
