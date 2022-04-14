package com.cell;

import com.cell.LinkedPath.PathInstance;
import com.util.Generator;
import com.vindig.manager.GraphManager;

public class PathCellComponent extends AbstractCellComponent {
	PathInstance[][] horizontalScan; // TODO rethink this algorithm approach, lot more reservation than I want.
	PathInstance[][] verticalScan; // TODO alt idea is to have the TBoxPathing method "measureOccupancy" return a 3d array of intersections instead, letting graph manager parse the max's
	
	public PathCellComponent(int w, int h, int xChannels, int yChannels) {
		super(w, h);
		verticalScan = new PathInstance[2][yChannels]; // 2 channels for each cardinal entrypoint and (potential) intersection
		horizontalScan = new PathInstance[2][xChannels]; // this model has a size difference of (x*y)/(2x+2y) ex (127*87)/(254+174) = 3300% overhead reduction
	}													   // look into using linked list datatype, flatten to array for generator operation, 
	
	protected PathInstance[][] getHorizontalScan() { return horizontalScan; }
	protected PathInstance[][] getVerticalScan() { return verticalScan; }
	
	@Override
	public Generator<Byte[]> generator() { //TODO LinkedPath needs more info or This needs more info to properly write path lines
		return new Generator<Byte[]>() {
			
			private int index = 0;
			private byte[] emptyData = GraphManager.getBackgroundColor().toPixel();
			private int incr = PathCellComponent.super.getWidth() * emptyData.length;
			Byte[] buffer = new Byte[incr];
			
			private int totalBytes = incr * PathCellComponent.super.getHeight();
			
			@Override
			public void release() {
				buffer = null;
				emptyData = null;
				
			}
			
			@Override
			public Byte[] yield() {
				// TODO Auto-generated method stub
				return null;
			
			}
		};
	}

}
