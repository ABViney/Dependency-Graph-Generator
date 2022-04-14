package Tester;

import java.io.File;
import java.io.IOException;

import com.record.PatternSet;
import com.record.Record;
import com.util.IOUtils;

public class RecordTester {
	
	
	public static void main(String[] args) throws IOException {
		File file1 = new File("DGG-Example/src/SimplerObject.cxx");
		File file2 = new File("DGG-Example/include/SimplerObject.hpp");
		File file3 = new File("DGG-Example/DGG-Example.cpp");
		File file4 = new File("DGG-Example/include/SimpleObject.h");
		
//		r1 = SimplerObject.cxx -- Imports from SimplerObject.hpp
//		r2 = SimplerObject.hpp -- Imports from SimpleObject.h
//		r3 = DGG-Example.cpp -- Entry point, Imports SimplerObject.hpp and iostream
//		r4 = SimpleObject.h -- SimpleObject.h -- no dependencies
		
		String file1Name = file1.getName();
		String file1Data = IOUtils.readToString(file1);
		
		Record r1 = new Record(file1Name, file1Data);
		Record r2 = new Record(file2);
		Record r3 = new Record(file3);
		Record r4 = new Record(file4);
		
		/**
		 * This regex iteration parses the file for lines that have #inlclude (<...> or "...")
		 * Sequential operations strip the contents of the include statement into the resource name
		 * Eg: #include "include/SimpleObject.h" -> "include/SimpleObject.h" -> include/SimpleObject.h -> SimpleObject.h
		 * 	   #include <iostream> -> <iostream> -> iostream -> iostream
		 * 
		 * Regex sequence is passed to PatternSet constructor which handles operations
		 */
		String[] regex = {
				"\\B#include\\s*((<.+>)|(\".+\"))",
				"((\".+\")|(<.+>))",
				"[^\"<>\\s]+",
				"[^\\/]+$"
		};
		PatternSet ps = new PatternSet(regex);
		
		r1.compileUsing(ps);
		r2.compileUsing(ps);
		r3.compileUsing(ps);
		r4.compileUsing(ps);
		
		/**
		 * As noted earlier, R1 is a source file that inherits from R2, it's header
		 * 	R2 does not have a reference to R1 because C++
		 * 	R3 references R2 because it utilizes the object in main method. By extension, R3 has access to R4, but that isn't covered by the current version
		 * 	This is reflected in the final output statement. R3, while it has access to R4, is not recognized to have it as a dependency.
		 */
		System.out.println("R1 has reference to R2 : " + r1.hasRecordOf(r2.getName()));
		System.out.println("R2 has reference to R1 : " + r2.hasRecordOf(r1.getName()));
		System.out.println("R3 has reference to R2 : " + r3.hasRecordOf(r2.getName()));
		System.out.println("R3 has reference to R4 : " + r3.hasRecordOf(r4.getName()));
	}
}
