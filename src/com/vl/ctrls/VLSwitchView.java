package com.vl.ctrls;

import com.vl.common.VLGeometry;
import com.vl.common.VLRectF;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.logic.VLLogicUtilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class VLSwitchView extends VLBaseButton {
	
	private Bitmap _imageThumb;
	private VLTextView _lbOn;
	private VLTextView _lbOff;
	private ImageView _thumbView;

	public VLSwitchView(Context context, Bitmap imageThumb, int backImageResourceId, String textON, String textOFF) {
		super(context);
		this.setBackgroundResource(backImageResourceId);
		
		_imageThumb = imageThumb;
		
		_lbOn = new VLTextView(context);
		_lbOn.setClickable(false);
		_lbOn.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		_lbOn.setTextColor(VLColor.createGray(1.0f));
		_lbOn.setText(textON);
		_lbOn.setFont(this.font());
		this.addView(_lbOn);
		
		_lbOff = new VLTextView(context);
		_lbOff.setClickable(false);
		_lbOff.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		_lbOff.setTextColor(VLColor.createGray(1.0f));
		_lbOff.setText(textOFF);
		_lbOff.setFont(this.font());
		this.addView(_lbOff);
		
		_thumbView = new ImageView(context);
		_thumbView.setScaleType(ScaleType.FIT_XY);
		_thumbView.setClickable(false);
		_thumbView.setBackgroundColor(Color.TRANSPARENT);
		_thumbView.setImageBitmap(_imageThumb);
		this.addView(_thumbView);
	}
	
	@Override
	protected void onDrawBackground(Canvas canvas) {
		
	}
	
	private void layoutControls(boolean animated) {
		VLRectF rcBnds = this.getBounds();
		
		float border = VLCtrlsUtils.partOfDisplayMinSideRounded(0.005f);
		VLRectF rcCtrls = rcBnds.rectWithInsets(border, border);
		rcCtrls = VLGeometry.roundRect(rcCtrls);
		rcCtrls.move(0, 1);
		
		VLRectF rcLbOn = new VLRectF(rcCtrls);
		VLRectF rcLbOff = new VLRectF(rcCtrls);
		float lbwidth = (int)(rcCtrls.getWidth()/2);
		rcLbOn.setWidth(lbwidth);
		rcLbOff.setWidth(lbwidth);
		rcLbOff.moveToX(rcCtrls.right - lbwidth);
		rcLbOn = VLGeometry.roundRect(rcLbOn);
		rcLbOff = VLGeometry.roundRect(rcLbOff);
		_lbOn.setFrame(rcLbOn);
		_lbOff.setFrame(rcLbOff);
		
		VLRectF rcThumbOn = rcLbOff;
		VLRectF rcThumbOff = rcLbOn;
		final VLRectF rcThumbCur = VLCtrlsUtils.getFrameOfView(_thumbView);
		final VLRectF rcThumbNeeded = (this.isOn() ? rcThumbOn : rcThumbOff);
		if((!rcThumbCur.equals(rcThumbOn) && !rcThumbCur.equals(rcThumbOff)) || !animated) {
			VLCtrlsUtils.setFrameToView(_thumbView, rcThumbNeeded);
		} else {
			VLLogicUtilities.VLDurationProcessHelper helper = new VLLogicUtilities.VLDurationProcessHelper();
			helper.start(0.0, 1.0, 120, new VLLogicUtilities.VLDurationProcessHelper.Listener() {
				public void onUpdate(double valCur, double valStart, double valStop, double durationRatio) {
					VLRectF rect = new VLRectF(rcThumbCur);
					rect.moveToX( (int)Math.round( rcThumbCur.left + (rcThumbNeeded.left - rcThumbCur.left) * valCur ) );
					VLCtrlsUtils.setFrameToView(_thumbView, rect);
				}
				public void onEnd() {
					layoutControls(false);
				}
			});
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		this.layoutControls(false);
	}
	
	public boolean isOn() {
		return isPressed();
	}
	public void setIsOn(boolean isOn) {
		if(isOn() != isOn) {
			setIsPressed(isOn);
			this.layoutControls(true);
		}
	}
	
	public void setFont(VLFontInfo font) {
		if(font == null)
			font = new VLFontInfo();
		if(!this.font().equals(font)) {
			super.setFont(font);
			_lbOn.setFont(this.font());
			_lbOff.setFont(this.font());
		}
	}
}
