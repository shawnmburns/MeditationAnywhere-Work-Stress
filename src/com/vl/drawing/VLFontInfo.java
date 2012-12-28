package com.vl.drawing;

public class VLFontInfo implements Cloneable {
	
	public String name = "Helvetica";
	public boolean isBold = false;
	public boolean isItalic = false;
	public float height = 15.0f;
	public float heightRatio = 0.0f;
	
	public VLFontInfo() {
		
	}
	
	public VLFontInfo(String vname, boolean visBold, boolean visItalic, float vheight, float vheightRatio) {
		name = vname;
		isBold = visBold;
		isItalic = visItalic;
		height = vheight;
		heightRatio = vheightRatio;
	}
	
	public VLFontInfo(boolean visBold, boolean visItalic, float vheight, float vheightRatio) {
		isBold = visBold;
		isItalic = visItalic;
		height = vheight;
		heightRatio = vheightRatio;
	}
	
	public void assign(Object other) {
		VLFontInfo otherInfo = (VLFontInfo)other;
		this.name = otherInfo.name;
		this.isBold = otherInfo.isBold;
		this.isItalic = otherInfo.isItalic;
		this.height = otherInfo.height;
		this.heightRatio = otherInfo.heightRatio;
	}
	
	@Override
	public Object clone() {
		VLFontInfo newObj = new VLFontInfo();
		newObj.assign(this);
		return newObj;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof VLColor))
			return false;
		VLFontInfo other = (VLFontInfo)o;
		return this.name.equals(other.name)
				&& this.isBold == other.isBold
				&& this.isItalic == other.isItalic
				&& this.height == other.height
				&& this.heightRatio == other.heightRatio;
	}
}
