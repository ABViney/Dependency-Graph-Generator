package com.cell;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.directional.Direction;
import com.directional.Vec2;
import com.vindig.image.Color;

/**
 * Convert a Vec2 list into a LinkedPath that provides
 * directional and value data of the Path's properties
 * @author viney
 *
 */
public class LinkedPath {
	
	
	public static void linkPaths(AbstractCellComponent[][] sheet, Set<List<Vec2>> pathSet, int newCellWidth, int newCellHeight) {
		for(List<Vec2> path : pathSet) {
			Vec2 coord = path.get(path.size()-1);
			int index = 2;
			
			PathInstance curPath = new PathInstance((NodeCellComponent)sheet[coord.getY()][coord.getX()]);
			
			List<Entry<Direction, List<PathCellComponent>>> vectors = new ArrayList<>();
			
			do {
				Vec2 nextCoord = path.get(path.size()-index++);
				Direction compass = coord.directionTo(nextCoord);
				
				Vec2 incr = Vec2.fromDirection(compass);
				List<PathCellComponent> vector = new ArrayList<>();
				while(!coord.equals(nextCoord)) { // TODO might be worth instantiating a few variables so this is more readable 
					coord = coord.add(incr);
					if(!(sheet[coord.getY()][coord.getX()] instanceof NodeCellComponent)) {
						if(sheet[coord.getY()][coord.getX()] == null) {
							sheet[coord.getY()][coord.getX()] = new PathCellComponent(newCellWidth, newCellHeight);
						} vector.add((PathCellComponent)sheet[coord.getY()][coord.getX()]);
					}
				}
				for(int i = 0; i < vector.size()-1; i++) {
					PathCellComponent prev = vector.get(i);
					PathCellComponent next = vector.get(i+1);
					prev.createBond(next, compass);
				}
				vectors.add(new SimpleEntry<>(compass, vector));
			} while(index < path.size());
			
			int[] assignmentOrder = new int[index];
			boolean pathFound = false;			
			
			PATHFINDER:
			while(true) { // labels reduce redundancy, see if there's a more straightforward way to write this
				
				PATHLOGIC:
				for(int i = 0; i < vectors.size(); i++) { // ensure opposite of first and final entry point can't overlap with this path
					Direction compass = vectors.get(i).getKey();
					System.out.println(compass.toString());
					List<PathCellComponent> vector = vectors.get(i).getValue();
					if(vector.get(0).getPathByDirection(Direction.oppositeOf(compass))[assignmentOrder[i]] != null) {
						assignmentOrder[i--]++;
						continue;
					}
					for(int i2 = 0; i2 < vector.size(); i2++) {
						if(assignmentOrder[i] >= vector.get(0).getPathByDirection(compass).length) {
							System.out.println("measureOccupancy function erroneous");
							break PATHFINDER;
						}
						if(vector.get(i2).getPathByDirection(compass)[assignmentOrder[i]] != null) {
							assignmentOrder[i--]++;
							continue PATHLOGIC;
						}
					}
				} pathFound = true;
				break PATHFINDER;
				
			}
			
			if(pathFound) {
				for(int i = 0; i < vectors.size(); i++) {
					index = 0;
					Entry<Direction, List<PathCellComponent>> vector = vectors.get(i);
					while(index < vector.getValue().size()-1) { // Don't assign from the last cell in the list
						vector.getValue().get(index++).getPathByDirection(vector.getKey())[assignmentOrder[i]] = curPath;
					}
				}
				
				// Assign this path's starting and ending point
				vectors.get(0).getValue().get(0).getPathByDirection(Direction.oppositeOf(vectors.get(0).getKey()))[assignmentOrder[0]] = curPath;
				vectors.get(vectors.size()-1).getValue().get(index).getPathByDirection(vectors.get(vectors.size()-1).getKey())[assignmentOrder[vectors.size()-1]] = curPath;
			} else System.out.println("PathFindingFailed");
		}
//		for(List<Vec2> path : pathSet) {
//			System.out.println("PathSize : " +path.size());
//			System.out.println("Start: " + Arrays.toString(path.get(path.size()-1).getOrigin()) + "\nEnd: " + Arrays.toString(path.get(0).getOrigin()));
//			Vec2 coord = path.get(path.size()-1); // coord is "end" of path. This position denotes the location of the NodeCellComponent this path will socket into
//			int index = 1;
//
//			PathInstance curPath = new PathInstance((NodeCellComponent)sheet[coord.getY()][coord.getX()]); // All NodeCells should be instantiated before this sheet is received
//			
//			
//			List<Entry<Direction, PathInstance[][][]>> vectors = new ArrayList<>();
//			System.out.println("Group");
//			do {
//				Vec2 nextCoord = path.get(path.size()-1 - index++);
//				Direction compass = coord.directionTo(nextCoord);
//				
//				Function<PathCellComponent, PathInstance[][]> getChannels;
//				switch(compass) {
//				case UP,DOWN:
//					getChannels = (pcc -> pcc.verticalScan);
//					break;
//				case LEFT,RIGHT:
//					getChannels = pcc -> pcc.horizontalScan;
//					break;
//				default:
//					getChannels = pcc -> null;
//				}
//				System.out.println("\tValids instantiated");
//				List<PathCellComponent> valids = new LinkedList<>();
//				Vec2 incr = Vec2.fromDirection(compass);
//				
//				System.out.println("Coord: " +Arrays.toString(coord.getOrigin()));
//				System.out.println("NextCoord" +Arrays.toString(nextCoord.getOrigin()));
//				
//				switch(compass) {
//				case UP:
//					System.out.println("UP");
//					break;
//				case DOWN:
//					System.out.println("DOWN");
//					break;
//				case LEFT:
//					System.out.println("LEFT");
//					break;
//				case RIGHT:
//					System.out.println("RIGHT");
//				}
//				
//				while(!coord.equals(nextCoord)) { // Get the PathCellComponents between coord and nextCoord
//					coord = coord.add(incr);
//					System.out.println("Incremented: " + Arrays.toString(coord.getOrigin()));
//					if(!(sheet[coord.getY()][coord.getX()] instanceof NodeCellComponent)) {
//						if(!(sheet[coord.getY()][coord.getX()] instanceof PathCellComponent)) // instantiate pathcomponents as necessary
//							sheet[coord.getY()][coord.getX()] = new PathCellComponent(newCellWidth, newCellHeight, xChannels, yChannels);
//						valids.add((PathCellComponent)sheet[coord.getY()][coord.getX()]);
//					}
//				}
//				vectors.add(new SimpleEntry<>(compass,
//						valids.stream().map(getChannels).toArray(PathInstance[][][]::new)));
//				
//			} while(index < path.size());
//			
//			int[] indexGroup = new int[vectors.size()];
//			
//			PATHFINDER:
//			while(true) {
//				
//				for(int i = 0; i < vectors.size(); i++) {
//					int[] accessOrder;
//					boolean accessToggle = false;
//					int indexLimit = 0;
//					switch(vectors.get(i).getKey()) {
//					case UP, LEFT:
//						accessOrder = new int[] {1, 0};
//						indexLimit = yChannels-1;
//						break;
//					case DOWN, RIGHT:
//						accessOrder = new int[] {0, 1};
//						indexLimit = xChannels-1;
//						break;
//					default:
//						accessOrder = null;
//					}
//					if(accessOrder == null) {
//						System.out.println("Error PATHFINDER");
//						break PATHFINDER;
//					}
//					
//					PathInstance[][][] instances = vectors.get(i).getValue();
//					Arrays.stream(instances).forEach(inst -> System.out.println(inst.length));
//					System.out.println(i + "/" + vectors.size() + " Instances Length: " + instances.length);
//					boolean unobstructed = true;
//					for(int c = 0; c < instances.length-1; c++) { // -1 leave last for next view
//						
//						if(instances[c][((accessToggle = !accessToggle) ? accessOrder[0] : accessOrder[1])][indexGroup[i]] != null) { // accessOrder puts the right array in the right order
//							indexGroup[i]++; //increment current index
//							unobstructed = false; // attempt is obstructed
//							for(int j = i; j < indexGroup.length; j++) {
//								if(indexGroup[i] >= indexLimit) {
//									if(i < indexGroup.length) {
//										indexGroup[i+1]++;
//										indexGroup[i] = 0;
//									} else {
//										System.out.println("Error indexGroup overflow");
//										break PATHFINDER;
//									}
//								}
//							}
//						}
//						if(!unobstructed) continue PATHFINDER;
//					}
//				}
//				
//				for(int i = 0; i < vectors.size(); i++) {
//					int[] accessOrder;
//					boolean accessToggle = false;
//					switch(vectors.get(i).getKey()) {
//					case UP, LEFT:
//						accessOrder = new int[] {1, 0};
//						break;
//					case DOWN, RIGHT:
//						accessOrder = new int[] {0, 1};
//						break;
//					default:
//						accessOrder = null;
//					}
//					if(accessOrder == null) {
//						System.out.println("Error PATHFINDER");
//						break PATHFINDER;
//					}
//					
//					PathInstance[][][] instances = vectors.get(i).getValue();
//					System.out.println(" Length " + instances.length);
//					
//					for(int c = 0; c < instances.length-1; c++) {
//						instances[c][((accessToggle = !accessToggle) ? accessOrder[0] : accessOrder[1])][indexGroup[i]] = curPath;
//					}
//				}
//				break PATHFINDER;
//			}
//		} System.out.println("worked?");
	}

	
	
	/**
	 * Draw reference utilized in incorporating classes.
	 * PathInstance infers its Color property from the parameter NodeCellComponent, and shares
	 * that information with the PathCellComponents that store it.
	 * @author viney
	 *
	 */
	public static class PathInstance {
		
		private Color pathColor;
		
		/**
		 * Initialize and socket a PathInstance to a NodeCellComponent
		 * @param parent
		 */
		public PathInstance(NodeCellComponent parent) {
			parent.socket(this);
			pathColor = parent.getColor();
		}
		
		Color getColor() { return pathColor; }
		byte[] toPixel() { return pathColor.toPixel(); }
		void setColor(Color newColor) { pathColor = newColor; }
	}
	
}
