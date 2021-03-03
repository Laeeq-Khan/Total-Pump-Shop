
package totaltuckshop.Connection;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database_Returns {
    

      
    public static String Code_Return(String table ,String matchingColumn , String name , String returnColumn){
        String abc = null;
        try {   
             
          
            Statement st = Connection_DB.db_Connection().createStatement();
            String query = "select "+returnColumn+"  from "+table+" where  "+matchingColumn+" = '"+name+"' ";
            ResultSet rs = st.executeQuery(query);
           if(rs.next()){
                abc = rs.getString(returnColumn);
           }else{
                System.out.println(returnColumn+" no exist in "+table+" Table");   
                return "Not Exsists";
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("database returnings class . code return functioin  "+e);
        }//end of catch statement   
        return abc;
    }//end of funciton
    
       
      
}//end of clss
