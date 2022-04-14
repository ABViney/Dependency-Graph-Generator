package Tester;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.record.PatternSet;
import com.util.IOUtils;

public class PatternSetTester {
	
	public static void main(String[] args) throws IOException {
		/** Pattern order finds all "#include <path/to/file>" statements and reduces them to "file"
		 * This tester provides two examples between the DGG-Example, which includes SimplerObject.hpp and iostream
		 * and CortexCommandCPS, which is much more verbose and has it's own internal dependencies. 
		 */
		String[] regex = {
				"\\B#include\\s*((<.+>)|(\".+\"))",
				"((\".+\")|(<.+>))",
				"[^\"<>\\s]+",
				"[^\\/]+$"
		};
		
		PatternSet ps = new PatternSet(regex);
//		String sample = IOUtils.readToString(new File("DGG-Example/DGG-Example.cpp"));
		String sample = IOUtils.readToString(new File("Cortex-Command-Community-Project-Source/Main.cpp"));
		List<String> results = ps.runPatternSet(sample);
		for(String s : results) System.out.println(s);
	}
	
}
