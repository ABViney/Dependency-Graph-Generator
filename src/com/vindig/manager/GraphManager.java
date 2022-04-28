package com.vindig.manager;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.collections.DependencyTree;
import com.record.AbstractRecord;
import com.vindig.TBoxPathing;
import com.vindig.image.Bitmap;
import com.vindig.image.Color;

/**
 * Construct m*n cellsheet and assign display and path nodes.
 * 
 * @author viney
 *
 */
public class GraphManager { // TODO verify boxing/unboxing performance in generator instances
	
	private static int BIT_DEPTH = 24; // Default - no alpha
	
	private static Color backgroundColor = new Color(1.f, 1.f, 1.f, 1.f);
	
	private CellSheet cs;
	
	private int xOverlaps;
	private int yOverlaps;
	
	TBoxPathing tbp;
	
//	private CellSheet cs;
	
	
	public GraphManager(DependencyTree<? extends AbstractRecord> adt, Function<AbstractRecord, String> definition, TBoxPathing tbp) {
		int[] maxOverlaps = tbp.measureOccupancy();
		xOverlaps = maxOverlaps[0]+1;
		yOverlaps = maxOverlaps[1]+1;
		cs = new CellSheet(adt,
				definition,
				tbp.getField(),
				tbp.getPathMap().values().stream().flatMap(Set::stream).collect(Collectors.toSet()),
				yOverlaps,
				xOverlaps);
		System.out.println(String.format("XOverlaps: %d  YOverlaps: %d", maxOverlaps[0], maxOverlaps[1]));
	}
	
	public static int getBitDepth() { return BIT_DEPTH; }
	public static Color getBackgroundColor() { return backgroundColor; }
	public static void setBackGroundColor(Color newColor) { backgroundColor = newColor; }
	
//	public Bitmap createBitmap() {
	public Bitmap createBitmap() throws IOException {
		return cs.toBMP();
	}
	public File createPNG(String outputDest) {
		return cs.toPNG(outputDest);
	}
	
}
