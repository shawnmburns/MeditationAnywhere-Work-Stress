package com.meditatenowWorkStress.views;

import com.meditatenowWorkStress.R;
import com.vl.common.VLGeometry;
import com.vl.common.VLRectF;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLImagesCache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MNProgressView extends MNBaseView {
	
	private float _value = 0.0f;

	public MNProgressView(Context context) {
		super(context);

		this.setBackgroundResource(R.drawable.loadbar_empty);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		VLRectF rcBnds = getBounds();
		VLRectF rcInner = new VLRectF(rcBnds);
		rcInner.setHeight(rcBnds.getHeight() * 0.33f);
		rcInner.moveToY(rcBnds.getMiddleY() - rcInner.getHeight()/2);
		rcInner.setWidth(rcBnds.getWidth() * 0.928f);
		rcInner.moveToX(rcBnds.getMiddleX() - rcInner.getWidth()/2);
		if(_value > 0) {
			Bitmap imageRound = VLImagesCache.getInstance().getImageByResource(R.drawable.loadbar_round, getResources());
			VLRectF rcRound = new VLRectF(rcInner);
			float stepPxl = rcInner.getWidth() / 320f;
			float curLeft = rcInner.left;
			while(true) {
				rcRound.left = curLeft - rcRound.getHeight()/2;
				rcRound.setWidth(rcRound.getHeight());
				VLGraphicsUtils.drawBitmap(canvas, imageRound, rcRound, true);
				if(curLeft >= (rcInner.left + rcInner.getWidth() * _value - 0.001f))
					break;
				curLeft += stepPxl;
			}
		}
	}
	
	public VLRectF getSliderArea() {
		VLRectF rcBnds = getBounds();
		VLRectF rcInner = new VLRectF(rcBnds);
		rcInner.setWidth(rcBnds.getWidth() * 0.928f);
		rcInner.moveToX(rcBnds.getMiddleX() - rcInner.getWidth()/2);
		return VLGeometry.roundRect(rcInner);
	}
	
	public void setValue(float value) {
		value = Math.max(Math.min(value, 1f), 0f);
		if(_value != value) {
			_value = value;
			invalidate();
		}
	}
}
