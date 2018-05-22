package application;
 

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class AlgraphMain extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/algraph_template.fxml"));
			Scene scene = new Scene(root, 1100, 700);
	    	primaryStage.setScene(scene);

		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        primaryStage.setTitle("Algraph!");
        
        primaryStage.show();
    }
}