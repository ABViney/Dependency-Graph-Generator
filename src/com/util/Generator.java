package com.util;

/**
 * A generator over a parameterized data type into an iteration over results processed from that data.
 * Intended purpose is to infer anonymous class instantiation for enabling a larger scale project to
 * generate in chunks using Generator instances to yield the expected buffered data from their respective
 * iterations.
 * 
 * @author viney
 *
 * @param <T> - dataType to yield
 */
public interface Generator<T> {
	
	/**
	 * Release any resources the generator may still contain after
	 * completion.
	 * Responsibility falls on the caller to ensure this method is called
	 * only when necessary, as remaining data in the generator may be lost.
	 */
	void release();
	
	/**
	 * Provide the next expected element or null if Generator is empty.
	 * @return U result
	 */
	T yield();
	
}
