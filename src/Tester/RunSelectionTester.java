package Tester;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import com.collections.FileTree;
import com.record.PatternSet;
import com.record.Record;
import com.vindig.RunSelection;
import com.vindig.TBoxPathing;
import com.vindig.image.Bitmap;
import com.vindig.manager.GraphManager;

public class RunSelectionTester {
	
	public static void main(String[] args) throws IOException {
		String[] regex1 = {
				"\\B#include\\s*((<.+>)|(\".+\"))",
				"((\".+\")|(<.+>))",
				"[^\"<>\\s]+",
				"[^\\/]+$"
		};
//		String[] regex2 = {
//				"int\\s+main\\(.*\\)",
//				"main"
//		};
		/**
		 * This regex array finds all comments in a C++ file. C++ commenting is similar to java, using // and /* ... *\/
		 */
		String[] excludes = {
				"\\?s\\/\\*(.|\\n)*\\*\\/",
				"\\/\\/.*$"
		};
		
		List<PatternSet> ps = new ArrayList<>();
		ps.add(new PatternSet(regex1));
//		ps.add(new PatternSet(regex2));
		
//		Node cur = ft.getRoot(); System.out.println("Disabling content");
//		for(Node c : cur.getChildren()) {
//			if(c.getElement().getName().endsWith(".txt")) c.disable();
//			if(c.getElement().getName().equals("build")) c.disable();
//		}
		FileTree ft = new FileTree(new File("DGG-Example"));
//		FileTree ft = new FileTree(new File("Cortex-Command-Community-Project-Source"));
//		FileTree ft = new FileTree(new File("/home/viney/Downloads/Cortex-Command-Community-Project-Source"));
//		FileTree ft = new FileTree(new File("/home/viney/Downloads/AngryGL"));
		File[] disableList = Arrays.stream(ft.getFileList()).filter(f -> !f.getName().matches(".*\\.(h.{0,2}|c.{1,2}?)$")).toArray(size -> new File[size]);
		Arrays.stream(disableList).forEach(f -> ft.disable(f.getPath()));
		
		/**
		 * End setup
		 */
				
		/**
		 * Runselection (the main hub of activity at current) utilizes a predicate statement in defining the "root" of a project.
		 * A current issue is that the "findRoots" method in the DependencyTreeCollection cannot differentiate between a source file, which is never imported,
		 * and an entrypoint. A stopgap fix is to store all occurences of "int main(...)" as this is a language defined entrypoint, but some projects may have
		 * multiple for different sections. Requiring the user to specify the entry point is the only solution at this time.
		 */
		Predicate<Record> filter = r -> r.getName().equals("DGG-Example.cpp");
//		Predicate<Record> filter = r->r.getName().equals("Main.cpp");
//		Predicate<Record> filter = r -> r.getName().equals("main.cc");
		System.out.println("instantiating");
		RunSelection result = RunSelection.instantiateUsing(ft, filter, ps, excludes);
		TBoxPathing tbp = result.getPathInfo();
		GraphManager gm = result.getGraphManager();
		System.out.println("Writing output");
//		FileOutputStream fos = new FileOutputStream(new File("runOutput.bmp"));
//		gm.createBitmap("runOutput.bmp");
//		FileOutputStream fos = new FileOutputStream(new File("DGG-Example.bmp"));
//		FileOutputStream fos = new FileOutputStream(new File("AngryGl.bmp"));
//		fos.write(gm.createBitmap().getBitmap());
		gm.createPNG("DGG-Example.png");
//		gm.createPNG("AngryGLMap.png");
//		gm.createPNG("CortexCommandMap.png");
		
		
		/**
		 * This is the collection of block path data that is generated upon initialization of the TBoxPathing class. Vec2 is a simple x,y coordinate collection with
		 * methods to assist in calculations and comparisons between other Vec2's.
		 */
//		Map<Pos, Set<List<Vec2>>> pathMap = tbp.getPathMap();
		
		/**
		 * This is a 2d array that contains the predicted positions of all Positions that are relevant to the defined root of the DependencyTree
		 */
//		Pos[][] field = tbp.getField();
		
		/**
		 * 	[ ][ ][ ][X]
			[X][ ][ ][ ]
			[ ][ ][ ][X]
			[ ][ ][ ][ ]
			[ ][ ][ ][X]
			[ ][ ][ ][ ]
			Example of what output looks like for the DGG-Example project
		 */
//		FileOutputStream fos = new FileOutputStream(new File("field-shape-output.txt"));
//		for(int y = 0; y < field.length; y++) {
//			for(int x = 0; x < field[0].length; x++) {
//				if(field[y][x] instanceof Pos) {
//					fos.write(new byte[] {0x5b, 0x58, 0x5d});
//				} else fos.write(new byte[] {0x5b, 0x20, 0x5d});
//			} fos.write(0xa);
//		}
//		
//		DependencyTree<? extends AbstractRecord> adt = result.getDependencyTree();
//		for(Pos p : tbp.getPathMap().keySet()) {
//			System.out.println(adt.get(p).getName());
//			for(List<Vec2> vec : tbp.getPathMap().get(p)) {
//				for(Vec2 v : vec)
//					System.out.println(Arrays.toString(v.getOrigin()));
//				System.out.println();
//			}
//		}
	}
	
}
