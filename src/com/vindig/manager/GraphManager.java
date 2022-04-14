package com.vindig.manager;

import java.util.function.Function;

import com.collections.DependencyTree;
import com.record.AbstractRecord;
import com.vindig.TBoxPathing;
import com.vindig.image.Color;

/**
 * Construct m*n cellsheet and assign display and path nodes.
 * 
 * @author viney
 *
 */
public class GraphManager { // TODO verify boxing/unboxing performance in generator instances
	
	private static int BIT_DEPTH = 8; // Default - Low res color pallette, no alpha channel
	
	private static Color backgroundColor = new Color(1.f, 1.f, 1.f);
	
	private int xOverlaps;
	private int yOverlaps;
	
	TBoxPathing tbp;
	
//	private CellSheet cs;
	
	
	public GraphManager(DependencyTree<? extends AbstractRecord> adt, Function<? extends AbstractRecord, String> definition, TBoxPathing tbp) {
		int[] maxOverlaps = tbp.measureOccupancy();
		xOverlaps = maxOverlaps[0];
		yOverlaps = maxOverlaps[1];
//		cs = new CellSheet(tbp.fieldWidth(), tbp.fieldHeight(), maxOverlaps[0], maxOverlaps[1]);
		
	}
	
	public static int getBitDepth() { return BIT_DEPTH; }
	public static Color getBackgroundColor() { return backgroundColor; }
	
}
