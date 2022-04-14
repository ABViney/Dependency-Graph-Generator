package com.cell;

import com.util.Generator;
import com.vindig.image.Color;

public class EmptyCellComponent extends AbstractCellComponent {
	
	Color backgroundColor;
	
	public EmptyCellComponent(int w, int h) {
		super(w, h);
		// TODO Auto-generated constructor stub
	}
	
	public void setBackgroundColor(Color newColor) { backgroundColor = newColor; }

	@Override
	public Generator<Byte[]> generator() {
		return new Generator<Byte[]>() {
			
			private int index = 0;
			private byte[] pixelData = backgroundColor.toPixel();
			private int incr = EmptyCellComponent.super.getWidth() * pixelData.length;
			
			Byte[] buffer = new Byte[incr];
			
			private int totalBytes = incr * EmptyCellComponent.super.getHeight();
			
			@Override
			public void release() { 
				buffer = null;
				pixelData = null;
			}
			
			@Override
			public Byte[] yield() {
				if(index >= totalBytes) return null;
				for(int i = 0; i < incr; i++)
					buffer[index++%incr] = Byte.valueOf(pixelData[i%pixelData.length]);
				return buffer;
			}
		};
	}

}
