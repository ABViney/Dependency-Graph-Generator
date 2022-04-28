package com.vindig.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.cell.AbstractCellComponent;
import com.cell.EmptyCellComponent;
import com.cell.LinkedPath;
import com.cell.NodeCellComponent;
import com.cell.PathCellComponent;
import com.collections.DependencyTree;
import com.collections.DependencyTree.Pos;
import com.directional.Vec2;
import com.record.AbstractRecord;
import com.util.ByteGenerator;
import com.vindig.image.Bitmap;
import com.vindig.image.Color;
import com.vindig.image.PNG;

/**
 * Manages CellComponent generation and allocation.
 * 
 * @author viney
 *
 */
public class CellSheet {
	
	/**
	 * Representative graphics based on a scaling derived from
	 * the amount of x and y axes interactions required to render
	 * a readable graph structure.
	 */
	private int availableXChannels;
	private int availableYChannels;
	
	static int defaultCellWidth = 1;
	static int defaultCellHeight = 1;
	
	static IntFunction<Integer> resize = (i -> (i*4)+2);
	static double nodeBuffer = 1/20;
	
	// TODO maybe add a scale function --e.g. IntFunc(i -> 2*i + 1)
	
	private AbstractCellComponent[][] layout;
	List<NodeCellComponent> nodeComponents;
	private EmptyCellComponent empty;
	
	/**
	 * Instantiate a cell sheet with m*n slots
	 * @param rowSize
	 * @param colSize
	 * @param cellWidth
	 * @param cellHeight
	 */
	public CellSheet(DependencyTree<? extends AbstractRecord> adt, Function<AbstractRecord, String> definition, Pos[][] field, Set<List<Vec2>> pathSet, int cellWidth, int cellHeight) {
		PathCellComponent.setGlobalXChannels(this.availableXChannels = cellHeight); // y axis intersections 
		PathCellComponent.setGlobalYChannels(this.availableYChannels = cellWidth); // x axis intersections
		
		
		
		CellSheet.defaultCellWidth = resize.apply(availableYChannels);//*4+2;
		CellSheet.defaultCellHeight = resize.apply(availableXChannels);//*4+2;
		
		System.out.println(String.format("Cell{Width: %d  Height: %d}", defaultCellWidth, defaultCellHeight));
		
		this.layout = new AbstractCellComponent[field.length][field[0].length];
		this.nodeComponents = new ArrayList<>();
		this.empty = new EmptyCellComponent(defaultCellWidth, defaultCellHeight);
		
		/** Assign NodeCells */
		Random r = new Random(12);
		for(int i = 0; i < field.length; i++) {
			for(int c = 0; c < field[0].length; c++) {
				if(field[i][c] instanceof Pos) {
					NodeCellComponent newNode =  new NodeCellComponent(defaultCellWidth, defaultCellHeight, definition.apply(adt.get(field[i][c])), new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.f));
					if(newNode.getNodeTitle().contains("main"))
						newNode.setColor(new Color(0.f,0.f,0.f,1.f));
					layout[i][c] = newNode; // TODO generate n-length queue of colors to pop out to each new node
					nodeComponents.add(newNode);
				}
			}
		}

		LinkedPath.linkPaths(layout, pathSet, defaultCellWidth, defaultCellHeight);
		System.out.println("Done linking paths");
		for(int i = 0; i < layout.length; i++) {
			for(int c = 0; c < layout[0].length; c++) {
				if(layout[i][c] == null)
					layout[i][c] = empty;
			}
		}
	}
	
	public static IntFunction<Integer> getResize() { return resize; }
	public static double getNodeBuffer() { return nodeBuffer; }
	
	/**
	 * Get the number of units this CellSheet spans
	 * @return int
	 */
	public int area() { return layout[0].length * layout.length * defaultCellWidth * defaultCellHeight; }
	
	public Bitmap toBMP() {
		Bitmap bmp = new Bitmap(defaultCellWidth*layout[0].length, defaultCellHeight*layout.length, GraphManager.getBitDepth());
		for(int i = 0; i < layout.length; i++) {//int i = layout.length-1; i >= 0; i--) {
			ByteGenerator[] cg = Arrays.stream(layout[i]).map(c -> c.generator()).toArray(ByteGenerator[]::new);
			boolean valid = true;
			while(valid) {
				for(ByteGenerator g : cg) {
					if(!bmp.write(g.yield())) {
						valid = false;
						break;
					}
				}
			}
		}  return bmp;
	}
	
	public File toPNG(String outputDest) {
		File output = new File(outputDest);
		PNG png = new PNG(defaultCellWidth*layout[0].length, defaultCellHeight*layout.length, output);
		int row = 1;
		for(int i = layout.length-1; i>= 0; i--) {
			System.out.println("Working on row " + row++ + "/" + layout.length);
			ByteGenerator[] cg = Arrays.stream(layout[i]).map(c -> c.generator()).toArray(ByteGenerator[]::new);
			boolean valid = true;
			while(valid) {
				for(ByteGenerator g : cg) {
					if(!png.write(g.yield())) {
						valid = false;
						break;
					}
				}
			}
		} return output;
	}
	
}
