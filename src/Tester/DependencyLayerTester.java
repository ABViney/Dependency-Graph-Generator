package Tester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.collections.FileTree;
import com.record.PatternSet;
import com.record.Record;
import com.vindig.RunSelection;

public class DependencyLayerTester {
	
	public static void main(String[] args) throws IOException {
		String[] regex1 = {
				"\\B#include\\s*((<.+>)|(\".+\"))",
				"((\".+\")|(<.+>))",
				"[^\"<>\\s]+",
				"[^\\/]+$"
		};
		String[] regex2 = {
				"int\\s+main\\(.*\\)",
				"main"
		};
		
		String[] excludes = {
				"\\?s\\/\\*(.|\\n)*\\*\\/",
				"\\/\\/.*$"
		};
		
		List<PatternSet> ps = new ArrayList<>();
		ps.add(new PatternSet(regex1));
		ps.add(new PatternSet(regex2));
		
//		FileTree ft = new FileTree(new File("DGG-Example"));
//		Node cur = ft.getRoot();
//		for(Node c : cur.getChildren()) {
//			if(c.getElement().getName().endsWith(".txt")) c.disable();
//			if(c.getElement().getName().equals("build")) c.disable();
//		}
//		
//		DependencyTree<Record> dt = RunSelection.generateDependencyTree(ft, ps);
//		
//		Pos entryPoint = null;
//		for(Pos p : dt.findRoots()) {
//			if(dt.get(p).hasRecordOf("main")) System.out.println(dt.get(entryPoint = p).getName() + " as root");
//		}
		
		FileTree ft = new FileTree(new File("Cortex-Command-Community-Project-Source"));
		Predicate<Record> filter = r -> r.getName().equals("Main.cpp");
//		FileTree ft = new FileTree(new File("DGG-Example"));
		File[] disableList = Arrays.stream(ft.getFileList()).filter(f -> !f.getName().matches(".*\\.(h.{0,2}|c.{1,2}?)$")).toArray(size -> new File[size]);
		Arrays.stream(disableList).forEach(f -> ft.disable(f.getPath()));
		RunSelection rs = RunSelection.instantiateUsing(ft, filter, ps, excludes);
		
//		for(Pos p : dt.findRoots()) {
//			if(dt.get(p).hasRecordOf("main")) System.out.println(dt.get(p).getName() + " as root");
//		}
		
		/**
		 * End setup
		 */
		
//		Predicate<Record> filter = r -> r.hasRecordOf("main");
//		DependencyLayers dl = new DependencyLayers(null);
//		
//
//		Iterator<LayerSet> layerItr = dl.reverseIterator();
//		int layerCount = 0;
//		while(layerItr.hasNext()) {
//			System.out.println("Layer " + layerCount++);
//			for(Pos p : layerItr.next().getSet()) System.out.print(dt.get(p).getName() + " ");
//			System.out.println();
//		}
//		in.close();
		
//		System.out.println("\n\nRoot set paths: ");
//		
//		for(Pos p : postRef.getSet()) {
//			System.out.println(dt.get(p).getOrigin().getPath());
//		}
		
	}
	/**
	 * Record subclass to break components into blocks for iterative selection
	 * Option to exclude externs so nested includes aren't binded to the record
	 * 
	 */
}
