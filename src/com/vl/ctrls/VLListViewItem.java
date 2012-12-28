package com.vl.ctrls;

import com.vl.common.VLBase64;
import com.vl.common.VLBase64DecoderException;
import com.vl.common.VLRectF;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.FloatMath;

public class VLListViewItem extends VLBaseListViewItem {
	
	private VLColor _colorBack = VLColor.createGray(251/255f);
	private VLColor _colorBackSel = VLColor.createGray(150/255f);
	private VLColor _colorBorder = new VLColor(190/255f, 190/255f, 182/255f);
	private float _borderWidth = 1.0f;
	private float _cornerRadiusRatio = 0.04f;
	private VLRectF _insetsRatios = new VLRectF(0.05f, 0f, 0.12f, 0f);
	private Bitmap _accessoryImage = null;
	private static Bitmap _defaultAccessoryImage = null;
	private float _shadowSizeRatio = 0.025f;
	private VLColor _colorShadow1 = new VLColor(0, 0, 0, 1.0f);
	private VLColor _colorShadow2 = new VLColor(0.0f, 0.0f, 0.0f, 0.0f);
	
	public VLListViewItem(Context context) {
		super(context);
		_accessoryImage = getDefaultAccessoryImage();
	}
	
	protected VLRectF getBoundsNoShadow() {
		VLRectF rcBnds = this.getBounds();
		float shadowSize = VLCtrlsUtils.partOfDisplayMinSideRounded(_shadowSizeRatio);
		rcBnds.left += shadowSize;
		rcBnds.right -= shadowSize;
		if(isFirstItem())
			rcBnds.top += shadowSize;
		if(isLastItem())
			rcBnds.bottom -= shadowSize;
		return rcBnds;
	}
	
	protected int getCornerRadius() {
		int cornerRadius = (int)VLCtrlsUtils.partOfDisplayMinSideRounded(_cornerRadiusRatio);
		return cornerRadius;
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void draw(Canvas canvas) {
		VLRectF rcBndsFull = this.getBounds();
		VLRectF rcBnds = this.getBoundsNoShadow();
		float cornerRadius = getCornerRadius();
		float shadowSize = VLCtrlsUtils.partOfDisplayMinSideRounded(_shadowSizeRatio);
		
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		
		if(shadowSize > 0) {
			VLRectF rect = new VLRectF();
			GradientDrawable gradient = null;
			int[] colors = {_colorShadow1.getHexArgbValue(), _colorShadow2.getHexArgbValue()};
			{
				rect.left = rcBndsFull.left;
				rect.top = rcBnds.top;
				if(isFirstItem())
					rect.top += cornerRadius;
				rect.bottom = rcBnds.bottom;
				if(isLastItem())
					rect.bottom -= cornerRadius;
				rect.right = rcBnds.left + cornerRadius;
				gradient = new GradientDrawable(Orientation.RIGHT_LEFT, colors);
				gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				//gradient.setDither(true);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
			} if(isFirstItem()) {
				rect.left = rcBndsFull.left;
				rect.top = rcBndsFull.top;
				rect.setWidth(shadowSize + cornerRadius);
				rect.setHeight(shadowSize + cornerRadius);
				gradient = new GradientDrawable(Orientation.BR_TL, colors);
				gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
				gradient.setGradientRadius(shadowSize + cornerRadius);
				gradient.setGradientCenter(1.0f, 1.0f);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
				
				rect.left = rcBnds.left + cornerRadius;
				rect.top = rcBndsFull.top;
				rect.right = rcBnds.right - cornerRadius;
				rect.bottom = rcBnds.top + cornerRadius;
				gradient = new GradientDrawable(Orientation.BOTTOM_TOP, colors);
				gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
				
				rect.left = rcBndsFull.right - shadowSize - cornerRadius;
				rect.top = rcBndsFull.top;
				rect.setWidth(shadowSize + cornerRadius);
				rect.setHeight(shadowSize + cornerRadius);
				gradient = new GradientDrawable(Orientation.BL_TR, colors);
				gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
				gradient.setGradientRadius(shadowSize + cornerRadius);
				gradient.setGradientCenter(0.0f, 1.0f);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
			}
			 {
				rect.left = rcBnds.right - cornerRadius;
				rect.top = rcBnds.top;
				if(isFirstItem())
					rect.top += cornerRadius;
				rect.bottom = rcBnds.bottom;
				if(isLastItem())
					rect.bottom -= cornerRadius;
				rect.right = rcBndsFull.right;
				gradient = new GradientDrawable(Orientation.LEFT_RIGHT, colors);
				gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
			}
			if(isLastItem()) {
				rect.left = rcBndsFull.left;
				rect.top = rcBndsFull.bottom - shadowSize - cornerRadius;
				rect.setWidth(shadowSize + cornerRadius);
				rect.setHeight(shadowSize + cornerRadius);
				gradient = new GradientDrawable(Orientation.TR_BL, colors);
				gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
				gradient.setGradientRadius(shadowSize + cornerRadius);
				gradient.setGradientCenter(1.0f, 0.0f);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
				
				rect.left = rcBnds.left + cornerRadius;
				rect.top = rcBnds.bottom - cornerRadius;
				rect.right = rcBnds.right - cornerRadius;
				rect.bottom = rcBndsFull.bottom;
				gradient = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
				gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
				
				rect.left = rcBnds.right - cornerRadius;
				rect.top = rcBnds.bottom - cornerRadius;
				rect.setWidth(shadowSize + cornerRadius);
				rect.setHeight(shadowSize + cornerRadius);
				gradient = new GradientDrawable(Orientation.TL_BR, colors);
				gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
				gradient.setGradientRadius(shadowSize + cornerRadius);
				gradient.setGradientCenter(0.0f, 0.0f);
				gradient.setBounds(rect.toRect());
				gradient.draw(canvas);
			}
		}
		
		VLColor colorBack = isSelected() ? _colorBackSel : _colorBack;
		Path pathLine = new Path();
		pathLine.moveTo(rcBnds.left, rcBnds.getMiddleY());
		Path pathFill = new Path();
		pathFill.moveTo(rcBnds.left, rcBnds.getMiddleY());
		if(isFirstItem()) {
			if(cornerRadius > 0) {
				pathLine.lineTo(rcBnds.left, rcBnds.top + cornerRadius);
				pathLine.arcTo(new RectF(rcBnds.left, rcBnds.top, cornerRadius*2, cornerRadius*2), 180, 90);
				pathLine.lineTo(rcBnds.right - cornerRadius, rcBnds.top);
				pathLine.arcTo(new RectF(rcBnds.right - cornerRadius*2, rcBnds.top, rcBnds.right, cornerRadius*2), -90, 90);
				pathLine.lineTo(rcBnds.right, rcBnds.getMiddleY());
				
				pathFill.lineTo(rcBnds.left, rcBnds.top + cornerRadius);
				pathFill.arcTo(new RectF(rcBnds.left, rcBnds.top, cornerRadius*2, cornerRadius*2), 180, 90);
				pathFill.lineTo(rcBnds.right - cornerRadius, rcBnds.top);
				pathFill.arcTo(new RectF(rcBnds.right - cornerRadius*2, rcBnds.top, rcBnds.right, cornerRadius*2), -90, 90);
				pathFill.lineTo(rcBnds.right, rcBnds.getMiddleY());
			} else {
				pathLine.lineTo(rcBnds.left, rcBnds.top);
				pathLine.lineTo(rcBnds.right, rcBnds.top);
				pathLine.lineTo(rcBnds.right, rcBnds.getMiddleY());
				
				pathFill.lineTo(rcBnds.left, rcBnds.top);
				pathFill.lineTo(rcBnds.right, rcBnds.top);
				pathFill.lineTo(rcBnds.right, rcBnds.getMiddleY());
			}
		} else {
			pathLine.lineTo(rcBnds.left, rcBnds.top);
			pathLine.moveTo(rcBnds.right, rcBnds.top);
			pathLine.lineTo(rcBnds.right, rcBnds.getMiddleY());
			
			pathFill.lineTo(rcBnds.left, rcBnds.top);
			pathFill.lineTo(rcBnds.right, rcBnds.top);
			pathFill.lineTo(rcBnds.right, rcBnds.getMiddleY());
		}
		if(isLastItem()) {
			if(cornerRadius > 0) {
				pathLine.lineTo(rcBnds.right, rcBnds.bottom - cornerRadius);
				pathLine.arcTo(new RectF(rcBnds.right - cornerRadius*2, rcBnds.bottom - cornerRadius*2,
						rcBnds.right, rcBnds.bottom), 0, 90);
				pathLine.lineTo(rcBnds.left + cornerRadius/2, rcBnds.bottom);
				pathLine.arcTo(new RectF(rcBnds.left, rcBnds.bottom - cornerRadius*2,
						rcBnds.left + cornerRadius*2, rcBnds.bottom), 90, 90);
				pathLine.lineTo(rcBnds.left, rcBnds.getMiddleY());
				
				pathFill.lineTo(rcBnds.right, rcBnds.bottom - cornerRadius);
				pathFill.arcTo(new RectF(rcBnds.right - cornerRadius*2, rcBnds.bottom - cornerRadius*2,
						rcBnds.right, rcBnds.bottom), 0, 90);
				pathFill.lineTo(rcBnds.left + cornerRadius/2, rcBnds.bottom);
				pathFill.arcTo(new RectF(rcBnds.left, rcBnds.bottom - cornerRadius*2,
						rcBnds.left + cornerRadius*2, rcBnds.bottom), 90, 90);
				pathFill.lineTo(rcBnds.left, rcBnds.getMiddleY());
			} else {
				pathLine.lineTo(rcBnds.right, rcBnds.bottom);
				pathLine.lineTo(rcBnds.left, rcBnds.bottom);
				pathLine.lineTo(rcBnds.left, rcBnds.getMiddleY());
				
				pathFill.lineTo(rcBnds.right, rcBnds.bottom);
				pathFill.lineTo(rcBnds.left, rcBnds.bottom);
				pathFill.lineTo(rcBnds.left, rcBnds.getMiddleY());
			}
		} else {
			pathLine.lineTo(rcBnds.right, rcBnds.bottom);
			pathLine.lineTo(rcBnds.left, rcBnds.bottom);
			pathLine.lineTo(rcBnds.left, rcBnds.getMiddleY());
			
			pathFill.lineTo(rcBnds.right, rcBnds.bottom);
			pathFill.lineTo(rcBnds.left, rcBnds.bottom);
			pathFill.lineTo(rcBnds.left, rcBnds.getMiddleY());
		}
		pathFill.close();
		
		paint.setColor(colorBack.toIntColor());
		paint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawPath(pathFill, paint);
		
		paint.setColor(_colorBorder.toIntColor());
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(_borderWidth);
		canvas.drawPath(pathLine, paint);
		
		if(_accessoryImage != null) {
			VLRectF rcRightArea = new VLRectF(rcBnds);
			rcRightArea.left = (int)FloatMath.ceil(rcRightArea.right - (_insetsRatios.right + _shadowSizeRatio) * rcBnds.getWidth());
			VLRectF rcImage = new VLRectF(rcRightArea);
			rcImage.left = (int)Math.round(rcRightArea.getMiddleX() - _accessoryImage.getWidth()/2);
			rcImage.setWidth(_accessoryImage.getWidth());
			rcImage.top = (int)Math.round(rcRightArea.getMiddleY() - _accessoryImage.getHeight()/2);
			rcImage.setHeight(_accessoryImage.getHeight());
			VLGraphicsUtils.drawBitmap(canvas, _accessoryImage, rcImage, true);
		}
	}
	
	public static Bitmap getDefaultAccessoryImage() {
		if(_defaultAccessoryImage == null) {
			final String sData = "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";
			byte[] buffer = null;
			try {
				buffer = VLBase64.decode(sData);
			} catch (VLBase64DecoderException e) {
				e.printStackTrace();
			}
			if(buffer != null) {
				_defaultAccessoryImage = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
			}
		}
		return _defaultAccessoryImage;
	}

	public VLColor getColorShadow1() {
		return _colorShadow1;
	}
	
	public void setColorShadow1(VLColor colorShadow1) {
		if(!_colorShadow1.equals(colorShadow1)) {
			_colorShadow1 = colorShadow1;
			invalidate();
		}
	}

	public VLColor getColorShadow2() {
		return _colorShadow2;
	}
	
	public void setColorShadow2(VLColor colorShadow2) {
		if(!_colorShadow2.equals(colorShadow2)) {
			_colorShadow2 = colorShadow2;
			invalidate();
		}
	}

	public float getShadowSizeRatio() {
		return _shadowSizeRatio;
	}
	
	public void setShadowSizeRatio(float shadowSizeRatio) {
		_shadowSizeRatio = shadowSizeRatio;
	}

	public float getCornerRadiusRatio() {
		return _cornerRadiusRatio;
	}
	
	public void setCornerRadiusRatio(float cornerRadiusRatio) {
		_cornerRadiusRatio = cornerRadiusRatio;
	}
}


