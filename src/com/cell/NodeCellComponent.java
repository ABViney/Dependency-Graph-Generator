package com.cell;

import java.util.Set;

import com.cell.LinkedPath.PathInstance;
import com.util.Generator;
import com.vindig.image.Color;
import com.vindig.manager.GraphManager;

/**
 * CellComponent utilized in bitmap generation.
 * Cell contains information on its (String) name and color.
 * 
 * @author viney
 *
 */
public class NodeCellComponent extends AbstractCellComponent {
	
	private String nodeTitle;
	private Set<PathInstance> pathSockets;
	private Color nodeColor;
	
	/**
	 * Instantiate a new NodeCellComponent
	 * @param w - pixel width
	 * @param h - pixel height
	 * @param nodeTitle - String
	 */
	public NodeCellComponent(int w, int h, String nodeTitle) {
		super(w, h);
		this.nodeTitle = nodeTitle;
	}
	
	public String getNodeTitle() { return nodeTitle; }
	public void setNodeTitle(String title) { nodeTitle = title; }
	
	/**
	 * Attach a PathInstance to this instance. PathInstance recieves color
	 * data from this object, and changes in this object will be reflected
	 * in the associated PathInstance.
	 * @param newSocket
	 */
	public void socket(PathInstance newSocket) { pathSockets.add(newSocket); }
	public void setColor(Color color) {
		nodeColor = color;
		if(pathSockets != null) pathSockets.forEach(p -> p.setColor(nodeColor));
	}
	
	public Color getColor() { return nodeColor; }

	/**
	 * Calling of this method provides a Generator that provides upon request, by line,
	 * the raw byte data necessary to draw all information of this NodeCellComponent as
	 * specified by implementation.
	 * 
	 * @return Generator<Byte[]>
	 */
	public Generator<Byte[]> generator() { // TODO create tester
		return new Generator<Byte[]>() {
			
			private int index = 0;
			private byte[] pixelData = nodeColor.toPixel();
			private byte[] emptyData = GraphManager.getBackgroundColor().toPixel();
			private int incr = NodeCellComponent.super.getWidth() * pixelData.length;
			Byte[] buffer = new Byte[incr];
			
			private int totalBytes = incr * NodeCellComponent.super.getHeight();
			
			@Override
			public void release() {
				buffer = null;
				pixelData = null;
				emptyData = null;
			}

			@Override
			public Byte[] yield() {
				if(index >= totalBytes) return null;
				
				if(index < incr || index+incr == totalBytes) {
					for(int i = 0; i < incr; i++) {
						buffer[index++%incr] = Byte.valueOf(emptyData[i%emptyData.length]);
					} return buffer;
				}
				for(int i = emptyData.length; i < incr-emptyData.length; i++) 
					buffer[index++%incr] = Byte.valueOf(pixelData[i%pixelData.length]);
				return buffer;
			}			
		};
	}

}
