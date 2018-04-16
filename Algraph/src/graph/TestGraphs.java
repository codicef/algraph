package graph;

import java.util.Set;

public class TestGraphs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Graph<String> g1 = new Graph<String>();
		
		Node<String> a = new Node<String>("A");
		Node<String> b = new Node<String>("B");
		Node<String> c = new Node<String>("C");
		
		g1.insertNode(a);
		g1.insertNode(b);
		g1.insertNode(c);
		
		g1.insertEdge(a, b, 6);
		g1.insertEdge(a, c, 8);
		
		g1.print();
		
		System.out.println("*********");
		Set<Node<String>> adjA = g1.adj(a);
		
		for (Node<String> nodeAdjA : adjA) {
			System.out.println(nodeAdjA);	
		}		
		
	}

}
