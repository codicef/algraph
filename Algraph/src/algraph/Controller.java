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
	import javafx.scene.control.ChoiceBox;
	import javafx.scene.control.Label;
	import javafx.scene.control.ProgressBar;
	import javafx.scene.control.Slider;
	import javafx.scene.control.SplitPane;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableColumn.CellDataFeatures;
	import javafx.scene.control.TableView;
	import javafx.scene.control.TreeTableView;
	import javafx.scene.control.cell.PropertyValueFactory;
	import javafx.scene.control.MenuButton;
	import javafx.scene.input.MouseEvent;
	import javafx.scene.layout.AnchorPane;
	import javafx.scene.layout.Pane;
	import javafx.stage.FileChooser;
	import javafx.util.Callback;
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.PrintWriter;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.HashSet;
	import java.util.LinkedList;
	import java.util.Map.Entry;
	import java.util.Random;
	import java.util.stream.Collectors;
	import java.util.stream.IntStream;
	import javafx.scene.shape.Circle;
	import javafx.scene.control.TextField;

	public class Controller {
	
		    @FXML private Pane view_pane;
		    @FXML private SplitPane split_pane;
		    @FXML private AnchorPane left_pane;
		    @FXML private Label status;
		    @FXML private Button auto_gen;
		    @FXML private Button file_gen;
		    @FXML private Button save_to_file;
		    @FXML private ChoiceBox<Integer> choice_box_nodes;
		    @FXML private ChoiceBox<String> choice_box_startbellman;
		    @FXML private ProgressBar progress_gen;
		    @FXML private Button full_bellman_ford;
		    @FXML private Button step_bellman_ford; 
		    @FXML private TableView<Entry<String,Integer>>  cost_table;
		    @FXML private TableColumn<Entry<String, Integer>, String> node_column;
		    @FXML private TableColumn<Entry<String, Integer>, Integer> cost_column;
		    @FXML private Button add_node;
		    @FXML private TextField add_node_field;
		    @FXML private ChoiceBox<String> remove_node_field;
		    @FXML private Button remove_node;
		    @FXML private Button add_edge;
		    @FXML private TextField add_edge_weight;
		    @FXML private ChoiceBox<Node<String>> add_edge_v;
		    @FXML private ChoiceBox<Node<String>> add_edge_u;
		    @FXML private Button remove_edge;
		    @FXML private ChoiceBox<String> remove_edge_field;
		    
	
		    
		    protected Integer step;
		    protected Graph<String> graph;
		    Random random;
		    
		    @FXML
		    public void initialize() {
	        	random =  new Random((long)1917);
	        	split_pane.setDividerPositions(0.245);
	            left_pane.maxWidthProperty().bind(split_pane.widthProperty().multiply(0.24));
		        choice_box_nodes.setItems(FXCollections.observableList(IntStream.range(2, 15).boxed().collect(Collectors.toList())));
		        choice_box_nodes.setValue(3);
	            
	            node_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<String, Integer>, String>, ObservableValue<String>>() {
		            @Override
		            public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry<String, Integer>, String> p) {
		                return new SimpleObjectProperty<String>(p.getValue().getKey());
		            }
	
		        });
	
	            add_edge.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						try {
							int weight = Integer.parseInt(add_edge_weight.getText());
							graph.insertEdge(add_edge_u.getValue(), add_edge_v.getValue(), weight);
							initializeGraph();
						}
						catch(Exception e) {
							status.setText("invalid operation");
						}
						
					}
	            	
	            });
	            
	           remove_edge.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            	
	            	@Override
	            	public void handle(MouseEvent arg0) {
	            		try {
	            			String[] s = remove_edge_field.getValue().split(",");
	            			String us = s[0].substring(1);
	            			String vs = s[1].substring(1, 2);
	            			Node<String> u = null;
	            			Node<String> v = null;
	            			for(Node<String> node : graph.V()) {
	            				if (node.getElement().equals(us))
	            					u = node;
	            				if (node.getElement().equals(vs))
	            					v = node;
	            				}
	            			if (u != null && v!= null)
	            				graph.deleteEdge(u, v);
	            			initializeGraph();
	            		}
	            		catch( Exception e) {
	            			status.setText("invalid operation");
	            		}
	            	}
	            });
	            
	            add_node.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						try {
							if(add_node_field.getText() == null || add_node_field.getText().length() > 2 )
								status.setText("invalid entry");
							else
								{
								graph.insertNode(new Node<String>(add_node_field.getText()));
								initializeGraph();
								}
							}
						catch(Exception e) {
							status.setText("invalid operation");
						}
					}
		        	
		        });
	            
	            
	            
	            
	            remove_node.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						try {
							for(Node<String> n : graph.V()) {
								if(n.toString()==remove_node_field.getValue()) {
									graph.deleteNode(n);
									break;
								}
							}
							initializeGraph();
						}
						catch(Exception e) {
							status.setText("invalid operation");
						}
					}
	            	
	            });
	            
		        cost_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<String, Integer>, Integer>, ObservableValue<Integer>>() {
		            @Override
		            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Entry<String, Integer>, Integer> p) {
		                return new SimpleObjectProperty<Integer>(p.getValue().getValue());
		            }
		        });
	
	
		            
		        choice_box_startbellman.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    		 
		            @Override
		            public void handle(MouseEvent t) {
		            	cost_table.getItems().removeAll(cost_table.getItems());
		            	step = -1;
		            }
		        });
		       
		        
		       
		    	
		    	auto_gen.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    		 
		            @Override
		            public void handle(MouseEvent t) {
		            	step = -1;
		            	graph = new Graph<String>();
		    			for(int i = 0; i < choice_box_nodes.getValue(); i++)
		    				graph.insertNode(new Node<String>(String.valueOf(i)));
		    				
		    			for(Node<String> start_node : graph.V()) 
		    				for(Node<String> end_node : graph.V()) 
			    				if(random.nextInt(100) < 25 && !start_node.equals(end_node))
			    					graph.insertEdge(start_node, end_node, random.nextInt(10) - 2);	
		    			initializeGraph();
		            }
		         });
		    	
		    	file_gen.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    		
		    		@Override
		            public void handle(MouseEvent t) {  
		    			HashMap<String, HashMap<String, Integer>> hash = new HashMap<String, HashMap<String, Integer>>();
		    			step = -1;
			            graph = new Graph<String>();
		            	FileChooser fileChooser = new FileChooser();
		    			fileChooser.setTitle("Open Resource File");
		    			File selected_file = fileChooser.showOpenDialog(view_pane.getScene().getWindow());	
		    			try {
							BufferedReader reader = new BufferedReader(new FileReader(selected_file.getAbsolutePath()));
							String line = reader.readLine();
							while(line!=null) {
								System.out.println(line.split("->").length);

								if(!line.contains("->") || line.split("->").length > 2 || line.split("->").length == 0) {
									System.out.println(line.split("->").length);
									throw new Exception("Wrong format");
								
								}
								hash.put(line.split("->")[0], new HashMap<String, Integer>());
								if(line.split("->").length > 1)
									for (String s : line.split("->")[1].split(";")) {
										hash.get(line.split("->")[0]).put(s.split(",")[0], Integer.decode(s.split(",")[1]));
									}
								line = reader.readLine();
								//per togliere warning
								reader.close();
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
							initializeGraph();
							status.setText("File correctly loaded");
							reader.close();
						}
		    			 catch (Exception e) {
							status.setText("Error loading file");
						}
		    		
		    			
		            }
		         });
		    	
		    	save_to_file.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    		
		    		@Override
		            public void handle(MouseEvent t) { 
		    			if(graph==null || graph.V().size() < 1) {
		    				status.setText("Can't save because no nodes");
		    				return;
		    			}
		    			else
		    				status.setText("Ok");
		    			String line;
		            	FileChooser fileChooser = new FileChooser();
		    			fileChooser.setTitle("Save Graph File");
		    			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		                fileChooser.getExtensionFilters().add(extFilter);
		                
		                //Show save file dialog
		              	File selected_file = fileChooser.showSaveDialog(view_pane.getScene().getWindow());	
              			System.out.println(selected_file);

		              	if (selected_file == null) {
		    				return;
		    			}
		              	try {
		              		PrintWriter writer = new PrintWriter(selected_file, "UTF-8");
		              		for (Node<String> start_node : graph.V()) {
		              			line = start_node + "->";
		              			for (Entry<Node<String>, Integer> edge : graph.adj_edges(start_node)) {
		              				line += edge.getKey() + "," + edge.getValue() + ";";
		              			}
		              			writer.println(line);
		              			System.out.println(line);
		              		}
			              	writer.close();

							
						}
		    			 catch (Exception e) {
							e.printStackTrace();
						}

		            }
		         });
		    	
		    	
		    	
		    	full_bellman_ford.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    		@Override
		            public void handle(MouseEvent t) {
		    			if(graph==null || graph.V().size() < 1) {
		    				status.setText("Can't do bellmanford because no nodes");
		    				return;
		    			}
		    			else
		    				status.setText("Okay");
		    			HashMap<String, Integer> map = graph.doBellmanFord(choice_box_startbellman.getValue()).get(graph.doBellmanFord("0").size() - 2);
		    	        ObservableList<Entry<String, Integer>> items = FXCollections.observableArrayList(map.entrySet());
		    	        cost_table.setItems(items);
	
		    		}
		    	});
		    	
		    	step_bellman_ford.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    		@Override
		            public void handle(MouseEvent t) {
		    			if(graph==null || graph.V().size() < 1) {
		    				status.setText("Can't do bellmanford because no nodes");
		    				return;
		    			}
		    			else
		    				status.setText("Okay");
		    			
		    			HashMap<String, Integer> old_items = new HashMap<String, Integer> ();
		    			ArrayList<HashMap<String, Integer>> array = graph.doBellmanFord(choice_box_startbellman.getValue().toString());
		    			if(step < array.size() - 1) {
		    				step ++;
		    				if(step > 0)
		    					old_items = array.get(step - 1);
		    			}
		    			
		    			HashMap<String, Integer> map = array.get(step);
		    			ObservableList<Entry<String, Integer>> items = FXCollections.observableArrayList(map.entrySet());
		    			if(! old_items.entrySet().equals(map.entrySet()) && map.entrySet().size() > 0) {
		    				cost_table.getItems().removeAll(cost_table.getItems());
		    				cost_table.setItems(items);
		    	        }
		    			
		    			for(javafx.scene.Node child : view_pane.getChildren()) {
		    				if (child.getClass() == Circle.class) {
		    					Circle circle = (Circle) child;
    							circle.setFill(javafx.scene.paint.Color.RED);
		    				}
		    			}
		    			
		    			for(javafx.scene.Node child : view_pane.getChildren()) {
		    				if (child.getClass() == Circle.class) {
		    					if(step >= 1 && map.get(child.getId()) < array.get(step - 1).get(child.getId())){
		    						Circle circle = (Circle) child;
	    							circle.setFill(javafx.scene.paint.Color.BLUE);
		    					}
		    				}
		    			}
		    			
		    		}
		    	});
		    		
		    	
		    }
		    
		    public void initializeGraph() {
    			view_pane.getChildren().removeAll(view_pane.getChildren());
    			Pane tmp = graph.getFxGraph((int) view_pane.getWidth(), (int) view_pane.getHeight(), 20, 20);
    			view_pane.getChildren().addAll(tmp.getChildren());
    			ArrayList <String>nodes_list = new ArrayList<String>();
    			for(Node<String> node_value : graph.V())
    				nodes_list.add(node_value.toString());
		        choice_box_startbellman.setItems(FXCollections.observableList(nodes_list));
		        choice_box_startbellman.setValue(nodes_list.get(0).toString());
		        cost_table.getItems().removeAll(cost_table.getItems());
		        remove_node_field.setItems(FXCollections.observableList(nodes_list));
		        
				LinkedList<Node<String>> l = new LinkedList<Node<String>>();
				for(Node<String> node : graph.V())
					l.add(node);
				add_edge_u.setItems(FXCollections.observableList(l));
				add_edge_v.setItems(FXCollections.observableList(l));
				
				LinkedList<String> edges = new LinkedList<String>();

				for(Node<String> node : graph.V()) {
					for(Entry<Node<String>,Integer> E : graph.adj_edges(node)) {
						String edge = "(" + node.toString() + ", " + E.getKey().toString() + ")";
						edges.add(edge);
					}
				}
				
				remove_edge_field.setItems(FXCollections.observableList(edges));
				




		    }
	}
	;