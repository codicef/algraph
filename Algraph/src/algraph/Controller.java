package algraph;
import graph.Graph;
import graph.Node;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
public class Controller {

	    @FXML private Pane view_pane;
	    @FXML private Button auto_gen;
	    @FXML private Button file_gen;
	    @FXML private Slider slider_nodes;
	    @FXML private ProgressBar progress_gen;
	    @FXML private Button full_bellman_ford;
	    @FXML private TableView<Entry<String,Integer>>  cost_table;
	    @FXML private TableColumn<Entry<String, Integer>, String> node_column;
	    @FXML private TableColumn<Entry<String, Integer>, Integer> cost_column;

	    
	    protected Graph<String> graph;
	    
	    @FXML
	    public void initialize() {
	    	
	        node_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<String, Integer>, String>, ObservableValue<String>>() {

	            @Override
	            public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry<String, Integer>, String> p) {
	                return new SimpleObjectProperty<String>(p.getValue().getKey());
	            }

	        });

	        cost_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<String, Integer>, Integer>, ObservableValue<Integer>>() {

	            @Override
	            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Entry<String, Integer>, Integer> p) {
	                return new SimpleObjectProperty<Integer>(p.getValue().getValue());
	            }
	        });


	    	
	    	
	    	
	    	slider_nodes.isShowTickMarks();
	    	auto_gen.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    		 
	            @Override
	            public void handle(MouseEvent t) {
	            	Random random =  new Random((long)1917);
	            	graph = new Graph<String>();
	    			for(int i = 0; i < slider_nodes.getValue(); i++)
	    				graph.insertNode(new Node<String>(String.valueOf(i)));
	    				
	    			for(Node<String> start_node : graph.V()) 
	    				for(Node<String> end_node : graph.V()) 
		    				if(random.nextInt(100) < 30)
		    					graph.insertEdge(start_node, end_node, random.nextInt(10) - 2);	
		    			

	    			Pane tmp = graph.getFxGraph(646, 519, 20, 20);
	    			view_pane.getChildren().removeAll(view_pane.getChildren());
	    			view_pane.getChildren().addAll(tmp.getChildren());
	            }
	         });
	    	
	    	file_gen.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    		
	    		
	    		@Override
	            public void handle(MouseEvent t) {
	    			HashMap<String, HashMap<String, Integer>> hash = new HashMap<String, HashMap<String, Integer>>();

		            graph = new Graph<String>();
	            	FileChooser fileChooser = new FileChooser();
	    			fileChooser.setTitle("Open Resource File");
	    			File selected_file = fileChooser.showOpenDialog(view_pane.getScene().getWindow());	
	    			try {
						BufferedReader reader = new BufferedReader(new FileReader(selected_file.getAbsolutePath()));
						String line = reader.readLine();
						while(line!=null) {
							hash.put(line.split("->")[0], new HashMap<String, Integer>());
							if(line.split("->").length > 1)
								for (String s : line.split("->")[1].split(";")) {
									hash.get(line.split("->")[0]).put(s.split(",")[0], Integer.decode(s.split(",")[1]));
								}
							line = reader.readLine();
						}
						for(Entry<String, HashMap<String, Integer>> node : hash.entrySet()) {
							graph.insertNode(new Node<String>(node.getKey()));
						}
						for(Node<String> start_node : graph.V()) {
							for(Node<String> end_node : graph.V()) {
								if (hash.get(start_node.toString()).containsKey(end_node.toString()))
									graph.insertEdge(start_node, end_node, hash.get(start_node.toString()).get(end_node.toString()));
							}
						}
					}
	    			 catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		
	    			Pane tmp = graph.getFxGraph(646, 519, 20, 20);
	    			view_pane.getChildren().removeAll(view_pane.getChildren());
	    			view_pane.getChildren().addAll(tmp.getChildren());
	            }
	         });
	    	
	    	full_bellman_ford.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    		@Override
	            public void handle(MouseEvent t) {
	    			int i = 0;
	    			HashMap<String, Integer> map = graph.doBellmanFord("0");
	    	        ObservableList<Entry<String, Integer>> items = FXCollections.observableArrayList(map.entrySet());
	    	        cost_table.setItems(items);

	    		}
	    	});
	    		
	    	
	    }
	    
	    
}
;