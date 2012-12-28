package com.vl.common;

import android.graphics.Rect;
import android.graphics.RectF;

public class VLRectF {
	public float left = 0;
	public float top = 0;
	public float right = 0;
	public float bottom = 0;
	
	public VLRectF() {
		
	}
	
	public VLRectF(float vleft, float vtop, float vright, float vbottom) {
		left = vleft;
		top = vtop;
		right = vright;
		bottom = vbottom;
	}
	
	public VLRectF(Rect rect) {
		left = rect.left;
		top = rect.top;
		right = rect.right;
		bottom = rect.bottom;
	}
	
	public VLRectF(VLRectF rect) {
		left = rect.left;
		top = rect.top;
		right = rect.right;
		bottom = rect.bottom;
	}
	
	public RectF toRectF() {
		RectF result = new RectF((float)left, (float)top, (float)right, (float)bottom);
		return result;
	}
	
	public Rect toRect() {
		Rect result = new Rect((int)left, (int)top, (int)right, (int)bottom);
		return result;
	}
	
	public float getWidth() {
		return right - left;
	}
	
	public float setWidth(float value) {
		right = left + value;
		return getWidth();
	}
	
	public float getHeight() {
		return bottom - top;
	}
	
	public float setHeight(float value) {
		bottom = top + value;
		return getHeight();
	}
	
	public float getMiddleX() {
		return left + getWidth()/2;
	}
	
	public void setMiddleX(float middleX) {
		this.moveToX(middleX - this.getWidth()/2);
	}
	
	public float getMiddleY() {
		return top + getHeight()/2;
	}
	
	public void setMiddleY(float middleY) {
		this.moveToY(middleY - this.getHeight()/2);
	}
	
	public VLPointF getCenterPoint() {
		return new VLPointF(getMiddleX(), getMiddleY());
	}
	
	public void setCenterPoint(VLPointF cp) {
		this.moveTo(cp.x - getWidth()/2, cp.y - getHeight()/2);
	}
	
	public boolean containsPoint(VLPointF pt) {
		if(right < left || bottom < top)
			return false;
		if(pt.x >= left && pt.x < right && pt.y >= top && pt.y < bottom)
			return true;
		return false;
	}
	
	public void move(float dx, float dy) {
		left += dx;
		right += dx;
		top += dy;
		bottom += dy;
	}
	
	public void moveTo(float x, float y) {
		this.move(x - left, y - top);
	}
	
	public void moveToX(float x) {
		this.moveTo(x, top);
	}
	
	public VLRectF moveToY(float y) {
		this.moveTo(left, y);
		return this;
	}
	
	public VLSizeF getSize() {
		return new VLSizeF(getWidth(), getHeight());
	}
	
	public void setSize(VLSizeF value) {
		setWidth(value.width);
		setHeight(value.height);
	}
	
	public VLRectF rectWithInsets(float insetX, float insetY) {
		VLRectF result = new VLRectF(this);
		result.left += insetX;
		result.top += insetY;
		result.right -= insetX;
		result.bottom -= insetY;
		return result;
	}
	
	public VLRectF rectWithInsets(VLRectF insets) {
		VLRectF result = new VLRectF(this);
		result.left += insets.left;
		result.top += insets.top;
		result.right -= insets.right;
		result.bottom -= insets.bottom;
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof VLRectF))
			return false;
		VLRectF other = (VLRectF)o;
		return this.left == other.left
			&& this.top == other.top
			&& this.right == other.right
			&& this.bottom == other.bottom;
	}
}
