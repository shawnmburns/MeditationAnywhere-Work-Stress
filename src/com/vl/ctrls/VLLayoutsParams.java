package com.vl.ctrls;

import java.util.ArrayList;

import com.vl.common.VLGeometry;
import com.vl.common.VLRectF;

import android.view.View;
import android.widget.LinearLayout;

public class VLLayoutsParams {
	
	private class ItemInfo {
		public float heightRatio;
		public float heightRatioBefore;
		public float heightRatioAfter;
		public float widthRatio;
		public float widthRatioBefore;
		public float widthRatioAfter;
		public Object item;
	}
	
	private ArrayList<ItemInfo> _items = new ArrayList<VLLayoutsParams.ItemInfo>();
	private int _orientation = LinearLayout.VERTICAL;
	private OnLayoutHandler _onLayoutHandler;
	
	public VLLayoutsParams(int orientation) {
		_orientation = orientation;
	}
	
	private void onLayoutView(View view, VLRectF rect) {
		if(_onLayoutHandler != null) {
			if(_onLayoutHandler.onLayoutView(view, rect))
				return;
		}
		VLCtrlsUtils.setFrameToView(view, rect);
	}
	
	public void add(Object item, float heightRatio, float heightRatioBefore, float heightRatioAfter,
			float widthRatio, float widthRatioBefore, float widthRatioAfter) {
		ItemInfo itemInfo = new ItemInfo();
		itemInfo.item = item;
		itemInfo.heightRatio = heightRatio;
		itemInfo.heightRatioBefore = heightRatioBefore;
		itemInfo.heightRatioAfter = heightRatioAfter;
		itemInfo.widthRatio = widthRatio;
		itemInfo.widthRatioBefore = widthRatioBefore;
		itemInfo.widthRatioAfter = widthRatioAfter;
		_items.add(itemInfo);
	}
	
	public void add(Object item, float heightRatio, float heightRatioBefore, float heightRatioAfter) {
		add(item, heightRatio, heightRatioBefore, heightRatioAfter, 1, 0, 0);
	}
	
	private void layoutVertInRect(VLRectF rect, boolean roundFrames) {
		if(rect.getWidth() < 1 || rect.getHeight() < 1)
			return;
		float allHeightRatio = 0;
		for(ItemInfo item : _items) {
			allHeightRatio += item.heightRatio;
			allHeightRatio += item.heightRatioBefore;
			allHeightRatio += item.heightRatioAfter;
		}
		if(allHeightRatio == 0)
			return;
		float curHeightRatioBefore = 0;
		for(ItemInfo item : _items) {
			curHeightRatioBefore += item.heightRatioBefore;
			VLRectF rcView = new VLRectF(rect);
			rcView.setHeight( rect.getHeight() * (item.heightRatio / allHeightRatio) );
			rcView.moveToY( rect.top + rect.getHeight() * (curHeightRatioBefore / allHeightRatio) );
			curHeightRatioBefore += item.heightRatio;
			curHeightRatioBefore += item.heightRatioAfter;
			float allWidthRatio = item.widthRatioBefore + item.widthRatio + item.widthRatioAfter;
			rcView.setWidth( rect.getWidth() * item.widthRatio / allWidthRatio );
			rcView.moveToX( rect.left + rect.getWidth() * item.widthRatioBefore / allWidthRatio );
			if(roundFrames)
				rcView = VLGeometry.roundRect(rcView);
			if(View.class.isAssignableFrom(item.item.getClass())) {
				View view = (View)item.item;
				onLayoutView(view, rcView);
			} else if(VLLayoutsParams.class.isAssignableFrom(item.item.getClass())) {
				VLLayoutsParams params = (VLLayoutsParams)item.item;
				params.layoutInRect(rcView, roundFrames);
			}
		}
	}
	
	private void layoutHorzInRect(VLRectF rect, boolean roundFrames) {
		if(rect.getWidth() < 1 || rect.getHeight() < 1)
			return;
		float allWidthRatio = 0;
		for(ItemInfo item : _items) {
			allWidthRatio += item.widthRatio;
			allWidthRatio += item.widthRatioBefore;
			allWidthRatio += item.widthRatioAfter;
		}
		if(allWidthRatio == 0)
			return;
		float curWidthRatioBefore = 0;
		for(ItemInfo item : _items) {
			curWidthRatioBefore += item.widthRatioBefore;
			VLRectF rcView = new VLRectF(rect);
			rcView.setWidth( rect.getWidth() * (item.widthRatio / allWidthRatio) );
			rcView.moveToX( rect.left + rect.getWidth() * (curWidthRatioBefore / allWidthRatio) );
			curWidthRatioBefore += item.widthRatio;
			curWidthRatioBefore += item.widthRatioAfter;
			float allHeightRatio = item.heightRatioBefore + item.heightRatio + item.heightRatioAfter;
			rcView.setHeight( rect.getHeight() * item.heightRatio / allHeightRatio );
			rcView.moveToY( rect.top + rect.getHeight() * item.heightRatioBefore / allHeightRatio );
			if(roundFrames)
				rcView = VLGeometry.roundRect(rcView);
			if(View.class.isAssignableFrom(item.item.getClass())) {
				View view = (View)item.item;
				onLayoutView(view, rcView);
			} else if(VLLayoutsParams.class.isAssignableFrom(item.item.getClass())) {
				VLLayoutsParams params = (VLLayoutsParams)item.item;
				params.layoutInRect(rcView, roundFrames);
			}
		}
	}
	
	public void layoutInRect(VLRectF rect, boolean roundFrames) {
		if(_orientation == LinearLayout.HORIZONTAL)
			layoutHorzInRect(rect, roundFrames);
		else
			layoutVertInRect(rect, roundFrames);
	}
	
	public static abstract class OnLayoutHandler {
		public abstract boolean onLayoutView(View view, VLRectF rect);
	}
	
	public void setOnLayoutHandler(OnLayoutHandler onLayoutHandler) {
		_onLayoutHandler = onLayoutHandler;
	}
}


