public class LinkedGT<T> implements GT<T> {

	private GTNode<T> root, current;

	public LinkedGT() {
		root = current = null;
	}
	
	// Return true if the tree is empty
	public boolean empty() {
		return root == null;
	}

	// Return true if the tree is full
	public boolean full() {
		return false;
	}

	// Return the data of the current node
	public T retrieve() {
		if (!empty())
			return current.data;
		else
			return null;
	}

	// Update the data of the current node
	public void update(T e) {
		current.data = e;
	}

	// If the tree is empty e is inserted as root. If the tree is not empty, e is
	// added as a child of the current node. The new node is made current and true
	// is returned.
	public boolean insert(T e) {
//		insert e as root
		if (empty()) {
			root = current = new GTNode<T>(e);
			return true;
		}
//		insert e as new child of current
		GTNode<T> n = new GTNode<T>(e);
		current.children.enqueue(n);
		current = n;
		return true;
	}

	// Return the number of children of the current node.
	public int nbChildren() {
		if (empty()) {
			return 0;
		} else
			return current.children.length();
	}

	// Put current on the i-th child of the current node (starting from 0), if it
	// exists, and return true. If the child does not exist, current is not changed
	// and the method returns false.
	public boolean findChild(int i) {
//		if i is minus or the there is no children will return false
		if (current == null || i < 0)
			return false;
//		n is temporary node as children list
		Queue<GTNode<T>> n = current.children;
		int c = n.length();
		boolean flag = false;
//		search for i-th child
		for (int j = 0; j < c; j++) {
			GTNode<T> tmp = n.serve();
			n.enqueue(tmp);
			if (j == i) {
				current = tmp;
				flag = true;
			}
		}
		return flag;
	}

	// Put current on the parent of the current node. If the parent does not exist,
	// current does not change and false is returned.
	public boolean findParent() {
		if (!empty()) {
			if (current == root)
				return false;
			else {
				findParent(root);
				return true;
			}
		} else
			return false;
	}

//	helper method to find parent
	private void findParent(GTNode<T> tmp) {
		if (tmp == null)
			return;
		if (tmp.children.length() == 0)
			return;
		else {
			Queue<GTNode<T>> child = tmp.children;
			int n = child.length();
			boolean flag = false;
			for (int i = 0; i < n; i++) {
				GTNode<T> ch = child.serve();
				child.enqueue(ch);
				if (ch == current) {
					current = tmp;
					flag = true;
				}
			}

			if (!flag) {
				for (int j = 0; j < n; j++) {
					GTNode<T> childQ = tmp.children.serve();
					tmp.children.enqueue(childQ);
					findParent(childQ);
				}
			} else
				return;
		}
	}

	// Put current on the root. If the tree is empty nothing happens.
	public void findRoot() {
		if (!empty())
			current = root;
	}

	// Remove the current subtree. The parent of current, if it exists, becomes the
	// new current.
	public void remove() {
		if (current == root)
			root = current = null;
		else {
			GTNode<T> tmp = current;
			if (findParent()) {
				Queue<GTNode<T>> child = current.children;
				int n = child.length();
				for (int i = 0; i < n; i++) {
					GTNode<T> ch = child.serve();
					if (ch == tmp) {
						return;
					} else
						child.enqueue(ch);
				}
			}
		}
	}
}
