package graph;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
	
	private T element;
	
	public Node(T e){
		this.element = e;
	}
	
	public T getElement(){
		return this.element;
	}

	public String toString(){
		return this.element.toString();
	}

	@Override
	public int compareTo(Node<T> o) {
		return this.element.compareTo(o.element);
	}
	
}
