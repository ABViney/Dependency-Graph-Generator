package com.vindig.image;

import java.io.ByteArrayOutputStream;
import java.io.File;

import ar.com.hjg.pngj.FilterType;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineByte;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;



public class PNG {
	
	int width;
	int height;
	int row = 0;
	
	int totalBytes;
	
	byte[] scanline;
	int cursor = 0;
	int pushedBytes = 0;
	
	ByteArrayOutputStream baos;
	PngWriter pngw;
	ImageLineByte ilb;
	ImageInfo imi;
	
	/**
	 * OutputStream based PNG writing utility 
	 * @param w
	 * @param h
	 * @param outputDest
	 */
	public PNG(int w, int h, File outputDest) {
		width = w;
		height = h;
		
		baos = new ByteArrayOutputStream();
		imi = new ImageInfo(w, h, 16, true); // width, height, (rbba), assume alpha nonstatic
		pngw = new PngWriter(outputDest, imi);
		pngw.setFilterType(FilterType.FILTER_SUB); // run-length encoding
		ilb = new ImageLineByte(imi); // scanline factory
		
		scanline = ilb.getScanlineByte();
	}
	
	/**
	 * Procedurally write lines of raw pixel data into this PNG.
	 * A pixel is defined as a series of 4 bytes (Red, Green, Blue, Alpha)
	 * Data is read until the end of scanline is reached, at which point the scanline is passed
	 * to the PNGWriter and a new scanline is assigned.
	 * Null data and overflow is avoided.
	 * 
	 * @param b byte array of pixel data
	 * @return true if a write took place, false if null data or PNG size reached
	 */
	public boolean write(byte[] b) {
		if(b == null || row == height) return false;
		
		for(int i = 0; i < b.length; i+=4) {
			
			scanline[cursor++] = b[i];
			scanline[cursor++] = b[i+1];
			scanline[cursor++] = b[i+2];
			scanline[cursor++] = b[i+3];
			
			if(cursor == scanline.length) {
				cursor = 0;
				pngw.writeRow(ilb);
				if(row == height) {
					pngw.end();
					pngw.close();
					scanline = null;
					return true;
				}
				else scanline = ilb.getScanlineByte();
			}
		} return true;
	}
}
