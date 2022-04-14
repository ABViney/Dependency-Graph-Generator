/*package com.vindig.dgg;

import java.io.File;
import java.io.IOException;

import com.vindig.DirLoader;

import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;

public class AppController {
	
	public Button setRoot;
	public Button run;
	public TreeView dirTree;
	
	
	public void clearProjectHierarchy() {
		
	}
	
	public void clearConfigFields() {
		
	}
	
	public void clearConfigFieldsReplaceDefault() {
		
	}
	
	public void displayDirectorySelector() {
		DirectoryChooser dc = new DirectoryChooser();
		File toplevel = dc.showDialog(DependencyGraphGenerator.primaryStage);
		if(toplevel != null && toplevel.isDirectory()) {
			try {
				DirLoader.setRoot(toplevel);
			} catch (IOException e) {
				//TODO
			}
		}
		
		
	}
	
	private populateTreeView() {
		
	}
	
	
}*/
