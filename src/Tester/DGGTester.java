package Tester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Scanner;

import com.collections.FileTree;
import com.util.IOUtils;

public class DGGTester {
	
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		
		FileTree ft = new FileTree(new File("DGG-Example"));
		ft.disable("DGG-Example/build");
		ft.disable("DGG-Example/CMakeLists.txt");
		
		List<String> filePrints = new ArrayList<>();
		Arrays.stream(ft.getFileList()).forEach((entry) -> {
			try {
				filePrints.add(IOUtils.readToString(entry));
			} catch (IOException e) {
				System.out.println(entry.getName() + " failed to open.");
				e.printStackTrace();
			}
		});
		
		System.out.println(filePrints.size() + " files read.");
		
		int count = 0;
		for(String f : filePrints) {
			System.out.println("File " + ++count);
			System.out.println(f);
			System.out.println("\n\n");
			in.nextLine();
		}
		in.close();
	}
}
