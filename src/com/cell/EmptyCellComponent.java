package com.cell;

import com.util.ByteGenerator;
import com.vindig.image.Color;
import com.vindig.manager.GraphManager;

public class EmptyCellComponent extends AbstractCellComponent {
	
	Color backgroundColor = GraphManager.getBackgroundColor();
	
	public EmptyCellComponent(int w, int h) {
		super(w, h);
		// TODO Auto-generated constructor stub
	}
	
	public EmptyCellComponent(int w, int h, Color backgroundColor) {
		super(w, h);
		this.backgroundColor = backgroundColor;
	}
	
	public void setBackgroundColor(Color newColor) { backgroundColor = newColor; }

	@Override
	public ByteGenerator generator() {
		return new ByteGenerator() {
			
			private int index = 0;
			private byte[] pixelData = backgroundColor.toPixel();
			private int incr = EmptyCellComponent.super.getWidth() * pixelData.length;
			
			byte[] buffer = new byte[incr];
			
			private int totalBytes = incr * EmptyCellComponent.super.getHeight();
			
			@Override
			public void release() { 
				buffer = null;
				pixelData = null;
			}
			
			@Override
			public byte[] yield() {
				if(index >= totalBytes) return null;
				for(int i = 0; i < incr; i++)
					buffer[i] = pixelData[index++%pixelData.length];
				return buffer;
			}
		};
	}

}
