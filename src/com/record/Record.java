package com.record;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.util.IOUtils;

/**
 * A singular data section collection of String data to enable simple comparison of
 * relevant info to outside queries.
 * 
 * @author viney
 *
 */
public class Record extends AbstractRecord {
	
	public static final int NO_CASE_SENSITIVE = 0; //TODO
	
	private Set<String> dataSet;
	private String fullData;
	private File origin = null;
	
	/**
	 * Create a new record with a reference name and data.
	 * 
	 * @param name - String
	 * @param data - String
	 */
	public Record(String name, String data) {
		super(name);
		fullData = data;
		dataSet = new HashSet<>();
	}
	
	/**
	 * Create a new record from a file.
	 * 
	 * @param target - File
	 * @throws IOException
	 */
	public Record(File target) throws IOException {
		this(target.getName(), IOUtils.readToString(target));
		origin = target;
	}
	
	/**
	 * Runs the fullData of this record through the passed PatternSet.runPatternSet method.
	 * Results of the operation is added to the dataSet.
	 * 
	 * @param ps
	 */
	public void compileUsing(PatternSet ps) {
		dataSet.addAll(ps.runPatternSet(fullData));
	}
	
	/**
	 * Initially trims the fullData content of all patterns matching the second argument
	 * before running the result through the first argument PatternSet.
	 * Results of the second operation is added to the dataSet.
	 * 
	 * @param ps - PatternSet comprised of 
	 * @param excludes - varargs plaintext regex to expunge content from fullData
	 */
	public void compileUsing(PatternSet ps, String... excludes) {
		String partialData = fullData;
		for(String e : excludes) partialData = partialData.replaceAll(e, "");
		dataSet.addAll(ps.runPatternSet(partialData));
	}
	
	/**
	 * Evaluate whether this record contains in its dataSet an equivalent reference of the
	 * passed String.
	 * 
	 * @param ref - String
	 * @return - true if this dataSet contains the passed String
	 */
	public boolean hasRecordOf(String ref) {
		return dataSet.contains(ref);
	}
	
	public Set<String> getDataSet() { return dataSet; }
	public String getFullData() { return fullData; }
	public File getOrigin() { return origin; }
	
}
