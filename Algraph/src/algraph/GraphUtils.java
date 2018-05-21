package algraph;

import java.util.HashMap;
import java.util.Map.Entry;

import graph.Node;
import graph.Graph;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


public final class GraphUtils {
    public Pane getFxGraph(Graph graph, int size_x, int size_y, int offset, double radius) { // ritorna oggetto pane con oggetti dentro per costruzione grafo
		Pane view = new Pane();
		HashMap<Node<String>, HashMap<Node<String>, Integer>> map = graph.getVertexes();
		HashMap<String, Circle> circles = new HashMap<String, Circle>();

		// Compute coordinates
		int n_nodes = graph.V().size();
		float x_nodes = (float) n_nodes / 3;

		double x_coord = offset;
		double y_coord = offset + 20;
		int raw = 0, coloumn = 0;
		for (Entry<Node<String>, HashMap<Node<String>, Integer>> node : map.entrySet()) {
			Circle circle = new Circle();
			Text text;
			circle.setFill(javafx.scene.paint.Color.RED);
			circle.setCenterX(x_coord);
			circle.setCenterY(y_coord);
			circle.setRadius(radius);
			circle.setId(node.getKey().toString());
			circles.put(node.getKey().toString(), circle);//associ nome nodo a oggetto FX
			text = new Text(x_coord, y_coord, node.getKey().toString());//crei oggetto text passando coordinante e nome nodo
			//offset vari
			if(coloumn % 2 == 0)
				y_coord += (int) radius * 3;
			else
				y_coord -= (int) radius * 3;

			x_coord += (size_x - offset * 2) / x_nodes;
			if(x_coord >= size_x) {
				y_coord += (size_y - offset * 2) / 3;
				x_coord = offset ;
				raw ++;
				if(raw % 2 != 0)
					x_coord += (int) radius * 3;

			}
			else
				coloumn ++;
			view.getChildren().add(circle);
			view.getChildren().add(text);
		}
		//insert edges
		for (Entry<Node<String>, HashMap<Node<String>, Integer>> start : map.entrySet()) {
			for(Entry <Node<String>, Integer> end : start.getValue().entrySet()) {
				Integer cost;
				cost = end.getValue();
				//punti medi
				double mx = (circles.get(start.getKey().toString()).getCenterX() + circles.get(end.getKey().toString()).getCenterX())/2;
				double my = (circles.get(start.getKey().toString()).getCenterY() + circles.get(end.getKey().toString()).getCenterY())/2;
				//angolo dell' arco
				double m = Math.atan2(circles.get(end.getKey().toString()).getCenterY() - circles.get(start.getKey().toString()).getCenterY() , circles.get(end.getKey().toString()).getCenterX() - circles.get(start.getKey().toString()).getCenterX());
				//segmento perpendicolare all'arco a cui "appendere" il costo
				Line tl = new Line(mx, my, mx - 10 * Math.cos(m + Math.toRadians(90)) , my - 10 * Math.sin(m + Math.toRadians(90)));
				Text costText;
				//controllo che fa s� che i costi degli archi andanti da destra verso sinistra non tocchino l' arco
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

    public Line[] drawEdge(Circle u, Circle v, double arrow_angle, double arrow_length, double radius) { // returna le lines per la costruzione di un arco
		double dx, dy, m;
		Line edge, up, down;
		dx = v.getCenterX() - u.getCenterX();
		dy = v.getCenterY() - u.getCenterY();
		m = Math.atan2(dy, dx);
		edge = new Line(); // line principale
			edge.setStartX(u.getCenterX() + radius * Math.cos(m));
			edge.setStartY(u.getCenterY() + radius * Math.sin(m));
			edge.setEndX(v.getCenterX() - radius * Math.cos(m));
			edge.setEndY(v.getCenterY() - radius * Math.sin(m));
			edge.setStrokeWidth(3);
		// sotto costruisco le lines per la costruzione della forma a freccia
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
