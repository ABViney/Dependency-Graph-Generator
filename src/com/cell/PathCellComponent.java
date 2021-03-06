package com.cell;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;

import com.cell.LinkedPath.PathInstance;
import com.directional.Direction;
import com.directional.Vec2;
import com.util.ByteGenerator;
import com.vindig.image.Color;
import com.vindig.manager.CellSheet;
import com.vindig.manager.GraphManager;

public class PathCellComponent extends AbstractCellComponent {
//	PathInstance[][] horizontalScan; // TODO rethink this algorithm approach, lot more reservation than I want.
//	PathInstance[][] verticalScan; // TODO alt idea is to have the TBoxPathing method "measureOccupancy" return a 3d array of intersections instead, letting graph manager parse the max's
	
	static int xChannels = 0;
	static int yChannels = 0;
	
	private PathInstance[] up = null;
	private PathInstance[] down = null;
	private PathInstance[] left = null;
	private PathInstance[] right = null;
	
	public PathCellComponent(int w, int h) {
		super(w, h);
//		verticalScan = new PathInstance[2][yChannels]; // 2 channels for each cardinal entrypoint and (potential) intersection
//		horizontalScan = new PathInstance[2][xChannels]; // this model has a size difference of (x*y)/(2x+2y) ex (127*87)/(254+174) = 3300% overhead reduction
//														   //look into using linked list datatype, flatten to array for generator operation, 
		
	}
	
	/**
	 * Set the predefined amount of x or y PathInstance positions available in all future PathCellComponents
	 * @param xChannels || yChannels
	 */
	public static void setGlobalXChannels(int xChannels) { PathCellComponent.xChannels = xChannels; }
	public static void setGlobalYChannels(int yChannels) { PathCellComponent.yChannels = yChannels; }
	
	/**
	 * Instantiate a PathInstance array shared by this PathCellComponent and another PathCellComponent
	 * on a specified cardinal direction.
	 * 
	 * @param o
	 * @param relativeToThis -- Direction enum UP DOWN LEFT RIGHT
	 */
	@SuppressWarnings("incomplete-switch")
	public void ensureBond(PathCellComponent o, Direction fromThis) {
		switch(fromThis) {
		case UP:
			if(this.up == null) { 
				if(o.down == null) this.up = o.down = new PathInstance[yChannels];
				else this.up = o.down;
			}
			else o.down = this.up; 
			break;
		case DOWN:
			if(this.down == null) {
				if(o.up == null) this.down = o.up = new PathInstance[yChannels];
				else this.down = o.up;
			}
			else o.up = this.down;
			break;
		case LEFT:
			if(this.left == null) {
				if(o.right == null) this.left = o.right = new PathInstance[xChannels];
				else this.left = o.right;
			}
			else o.right = this.left;
			break;
		case RIGHT:
			if(this.right == null) {
				if(o.left == null) this.right = o.left = new PathInstance[xChannels];
				else this.right = o.left;
			}
			else o.left = this.right;
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public PathInstance[] getPathByDirection(Direction fromThis) {
		switch(fromThis) {
		case UP:
			if(up == null) up = new PathInstance[yChannels];
			return up;
		case DOWN:
			if(down == null) down = new PathInstance[yChannels];
			return down;
		case LEFT:
			if(left == null) left = new PathInstance[xChannels];
			return left;
		case RIGHT:
			if(right == null) right = new PathInstance[xChannels];
			return right;
		} return null;
	}
	
	@SuppressWarnings("incomplete-switch") //TODO gotta rewrite this, goes out of bounds sometimes somehow, related ~last switch statement setting an intercept pos out of bounds
	private Color[] mergeScans() { // TODO can be simplified immensely anyways
		Color[][] result = new Color[super.getHeight()][super.getWidth()];
		Arrays.stream(result).forEach(a -> Arrays.fill(a, GraphManager.getBackgroundColor()));
		
		IntFunction<Integer> resize = CellSheet.getResize();
		
		Direction from;
		Direction to;
		PathInstance[] pathFrom;
		PathInstance[] pathTo;
		Set<PathInstance> used = new HashSet<>();
		for(int i = 0; i < 3; i++) {
			switch(i) {
			case 0:
				if(up == null) continue;
				from = Direction.UP;
				pathFrom = up;
				break;
			case 1:
				if(right == null) continue;
				from = Direction.RIGHT;
				pathFrom = right;
				break;
			case 2:
				if(down == null) continue;
				from = Direction.DOWN;
				pathFrom = down;
				break;
			default:
				return null;
			}
			for(int i2 = i; i2 < 3; i2++) {
				switch(i2) {
				case 0:
					if(right == null) continue;
					to = Direction.RIGHT;
					pathTo = right;
					break;
				case 1:
					if(down == null) continue;
					to = Direction.DOWN;
					pathTo = down;
					break;
				case 2:
					if(left == null) continue;
					to = Direction.LEFT;
					pathTo = left;
					break;
				default:
					return null;
				}
				
				for(int c = 0; c < pathFrom.length; c++) {
					if(!(pathFrom[c] instanceof PathInstance) || used.contains(pathFrom[c])) continue;
					PathInstance comparator;
					comparator = pathFrom[c];
					for(int c2 = 0; c2 < pathTo.length; c2++) {
						int x = -1;
						int y = -1;
						if(pathTo[c2] == comparator) {
							Vec2 incr = new Vec2(0,0);
							switch(from) {
							case UP,DOWN:
								x = resize.apply(c);//(c*4+2);
								incr = incr.add(Vec2.fromDirection(Direction.oppositeOf(from)));
								break;
							case LEFT,RIGHT:
								y = result.length - resize.apply(c)-1;//(c*4+2)-1;
								incr = incr.add(Vec2.fromDirection(from));
							}
							switch(to) {
							case UP,DOWN:
								x = resize.apply(c2);//(c2*4+2);
								incr = incr.add(Vec2.fromDirection(Direction.oppositeOf(to)));
								break;
							case LEFT,RIGHT:
								y = result.length - resize.apply(c2)-1;//(c2*4+2)-1;
								incr = incr.add(Vec2.fromDirection(to));
							}
							if(x == -1) {
								for(int j = 0; j < result[0].length; j++) result[y][j] = comparator.getColor();
								used.add(comparator);
								continue;
							}
							if(y == -1) {
								for(int j = 0; j < result.length; j++) result[j][x] = comparator.getColor();
								used.add(comparator);
								continue;
							}
							
							int xScan = 0;
							int yScan = 1;
							if(from == Direction.UP || from == Direction.DOWN) {
								xScan = 1;
								yScan = 0;
							}

							for(int order = 0; order < 2; order++) {
								if(order == yScan) {
									for(int x2 = x; x2 >= 0 && x2 < result[0].length; x2+=incr.getX())
										result[y][x2] = comparator.getColor();
								}
								if(order == xScan) {
									for(int y2 = y; y2 >= 0 && y2 < result.length; y2+=incr.getY()) 
										result[y2][x] = comparator.getColor();
								}
							}
							used.add(comparator);
							break;
						}
					}
				}
			}
		}
//		for(int i = 0; i < (int)result.length/2; i++) { // TODO I can modify the above method to do this during writing
//			Color[] swap = result[i];
//			result[i] = result[result.length-1-i];
//			result[result.length-1-i] = swap;
//		}
		Color[] flattened =  Arrays.stream(result).flatMap(Arrays::stream).toArray(Color[]::new);
		return flattened;
	}
	
	@Override
	public ByteGenerator generator() { //TODO Yield function needs to space horizontal occurences
		final Color[] colorScan = mergeScans();
		
		return new ByteGenerator() {
			
			private int index = 0;
			private byte[] emptyData = GraphManager.getBackgroundColor().toPixel();
			private int incr = PathCellComponent.super.getWidth() * emptyData.length;
			byte[] buffer = new byte[incr];
			Color[] full = colorScan;
			private int totalReads = full.length-1;
			
			@Override
			public void release() {
				emptyData = null;
				full = null;
			}
			
			@Override
			public byte[] yield() {
				if(index > totalReads) return null;

				for(int i = 0; i < incr; i+= emptyData.length) {
					byte[] b = full[index++].toPixel();
					for(int i2 = 0; i2 < b.length; i2++) {
						buffer[i+i2] = b[i2];
					}
				}
				return buffer;
			}
		};
	}

}
