package com.collections;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tree based collection for creating a traversable system for target files
 * via a relative path structure.
 * 
 * @author viney
 *
 */
public class FileTree {
	
	/**
	 * Top level directory/file
	 */
	private Node root = null;
	
	/**
	 * Mapping for string paths to relevant nodes
	 */
	Map<String, Node> fileMap; //TODO check hashing efficiency in loading larger systems
	
	/**
	 * Nodes denoting the parent/child relationships of all files in the described system.
	 * 
	 * @author viney
	 *
	 */
	public static class Node {
		
		/**
		 * File will return with getFileList
		 */
		private boolean enabled = true;
		private boolean dir;
		
		private File ele;
		
		private Node parent;
		private List<Node> children = null;
		
		private Node(File ele, Node parent) {
			this.ele = ele;
			this.parent = parent;
			if(parent != null) parent.children.add(this);
			if(dir = ele.isDirectory()) {
				children = new ArrayList<>();
			}
		}
		
		/**
		 * Whether this Node and any child Nodes will appear in a FileTree result
		 * @return true/false
		 */
		public boolean isEnabled() { return enabled; }
		/**
		 * Disable this Node and any child Nodes from appearing in a FileTree result
		 */
		public void disable() { 
			enabled = false;
			if(children != null) children.stream().forEach(n -> n.disable());
		}
		/**
		 * Call is assumed to override any parental changes that may obfuscate this
		 * Node from appearing in a concurrent query.
		 */
		public void enable() { 
			enabled = true;
			if(parent != null) parent.enable();
		}
		/**
		 * Node is a directory
		 * @return true/false
		 */
		public boolean isDir() { return dir; }
		
		/**
		 * Retrive a reference to the File object stored in this Node
		 * @return
		 */
		public File getElement() { return ele; }
		/**
		 * Retrieve an array of all children of this Node irrespective of whether
		 * they are enabled or not.
		 * @return File[]
		 */
		public Node[] getChildren() { return children.toArray(new Node[children.size()]); }
		/**
		 * Gets the parent Node of this Node.
		 * Return value of null indicates this Node is the root, or that something has
		 * horribly gone wrong.
		 * @return Node
		 */
		public Node getParent() { return parent; }
		
		/**
		 * Returns a string of the relative path of this Node to the root
		 * @return toString()
		 */
		public String toString() {
			return this.getElement().getPath();
		}

	}
	
	/**
	 * Construct an empty FileTree
	 */
	public FileTree() {}
	/**
	 * Construct a FileTree resembling the passed root and all children.
	 * @param root
	 * @throws IOException
	 */
	public FileTree(File root) throws IOException {
		setRoot(root);
	}
	
	/**
	 * Establish a root for this FileTree
	 * Invocation reconstructs this FileTree
	 * 
	 * @param root
	 * @throws IOException
	 */
	public void setRoot(File root) throws IOException {
		this.root = new Node(root, null);
		fileMap = new HashMap<>();
		if(root.isDirectory()) {
			buildTree(this.root);
		}
	}
	
	/**
	 * @return (Node)this.root
	 */
	public Node getRoot() { return root; }
	
	/**
	 * Store all files passed directory, then iterate through child directories by passing them to this function
	 * 
	 * @param root
	 * @return
	 * @throws IOException
	 */
	private void buildTree(Node cur) throws IOException {
		File[] children = cur.getElement().listFiles();
		for(File child : children) {
			Node c = new Node(child, cur);
			fileMap.put(c.toString(), c);
			if(child.isDirectory()) buildTree(c);
		}
	}
	
	/**
	 * Flatten all enabled files into a singular array.
	 * Array is constructed through recursive digging from the root, therefore directory Nodes
	 * that are disabled will not have their children added to the result.
	 * 
	 * @return File[]
	 */
	public File[] getFileList() {
		if(root.isEnabled()) {
			if(root.isDir()) {
				List<File> result = new ArrayList<>();
				getFileList(root, result);
				return result.toArray(new File[result.size()]);
			} else return new File[] { root.getElement() };
		} return null;
	}
	private void getFileList(Node dir, List<File> list) {
		for(Node child : dir.getChildren()) {
			if(child.isEnabled()) {
				if(child.isDir()) getFileList(child, list);
				else list.add(child.getElement());
			}
		}
	}
	
	/**
	 * Disable the node canon with the String represented path
	 * 
	 * @param path
	 * @return true if path exists - false if invalid path
	 */
	public boolean disable(String path) {
		if(!fileMap.containsKey(path)) return false;
		fileMap.get(path).disable();
		return true;
	} public void disable(Node ref) { disable(ref.toString()); }
	
	/**
	 * Enable the node canon with the String represented path
	 * Child calls to this method are interpreted to override parental modifications.
	 * 
	 * @param path
	 * @return true if path exists - false if invalid
	 */
	public boolean enable(String path) {
		if(!fileMap.containsKey(path)) return false;
		fileMap.get(path).enable();
		return true;
	} public void enable(Node ref) { enable(ref.toString()); }
	
	
	/**
	 * Enable all parents and access child Nodes of the represented path, enabling them as well.
	 * 
	 * @param path
	 * @return true if path exists - false if invalid
	 */
	public boolean enableAllChilds(String path) {
		if(!fileMap.containsKey(path)) return false;
		Node ref = fileMap.get(path);
		ref.enable();
		for(Node c : ref.getChildren()) {
			c.enabled = true;
			if(c.isDir()) enableAllChilds(c);
		} return true;
	}
	public void enableAllChilds(Node ref) {
		ref.enable();
		for(Node c : ref.getChildren()) {
			c.enabled = true;
			if(c.isDir()) enableAllChilds(c);
		}
	}
	
	/**
	 * Constructs a newline delimited string of all file paths included in this system.
	 * Result is inclusive of all files irrespective of their enabled values.
	 * 
	 * @return toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(root.toString() + System.lineSeparator());
		for(Node child : root.getChildren()) {
			sb.append(child.toString() + System.lineSeparator());
			if(child.isDir()) toString(child, sb);
		} return sb.substring(0, sb.length()-1);
	}
	private void toString(Node dir, StringBuilder sb) {
		for(Node child : dir.getChildren()) {
			sb.append(child.toString() + System.lineSeparator());
			if(child.isDir()) toString(child, sb);
		}
	}
	
}
