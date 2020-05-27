public class PowerGridUtils {

	// Return the IDs of all elements in the power grid pg in pre-order.
	public static Queue<Integer> collectPreorder(GT<PGElem> pg) {
		Queue<Integer> cpo = new LinkedQueue<Integer>();
		pg.findRoot();
		collectPreorder(pg, cpo);
		return cpo;
	}

	private static void collectPreorder(GT<PGElem> pg, Queue<Integer> cpo) {
		if (pg == null || pg.empty()) {
			return;
		}
		cpo.enqueue(pg.retrieve().getId());
		if (pg.nbChildren() != 0) {
			int n = pg.nbChildren();
			for (int i = 0; i < n; i++) {
				pg.findChild(i);
				collectPreorder(pg, cpo);
				pg.findParent();
			}
		}
	}

	// Searches the power grid pg for the element with ID id. If found, it is made
	// current and true is returned, otherwise false is returned.
	public static boolean find(GT<PGElem> pg, int id) {
		if (pg != null && !pg.empty()) {
			GT<PGElem> tmp = pg;
			Queue<Integer> cpo = collectPreorder(pg);
			pg.findRoot();
			boolean flag = false;
			int n = cpo.length();
			for (int i = 0; i < n; i++) {
				if (cpo.serve() == id) {
					findRec(pg, id);
					flag = true;
				}
			}
			if (!flag) {
				pg = tmp;
			}
			return flag;
		} else
			return false;
	}

	private static boolean findRec(GT<PGElem> pg, int id) {
		if (pg.retrieve().getId() == id) {
			return true;
		} else {
			int n = pg.nbChildren();
			boolean flag = false;
			for (int i = 0; i < n; i++) {
				pg.findChild(i);
				flag = findRec(pg, id);
				if (!flag)
					pg.findParent();
				else
					return true;
			}
			return flag;
		}
	}

	// Add the generator element gen to the power grid pg. This can only be done if
	// the grid is empty. If successful, the method returns true. If there is
	// already a generator, or gen is not of type Generator, false is returned.
	public static boolean addGenerator(GT<PGElem> pg, PGElem gen) {
		if (pg.empty() && gen.getType() == ElemType.Generator) {
			pg.insert(gen);
			return true;
		} else
			return false;
	}

	// Attaches pgn to the element id and returns true if successful. Note that a
	// consumer can only be attached to a transmitter, and no element can be be
	// attached to it. The tree must not contain more than one generator located at
	// the root. If id does not exist, or there is already element with the same ID
	// as pgn, pgn is not attached, and the method returns false.
	public static boolean attach(GT<PGElem> pg, PGElem pgn, int id) {
		if (pg.empty() || pgn.getType() == ElemType.Generator || find(pg, pgn.getId()) || !find(pg, id)) {
			return false;
		} else {
			find(pg, id);
			if (pg.retrieve().getType() == ElemType.Consumer
					|| (pgn.getType() == ElemType.Consumer && pg.retrieve().getType() != ElemType.Transmitter))
				return false;
			pg.insert(pgn);
			return true;
		}
	}

	// Removes element by ID, all corresponding subtree is removed. If removed, true
	// is returned, false is returned otherwise.
	public static boolean remove(GT<PGElem> pg, int id) {
		if (find(pg, id)) {
			pg.remove();
			return true;
		}
		return false;

	}

	// Computes total power that consumed by a element (and all its subtree). If id
	// is incorrect, -1 is returned.
//	public static double totalPower(GT<PGElem> pg, int id) {
//		if (find(pg, id)) {
//			return totalPower(pg);
//		} else
//			return -1;
//
//	}
//
//	private static double totalPower(GT<PGElem> pg) {
//		if (pg.nbChildren() == 0) {
//			if (pg.retrieve().getType() == ElemType.Consumer) {
//				return pg.retrieve().getPower();
//			} else
//				return 0;
//		}
//		int n = pg.nbChildren();
//		double t = 0;
//		for (int i = 0; i < n; i++) {
//			pg.findChild(n);
//			t += totalPower(pg);
//			pg.findParent();
//		}
//		return t;
//	}

	// Checks if the power grid contains an overload. The method returns the ID of
	// the first element preorder that has an overload and -1 if there is no
	// overload.
	public static int findOverload(GT<PGElem> pg) {
		if (pg.empty()) {
			return -1;
		} else {
			Queue<Integer> cpo = collectPreorder(pg);
			int id = 0;
			while (cpo.length() != 0) {
				id = cpo.serve();
				find(pg, id);
				if (pg.retrieve().getType() != ElemType.Consumer && pg.retrieve().getPower() < totalPower(pg, id)) {
					return id;
				}
			}
			return -1;
		}
	}

// Computes total power that consumed by a element (and all its subtree).
//If id is incorrect, -1 is returned.
	public static double totalPower(GT<PGElem> pg, int id) {
		if (pg == null) {
			return -1;
		}
		if (pg.empty()) {
			return -1;
		}
		boolean found = find(pg, id);
		if (!found)
			return -1;
		return rectotalPower(pg, id);
///////////////////
	}

	private static double rectotalPower(GT<PGElem> pg, int id) {
		if (pg.nbChildren() == 0 && pg.retrieve().getType() == ElemType.Consumer) {
			return pg.retrieve().getPower();
		}
		if (pg.nbChildren() == 0 && pg.retrieve().getType() != ElemType.Consumer) {
			return 0;
		}
		double sum = 0;
		for (int i = 0; i < pg.nbChildren(); i++) {
			pg.findChild(i);
			sum += rectotalPower(pg, id);
			pg.findParent();
		}
		return sum;
	}
}
