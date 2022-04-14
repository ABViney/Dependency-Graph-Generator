/**
 * Prob a duplicate of a JDK provided asset.
 * Dual link tree where positions aren't specific on the origin of the tree. Includes an array for adding/removing root(s)
 * based on user settings. Root establishment runs a validation course ensuring that all nodes in the tree are accessible via recursive
 * calls originating from the root.
 * 
 * TODO add linear id increment counter that can decrement necessary elements to allow runtime removal of objects from tree
 * 
 */
package UnusedAssets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Relationship based collection allowing the ability to denote multiple origins and establish
 * relationship links between multiple positions.
 * 
 * @author viney
 *
 * @param <E>
 */
public class DualLinkTree<E> {
	
	private List<Pos<E>> roots;
	private List<Pos<E>> branches;
	
	/**
	 * Positional object containing itself and references to adjacent members.
	 * 
	 * @author viney
	 *
	 * @param <E>
	 */
	public static class Pos<E> {
		private E ref;
		protected int id;
		private Set<Pos<E>> adjacents;
		
		private Pos(E ref, int id) {
			this.ref = ref;
			this.id = id;
			adjacents = new LinkedHashSet<>();
		}
		private Pos(E ref, int id, @SuppressWarnings("unchecked") Pos<E>... adjacents) {
			this(ref, id);
			this.adjacents.addAll(Arrays.asList(adjacents));
			for(Pos<E> a : adjacents) {
				a.adjacents.add(this);
			}
		}
		
		private void remove() {
			for(Pos<E> a : adjacents) a.depose(this);
			adjacents = null;
		}
		private void depose(Pos<E> o) { adjacents.remove(o); }
		private void decrement() { id--; }
		
		/** Public Methods */
		public E value() { return ref; }
		public List<Pos<E>> connectedTo() { return new ArrayList<>(adjacents); }
		
		@SafeVarargs
		public final void link(Pos<E>... adjacents) {
			for(Pos<E> a : adjacents) {
				this.adjacents.add(a);
				a.link(this);
			}
		}
	}
	
	public DualLinkTree() {
		roots = new ArrayList<>();
		branches = new ArrayList<>();
	}
	
	public int size() { return branches.size(); }
	
	/**
	 * Add an object to this tree.
	 * Object can be added to the tree even without a relation to other branches of the tree.
	 * @param ref
	 * @param adjacents(Optional) adjacent branches this position will have.
	 * @return
	 */
	public synchronized Pos<E> add(E ref) {
		Pos<E> branch = new Pos<E>(ref, branches.size());
		branches.add(branch);
		return branch;
	}
	@SafeVarargs
	public final synchronized Pos<E> add(E ref, Pos<E>... adjacents) {
		Pos<E> branch = new Pos<E>(ref, branches.size(), adjacents);
		branches.add(branch);
		return branch;
	}
	/**
	 * Returns the first root in the roots collection.
	 * @return Pos<E>
	 */
	public final Pos<E> getRoot() { return roots.get(0); }
	/**
	 * Returns the roots collection
	 * @return List<Pos<E>>
	 */
	public final List<Pos<E>> getRoots() { return roots; }
	
	/**
	 * Assign Position(s) as root(s)
	 * @param newRoots
	 */
	@SafeVarargs
	public final void putRoot(Pos<E>... newRoots) {
		roots.addAll(Arrays.asList(newRoots));
	}
	
	/**
	 * Remove Position(s) as root(s)
	 * @param oldRoots
	 */
	@SafeVarargs
	public final void pullRoot(Pos<E>... oldRoots) {
		for(Pos<E> r : oldRoots) roots.remove(r);
	}
	
	/**
	 * Clear the roots list
	 */
	public void clearRoots() { roots = new ArrayList<>(); }
	
	/**
	 * Returns a copy of the branches collection
	 * @return
	 */
	public List<Pos<E>> getBranches() {
		return List.copyOf(branches);
	}
	
	/**
	 * Link a position with all following positions.
	 * @param base
	 * @param targets
	 */
	@SafeVarargs
	public final void link(Pos<E> base, Pos<E>... targets) { base.link(targets); }
	
	/**
	 * Remove a Position from the tree. All references to this position internally are removed, the object still existing.
	 * Positions adjacent to this one are unaffected, as this collection is not directional. 
	 * @param branch
	 */
	public synchronized void remove(Pos<E> branch) {
		int gap = branch.id;
		branches.remove(branch);
		if(roots.contains(branch)) roots.remove(branch);
		branch.remove();
		for(int i = gap; i < branches.size(); i++) branches.get(i).decrement();
	}
	
}
