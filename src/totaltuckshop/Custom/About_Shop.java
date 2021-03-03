
package totaltuckshop.Custom;

public class About_Shop {
    String id;
    String storeName;
    String username;

    private static About_Shop instance;
    
    private About_Shop(){
            
    }
    public static About_Shop getInstance(){
        if(instance == null){
              instance = new About_Shop();
        }
        return instance;
    }
     
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
}
