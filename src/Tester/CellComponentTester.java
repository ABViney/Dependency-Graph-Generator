package Tester;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vindig.image.Bitmap;
import com.vindig.image.Color;

public class CellComponentTester {
	
	public static void main(String[] args) throws IOException {
		/**
		 * Initially to test a Cell's independent draw instance
		 * Became a tester for my implementation of BitMap writing, proofing the bitmap aligns with it's
		 * padding and the pixels aren't skewed.
		 * Bitmap can be transferred to PNG which uses a chunk system that, at current,
		 */
//		int w = 125*87*2+1;
//		int h = 9*127*2+1;
		int w = 11;
		int h = 11;
		int bits = 24;
		
//		int paddingSize = ( 4 + ( w * (bits/8) % 4 ) % 4 );
//		final int fileSize = 54 + w*h* (bits / 8) + paddingSize*h;
//		// 0x1234
//		
//		int i = 54;
//		byte[] bmpHeader = bitmapByteArray(w, h, bits);
//		int padding = ( 4 - ( w * (bits/8) % 4 )) % 4 ;
		
//		for(int y = 0; y < h; y++) {
//			int x;
//			for(x = 0; x < w; x++) {
//				if(x + y == 1020 || x == y) {
//					b[i + y*w + x*3 + 0] = 0;
//					b[i + y*w + x*3 + 1] = 0;
//					b[i + y*w + x*3 + 2] = 0;
//					continue;
//				}
//				b[i + (w*y) + x*3 + 0] = (byte)255;
//				b[i + (w*y) + x*3 + 1] = (byte)255;
//				b[i + (w*y) + x*3 + 2] = (byte)255;
//			}
//			for(int c = 0; c < padding; c++) {
//				b[i+x+c] = 0;
//			}
//		}
		
		Bitmap bmp = new Bitmap(w, h, bits);
		
//		Color c = new Color(0f,0f,0f);
		Color blu = new Color(0.f,0.f,1.f);
		Color wht = new Color(1f, 1f, 1f);
//		fos.write(b, 0, 54);
//		ByteArrayInputStream bais = new ByteArrayInputStream
//		for(int y = 0; y < h; y++) {
//			for(int x = 0; x < w; x++) {
//				if(y == x || x + 1 == y || x - 1 == y) {
////					fos.write(blk.getColor(), 0, 3);
////					c = blk.getColor();
////					iis.read(c);
//					bmp.write(blu.toPixel());
//					continue;
//				} 
////				fos.write(wht.getColor(), 0, 3);
////				fos.write(c.getColor(), 0, 3);
////				c = new Color((float)(x/w), 1.f - ((float)(x/w)), (float)(y/h));
////				c = wht.getColor();
////				iis.read(c);
//				bmp.write(wht.toPixel());
//			}
////			fos.write(new byte[padding], 0, padding);
//		} 
		
//		
		
		bmp = new Bitmap(3, 3, 24);
		Color ylw = new Color(0f, 1.f, 1.f);
		Color ppl = new Color(1.f, 0.f, 1.f);
		for(int i = 0; i < 3; i++) {
			for(int c = 0; c < 3; c++) {
				if( (i==0 && (c==0 || c == 2)) || (i==2 && (c==0 || c==2))) bmp.write(ylw.toPixel());
				else bmp.write(ppl.toPixel());
			}
		}
		
		
		
		BufferedImage img = ImageIO.read(new DataInputStream(new ByteArrayInputStream(bmp.getBitmap(), 0, bmp.size())));
		
		FileOutputStream fos = new FileOutputStream(new File("test.bmp"));
		fos.write(bmp.getBitmap(), 0, bmp.size());
		
		ImageIO.write(img, "png" , new File("test.png"));
		
//		fos.flush();
//		fos.close();
		
//		Byte[] b = new Byte[54+w*h*(bits/8)];
		// Bitmap header -- 14 bytes
		// DIB header -- 
		// Extra bit masks -- (3,4) * (4 bytes per DWORD) -- optional
		// Color table -- semi-optional ( OPTIONAL IF bit_depth > 8 )
		// Gap1 -- optional
		// Pixel array -- required -- each row padded to multiple of 4 bytes -- 32bit divisible row length
		// Gap2 -- optional
		// ICC color profile -- optional 
//		
//		b[i++] = 0x42; // B
//		b[i] = 0x4D; // M 
//		
//		while(++i < 6) 
//			b[i] = 0x0; // RESERVED - FILESIZE [offset 2-5]
//		
//		b[i++] = 0x0; // RESERVED - NOT NEEDED
//		b[i++] = 0x0;
//		
//		b[i++] = 0x0; // RESERVED - NOT NEEDED
//		b[i] = 0x0;
//		
//		while(++i < 14)
//			b[i] = 0x0; // RESERVED - STARTING ADDRESS [offset 10-13]
//		
//		
		
		
		
			
		
	}
	
	/**
	 * Generate a binary header info for bitmap output type.
	 * To be used to feed into a [figure out what I'm doing] stream
	 * to generate a png or compressed bitmap to reduce filesize and load.
	 * 
	 * @param width
	 * @param height
	 * @param bitCount
	 * @return byte[54]
	 */
	static byte[] bitmapByteArray(int width, int height, int bitCount) {
		final byte fileHeaderSize = 14;
		final byte infoHeaderSize = 40;
		final int paddingSize = ( 4 + ( width * (bitCount/8) % 4 ) % 4 );
		final int fileSize = fileHeaderSize + infoHeaderSize + width*height * (bitCount / 8) + paddingSize * height;
		
		byte[] bmp = new byte[54];
		/** File Type */
		bmp[0] = 0x42;
		bmp[1] = 0x4D;
		/** File Size */
		bmp[2] = (byte)fileSize;
		bmp[3] = (byte)(fileSize >> 8);
		bmp[4] = (byte)(fileSize >> 16);
		bmp[5] = (byte)(fileSize >> 24);
		/** Reserved 1 */
		bmp[6] = 0;
		bmp[7] = 0;
		/** Reserved 2 */
		bmp[8] = 0;
		bmp[9] = 0;
		/** Pixel data offset */
		bmp[10] = 0x36;
		bmp[11] = 0;
		bmp[12] = 0;
		bmp[13] = 0;
		
		/** Information Header Section */
		bmp[14] = infoHeaderSize;
		bmp[15] = 0;
		bmp[16] = 0;
		bmp[17] = 0;
		/** Image width */
		bmp[18] = (byte)width;
		bmp[19] = (byte)(width >> 8);
		bmp[20] = (byte)(width >> 16);
		bmp[21] = (byte)(width >> 24);
		/** Image height */
		bmp[22] = (byte)height;
		bmp[23] = (byte)(height >> 8);
		bmp[24] = (byte)(height >> 16);
		bmp[25] = (byte)(height >> 24);
		/** Planes */
		bmp[26] = 0x1;
		bmp[27] = 0;
		/** Bits per pixel */
		bmp[28] = (byte)bitCount;
		bmp[29] = (byte)(bitCount >> 8);
		/** Compression */
		bmp[30] = 0;
		bmp[31] = 0;
		bmp[32] = 0;
		bmp[33] = 0;
		/** Image size ^ */
		bmp[34] = 0;
		bmp[35] = 0;
		bmp[36] = 0;
		bmp[37] = 0;
		/** X pixels per meter */
		bmp[38] = 0;
		bmp[39] = 0;
		bmp[40] = 0;
		bmp[41] = 0;
		/** Y pixels per meter */
		bmp[42] = 0;
		bmp[43] = 0;
		bmp[44] = 0;
		bmp[45] = 0;
		/** Total colors */ //TODO put logic where implemented
		bmp[46] = 0;
		bmp[47] = 0;
		bmp[48] = 0;
		bmp[49] = 0;
		/** Important colors */ //TODO logic here as well
		bmp[50] = 0;
		bmp[51] = 0;
		bmp[52] = 0;
		bmp[53] = 0;
		/** End of Header */
		
		return bmp;
	}
}