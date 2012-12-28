package com.vl.ctrls;

import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class VLImagesPartsButton extends VLBaseButton {
	
	public VLImagesPartsButton(Context context) {
		super(context);
		this.setBackgroundColor(Color.TRANSPARENT);
	}
	
	private void drawImage(Canvas canvas, Bitmap image, VLRectF rect, float xMax, float yMax, VLPaint paint) {
		if(xMax >= rect.right && yMax >= rect.bottom) {
			VLGraphicsUtils.drawBitmap(canvas, image, rect, paint, true);
			return;
		}
		VLRectF rcClip = new VLRectF(rect);
		if(xMax < rect.right)
			rcClip.setWidth(xMax - rcClip.left);
		if(yMax < rect.bottom)
			rcClip.setHeight(yMax - rcClip.top);
		canvas.save();
		canvas.clipRect(rcClip.toRect());
		VLGraphicsUtils.drawBitmap(canvas, image, rect, paint, true);
		canvas.restore();
	}
	
	@Override
	protected void onDrawBackground(Canvas canvas) {
		super.onDrawBackground(canvas);
		
		VLRectF rcBnds = getBounds();
		if(rcBnds.getWidth() < 1 || rcBnds.getHeight() < 1)
			return;
		Bitmap imageL = this.getLeftImage();
		Bitmap imageM = this.getMiddleImage();
		Bitmap imageR = this.getRightImage();
		if(imageL == null || imageM == null || imageR == null)
			return;
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		VLRectF rcL = new VLRectF(rcBnds);
		rcL.setWidth(Math.round(rcL.getHeight() * imageL.getWidth() / imageL.getHeight()));
		VLRectF rcR = new VLRectF(rcBnds);
		rcR.setWidth(Math.round(rcR.getHeight() * imageR.getWidth() / imageR.getHeight()));
		rcR.moveToX(rcBnds.right - rcR.getWidth());
		VLGraphicsUtils.drawBitmap(canvas, imageL, rcL, paint, true);
		VLGraphicsUtils.drawBitmap(canvas, imageR, rcR, paint, true);
		VLSizeF szM = rcBnds.getSize();
		szM.width = Math.round(szM.height * imageM.getWidth() / imageM.getHeight());
		for(float x = rcL.right; szM.width > 0 ; x += szM.width) {
			float xMax = rcBnds.right - rcR.getWidth();
			if(x >= xMax)
				break;
			VLRectF rcM = new VLRectF(rcBnds);
			rcM.setSize(szM);
			rcM.moveToX(x);
			this.drawImage(canvas, imageM, rcM, xMax, rcBnds.bottom, paint);
		}
	}
	
	protected Bitmap getLeftImage() {
		return null;
	}
	
	protected Bitmap getMiddleImage() {
		return null;
	}
	
	protected Bitmap getRightImage() {
		return null;
	}
}
