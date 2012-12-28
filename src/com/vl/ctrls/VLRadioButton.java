package com.vl.ctrls;

import com.vl.common.VLRectF;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Gravity;

public class VLRadioButton extends VLBaseButton {
	
	private Bitmap _imageChecked;
	private Bitmap _imageUnchecked;
	int _radioGravity = Gravity.LEFT;
	
	public VLRadioButton(Context context) {
		super(context);
		this.setTextGravity(Gravity.LEFT);
	}
	
	@Override
	protected void onDrawContent(Canvas canvas) {
		VLRectF rcContent = this.getRectOfContent();
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		
		boolean isChecked = isTouched() || isPressed();
		Bitmap bmp = isChecked ? _imageChecked : _imageUnchecked;
		
		VLRectF rcImage = new VLRectF(rcContent);
		if((_radioGravity & Gravity.RIGHT) == Gravity.RIGHT)
			rcImage.left = rcImage.right - rcImage.getHeight();
		else
			rcImage.right = rcImage.left + rcImage.getHeight();
		if(bmp != null) {
			VLGraphicsUtils.drawBitmap(canvas, bmp, rcImage, true);
		}
		
		int distX = VLCtrlsUtils.partOfDisplayMinSideRounded(0.02f);
		if(distX > rcContent.getWidth()/4)
			distX = (int) (rcContent.getWidth()/4);
		
		VLRectF rcText = new VLRectF(rcContent);
		if((_radioGravity & Gravity.RIGHT) == Gravity.RIGHT)
			rcText.right = rcImage.left - distX;
		else
			rcText.left = rcImage.right + distX;
		
		drawTitle(canvas, getTitle(), rcText, paint);
	}
	
	public boolean isChecked() {
		return isPressed();
	}
	
	public void setIsChecked(boolean checked) {
		setIsPressed(checked);
	}
	
	public void setImageChecked(Bitmap bmp) {
		if(_imageChecked != bmp) {
			_imageChecked = bmp;
			invalidate();
		}
	}
	
	public void setImageUnchecked(Bitmap bmp) {
		if(_imageUnchecked != bmp) {
			_imageUnchecked = bmp;
			invalidate();
		}
	}
}
