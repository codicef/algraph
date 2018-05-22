package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;


public class Graph implements IGraph<String> {
	//variabili Globali per funzione isCyclic
	HashMap<String, Integer> dt; 
	HashMap<String, Integer> ft; 

	int time = 0;
	
    public HashMap<Node<String>, HashMap<Node<String>, Integer>> vertexes;

	public Graph(){
		this.vertexes = new HashMap<Node<String>, HashMap<Node<String>, Integer>>();
		time = 0;
		dt = new HashMap<String, Integer>(); 
		ft = new HashMap<String, Integer>(); 
	}

	@Override
	public void insertNode(Node<String> u) { // inserimento nodo
		if (!this.vertexes.containsKey(u)){
			HashMap<Node<String>, Integer> eHashMap = new HashMap<Node<String>, Integer>();
			this.vertexes.put(u, eHashMap);
			dt.put(u.toString(), 0);
			ft.put(u.toString(), 0);
			

		}
	}

	@Override
	public void deleteNode(Node<String> u) { // rimozione nodo
		if (this.vertexes.containsKey(u)){
			this.vertexes.remove(u);
		}
		for (Entry<Node<String>, HashMap<Node<String>, Integer>> e : this.vertexes.entrySet()) {
			if (e.getValue().containsKey(u)){
				e.getValue().remove(u);
			}
		}
	}


	@Override
	public void insertEdge(Node<String> u, Node<String> v, Integer cost) { // inserimento arco dati i nodi e costo/peso

		if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
			this.vertexes.get(u).put(v, cost);
	}


	@Override
	public void deleteEdge(Node<String> u, Node<String> v) { // rimozione di un arco

		if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
			this.vertexes.get(u).remove(v);
	}


	@Override
	public Set<Node<String>> adj(Node<String> u) { // ritorna i nodi adiacenti ad un dato nodo

		Set<Node<String>> adjSet = new HashSet<Node<String>>();

		if (this.vertexes.containsKey(u)) {
			adjSet = this.vertexes.get(u).keySet();
		}

		return adjSet;
	}
	
	public Set<Entry<Node<String>, Integer>> adj_edges(Node<String> u) { // ritorna gli archi adiacenti
		Set<Entry<Node<String>, Integer>> adjSet = new HashSet<Entry<Node<String>, Integer>>();

		if (this.vertexes.containsKey(u)) {
				adjSet = this.vertexes.get(u).entrySet();
		}
		return adjSet;
	}
   
	public HashMap<Node<String>, HashMap<Node<String>, Integer>> getVertexes() { // returna l'hashmap
		return vertexes;
	}


	@Override
	public Set<Node<String>> V() { // ritorna l'insieme dei nodi vertice
		Set<Node<String>> set = this.vertexes.keySet();
		return set;
	}
	
	
	public Set<Entry<Node<String>, HashMap<Node<String>, Integer>>> map() { // ritorna l'insieme dei nodi vertice
		return this.vertexes.entrySet();
	}


	@Override
	// utile per stampare e visualizzare grafo attraverso liste di adiacenza
	public void print() {
		for (Entry<Node<String>, HashMap<Node<String>, Integer>> e : this.vertexes.entrySet()) {

			System.out.print(e.getKey() + " : ");

			for (Node<String> l : e.getValue().keySet()) {
				System.out.print("(" + l + "," + e.getValue().get(l) + ")");
			}

			System.out.println();

		}
	}

	public Boolean isCyclic(Node<String> U) { // Implementazione funzione da libro Bertossi/Montresor
			time ++; 
			dt.put(U.toString(), time);
		    for(Node<String> v : this.adj(U)) {
		    	if(dt.get(v.toString()) == 0) {
		    		if(isCyclic(v))
		    			return true;
		    		}
		    	else {
		    		if (dt.get(U.toString()) > dt.get(v.toString()) && ft.get(v.toString()) == 0)
		    			return true;
		    	}
		    }
		    time ++;
		    ft.put(U.toString(), time);		
		    return false;
	}

	public ArrayList<HashMap<String, Integer>> doBellmanFord(String start){ //implementazione dell'algoritmo di bellman ford
		ArrayList<HashMap<String, Integer>> M = new ArrayList<HashMap<String, Integer>>();
		int j = 0;
		//inizializzazione
		M.add(new HashMap<String, Integer> ());
		for(Node<String> node : this.V()) {
			M.get(j).put(node.toString(), Integer.MAX_VALUE - 100); // val = infinito
		}
		M.get(j).put(start, 0);

		M.get(j).put(start + "!", 0); //utilizzato per questioni grafiche dalle funzioni utilizzatrici

		
		//rilassamento archi

		for(int i = 1; i < this.V().size(); i ++) {
			for(Node<String> start_node : this.V()) {
				j ++;
				M.add(new HashMap<String, Integer> ());
				for(Node<String> node : this.V())
					M.get(j).put(node.toString(), M.get(j - 1).get(node.toString()));
				M.get(j).put(start_node.toString() + "!", 0); //utilizzato per questioni grafiche dalle funzioni utilizzatrici

				for(Entry<Node<String>, Integer> end_node : this.adj_edges(start_node)) {
					M.get(j).put(end_node.getKey().toString(), M.get(j - 1).get(end_node.getKey().toString()));
					M.get(j).put(end_node.getKey().toString(), Math.min(M.get(j).get(end_node.getKey().toString()), end_node.getValue() + M.get(j).get(start_node.toString())));	
					
				}
			}
		}
		
		for(Node<String> start_node : this.V()) {
			for(Entry<Node<String>, Integer> end_node : this.adj_edges(start_node)) {
				if(M.get(M.size() - 1).get(start_node.toString()) < Integer.MAX_VALUE / 2 && M.get(M.size() - 1).get(start_node.toString()) + end_node.getValue() < M.get(M.size() - 1).get(end_node.getKey().toString())) {
					M = new ArrayList<HashMap<String, Integer>>(); // trovato un ciclo di costo negativo
					return M;
				}
			}
		}
		return M;
	}




}
