package com.cell;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

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
	
	
	public LinkedPath(AbstractCellComponent[][] sheet, Set<List<Vec2>> pathSet, int newCellWidth, int newCellHeight, int xChannels, int yChannels) {
		for(List<Vec2> path : pathSet) {
			
			Vec2 coord = path.get(path.size()-1); // coord is "end" of path. This position denotes the location of the NodeCellComponent this path will socket into
			Vec2 nextCoord = path.get(path.size()-2); // The amount of Vec2's in any path is at minimum 2
			int index = 3;

			PathInstance curPath = new PathInstance((NodeCellComponent)sheet[coord.getY()][coord.getX()]); // All NodeCells should be instantiated before this sheet is recieved
			
			Direction compass = coord.directionTo(nextCoord);
			
			PathCellComponent lastPathCell;
			List<Entry<Direction, List<PathInstance[][]>>> vectors = new ArrayList<>();
			
			Function<PathCellComponent, PathInstance[][]> getChannels;
			
			switch(compass) {
			case UP, DOWN:
				getChannels = pcc -> pcc.verticalScan;
			case LEFT, RIGHT:
				getChannels = pcc -> pcc.horizontalScan;
			default:
				getChannels = pcc -> null;
			}
			
			do {
				Vec2 incr = Vec2.fromDirection(compass);
				List<PathInstance[]> vector = new ArrayList<>();
				List<PathCellComponent> valids = new LinkedList<>();
				while(!coord.equals(nextCoord)) { // Get the PathCellComponents between coord and nextCoord
					coord = coord.add(incr);
					if(!(sheet[coord.getY()][coord.getX()] instanceof NodeCellComponent)) {
						if(!(sheet[coord.getY()][coord.getX()] instanceof PathCellComponent)) // add pathcomponents as necessary
							sheet[coord.getY()][coord.getX()] = new PathCellComponent(newCellWidth, newCellHeight, xChannels, yChannels);
						valids.add((PathCellComponent)sheet[coord.getY()][coord.getX()]);
					}
				}
				
				
				vectors.add(new SimpleEntry<>(compass, valids.stream().map(getChannels).toList()));
				
				nextCoord = path.get(path.size() - index++);
				compass = coord.directionTo(nextCoord);
			} while(index < path.size());
			
			
			boolean pathFound = false;
			int startingOffset = 0;
			while(!pathFound) {
				
				Direction lastCompass = vectors.get(0).getKey();
				PathInstance[][] lastChannel = null;
				for(int i = 0; i < vectors.size(); i++) {
					int cd = 0; // Channel Descriptor (x, y)
					switch(lastCompass) {
					case UP,DOWN:
						cd = yChannels;
						break;
					case LEFT,RIGHT:
						cd = xChannels;
						break;
					}
					
					List<PathInstance[][]> valRef = vectors.get(i).getValue();
					
					PathInstance[][][] channelBundle = valRef.toArray(new PathInstance[vectors.get(i).getValue().get(0).length][2][]);
				}
				
			}
			
			
			
			
			
//			int index = path.size() - 1;
//			Vec2 coord = path.get(index--);
//			
//			PathInstance pi = new PathInstance((NodeCellComponent)(sheet[coord.getY()][coord.getX()]));
//			
//			List<Entry<Direction, List<PathCellComponent>>> flow = new ArrayList<>();
//			
//			do {
//				Vec2 nextCoord = path.get(index--);
//				List<PathCellComponent> vector = new ArrayList<>();
//				Direction compass;
//				int[] curPos = coord.getOrigin();
//				int[] endPos = nextCoord.getOrigin();
//				int[] mod;
//				
//				switch(coord.directionTo(nextCoord)) {
//				case UP:
//					compass = Direction.UP;
//					mod = new int[] {0, -1};
//					break;
//				case DOWN:
//					compass = Direction.DOWN;
//					mod = new int[] {0, 1};
//					break;
//				case LEFT:
//					compass = Direction.LEFT;
//					mod = new int[] {0, -1};
//					break;
//				case RIGHT:
//					compass = Direction.RIGHT;
//					mod = new int[] {0, 1};
//					break;
//				default:
//					System.out.println("Something went wrong finding the next direction");
//					return;
//				}
//				
//				while(curPos[0] != endPos[0] && curPos[1] != endPos[1]) {
//					if(!(sheet[curPos[1]][curPos[0]] instanceof NodeCellComponent)) {
//						if(!(sheet[curPos[1]][curPos[0]] instanceof PathCellComponent)) {
//							sheet[curPos[1]][curPos[0]] = new PathCellComponent(newCellWidth, newCellHeight, xChannels, yChannels);
//						}
//						vector.add((PathCellComponent)(sheet[curPos[1]][curPos[0]]));
//					}
//					curPos[0] += mod[0];
//					curPos[1] += mod[1];
//				} 
//				
//				coord = nextCoord;
//				flow.add(new SimpleEntry<>(compass, vector));
//			} while(index >= 0);
//			
//			/**
//			 * Above logic should result an instruction and traversal compilation
//			 * Following logic must traverse the PathCell along relevant axis and
//			 * infer the non occupied lane of travel to inhabit.
//			 */
//			
//			for(Entry<Direction, List<PathCellComponent>> instruction : flow) {
//				//TODO trying impl without making multiple collections
//			}
			
			
			
		}
	}
	
	/**
	 * Draw reference utilized in incorporating classes.
	 * PathInstance infers its Color property from the parameter NodeCellComponent, and shares
	 * that information with the PathCellComponents that store it.
	 * @author viney
	 *
	 */
	class PathInstance {
		
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
