package com.record;

/**
 * Abstraction of record class to simplify representation and instantiation of low info Record definitions
 * @author viney
 *
 */
public abstract class AbstractRecord {
	private String name;
	
	public AbstractRecord(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
}
