package com.util;

/**
	 * Extension of Generator permitting primitive exclusive yielding.
	 * 
	 */
public interface ByteGenerator extends Generator {
	
	/**
	 * Provide the next expected array of byte values or null if ByteGenerator is empty.
	 * 
	 * @return byte[]
	 */
	@Override
	byte[] yield();
}
