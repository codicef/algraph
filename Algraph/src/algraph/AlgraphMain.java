package algraph;
 
import graph.Graph;
import graph.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
 
public class AlgraphMain extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	
    	Parent root;
		try {
			URL url = new File("src/algraph/algraph_template.fxml").toURL();
			root = FXMLLoader.load(url);
			Scene scene = new Scene(root, 1100, 700);
	    	primaryStage.setScene(scene);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        primaryStage.setTitle("Algraph!");
        
        primaryStage.show();
    }
}