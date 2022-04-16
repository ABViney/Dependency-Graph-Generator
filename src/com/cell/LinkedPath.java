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
	
	
	public static void linkPaths(AbstractCellComponent[][] sheet, Set<List<Vec2>> pathSet, int newCellWidth, int newCellHeight, int xChannels, int yChannels) {
//		System.out.println("Group");
//		for(List<Vec2> vector : pathSet) {
//			System.out.println("\tVector");
//			for(Vec2 v : vector) {
//				System.out.println("\t\t"+Arrays.toString(v.getOrigin()));
//			}
//			
//		}
		for(List<Vec2> path : pathSet) {
			System.out.println("PathSize : " +path.size());
			System.out.println("Start: " + Arrays.toString(path.get(path.size()-1).getOrigin()) + "\nEnd: " + Arrays.toString(path.get(0).getOrigin()));
			Vec2 coord = path.get(path.size()-1); // coord is "end" of path. This position denotes the location of the NodeCellComponent this path will socket into
			int index = 1;

			PathInstance curPath = new PathInstance((NodeCellComponent)sheet[coord.getY()][coord.getX()]); // All NodeCells should be instantiated before this sheet is received
			
			
			List<Entry<Direction, PathInstance[][][]>> vectors = new ArrayList<>();
			System.out.println("Group");
			do {
				Vec2 nextCoord = path.get(path.size()-1 - index++);
				Direction compass = coord.directionTo(nextCoord);
				
				Function<PathCellComponent, PathInstance[][]> getChannels;
				switch(compass) {
				case UP,DOWN:
					getChannels = (pcc -> pcc.verticalScan);
					break;
				case LEFT,RIGHT:
					getChannels = pcc -> pcc.horizontalScan;
					break;
				default:
					getChannels = pcc -> null;
				}
				System.out.println("\tValids instantiated");
				List<PathCellComponent> valids = new LinkedList<>();
				Vec2 incr = Vec2.fromDirection(compass);
				
				System.out.println("Coord: " +Arrays.toString(coord.getOrigin()));
				System.out.println("NextCoord" +Arrays.toString(nextCoord.getOrigin()));
				
				switch(compass) {
				case UP:
					System.out.println("UP");
					break;
				case DOWN:
					System.out.println("DOWN");
					break;
				case LEFT:
					System.out.println("LEFT");
					break;
				case RIGHT:
					System.out.println("RIGHT");
				}
				
				while(!coord.equals(nextCoord)) { // Get the PathCellComponents between coord and nextCoord
					coord = coord.add(incr);
					System.out.println("Incremented: " + Arrays.toString(coord.getOrigin()));
					if(!(sheet[coord.getY()][coord.getX()] instanceof NodeCellComponent)) {
						if(!(sheet[coord.getY()][coord.getX()] instanceof PathCellComponent)) // instantiate pathcomponents as necessary
							sheet[coord.getY()][coord.getX()] = new PathCellComponent(newCellWidth, newCellHeight, xChannels, yChannels);
						valids.add((PathCellComponent)sheet[coord.getY()][coord.getX()]);
					}
				}
				vectors.add(new SimpleEntry<>(compass,
						valids.stream().map(getChannels).toArray(PathInstance[][][]::new)));
				
			} while(index < path.size());
			
			int[] indexGroup = new int[vectors.size()];
			
			PATHFINDER:
			while(true) {
				
				for(int i = 0; i < vectors.size(); i++) {
					int[] accessOrder;
					boolean accessToggle = false;
					int indexLimit = 0;
					switch(vectors.get(i).getKey()) {
					case UP, LEFT:
						accessOrder = new int[] {1, 0};
						indexLimit = yChannels-1;
						break;
					case DOWN, RIGHT:
						accessOrder = new int[] {0, 1};
						indexLimit = xChannels-1;
						break;
					default:
						accessOrder = null;
					}
					if(accessOrder == null) {
						System.out.println("Error PATHFINDER");
						break PATHFINDER;
					}
					
					PathInstance[][][] instances = vectors.get(i).getValue();
					Arrays.stream(instances).forEach(inst -> System.out.println(inst.length));
					System.out.println(i + "/" + vectors.size() + " Instances Length: " + instances.length);
					boolean unobstructed = true;
					for(int c = 0; c < instances.length-1; c++) { // -1 leave last for next view
						
						if(instances[c][((accessToggle = !accessToggle) ? accessOrder[0] : accessOrder[1])][indexGroup[i]] != null) { // accessOrder puts the right array in the right order
							indexGroup[i]++; //increment current index
							unobstructed = false; // attempt is obstructed
							for(int j = i; j < indexGroup.length; j++) {
								if(indexGroup[i] >= indexLimit) {
									if(i < indexGroup.length) {
										indexGroup[i+1]++;
										indexGroup[i] = 0;
									} else {
										System.out.println("Error indexGroup overflow");
										break PATHFINDER;
									}
								}
							}
						}
						if(!unobstructed) continue PATHFINDER;
					}
				}
				
				for(int i = 0; i < vectors.size(); i++) {
					int[] accessOrder;
					boolean accessToggle = false;
					switch(vectors.get(i).getKey()) {
					case UP, LEFT:
						accessOrder = new int[] {1, 0};
						break;
					case DOWN, RIGHT:
						accessOrder = new int[] {0, 1};
						break;
					default:
						accessOrder = null;
					}
					if(accessOrder == null) {
						System.out.println("Error PATHFINDER");
						break PATHFINDER;
					}
					
					PathInstance[][][] instances = vectors.get(i).getValue();
					System.out.println(" Length " + instances.length);
					
					for(int c = 0; c < instances.length-1; c++) {
						instances[c][((accessToggle = !accessToggle) ? accessOrder[0] : accessOrder[1])][indexGroup[i]] = curPath;
					}
				}
				break PATHFINDER;
			}
		} System.out.println("worked?");
	}
	
	
	/**
	 * Draw reference utilized in incorporating classes.
	 * PathInstance infers its Color property from the parameter NodeCellComponent, and shares
	 * that information with the PathCellComponents that store it.
	 * @author viney
	 *
	 */
	static class PathInstance {
		
		private Color pathColor;
		
		/**
		 * Initialize and socket a PathInstance to a NodeCellComponent
		 * @param parent
		 */
		PathInstance(NodeCellComponent parent) {
			parent.socket(this);
			pathColor = parent.getColor();
		}
		
		Color getColor() { return pathColor; }
		void setColor(Color newColor) { pathColor = newColor; }
	}
	
}
