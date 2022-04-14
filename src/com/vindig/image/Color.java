package com.vindig.image;

public class Color {
	
	public static int BIT_24 = 24; // TODO consider implementation for lower scale mapping so a user can design the graph depiction
	
	private float r, g, b;
	
	private int range = 255;
	
	/**
	 * Fabricate a color using float ranges.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Fabricate a color using byte values
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color(byte r, byte g, byte b) {
		this.r = r/range;
		this.g = g/range;
		this.b = b/range;
	}
	
	/**
	 * Writes this color to a byte array.
	 * @return byte[]
	 */
	public byte[] toPixel() { return new byte[] {(byte)(r*range), (byte)(g*range), (byte)(b*range)}; }
	
	/**
	 * Set this Color's rgb values
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Set this Color's rgb values
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(byte r, byte g, byte b) {
		this.r = r/range;
		this.g = g/range;
		this.b = b/range;
	}
	
	/**
	 * Instantiate a color using floats
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static Color getColor(float r, float g, float b) {
		return new Color(r, g, b);
	}
	
	/**
	 * Instantiate a color using bytes
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static Color getColor(byte r, byte g, byte b) {
		return new Color(r, g, b);
	}
	
}
