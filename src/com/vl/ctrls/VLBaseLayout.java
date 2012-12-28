package com.vl.ctrls;

import com.vl.common.VLPointF;
import com.vl.common.VLRectF;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class VLBaseLayout extends LinearLayout {
	
	public VLBaseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
	}
	
	public VLBaseLayout(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
	}
	
	public VLRectF getBounds() {
		VLRectF res = new VLRectF(0, 0, getWidth(), getHeight());
		return res;
	}
	
	public VLRectF getFrame() {
		VLRectF res = new VLRectF(this.getLeft(), getTop(), getRight(), getBottom());
		return res;
	}
	
	public void setFrame(VLRectF value) {
		this.layout((int)value.left, (int)value.top, (int)value.right, (int)value.bottom);
	}
	
	public void layoutSubviews() {
		this.onLayout(true, getLeft(), getRight(), getRight(), getBottom());
	}
	
	public void onUpdateView() {
		
	}
	
	public void updateView() {
		this.onUpdateView();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}
	
	public VLPointF getLocationOnScreen() {
		int[] locations = new int[2];
		this.getLocationOnScreen(locations);
		VLPointF res = new VLPointF();
		res.x = locations[0];
		res.y = locations[1];
		return res;
	}
	
	public VLPointF convertPointToScreen(VLPointF pt) {
		VLPointF locScr = getLocationOnScreen();
		VLPointF res = new VLPointF();
		res.x = locScr.x = pt.x;
		res.y = locScr.y = pt.y;
		return res;
	}
}
