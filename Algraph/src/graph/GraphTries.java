package graph;

import java.util.ArrayList;
import java.util.HashMap;
//perchè abbiamo usato un treeset e non un hashset? domanda più probabile del sorgere del sole
//import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GraphTries<T extends Comparable<T>> implements IGraph<T> {

	HashMap<Node<T>, HashMap<Node<T>, Integer>> vertexes;
	
	public GraphTries(){
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
	public void insertEdge(Node<T> u, Node<T> v, Integer cost) {
		
		if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
			this.vertexes.get(u).put(v, cost);
	
	}

	
	@Override
	public void deleteEdge(Node<T> u, Node<T> v) {
		
		if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
			this.vertexes.get(u).remove(v);
	}


	
	@Override
	public Set<Node<T>> adj(Node<T> u) {
		
		Set<Node<T>> adjSet = new TreeSet<Node<T>>();
		
		if (this.vertexes.containsKey(u)) {
			adjSet = this.vertexes.get(u).keySet();
		}
		
		return adjSet;
	}
	
	public Set<Entry<Node<T>, Integer>> adj_edges(Node<T> u) {
		
		Set<Entry<Node<T>, Integer>> adjSet = new TreeSet<Entry<Node<T>, Integer>>();
		
		if (this.vertexes.containsKey(u)) {
			adjSet = this.vertexes.get(u).entrySet();
		}
		return adjSet;
	}

	
	@Override
	public Set<Node<T>> V() {
		Set<Node<T>> set = this.vertexes.keySet();
		return set;
	}

	
	
	
	@Override
	// NOTA: metodo utility per verificare il contenuto delle liste di adiacenza
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
	
	//ritorno circles per evitare di fare troppi cambiamenti con archi dopo, passo il Pane e n°nodi
	public HashMap <String,Circle> polygon(Pane p, int nodes){
		HashMap<String, Circle> circles = new HashMap<String, Circle>();
		switch(nodes) {
		case 2:
			double startX = (p.getWidth()/2) + 300;
			double startY = (p.getHeight()/2) + 300;
			double endX = (p.getWidth()/2) + 500;
			double endY =  (p.getHeight()/2) + 300;
			boolean flag = false;
			System.out.println("width = " + p.getWidth() + ", height = " + p.getHeight());
			for (Entry<Node<T>, HashMap<Node<T>, Integer>> node : this.vertexes.entrySet()) {
				Circle circle = new Circle();
				Text text;
				circle.setFill(javafx.scene.paint.Color.AQUA);
				if (flag == true) {
					circle.setCenterX(startX);
					circle.setCenterY(startY);
					}
				else {
					circle.setCenterX(endX);
					circle.setCenterY(endY);
				}
				circle.setRadius(20);
				circle.setId(node.getKey().toString());
				circles.put(node.getKey().toString(), circle);//associ nome nodo a oggetto FX
				if(flag == true)
					text = new Text(startX, startY, node.getKey().toString());//crei oggetto text passando coordinante e nome nodo
				else
					text = new Text(endX, endY, node.getKey().toString());
				//offset
				p.getChildren().add(circle);
				p.getChildren().add(text);
				flag = true;
			}
			break;
		case 3:
			Circle[] tria = new Circle[3];
			for(int i = 0; i < 3; i++) {
				tria[i] = new Circle();
				tria[i].setFill(javafx.scene.paint.Color.AQUA);
				tria[i].setRadius(20);
				}
			for (Entry<Node<T>, HashMap<Node<T>, Integer>> node : this.vertexes.entrySet()) {
					int j = 0;
					tria[j].setId(node.getKey().toString());
					j++;
					circles.put(node.getKey().toString(), tria[j]);
				}
			tria[0].setCenterX(p.getWidth() + 400);
			tria[0].setCenterY(p.getHeight() + 100);
			tria[1].setCenterX(p.getWidth() + 150);
			tria[1].setCenterY(p.getHeight() + 550);
			tria[2].setCenterX(p.getWidth() + 650);
			tria[2].setCenterY(p.getHeight() + 550);
			Text t0,t1,t2;
			t0 = new Text(p.getWidth() + 400, p.getHeight() + 100, tria[0].getId());
			t1 = new Text(p.getWidth() + 150, p.getHeight() + 550, tria[1].getId());
			t2 = new Text(p.getWidth() + 650, p.getHeight() + 550, tria[2].getId());
			p.getChildren().addAll(tria);
			p.getChildren().addAll(t0,t1,t2);
			break;
		}
		
		return circles;
	}
	
	public Pane getFxGraph(int size_x, int size_y, int offset, double radius) {
		Pane view = new Pane();
		HashMap<String, Circle> circles = new HashMap<String, Circle>();
		
		// Compute coordinates
		int n_nodes = this.vertexes.size();
		circles = polygon(view,n_nodes);
		//insert edges
		for (Entry<Node<T>, HashMap<Node<T>, Integer>> start : this.vertexes.entrySet()) {
			for(Entry <Node<T>, Integer> end : start.getValue().entrySet()) {
				Integer cost;
				cost = end.getValue();
				//punti medi
				double mx = (circles.get(start.getKey().toString()).getCenterX() + circles.get(end.getKey().toString()).getCenterX())/2;
				double my = (circles.get(start.getKey().toString()).getCenterY() + circles.get(end.getKey().toString()).getCenterY())/2;
				//angolo dell' arco
				double m = Math.atan2(circles.get(end.getKey().toString()).getCenterY() - circles.get(start.getKey().toString()).getCenterY() , circles.get(end.getKey().toString()).getCenterX() - circles.get(start.getKey().toString()).getCenterX());
				System.out.println("angolo m fra archi "+start.getKey().toString() + " e " +end.getKey().toString() + "= "+ m);
				//segmento perpendicolare all'arco a cui "appendere" il costo
				Line tl = new Line(mx, my, mx - 10 * Math.cos(m + Math.toRadians(90)) , my - 10 * Math.sin(m + Math.toRadians(90)));
				Text costText;
				//controllo che fa sì che i costi degli archi andanti da destra verso sinistra non tocchino l' arco
				if ( circles.get(start.getKey().toString()).getCenterX() < circles.get(end.getKey().toString()).getCenterX())
					costText = new Text(tl.getEndX(),tl.getEndY(),cost.toString());
				else 
					costText = new Text(tl.getEndX(),tl.getEndY() + 7,cost.toString());
				costText.setRotate(Math.toDegrees(m));
				view.getChildren().addAll(drawEdge(circles.get(start.getKey().toString()), circles.get(end.getKey().toString()), 9, 20, radius));
				view.getChildren().add(costText);
			}
		}
		return view;
	}

	public Line[] drawEdge(Circle u, Circle v, double arrow_angle, double arrow_length, double radius) {
		double dx, dy, m;
		Line edge, up, down;
		dx = v.getCenterX() - u.getCenterX();
		dy = v.getCenterY() - u.getCenterY();
		m = Math.atan2(dy, dx);
		edge = new Line();
			edge.setStartX(u.getCenterX() + radius * Math.cos(m));
			edge.setStartY(u.getCenterY() + radius * Math.sin(m));
			edge.setEndX(v.getCenterX() - radius * Math.cos(m));
			edge.setEndY(v.getCenterY() - radius * Math.sin(m));
			edge.setStrokeWidth(3);
		up = new Line();
			up.setStartX(edge.getEndX());
			up.setStartY(edge.getEndY());
			up.setEndX(edge.getEndX() - arrow_length * Math.cos(m + Math.toRadians(arrow_angle)));
			up.setEndY(edge.getEndY() - arrow_length * Math.sin(m + Math.toRadians(arrow_angle)));
			up.setStrokeWidth(2);
		down = new Line();
			down.setStartX(edge.getEndX());
			down.setStartY(edge.getEndY());
			down.setEndX(edge.getEndX() - arrow_length * Math.cos(m + Math.toRadians(-arrow_angle)));
			down.setEndY(edge.getEndY() - arrow_length * Math.sin(m + Math.toRadians(-arrow_angle)));
			down.setStrokeWidth(2);
		Line[] res = {edge, up, down};
		return res;	
	}
	
	
}