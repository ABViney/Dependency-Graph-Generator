package com.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Graph collection type built off a positional tree structure.
 * 
 * Collection of Positions that link to one another by description of
 * "dependency" and "dependent"
 * 
 * Collection can contain positions that have no established connections to
 * any other positions.
 * 
 * Enables definition of root via Predicate, or inferring of roots via an iterative
 * check of all positions for those that contain no dependents.
 * 
 * @author viney
 *
 * @param <E>
 */
public class DependencyTree<E> {
	
	/** Reference map for obtaining the relevant position for the specified reference, and vice versa */
	private Map<E, Pos> top;
	private Map<Pos, E> bot;
	
	/**
	 * Positional element class.
	 * Object existence acts as a comparison gateway for this Position to the relevant resource.
	 * 
	 * @author viney
	 *
	 */
	public static class Pos { // Unstatic this if static
		
		/** Positions this instance points to */
		private Set<Pos> dependsOn;
		/** Positions this instance is pointed to by */
		private Set<Pos> dependedBy;
		
		private Pos() {
			dependsOn = new LinkedHashSet<>();
			dependedBy = new LinkedHashSet<>();
		}
		
		/**
		 * Direct this Position as a dependant of another Position
		 * @param d
		 */
		public void addDependency(Pos d) { 
			dependsOn.add(d);
			d.addDependent(this);
		}
		private void addDependent(Pos d) {
			dependedBy.add(d);
		}
		
		/**
		 * Remove all references to this instance in linked Positions
		 * @return this dereferenced Position
		 */
		private Pos dereference() {
			dependsOn.forEach(p -> p.dependedBy.remove(this));
			dependedBy.forEach(p -> dependsOn.remove(this));
			return this;
		}
		
		/**
		 * Get the list of all Positions this Position is dependent on.
		 * @return List<Pos>
		 */
		public List<Pos> getDependencies() { return new ArrayList<>(dependsOn); }
		public List<Pos> getDependents() { return new ArrayList<>(dependedBy); }
	}
	
	/**
	 * Instantiates an empty DependencyTree
	 */
	public DependencyTree() {
		top = new HashMap<>();
		bot = new HashMap<>();
	}
	
	public DependencyTree(Collection<? extends E> collection) {
		this();
		collection.stream().forEach(i -> newEntry(i));
	}
	
	/**
	 * Create a copy of a DependencyTree.
	 * Copy contains reference to type definition but all Node structures are new.
	 * New tree keeps the relationship structure the argument tree held.
	 * @param dt
	 */
	public DependencyTree(DependencyTree<? extends E> dt) {
		this();
		for(Pos op : dt.bot.keySet()) newEntry(dt.get(op));
		for(Pos op : dt.bot.keySet()) {
			E item = dt.get(op); 
			for(Pos branch : op.getDependencies()) {
				E otherItem = dt.get(branch);
				top.get(item).addDependency(top.get(otherItem));
			}
		}
	}
	
	
	/**
	 * Put a new reference into this tree.
	 * 
	 * @param ref
	 * @return
	 */
	public Pos put(E ref) {
		return newEntry(ref);
	}
	
	/**
	 * Put a collection of references into this tree.
	 * 
	 * @param list
	 */
	public void putAll(Collection<? extends E> collection) {
		collection.stream().forEach(e -> newEntry(e));
	}
	
	/**
	 * Get the Position of this item in this Tree.
	 * 
	 * @param ref - E
	 * @return Pos -- null if doesn't exist
	 */
	public Pos get(E ref) {
		if(!top.containsKey(ref)) return null;
		return top.get(ref);
	}
	
	/**
	 * Get the reference element attached to this Position.
	 * 
	 * @param ref - Pos
	 * @return
	 */
	public E get(Pos ref) { //TODO add exception throw if Position is not of this tree.
		return bot.get(ref);
	}
	
	/**
	 * Create a new Pos element and map it to the argument.
	 * 
	 * @param ref - E
	 * @return
	 */
	private Pos newEntry(E ref) {
		Pos newPos = new Pos();
		top.put(ref, newPos);
		bot.put(newPos, ref);
		return newPos;
	}
	
	public void link(Pos a, Pos b) { a.addDependency(b); }
	
	/**
	 * Remove if it exists all references to this Position from this
	 * DependencyTree.
	 * @param ref - Pos
	 */
	public void remove(Pos ref) {
		if(bot.containsKey(ref)) {
			top.remove(bot.remove(ref)).dereference();
		}
	}
	/**
	 * Remove if it exists all references to the Position associated with this reference
	 * from this DependencyTree.
	 * @param ref - E
	 */
	public void remove(E ref) {
		if(top.containsKey(ref)) {
			bot.remove(top.remove(ref).dereference());
		}
	}
	
	/**
	 * Find roots of this collection by iterating through all Positions generated through this tree
	 * and evaluating if the Position is referred to by another.
	 * Positions with no dependents are added to the collection to be returned as roots.
	 * 
	 * @return List<Pos>
	 */
	public List<Pos> findRoots() {
		List<Pos> roots = new ArrayList<>();
		Pos[] allLeaves = bot.keySet().toArray(new Pos[bot.size()]);
		boolean[] hasRef = new boolean[allLeaves.length];
		for(int i = 0; i < allLeaves.length; i++) {
			if(allLeaves[i].dependedBy.size() > 0) hasRef[i] = true;
		}
		for(int i = 0; i < hasRef.length; i++) {
			if(!hasRef[i]) roots.add(allLeaves[i]);
		} return roots;
	}
	
	public Pos findRoot(Predicate<? super E> filter) {
		List<Pos> result = top.keySet().stream().filter(filter).map(p -> top.get(p)).toList();
		if(result.size() != 1) throw new IllegalArgumentException("Could not find root.");
		return result.get(0);
	}

	public int size() {
		return top.size();
	}
}
