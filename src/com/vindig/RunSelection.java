package com.vindig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.collections.DependencyLayers;
import com.collections.DependencyTree;
import com.collections.FileTree;
import com.directional.Vec2;
import com.record.AbstractRecord;
import com.record.FauxRecord;
import com.record.PatternSet;
import com.record.Record;
import com.vindig.manager.GraphManager;

/**
 * Application centerfold dedicated to generating a DependencyTree reflective of the target
 * file system and pattern based arguments.
 * 
 * @author viney
 *
 */
public class RunSelection {
	
	private List<Record> explainedContent;
	private DependencyTree<? extends AbstractRecord> fullTree;
	private TBoxPathing pathInfo;
	private GraphManager gm;
	
	private RunSelection(List<Record> definedContent, DependencyTree<? extends AbstractRecord> abstractedTree, TBoxPathing productMap) {
		explainedContent = definedContent;
		fullTree = abstractedTree;
		pathInfo = productMap;
		gm = new GraphManager(fullTree, (r -> r.getName()), pathInfo);
	}
	
	/**
	 * Generate a DependencyTree comprised of the enabled elements of the FileTree that
	 * match other elements based on the provided PatternSets.
	 * 
	 * @param fileTree - FileTree
	 * @param patternSets - Collection of PatternSets
	 * @return - new DependencyTree<Record>
	 * @throws Exception -- IOException if file was not read.
	 */
	public static RunSelection instantiateUsing(FileTree fileTree, Predicate<? super Record> rootFilter, List<PatternSet> patternSets, String... excludes) throws IOException {
		List<String> unread = new ArrayList<>(); //TODO figure out logger
		List<Record> records = Arrays.stream(fileTree.getFileList())
				.map(file -> {
					try {
						return new Record(file);
					} catch (IOException e) {
						unread.add(file.getName());
					} return null;
				})
				.filter(r -> r != null).toList();
		
		records.stream().forEach(r -> patternSets.forEach(ps -> r.compileUsing(ps, excludes))); System.out.println("Records compiled");
		
		if(!unread.isEmpty()) throw new IOException("Unable to read file(s):\n" + Arrays.toString(unread.toArray(new String[unread.size()])));
		
		DependencyTree<Record> dt = new DependencyTree<>(records); // two trees
		Set<String> mentioned = new HashSet<>(); System.out.println("Trees generated");
		
		for(int i = 0; i < records.size(); i++) { //TODO figure if this can be simplified to a stream. Low footprint as is, low priority refactor
			Record r1 = records.get(i);
			mentioned.addAll(r1.getDataSet());
			for(int j = 0; j < records.size(); j++) {
				if(j == i) continue;
				Record r2 = records.get(j);
				if(r1.hasRecordOf(r2.getName())) {
					dt.get(r1).addDependency(dt.get(r2));
				}
			}
		} System.out.println("Initial Tree mapped");
		
		if(records.size() > 0) {
			Record last = records.get(records.size() - 1);
			mentioned.addAll(last.getDataSet());
		}
		
		records.stream().forEach(r -> mentioned.remove(r.getName()));
		
		List<? extends AbstractRecord> ar = mentioned.stream().map(m -> new FauxRecord(m)).toList(); System.out.println("Faux records generated");
		DependencyTree<AbstractRecord> adt = new DependencyTree<>(dt); System.out.println("Abstracted tree instantiated");
		adt.putAll(ar);
		for(Record r1 : records) {
			for(AbstractRecord r2 : ar) {
				if(r1.hasRecordOf(r2.getName())) adt.get(r1).addDependency(adt.get(r2));
			}
		}
		
		DependencyLayers dl = new DependencyLayers(adt.get(dt.get(dt.findRoot(rootFilter))), ar.stream().map(r -> adt.get(r)).toList()); System.out.println("Layer stack created");
//		dl.getAside().getSet().forEach(p -> System.out.println(adt.get(p).getName())); // Print FauxRecords
//		DependencyLayers dl = new DependencyLayers(dt.findRoot(rootFilter)); System.out.println("Layer stack created");
//		printLayers(dl, adt);
		System.out.println(dl.sizeOf() + " dl sizeOf");
		System.out.println(dl.stackWidth() + " dl stackWidth");
		System.out.println(dl.stackDepth() + " dl stackDepth");
		System.out.println(dl.getAside().getSet().size() + " aside size");
		
		TBoxPathing tbp = new TBoxPathing(dl); System.out.println("Paths mapped");
		
		return new RunSelection(records, adt, tbp);
//		return new RunSelection(null, null);
	}
	
	public TBoxPathing getPathInfo() { return pathInfo; }
	public DependencyTree<? extends AbstractRecord> getDependencyTree() { return fullTree; }
	
	public GraphManager getGraphManager() { return gm; }
	
//	private static void debugFunc(DependencyTree<Record> dt, DependencyTree<AbstractRecord> adt, Predicate<? super Record> filter) {
//		System.out.println("Initial Tree");
//		Pos dRoot = dt.findRoot(filter); System.out.println(dt.get(dRoot).getName());
//		for(Pos d : dRoot.getDependencies()) {
//			System.out.println(dt.get(d).getName());
//		}
//		
//		System.out.println("Abstract Tree");
//		Pos aRoot = adt.get(dt.get(dRoot)); System.out.println(adt.get(aRoot).getName());
//		for(Pos d : aRoot.getDependencies()) {
//			System.out.println(adt.get(d).getName());
//		}
//	}
//	
//	private static void printtree(DependencyTree<? extends AbstractRecord> dt, Pos cursor, String buffer) {
//		System.out.println(buffer + dt.get(cursor).getName());
//		for(Pos c : cursor.getDependencies()) {
//			printtree(dt, c, buffer+"   ");
//		}
//	}
//	
//	private static void printLayers(DependencyLayers dl, DependencyTree<? extends AbstractRecord> adt) {
//		if(dl.getAside().getSet().size() > 0) {
//			System.out.print("Aside : ");
//			for(Pos ele : dl.getAside().getSet()) System.out.print(" " + adt.get(ele).getName());
//			System.out.println();
//		}
//		Iterator<LayerSet> itr = dl.iterator();
//		int i = 0;
//		while(itr.hasNext()) {
//			System.out.print("Layer " + i++ + ":");
//			for(Pos ele : itr.next().getSet()) {
//				System.out.print(" " + adt.get(ele).getName());
//			} System.out.println();
//		}
//	}
}
