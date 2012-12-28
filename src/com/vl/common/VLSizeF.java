package com.vl.common;

import com.vl.drawing.VLColor;

public class VLSizeF {
	public float width = 0;
	public float height = 0;
	
	public VLSizeF() {
	}
	
	public VLSizeF(VLSizeF other) {
		this.width = other.width;
		this.height = other.height;
	}
	
	public VLSizeF(float vwidth, float vheight) {
		width = vwidth;
		height = vheight;
	}
	
	public void roundValues() {
		width = Math.round(width);
		height = Math.round(height);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof VLColor))
			return false;
		VLSizeF other = (VLSizeF)o;
		return this.width == other.width
				&& this.height == other.height;
	}
}
