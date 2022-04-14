package com.vindig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import com.collections.DependencyLayers;
import com.collections.DependencyLayers.LayerSet;
import com.collections.DependencyTree.Pos;
import com.directional.Vec2;

/**
 * {
 * 	[ ][ ][ ][ ][ ][ ][ ][X]
 * 	[X][ ][ ][ ][ ][ ][ ][ ]
 * 	[X][ ][ ][ ][ ][X][X][X]
 * 	[X][ ][ ][ ][ ][ ][ ][|]
 * 	[X][ ][ ][X][X][X][X][X]
 * 	[X][ ][ ][ ][ ][ ][ ][ ]
 * 	[ ][ ][ ][ ][ ][X][X][X]
 *  [ ][ ][ ][ ][ ][ ][ ][ ]
 * }
 * Class generates a 2d array of minimal size to encompass
 * all defined layer connections (right side) and undefined
 * components (left side).
 * 
 * Size of this structure will be {
 * 		width 	= 	max layer width + 3
 * 		height 	= 	max of (layerCount * 2 - 1) or (undefinedComponentCount + 2)
 * 	}
 * 
 * Class produces a collection of vector groups to denote lines mapping their 
 * 
 * 
 * @author viney
 *
 */
public class TBoxPathing {
	
	Pos[][] field;
	Map<Pos, Set<List<Vec2>>> pathMap = new HashMap<>();
	
	/**
	 * TBoxPathing resolves the initial blocking of graph components and designates a pathing suite for
	 * each component to all referenced components in the same collection.
	 * Tools generated from instantiation include --
	 * Pos[][] field denoting relative location of Pos elements in a 2d collection
	 * Map<Pos, Set<List<Vec2>> pathMap collecting all pathings from a Pos to it's dependencies.
	 * 
	 * @param dl
	 * @return
	 */
	public TBoxPathing(DependencyLayers dl) {
		boolean hasAside = !dl.getAside().getSet().isEmpty();
		int stackMax = dl.stackWidth();
		int xMax = stackMax + (hasAside ? 3 : 1);
		int yMax = dl.getAside().getSet().size()+2 >= dl.stackDepth()*2 ? dl.getAside().getSet().size()+2 : dl.stackDepth()*2;
		field = new Pos[yMax][xMax];
		Vec2[][] vecRef = new Vec2[yMax][xMax];
		for(int y = 0; y < yMax; y++) {
			for(int x = 0; x < xMax; x++) vecRef[y][x] = new Vec2(x, y); //TODO verify reference collections provide memory optimization in this path generation
		}
		Map<Pos, Vec2> posMap = new HashMap<>();
		//Logic
		
		//Set position of aside layer if exists
		if(hasAside) { // map aside pos' vec2s
			int y = 1;
			for(Pos aside : dl.getAside().getSet()) {
				field[y][0] = aside;
				posMap.put(aside, vecRef[y++][0]);
			}
		}
		//Set position of layers ordered from top
		Iterator<LayerSet> itr = dl.iterator(); // map layered pos' vec2s
		for(int y = 0; itr.hasNext(); y+=2) {
			Iterator<Pos> itr2 = itr.next().getSet().iterator();
			int x = 1;
			while(itr2.hasNext()) {
				Pos p = itr2.next();
				field[y][xMax-x] = p;
				posMap.put(p, vecRef[y][xMax-x]);
				x++;
			}
		}
		
		boolean isAside = false;
		boolean sameLayer = false;
		BiPredicate<Integer[], Integer[]> adjacentSeqBreak = new BiPredicate<>() { // evalute if pathing from start to end is a single vector
					@Override
					public boolean test(Integer[] start, Integer[] end) {
						if(start[1] == end[1]) { // same row
							if(end[0] == 0) { // end is aside
								if(field[start[1]][start[0]-1] == null) return true; // element to the left of start is empty
								return false;
							}
//							int offsetX = end[0]-start[0]; // x difference between end and start
//							if(offsetX*offsetX == 1) return true; // start and end are adjacent -- Blocked as keying between two x-axis adjacent nodes not currently possible
							return false;
						}
						if(start[0] == end[0]) {
							int offsetY = end[1] - start[1];
							for(int y = start[1]+(offsetY > 0 ? 1 : -1); y != end[1]; y+=(offsetY > 0 ? 1 : -1)) {// path vertical start-end
								if(field[y][start[0]] != null) return false;// path obstructed
							} return true; // path unobstructed
						} return false; //unqualified
					}
				};
		for(Pos cursor : posMap.keySet()) { //System.out.println("Mapping " + depCount++ + "/" + posMap.size()); // for every Pos with a vec2, map that pos to it's dependants
			int[] start = posMap.get(cursor).getOrigin();
			if(start[0] == 0) continue; // Don't seek from asides
			Set<List<Vec2>> pathSet = new HashSet<>();
			for(Pos d : cursor.getDependencies()) {
				int[] end = posMap.get(d).getOrigin(); // [0] = x, [1] = y
				isAside = end[0] == 0;
				sameLayer = !isAside && start[1] == end[1];
				boolean isAdjacent = adjacentSeqBreak.test(Arrays.stream(start).boxed().toArray(Integer[]::new), Arrays.stream(end).boxed().toArray(Integer[]::new)); // thanks object oriented programming
				List<Vec2> path = new ArrayList<>();
				
				int x = start[0];
				int y = start[1];
				path.add(vecRef[y][x]);// Starting point 
				if(isAdjacent) { // 2 points
					path.add(vecRef[end[1]][end[0]]);
					pathSet.add(path);
					continue;
				}
				path.add(vecRef[++y][x]);// Takeoff point
				if(sameLayer) { // 4 points
					path.add(vecRef[y--][x=end[0]]);
					path.add(vecRef[y][x]);
					pathSet.add(path);
					continue;
				}
				if(isAside) {// 3 or 5 points
					if(end[1] == y) { // end is adjacent to Takeoff point
						path.add(vecRef[y][end[0]]);
						pathSet.add(path);
						continue;
					}
					path.add(vecRef[y][x=1]);
					path.add(vecRef[y=end[1]][x]);
					path.add(vecRef[y][0]);
					pathSet.add(path);
					continue;
				}
				if(x > end[0]) x = end[0]; // shortcut x to first adjacent or unobstructed coordinate
				boolean routeFound = true;
				for(int x2 = x; x2 > (hasAside ? -1 : 1); x2--) {
					for(int y2 = y; y2 != end[1]; y2+=end[1]-start[1] > 0 ? 1 : -1) { /** Warning */// Loop has gotten stuck here a few times. Innovate for a clearer code stack
						if(field[y2][x2] != null) { // collision with non target pos
							routeFound = false;
							break;
						}
					}
					if(routeFound) {
						path.add(vecRef[y][x=x2]);
						break;
					} routeFound = true;
				}
				if(end[0] == x) {
					path.add(vecRef[end[1]][x]);
					continue;
				}
				path.add(vecRef[y=end[1]-1][x]);
				path.add(vecRef[y++][x=end[0]]);
				path.add(vecRef[y][x]);
				pathSet.add(path); // Final route
			}
			pathMap.put(cursor, pathSet);
		}
		
	}
	
	/**
	 * Creates a copy of the 2D Pos array representing the graph structure.
	 * @return copyOf( Pos[][] field )
	 */
	public Pos[][] getField() { 
		Pos[][] fieldCopy = new Pos[field.length][field[0].length];
		for(int i = 0; i < fieldCopy.length; i++) {
		  Pos[] layer = field[i];
		  int   aLength = layer.length;
		  fieldCopy[i] = new Pos[layer.length];
		  System.arraycopy(field[i], 0, fieldCopy[i], 0, aLength);
		} return fieldCopy;
	}
	
	public int fieldWidth() { return field[0].length; }
	public int fieldHeight() { return field.length; }
	
	/**
	 * Gets the x and y axis intersections of every path generated in this instance.
	 * @return int[] {xOverlaps, yOverlaps }
	 */
	public int[] measureOccupancy() {
		int[][] xOverlaps = new int[field.length][field[0].length];
		int[][] yOverlaps = new int[field.length][field[0].length];
		
		for(Set<List<Vec2>> pathSet : pathMap.values()) { 
			for(List<Vec2> path : pathSet) {
				Vec2 start = path.get(0);
				int x = start.getX();
				int y = start.getY();
				
				for(int i = 1; i < path.size(); i++) {
					Vec2 point = path.get(i);
					int nextX = point.getX();
					int nextY = point.getY();
					
					if(x != nextX) {
						int incr = (x < nextX) ? 1 : -1;
						while(x != nextX) xOverlaps[y][x+=incr]++;
					}
					if(y != nextY) {
						int incr = (y < nextY) ? 1 : -1;
						while(y != nextY) yOverlaps[y+=incr][x]++;
					}
				}
			}
		}
		
		int xMaxCollisions = Arrays.stream(xOverlaps).flatMapToInt(Arrays::stream).max().getAsInt();
		int yMaxCollisions = Arrays.stream(yOverlaps).flatMapToInt(Arrays::stream).max().getAsInt();
		
		return new int[] {xMaxCollisions, yMaxCollisions};
		
	}
	
	public Map<Pos, Set<List<Vec2>>> getPathMap() { return pathMap;	}
	
}
