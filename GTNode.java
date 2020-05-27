class GTNode<T> {

	public T data;
	public Queue<GTNode<T>> children;

	public GTNode(T e) {
		data = e;
		children = new LinkedQueue<GTNode<T>>();
	}
}
