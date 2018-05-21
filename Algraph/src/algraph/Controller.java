	package algraph;
	
	import graph.Graph;
	import graph.Node;
	import javafx.beans.property.SimpleObjectProperty;
	import javafx.beans.value.ObservableValue;
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.EventHandler;
	import javafx.fxml.FXML;
	import javafx.scene.control.Button;
	import javafx.scene.control.ChoiceBox;
	import javafx.scene.control.Label;
	import javafx.scene.control.ProgressBar;
	import javafx.scene.control.SplitPane;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.input.MouseEvent;
	import javafx.scene.layout.AnchorPane;
	import javafx.scene.layout.Pane;
	import javafx.stage.FileChooser;
	import javafx.util.Callback;
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
	import java.io.PrintWriter;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.LinkedList;
	import java.util.Map.Entry;
	import java.util.Random;
	import java.util.stream.Collectors;
	import java.util.stream.IntStream;
	import javafx.scene.shape.Circle;
	import javafx.scene.control.TextField;

	public class Controller {
	    
	        // Riferimenti per gestione di elementi FXML
			// Pannelli
		    @FXML private Pane view_pane;
		    @FXML private SplitPane split_pane;
		    @FXML private AnchorPane left_pane;
		    // Gestione grafo e salvataggio
		    @FXML private Label status;
		    @FXML private Button auto_gen;
		    @FXML private Button file_gen;
		    @FXML private Button save_to_file;
		    @FXML private ChoiceBox<Integer> choice_box_nodes;
		    // Bellmanford 
		    @FXML private ChoiceBox<String> choice_box_startbellman;
		    @FXML private ProgressBar progress_gen;
		    @FXML private Button full_bellman_ford;
		    @FXML private Button step_bellman_ford; 
		    @FXML private TableView<Entry<String,String>>  cost_table;
		    @FXML private TableColumn<Entry<String, String>, String> node_column;
		    @FXML private TableColumn<Entry<String, String>, String> cost_column;
		    // Modifica grafo		    
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


		    protected Integer step; // utilizzato per step di bellman ford
		    protected Graph graph; // salva un istanza del grafo visualizzato
		    protected GraphUtils utils; // utils per gen graph
		    Random random;
		    
		    @FXML
		    public void initialize() { // inizializza parti grafiche
		    	utils = new GraphUtils();
	        	random =  new Random((long)1917);
	        	split_pane.setDividerPositions(0.245);
	            left_pane.maxWidthProperty().bind(split_pane.widthProperty().multiply(0.24));
		        choice_box_nodes.setItems(FXCollections.observableList(IntStream.range(3, 9).boxed().collect(Collectors.toList())));
		        choice_box_nodes.setValue(3);
	            
	            node_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<String, String>, String>, ObservableValue<String>>() { // inizializzazione colonna per nodi bellman
		            @Override
		            public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry<String, String>, String> p) {
		                return new SimpleObjectProperty<String>(p.getValue().getKey());
		            }
	
		        });
	            
	            cost_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<String, String>, String>, ObservableValue<String>>() {// inizializzazione colonna per costi bellman
		            @Override
		            public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry<String, String>, String> p) {
		                return new SimpleObjectProperty<String>(p.getValue().getValue());
		            }
		        });
	
	
		            
		        choice_box_startbellman.setOnMouseClicked(new EventHandler<MouseEvent>() { // fa si che cliccando su tendina la computazione di bellman riparta da zero
		    		 
		            @Override
		            public void handle(MouseEvent t) {
		            	cost_table.getItems().removeAll(cost_table.getItems());
		            	step = -1;
		            }
		        });
	
	            add_edge.setOnMouseClicked(new EventHandler<MouseEvent>() { // gestione dell'aggiunta di un arco da parte dell'utente
					@Override
					public void handle(MouseEvent arg0) {
						try {
							int weight = Integer.parseInt(add_edge_weight.getText()); // prende il peso dal campo input
							if(!add_edge_u.getValue().equals(add_edge_v.getValue()))
								graph.insertEdge(add_edge_u.getValue(), add_edge_v.getValue(), weight); // utilizza i valori delle due tendine
							else
								throw new Exception("same value");
							initializeGraph();
							status.setText("Okay");
						}
						catch(Exception e) {
							status.setText("invalid operation");
						}
					}
	            	
	            });
	            
	           remove_edge.setOnMouseClicked(new EventHandler<MouseEvent>() { // gestione della rimozione di un arco da parte dell'utente
	            	
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
	            			if (u != null && v!= null) // nel caso uno dei nodi non fosse specificato
	            				graph.deleteEdge(u, v);
	            			else
	            				throw new Exception("Wrong format");
	            			initializeGraph();
	            			status.setText("Ok");
	            		}
	            		catch( Exception e) {
	            			status.setText("Invalid operation");
	            		}
	            	}
	            });
	            
	            add_node.setOnMouseClicked(new EventHandler<MouseEvent>() { // aggiunta nodo da parte dell'utente

					@Override
					public void handle(MouseEvent arg0) {
						try {
							if(add_node_field.getText() == null || add_node_field.getText().length() > 1 ) // check per eventuale non conformita dell'input
								throw new Exception("Invalid entry");
							else
								{
								graph.insertNode(new Node<String>(add_node_field.getText()));
								initializeGraph();
								status.setText("Okay");
								}
							}
						catch(Exception e) {
							status.setText("invalid operation");
						}
					}
		        	
		        });
	            
	            
	            
	            
	            remove_node.setOnMouseClicked(new EventHandler<MouseEvent>() { // rimozione nodo da parte dell' utente
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
	            
		        
		       
		        
		       
		    	
		    	auto_gen.setOnMouseClicked(new EventHandler<MouseEvent>() { // generazione automatica del grafo
		    		 
		            @Override
		            public void handle(MouseEvent t) {
		            	step = -1;
		            	graph = new Graph();
		    			for(int i = 0; i < choice_box_nodes.getValue(); i++)
		    				graph.insertNode(new Node<String>(String.valueOf(i)));
		    				
		    			for(Node<String> start_node : graph.V()) 
		    				for(Node<String> end_node : graph.V()) 
			    				if(random.nextInt(100) < 25 && !start_node.equals(end_node)) // per ogni coppia di nodi genera un arco con probabilita del 25 percento
			    					graph.insertEdge(start_node, end_node, random.nextInt(10) - 2);	
		    			initializeGraph();
		            }
		         });
		    	
		    	file_gen.setOnMouseClicked(new EventHandler<MouseEvent>() { // generazione del grafo a partire dal file
		    		
		    		@Override
		            public void handle(MouseEvent t) {  
		    			HashMap<String, HashMap<String, Integer>> hash = new HashMap<String, HashMap<String, Integer>>();
		    			step = -1;
			            graph = new Graph();
		            	FileChooser fileChooser = new FileChooser();
		    			fileChooser.setTitle("Open Resource File");
		    			File selected_file = fileChooser.showOpenDialog(view_pane.getScene().getWindow());	
		    			try { // costruzione riga per riga, se non va a buon fine msg di errore
							@SuppressWarnings("resource")
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
							}
							reader.close();
							for(Entry<String, HashMap<String, Integer>> node : hash.entrySet()) {
								graph.insertNode(new Node<String>(node.getKey()));
							}
							for(Node<String> start_node : graph.V()) {
								for(Node<String> end_node : graph.V()) {
									if (hash.get(start_node.toString()).containsKey(end_node.toString()))
										graph.insertEdge(start_node, end_node, hash.get(start_node.toString()).get(end_node.toString()));
								}
							}
							
							if(graph.V().size() > 10)
								throw new Exception("Wrong format");
							else {
								initializeGraph();
								status.setText("File correctly loaded");
							}
							reader.close();
						}
		    			 catch (Exception e) {
							status.setText("Error loading file");
						}
		    		
		    			
		            }
		         });
		    	
		    	save_to_file.setOnMouseClicked(new EventHandler<MouseEvent>() { // gestione salvataggio del grafo su file
		    		
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

		              	if (selected_file == null) {
		    				return;
		    			}
		              	try {
		              		PrintWriter writer = new PrintWriter(selected_file, "UTF-8");
		              		for (Node<String> start_node : graph.V()) { // per ogni nodo salva su file NODO -> arco1,costo1; arco2, costo2; .....
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
		    				status.setText("Error");
							e.printStackTrace();
						}

		            }
		         });
		    	
		    	
		    	
		    	full_bellman_ford.setOnMouseClicked(new EventHandler<MouseEvent>() { // utilizzando l'ultimo valore dell' array  di risultati di bellmanford generato ottengo i costi minimi
		    		@Override
		            public void handle(MouseEvent t) {
		    			try {
			    			ArrayList<HashMap<String, Integer>> bellman_array = graph.doBellmanFord(choice_box_startbellman.getValue());
			    			if (bellman_array.size() == 0)
		    					throw new Exception("Cycle");
			    			ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>> (); //Cambiare tipo hash map per renderla string -> string
			    			for(int i = 0; i < bellman_array.size(); i++) {
			    				array.add(new HashMap<String, String>());
			    				for(Entry<String, Integer> entry : bellman_array.get(i).entrySet()) {
			    					String tmp;
			    					if (entry.getValue() < Integer.MAX_VALUE / 2)
			    						tmp = entry.getValue().toString();
			    					else
			    						tmp = "Infinity";
			    					array.get(i).put(entry.getKey(), tmp);
			    				}
				
			    			}
			    			HashMap<String, String> map = array.get(array.size() - 2);
			    	        ObservableList<Entry<String, String>> items = FXCollections.observableArrayList(map.entrySet());
			    	        cost_table.setItems(items);
			    	        status.setText("Okay");
		    			}
		    			catch(Exception e) {
		    				status.setText("Can't do bellmanford");
		    			}
	
		    		}
		    	});
		    	
		    	step_bellman_ford.setOnMouseClicked(new EventHandler<MouseEvent>() { // azione su bellman ford passo per passo, e' utilizzato step come ausilio
		    		@Override
		            public void handle(MouseEvent t) {
		    			
		    			try {
		    				if(graph==null || graph.V().size() < 1) 
		    					throw new Exception("No nodes");
			    			
		    				HashMap<String, String> old_items = new HashMap<String, String> ();
		    				ArrayList<HashMap<String, Integer>> bellman_array = graph.doBellmanFord(choice_box_startbellman.getValue().toString()); // array con mappa che associa nodo destinazione costo
		    				if (bellman_array.size() == 0)
		    					throw new Exception("Cycle");
		    				
		    				ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>> (); //Cambiare tipo hash map per renderla string -> string
		    				for(int i = 0; i < bellman_array.size(); i++) {
		    					array.add(new HashMap<String, String>());
		    					for(Entry<String, Integer> entry : bellman_array.get(i).entrySet()) {
		    						String tmp;
		    						if (entry.getValue() < Integer.MAX_VALUE / 2)
		    							tmp = entry.getValue().toString();
		    						else
		    							tmp = "Infinity"; // se valore entry nel range dell'infinito scrivo infinity nel campo
		    						array.get(i).put(entry.getKey(), tmp);
		    					}
			
		    				}
		    			
		    				if(step < array.size() - 1) { // incremento contatore globale step
		    					step ++;
		    					if(step > 0)
		    						old_items = array.get(step - 1);
		    				}
		    			
		    				HashMap<String, String> map = array.get(step);
		    				HashMap<String, Integer> int_map = bellman_array.get(step);

		    				ObservableList<Entry<String, String>> items = FXCollections.observableArrayList(map.entrySet());
		    				if(! old_items.entrySet().equals(map.entrySet()) && map.entrySet().size() > 0) {
		    					cost_table.getItems().removeAll(cost_table.getItems());
		    					cost_table.setItems(items);
		    				}
		    			
		    				for(javafx.scene.Node child : view_pane.getChildren()) { // rimetto i nodi tutti rossi ad ogni step
		    					if (child.getClass() == Circle.class) {
		    						Circle circle = (Circle) child;
		    						circle.setFill(javafx.scene.paint.Color.RED);
		    					}
		    				}
		    			
		    				for(javafx.scene.Node child : view_pane.getChildren()) { // i nodi che hanno migliorato il costo divengono blu
		    					if (child.getClass() == Circle.class) {
		    						if(step >= 1 && int_map.get(child.getId()) < bellman_array.get(step - 1).get(child.getId()) && int_map.get(child.getId()) < Integer.MAX_VALUE / 2){
		    							Circle circle = (Circle) child;
		    							circle.setFill(javafx.scene.paint.Color.BLUE);
		    						}
		    					}
		    				}
	    					status.setText("Okay");

		    			
		    			}
		    			catch (Exception e) {
							e.printStackTrace();

		    				status.setText("Error, possible cycle");
		    			}
		    			
		    		}});
		    		
		    	
		    }
		    
		    public void initializeGraph() { // utilizzato per inizializzare/reinizializzare il grafo e tutte le componenti/funzioni connesse
    			view_pane.getChildren().removeAll(view_pane.getChildren());
    			Pane tmp = utils.getFxGraph(graph, (int) view_pane.getWidth(), (int) view_pane.getHeight(), 20, 20);
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
				
				step = -1;



		    }
	}
	;
