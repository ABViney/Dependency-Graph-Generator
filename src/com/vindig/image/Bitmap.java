package com.vindig.image;

import java.nio.ByteBuffer;

public class Bitmap {
	
	private final int width;
	private final int height;
	private final int paddingSize;
	private final int fileSize;
	private final int rowLength;
	
	private final byte[] buffer;

	private int cursor = 0x36;
	private int yIndex = 0;
	
	/**
	 * Bitmap filetype representation.
	 * Object handles header generation and byte rounding.
	 * 
	 * @param width
	 * @param height
	 * @param bitDepth
	 */
	public Bitmap(int w, int h, int bitDepth) {
		System.out.println("Creating bitmap:\nWidth: " +w +" Height: " + h+ " BitDepth: " + bitDepth);
		this.width = w;
		this.height = h;
//		this.paddingSize = ( 4 - ( width * (bitDepth/8) % 4 ) % 4); // No clue why this was written this way
		this.paddingSize = ((width*(bitDepth/8) % 4 == 0)) ? 0 : 4-(width * (bitDepth/8) % 4)%4;
		this.fileSize = 0x36+(width*height*(bitDepth/8)+(paddingSize*height));
		this.rowLength = w * (bitDepth/8);
		
		this.buffer = new byte[fileSize];
		
		/** 54 byte allocation for header information */
		/** File Type */
		buffer[0] = 0x42; buffer[1] = 0x4D;
		/** File Size */
//		buffer[2] = (byte)fileSize; buffer[3] = (byte)(fileSize >> 8); buffer[4] = (byte)(fileSize >> 16); buffer[5] = (byte)(fileSize >> 24); // Improper truncation
		byte[] fileSizeBytes = ByteBuffer.allocate(4).putInt(fileSize).array();
		buffer[2] = fileSizeBytes[0]; buffer[3] = fileSizeBytes[1]; buffer[4] = fileSizeBytes[2]; buffer[5] = fileSizeBytes[3];
		/** Reserved 1 */
		buffer[6] = 0; buffer[7] = 0;
		/** Reserved 2 */
		buffer[8] = 0; buffer[9] = 0;
		/** Pixel data offset */
		buffer[10] = 0x36; buffer[11] = 0; buffer[12] = 0; buffer[13] = 0;		
		/** Information buffer Section */
		buffer[14] = 0x28; buffer[15] = 0; buffer[16] = 0; buffer[17] = 0;
		/** Image width */
		buffer[18] = (byte)width; buffer[19] = (byte)(width >> 8); buffer[20] = (byte)(width >> 16); buffer[21] = (byte)(width >> 24);
		/** Image height */
		buffer[22] = (byte)height; buffer[23] = (byte)(height >> 8); buffer[24] = (byte)(height >> 16); buffer[25] = (byte)(height >> 24);
		/** Planes */
		buffer[26] = 0x1; buffer[27] = 0;
		/** Bits per pixel */
		buffer[28] = (byte)bitDepth; buffer[29] = (byte)(bitDepth >> 8);
		/** Compression */
		buffer[30] = 0; buffer[31] = 0; buffer[32] = 0; buffer[33] = 0;
		/** Image size ^ */
		buffer[34] = 0; buffer[35] = 0; buffer[36] = 0; buffer[37] = 0;
		/** X pixels per meter */
		buffer[38] = 0; buffer[39] = 0; buffer[40] = 0; buffer[41] = 0;
		/** Y pixels per meter */
		buffer[42] = 0; buffer[43] = 0; buffer[44] = 0; buffer[45] = 0;
		/** Total colors */ //TODO put logic where implemented
		buffer[46] = 0; buffer[47] = 0; buffer[48] = 0; buffer[49] = 0;
		/** Important colors */ //TODO logic here as well
		buffer[50] = 0; buffer[51] = 0; buffer[52] = 0; buffer[53] = 0;
		/** End of Header */
	}
	
	/**
	 * Unsafe write operation to paste the parameter byte array into this
	 * Bitmap's buffer.
	 * 
	 * @param b
	 * @return true if byte array is not null
	 */
	public boolean write(byte[] b) {
		if(b == null) return false;
		for(byte info : b) {
			buffer[cursor++] = info;
			if((cursor-54-(paddingSize*yIndex)) % rowLength == 0) {
				cursor += paddingSize;
				++yIndex;
			}
		} return true;
	}
	
	
	public byte[] getBitmap() { return buffer; }
	
	/**
	 * Check if this Bitmap is complete.
	 * @return - true if buffer is full
	 */
	public boolean isFull() { return cursor == fileSize; }
	public int imageWidth() { return width; }
	public int imageHeight() { return height; }
	/**
	 * Get the integer value of bytes this Bitmap reserves.
	 * @return
	 */
	public int size() { return fileSize; }
	public int cursorPos() { return cursor; }
	public float writtenPercent() { return cursor/fileSize; }
	public String writtenPercentAsString() { return String.format("%d/%d", cursor, fileSize); }
	public int padSize() { return paddingSize; }
	
}
