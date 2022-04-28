package com.vindig.image;

public class Color {
	
	public static final float OPAQUE = 1.f;
	public static final float TRANSPARENT = 0.f;
	
	private float r, g, b, a;
	
	private final int range = 255;
	
	/**
	 * Fabricate a color using float ranges.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Fabricate a color using byte values
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color(byte r, byte g, byte b, byte a) {
		this.r = r/range;
		this.g = g/range;
		this.b = b/range;
		this.a = a/range;
	}
	
	/**
	 * Writes this color to a byte array.
	 * @return byte[]
	 */
	public byte[] toPixel() { return new byte[] {(byte)(r*range), (byte)(g*range), (byte)(b*range), (byte)(a*range)}; }
	
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
	public static Color getColor(float r, float g, float b, float a) {
		return new Color(r, g, b, a);
	}
	
	/**
	 * Instantiate a color using bytes
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static Color getColor(byte r, byte g, byte b, byte a) {
		return new Color(r, g, b, a);
	}
	
}
