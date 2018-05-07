package graph;

import java.util.ArrayList;
import java.util.HashMap;
//perch� abbiamo usato un treeset e non un hashset? domanda pi� probabile del sorgere del sole
//import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;


public class Graph<T extends Comparable<T>> implements IGraph<T> {

        public HashMap<Node<T>, HashMap<Node<T>, Integer>> vertexes;

	public Graph(){
		this.vertexes = new HashMap<Node<T>, HashMap<Node<T>, Integer>>();
	}

	@Override
	public void insertNode(Node<T> u) {
		if (!this.vertexes.containsKey(u)){
			HashMap<Node<T>, Integer> eHashMap = new HashMap<Node<T>, Integer>();
			this.vertexes.put(u, eHashMap);
		}
	}

	@Override
	public void deleteNode(Node<T> u) {
		if (this.vertexes.containsKey(u)){
			this.vertexes.remove(u);
		}
		for (Entry<Node<T>, HashMap<Node<T>, Integer>> e : this.vertexes.entrySet()) {
			if (e.getValue().containsKey(u)){
				e.getValue().remove(u);
			}
		}
	}


	@Override
	public void insertEdge(Node<T> u, Node<T> v, Integer cost) { // inserimento arco dati i nodi e costo/peso

		if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
			this.vertexes.get(u).put(v, cost);
	}


	@Override
	public void deleteEdge(Node<T> u, Node<T> v) { // rimozione di un arco

		if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
			this.vertexes.get(u).remove(v);
	}


	@Override
	public Set<Node<T>> adj(Node<T> u) { // ritorna i nodi adiacenti ad un dato nodo

		Set<Node<T>> adjSet = new TreeSet<Node<T>>();

		if (this.vertexes.containsKey(u)) {
			adjSet = this.vertexes.get(u).keySet();
		}

		return adjSet;
	}
	
	public Set<Entry<Node<T>, Integer>> adj_edges(Node<T> u) { // ritorna gli archi adiacenti
		Set<Entry<Node<T>, Integer>> adjSet = new TreeSet<Entry<Node<T>, Integer>>();

		if (this.vertexes.containsKey(u)) {
				adjSet = this.vertexes.get(u).entrySet();
		}
		return adjSet;
	}
   
	public HashMap<Node<T>, HashMap<Node<T>, Integer>> getVertexes() {
		return vertexes;
	}


	@Override
	public Set<Node<T>> V() { // ritorna l'insieme dei nodi vertice
		Set<Node<T>> set = this.vertexes.keySet();
		return set;
	}
	
	
	public Set<Entry<Node<T>, HashMap<Node<T>, Integer>>> map() { // ritorna l'insieme dei nodi vertice
		return this.vertexes.entrySet();
	}


	@Override
	// utile per stampare e visualizzare grafo attraverso liste di adiacenza
	public void print() {
		for (Entry<Node<T>, HashMap<Node<T>, Integer>> e : this.vertexes.entrySet()) {

			System.out.print(e.getKey() + " : ");

			for (Node<T> l : e.getValue().keySet()) {
				System.out.print("(" + l + "," + e.getValue().get(l) + ")");
			}

			System.out.println();

		}
	}


	public ArrayList<HashMap<String, Integer>> doBellmanFord(String start){
		ArrayList<HashMap<String, Integer>> M = new ArrayList<HashMap<String, Integer>>();

		for(Node<T> node : this.V()) {
			M.add(new HashMap<String, Integer> ());
			M.get(0).put(node.toString(), Integer.MAX_VALUE - 100);
		}
		M.get(0).put(start, 0);

		for(int i = 1; i < this.V().size(); i ++) {
			System.out.println(i + "  " + M);
			for(Node<T> node : this.V())
				M.get(i).put(node.toString(), M.get(i - 1).get(node.toString()));

			for(Node<T> start_node : this.V()) {
				for(Entry<Node<T>, Integer> end_node : this.adj_edges(start_node))
					M.get(i).put(end_node.getKey().toString(), Math.min(M.get(i).get(end_node.getKey().toString()), end_node.getValue() + M.get(i).get(start_node.toString())));
			}
		}
		return M;
	}




}
