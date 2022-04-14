package com.vindig.manager;

import java.util.List;
import java.util.Set;

import com.cell.AbstractCellComponent;
import com.cell.NodeCellComponent;
import com.collections.DependencyTree;
import com.collections.DependencyTree.Pos;
import com.directional.Vec2;
import com.record.AbstractRecord;

/**
 * Assume deprecated
 * @author viney
 *
 */
public class CellSheet {
	
	/** Relative sizing of cells in x,y dimensions */
	private int cellDrawWidth;
	private int cellDrawHeight;
	
	private AbstractCellComponent[][] layout;
	List<NodeCellComponent> nodeComponents;
	
	/**
	 * Instantiate a cell sheet with m*n slots
	 * @param rowSize
	 * @param colSize
	 * @param cellWidth
	 * @param cellHeight
	 */
	public CellSheet(DependencyTree<? extends AbstractRecord> adt, Pos[][] field, Set<List<Vec2>> pathSet, int cellWidth, int cellHeight) {
		this.cellDrawWidth = cellWidth*2+1;
		this.cellDrawHeight = cellHeight*2+1; //Cell parameters doubled and shifted odd to ensure spacing between path lines that will scale with final resolution
		
		/** Sheet initialization */
		layout = new AbstractCellComponent[field.length][field[0].length];
		
		/** Assign NodeCells */
		for(int i = 0; i < field.length; i++) {
			for(int c = 0; c < field[0].length; c++) {
				if(field[i][c] instanceof Pos) {
					layout[i][c] = new NodeCellComponent(cellDrawWidth, cellDrawHeight, adt.get(field[i][c]).getName());
				}
			}
		}
		
		
		
		
	}
	
	/**
	 * Get the number of units this CellSheet spans
	 * @return int
	 */
	public int area() { return layout[0].length * layout.length * cellDrawWidth * cellDrawHeight; }
	
}
