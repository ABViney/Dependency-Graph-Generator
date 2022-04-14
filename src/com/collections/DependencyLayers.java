package com.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.collections.DependencyTree.Pos;

/**
 * Define layers in a DependencyTree if possible.
 * Upon successful generation of DependencyLayers object this instance
 * can operate in tandem with graph generation of DependencyTree to assist
 * Node placement and organization.
 * 
 * Until further information can contradict -- Instances of DependencyLayers will define topLayer as
 * a single entity in all provided arguments. A list of roots can be passed, but each root will have their own LayerSet linked to them.
 * 
 * 
 * @author viney
 *
 */
public class DependencyLayers { //TODO make aside layer that can be interpretted seperate from the main stack of a root
								// TODO also rectactor out rootMap and List<Pos> requirements. 
	
	private LayerSet topLayer;
	private LayerSet aside = new LayerSet();
	private int depth = 1;
	
	public DependencyLayers(Pos root) {
		if(root == null || root.getDependencies().size() == 0) return;
		generateLayerHierarchy(root);
	}
	
	public DependencyLayers(Pos root, List<Pos> asideContent) {
		if(root == null || root.getDependencies().size() == 0) return;
		aside.addAll(asideContent);
		generateLayerHierarchy(root);
	}
	
	/**
	 * Get an iterator of the LayerSets created from reading the passed Position
	 * @param root
	 * @return Iterator<LayerSet> or null if root not found
	 */
	public Iterator<LayerSet> iterator() {
		if(topLayer != null) {
			return new LayerIterator(topLayer);
		} return null;
	}
	
	public Iterator<LayerSet> reverseIterator() {
		return new ReverseLayerIterator(iterator());
	}
	
	/**
	 * Get the total number of elements in this collection
	 * @return int - Total Layers 
	 */
	public int sizeOf() {
		Iterator<LayerSet> itr = iterator();
		int eleCtr = 0;
		while(itr.hasNext()) {
			eleCtr += itr.next().getSet().size();
		} return eleCtr + aside.getSet().size();
	}
	
	public int stackDepth() { return depth; }
	
	/**
	 * Get the maximum width a LayerSet has (exclusive of undefined LayerSet)
	 * @return int Size of largest defined LayerSet's set
	 */
	public int stackWidth() {
		Iterator<LayerSet> itr = iterator();
		int maxWidth = 0;
		while(itr.hasNext()) {
			int comp = itr.next().getSet().size();
			if(maxWidth < comp) maxWidth = comp;
		} return maxWidth;
	}
	
	/**
	 * Get the loose layer in this DependencyLayers
	 * @return Layerset - or null if aside not defined
	 */
	public LayerSet getAside() { return aside; }
	
	/**
	 * Creates a LayerSet chain representing the hierarchy of all Positions in the DependencyTree.
	 * If the aside layer is instantiated with positions, those will be ignored in the determining the
	 * hierarchy of the passed tree.
	 * @param root
	 */
	private void generateLayerHierarchy(Pos root) {
		LayerSet curLayer = new LayerSet(root);
		topLayer = curLayer;
		Set<Pos> spent = new LinkedHashSet<>(aside.getSet());
		
		Predicate<Pos> itrFilter = c -> !spent.contains(c);
		Set<Pos> nextItr = new LinkedHashSet<>(root.getDependencies().stream().filter(itrFilter).toList());
		spent.add(root);
		while(!nextItr.isEmpty()) {
			Set<Pos> curItr = nextItr;
			nextItr = new LinkedHashSet<>();
			curLayer = new LayerSet(curLayer, curItr);
			depth++;
			for(Pos p : curItr) {
				nextItr.addAll(p.getDependencies().stream().filter(itrFilter).toList());
				spent.addAll(curItr);
			}
		}
	}
	
	/**
	 * Stack styled Node system to construct an estimated representation of the hierarchical
	 * structure of the Positions in a DependencyTree
	 * 
	 * @author viney
	 *
	 */
	public static class LayerSet {
		private LayerSet parent;
		private LayerSet child = null;
		
		private Set<Pos> thisSet;
		
		/* Constructor Block */
		/**
		 * Create an empty LayerSet with no parent
		 */
		private LayerSet() {
			this.parent = null;
			thisSet = new LinkedHashSet<>();
		}
		
		/**
		 * Create a LayerSet of one entity with no parent
		 * @param p
		 */
		private LayerSet(Pos p) {
			this();
			thisSet.add(p);
		}
		
		/**
		 * Create an empty, child LayerSet
		 * @param parent - null or LayerSet
		 */
		private LayerSet(LayerSet parent) {
			this.parent = parent;
			parent.child = this;
			thisSet = new LinkedHashSet<>();
		}
		
		/**
		 * Create a child LayerSet comprised of the passed collection.
		 * @param p
		 * @param parent
		 */
		private LayerSet(LayerSet parent, Collection<? extends Pos> p) {
			this(parent);
			thisSet.addAll(p);
		}
		/* End Constructors */
		
		/**
		 * Add position to this layer, removing references to this position
		 * in above layers if they exist.
		 * @param p
		 */
		public void add(Pos p) { 
			downgrade(p);
			thisSet.add(p);
		}
		
		/**
		 * Add all positions in this collection to this layer, removing references
		 * to them in above layers if they exist.
		 * @param collection
		 */
		public void addAll(Collection<? extends Pos> collection) { 
			for(Pos p : collection) downgrade(p);
			thisSet.addAll(collection);
		}
		
		/**
		 * Get collection of positions on this layer
		 * @return - LinkedHashSet<Pos>
		 */
		public Set<Pos> getSet() { return thisSet; }
		
		/**
		 * Get the layer before this layer
		 * @return
		 */
		public LayerSet up() { return parent; }
		/**
		 * Get the layer after this layer
		 * @return
		 */
		public LayerSet down() { return child; }
		
		/**
		 * Remove a reference to the argument in this instance's set.
		 * Instruct this instance's parent to perform the same operation
		 * if it exists.
		 * @param sub
		 */
		private void downgrade(Pos sub) {
			thisSet.remove(sub);
			if(parent != null) parent.downgrade(sub);
		}
		
	}
	
	private class LayerIterator implements Iterator<LayerSet> {
		
		private LayerSet cursor;
		
		private LayerIterator(LayerSet topLayer) {
			cursor = topLayer;
		}
		
		@Override
		public boolean hasNext() {
			return cursor != null;
		}

		@Override
		public LayerSet next() {
			LayerSet result = cursor;
			cursor = cursor.down();
			return result;
		}
	}
	
	private class ReverseLayerIterator implements Iterator<LayerSet> {
		
		private LayerSet cursor;
		
		private ReverseLayerIterator(Iterator<LayerSet> itr) {
			while(itr.hasNext()) {
				cursor = itr.next();
			}
		}
		
		@Override
		public boolean hasNext() {
			return cursor != null;
		}

		@Override
		public LayerSet next() {
			LayerSet result = cursor;
			cursor = cursor.up();
			return result;
		}
		
	}
	
}
