package com.cell;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

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
	
	/**
	 * TODO: This algo and TBoxPathing probably need either revision or rewritten.
	 * This algo has issue with turning points at vectors. I don't want the overhead so figure out a non-recursive solution.
	 * TBoxPathing it might be better to make 2 Vec paths 4, denoting start and end separate of the bridge.
	 * 	This idea can be ignored if the cornering issue is solved in a revission of this class.
	 */
	public static void linkPaths(AbstractCellComponent[][] sheet, Set<List<Vec2>> pathSet, int newCellWidth, int newCellHeight) {
		
		for(List<Vec2> path : pathSet) {
			Vec2 coord = path.get(path.size()-1);
			
			PathInstance curPath = new PathInstance((NodeCellComponent)(sheet[coord.getY()][coord.getX()]));
			
			List<Entry<Direction, PathCellComponent>> flow = new ArrayList<>();
			int index = 2;
			int directionCount = 0;
			while(!coord.equals(path.get(0))) {
				Vec2 nextCoord = path.get(path.size()-index++);
				
				Direction compass = coord.directionTo(nextCoord);
				directionCount++;
				if(compass == Direction.SAME) continue; // TODO debug TBoxPathing for this issue
				Vec2 incr = Vec2.fromDirection(compass);
				
				if(flow.size() > 0) { // start a new direction with the last cell of the previous vector. This implies this cell as a turning point
					flow.add(new SimpleEntry<>(compass, flow.get(flow.size()-1).getValue()));
				}
				
				while(!coord.equals(nextCoord)) {
					coord = coord.add(incr);
					if(sheet[coord.getY()][coord.getX()] instanceof NodeCellComponent) {
						continue;
					}
					if(!(sheet[coord.getY()][coord.getX()] instanceof PathCellComponent)) {
						sheet[coord.getY()][coord.getX()] = new PathCellComponent(newCellWidth, newCellHeight);
					}
					flow.add(new SimpleEntry<>(compass, (PathCellComponent)sheet[coord.getY()][coord.getX()]));
				}
			}
			
			for(int i = 0; i < flow.size()-2; i++) {
				if(flow.get(i).getValue() == flow.get(i+1).getValue()) {
					continue;
				}
				flow.get(i+1).getValue().ensureBond(flow.get(i).getValue(), Direction.oppositeOf(flow.get(i+1).getKey()));
			}
			
			int[] assignmentOrder = new int[directionCount];
			
			index = 0;
			directionCount = 0;
			Direction lastDirection = flow.get(index).getKey();
			while(index < flow.size()) {
				Queue<PathInstance[]> pathStack = new LinkedList<>();
				while(index != flow.size() && flow.get(index).getKey() == lastDirection) {
					pathStack.add(flow.get(index).getValue().getPathByDirection(Direction.oppositeOf(lastDirection)));
					pathStack.add(flow.get(index++).getValue().getPathByDirection(lastDirection));
				}
				
				boolean reiterate = false;
				do {
					reiterate = false;
					for(PathInstance[] p : pathStack) {
						if(p[assignmentOrder[directionCount]] instanceof PathInstance) {
							while(p[assignmentOrder[directionCount]] instanceof PathInstance) {
								assignmentOrder[directionCount]++;
							}
							reiterate = true;
						}
					}
				} while(reiterate);
				
				if(index == flow.size()) break;
				lastDirection = flow.get(index).getKey();
				directionCount++;
			}
			
			while(flow.get(flow.size()-1).getValue().getPathByDirection(flow.get(flow.size()-1).getKey())[assignmentOrder[assignmentOrder.length-1]] instanceof PathInstance) {
				assignmentOrder[assignmentOrder.length-1]++;
			} // Last movement operation to ensure the final exit point of this flow isn't overwriting a pre-existing path

			
			PathCellComponent lastCell = null;
			index = 0;
			for(int i = 0; i < flow.size(); i++) {
				if(flow.get(i).getValue() == lastCell) {
					index++;
					continue;
				}
				lastCell = flow.get(i).getValue();
				lastCell.getPathByDirection(Direction.oppositeOf(flow.get(i).getKey()))[assignmentOrder[index]] = curPath;
			}
			flow.get(flow.size()-1).getValue().getPathByDirection(flow.get(flow.size()-1).getKey())[assignmentOrder[assignmentOrder.length-1]] = curPath;
		}
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
