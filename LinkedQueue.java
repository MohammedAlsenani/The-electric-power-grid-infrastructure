
public class LinkedQueue<T> implements Queue<T> {
	private Node<T> head, tail;
	private int size;

	public LinkedQueue() {
		head = tail = null;
		size = 0;
	}

	public int length() {
		return size;
	}

	public boolean full() {
		return false;
	}

	public void enqueue(T e) {
		if (tail == null) {
			head = tail = new Node<T>(e);
		} else {
			tail.next = new Node<T>(e);
			tail = tail.next;
		}
		size++;
	}

	public T serve() {
		T x = head.data;
		head = head.next;
		size--;
		if (size == 0)
			tail = null;
		return x;
	}

}

class Node<T> {
	public T data;
	public Node<T> next;

	public Node(T val) {
		data = val;
		next = null;
	}
}
