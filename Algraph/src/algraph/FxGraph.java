package algraph;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import graph.Graph;
import graph.Node;
import javafx.scene.control.Cell;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;

public class FxGraph <T extends Comparable<T>>{
	protected HashMap<Circle, HashMap<Line, Integer>> nodes;

	public FxGraph(Graph graph) {}
		/*HashMap<Line, Integer> edges;
		Set<Node<T>> set = graph.V();
		for (Node<T> e : set) {
			Set<Node<T>> adjs= graph.adj(e);
			edges = new HashMap <Line, Integer>();
			for (Node<T> l : adjs) {
				edges.put(createEdge(start, end, cost)
			}
		}
			
		for (Node<T> l : e.getValue().keySet()) {
			System.out.print("(" + l + "," + e.getValue().get(l) + ")");
		}
		 
		
	}*/
	
	
	public Line createEdge(Circle start, Circle end, Integer cost) {
		Line line = null;
		line = new Line(start.getLayoutX(), start.getLayoutY(), end.getLayoutX(), end.getLayoutY());
		return line;
	}
}
