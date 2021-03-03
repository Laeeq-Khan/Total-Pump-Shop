/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totaltuckshop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import totaltuckshop.Connection.Connection_DB;
import totaltuckshop.Custom.About_Shop;

/**
 *
 * @author Laeeq Khan
 */
public class TotalTuckShop extends Application {
    
    static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        Connection_DB.connect_with_db();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("totaltuckshop/Layouts/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setHeight(600);
        stage.setWidth(493);
        stage.show();
        stage.setTitle(About_Shop.getInstance().getStoreName());
        stage.getIcons().add(new Image("totaltuckshop/Resources/total.jpg"));
        this.stage  = stage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
       
    }
    public static Stage getStage(){
        return stage;
    }
}
