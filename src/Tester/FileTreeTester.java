package Tester;

import java.io.File;
import java.io.IOException;

import com.collections.FileTree;
import com.collections.FileTree.Node;

public class FileTreeTester {
	public static void main(String[] args) throws IOException {
		/**
		 * Early version tester of FileTree collection and methods
		 */
		FileTree ft = new FileTree(new File("DGG-Example"));
		
		System.out.println("Relative Pathing:");
		System.out.println(ft.toString());
		System.out.println();
		
		System.out.println("Changing permissions of immediate children:");
		Node cur = ft.getRoot();
		for(Node c : cur.getChildren()) {
			if(c.getElement().getName().endsWith(".txt")) c.disable();
			if(c.getElement().getName().equals("build")) c.disable();
			System.out.println(c.toString() + (c.isEnabled() ? " is enabled" : " is disabled"));
		}
		System.out.println();
		
		System.out.println("Printing all enabled files:");
		File[] fl = ft.getFileList();
		for(File f : fl) System.out.println(f.getName());
		
	}
	
}
