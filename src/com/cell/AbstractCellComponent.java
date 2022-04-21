package com.cell;

import com.util.ByteGenerator;
import com.vindig.image.Color;

/**
 * Building blocks of graph visualization.
 * Abstract superclass 
 * @author viney
 *
 */
public abstract class AbstractCellComponent {
	
	private int cellWidth;
	private int cellHeight;
	
	/**
	 * Generate a CellComponent of passed width and height
	 * @param w
	 * @param h
	 */
	public AbstractCellComponent(int w, int h) {
		cellWidth = w;
		cellHeight = h;
	}
	
	public int getWidth() { return cellWidth; }
	public int getHeight() { return cellHeight; }
	
	/**
	 * Instantiate a Generator to yield the next expected byte content for
	 * this extension of AbstractCellComponent.
	 * @return - Generator<Byte[]>
	 * @see com.util.Generator.java
	 */
	public abstract ByteGenerator generator();

}
