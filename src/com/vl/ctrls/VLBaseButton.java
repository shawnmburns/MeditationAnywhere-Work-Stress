package com.vl.ctrls;

import com.vl.common.VLGeometry;
import com.vl.common.VLPointF;
import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;

public class VLBaseButton extends VLBaseLayout {
	private boolean _isTouched = false;
	private boolean _isPressed = false;
	
	String _title = "";
	int _textGravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
	Bitmap _image;
	VLFontInfo _font = new VLFontInfo();
	VLColor _textColor = new VLColor();
	VLColor _shadowColor = new VLColor();
	VLSizeF _shadowOffset = new VLSizeF();
	VLRectF _contentInsetsRel = new VLRectF();
	VLRectF _imageInsetsRel = new VLRectF();
	float _imageTitleGap = 0.1f;
	
	Bitmap _backImage;
	Bitmap _backImageTouched;
	
	public VLBaseButton(Context context) {
		super(context);
		this.setBackgroundColor(Color.TRANSPARENT);
		
		setClickable(true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.isEnabled())
			return super.onTouchEvent(event);
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		VLPointF pt = new VLPointF(x, y);
		VLRectF rcBnds = this.getBounds();
		if(action == MotionEvent.ACTION_DOWN) {
			this.setIsTouched(true);
			return true;
		} else if(action == MotionEvent.ACTION_MOVE) {
			this.setIsTouched(rcBnds.containsPoint(pt));
			return true;
		} else if(action == MotionEvent.ACTION_UP) {
			this.setIsTouched(false);
			if(rcBnds.containsPoint(pt)) {
				this.performClick();
			}
			return true;
		} else if(action == MotionEvent.ACTION_CANCEL) {
			this.setIsTouched(false);
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		onDrawBackground(canvas);
		onDrawContent(canvas);
	}
	
	protected void onDrawBackground(Canvas canvas) {
		if(_backImage != null || _backImageTouched != null) {
			VLRectF rcBnds = getBounds();
			Bitmap image = null;
			if(this.isTouched()) {
				image = _backImageTouched;
				if(image == null)
					image = _backImage;
			} else {
				image = _backImage;
			}
			if(image != null) {
				VLPaint paint = new VLPaint();
				paint.setAntiAlias(true);
				VLGraphicsUtils.drawBitmap(canvas, image, rcBnds, paint, true);
			}
		}
	}
	
	protected void onDrawContent(Canvas canvas) {
		if(_image != null || _title.length() > 0) {
			VLRectF rcContent = this.getRectOfContent();
			VLPaint paint = new VLPaint();
			paint.setAntiAlias(true);
			if(_image != null) {
				int distHorz = (int) (Math.min(rcContent.getWidth(), rcContent.getHeight()) * _imageTitleGap);
				VLRectF rcImage = new VLRectF(rcContent);
				rcImage.setWidth(rcContent.getHeight());
				VLSizeF szImage = new VLSizeF(_image.getWidth(), _image.getHeight());
				VLRectF rcImageToDraw = VLGeometry.rectOfFitToRectWithSize(rcImage, szImage);
				
				float side = Math.min(rcImageToDraw.getWidth(), rcImageToDraw.getHeight());
				rcImageToDraw.left += side * _imageInsetsRel.left;
				rcImageToDraw.top += side * _imageInsetsRel.top;
				rcImageToDraw.right -= side * _imageInsetsRel.right;
				rcImageToDraw.bottom -= side * _imageInsetsRel.bottom;
				rcImageToDraw = VLGeometry.roundRect(rcImageToDraw);
				VLGraphicsUtils.drawBitmap(canvas, _image, rcImageToDraw, true);
				
				rcContent.left = rcImageToDraw.right + distHorz;
			}
			if(_title.length() > 0) {
				VLRectF rcText = new VLRectF(rcContent);
				this.drawTitle(canvas, _title, rcText, paint);
			}
		}
	}
	
	protected void drawTitle(Canvas canvas, String title, VLRectF rcText, VLPaint paint) {
		VLRectF rcContent = new VLRectF(rcText);
		rcText.setSize(VLGraphicsUtils.getTextSize(title, _font));
		rcText.moveToY(rcContent.getMiddleY() - rcText.getHeight() / 2);
		if((_textGravity & Gravity.LEFT) == Gravity.LEFT)
			rcText.moveToX(rcContent.left);
		else if((_textGravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL)
			rcText.moveToX(rcContent.getMiddleX() - rcText.getWidth() / 2);
		else if((_textGravity & Gravity.RIGHT) == Gravity.RIGHT)
			rcText.moveToX(rcContent.right - rcText.getWidth());
		rcText = VLGeometry.roundRect(rcText);
		if(_shadowOffset.width != 0 || _shadowOffset.height != 0) {
			rcText.move(_shadowOffset.width, _shadowOffset.height);
			paint.setColor(_shadowColor.toIntColor());
			VLGraphicsUtils.drawText(title, _font, canvas, rcText.left, rcText.top, paint);
			rcText.move(-_shadowOffset.width, -_shadowOffset.height);
		}
		paint.setColor(_textColor.toIntColor());
		VLGraphicsUtils.drawText(title, _font, canvas, rcText.left, rcText.top, paint);
	}
	
	protected VLRectF getRectOfContent() {
		VLRectF rcBnds = getBounds();
		float side = Math.min(rcBnds.getWidth(), rcBnds.getHeight());
		VLRectF result = new VLRectF(rcBnds);
		result.left += side * _contentInsetsRel.left;
		result.top += side * _contentInsetsRel.top;
		result.right -= side * _contentInsetsRel.right;
		result.bottom -= side * _contentInsetsRel.bottom;
		result = VLGeometry.roundRect(result);
		return result;
	}
	
	void setIsTouched(boolean isTouched) {
		if(_isTouched != isTouched) {
			_isTouched = isTouched;
			this.invalidate();
		}
	}
	
	public boolean isTouched() {
		return _isTouched;
	}

	public boolean isPressed() {
		return _isPressed;
	}
	
	public void setIsPressed(boolean isPressed) {
		if(_isPressed != isPressed) {
			_isPressed = isPressed;
			this.invalidate();
		}
	}
	
	public String getTitle() {
		return _title;
	}
	
	public void setTitle(String title) {
		if(title == null)
			title = "";
		if(!_title.equals(title)) {
			_title = title;
			this.invalidate();
		}
	}
	
	public void setImage(Bitmap image) {
		if(_image != image) {
			_image = image;
			this.invalidate();
		}
	}
	
	public void setBackImage(Bitmap image) {
		if(_backImage != image) {
			_backImage = image;
			this.invalidate();
		}
	}
	
	public void setBackImageTouched(Bitmap image) {
		if(_backImageTouched != image) {
			_backImageTouched = image;
			this.invalidate();
		}
	}
	
	public void setContentInsetsRel(VLRectF contentInsetsRel) {
		if(contentInsetsRel == null)
			contentInsetsRel = new VLRectF();
		if(!_contentInsetsRel.equals(contentInsetsRel)) {
			_contentInsetsRel = new VLRectF(contentInsetsRel);
			this.invalidate();
		}
	}
	public void setContentInsetsRel(float allInsets) {
		this.setContentInsetsRel(new VLRectF(allInsets, allInsets, allInsets, allInsets));
	}
	
	public void setImageInsetsRel(VLRectF imageInsetsRel) {
		if(imageInsetsRel == null)
			imageInsetsRel = new VLRectF();
		if(!_imageInsetsRel.equals(imageInsetsRel)) {
			_imageInsetsRel = new VLRectF(imageInsetsRel);
			this.invalidate();
		}
	}
	public void setImageInsetsRel(float allInsets) {
		this.setImageInsetsRel(new VLRectF(allInsets, allInsets, allInsets, allInsets));
	}
	
	public void setImageTitleGap(float imageTitleGap) {
		if(_imageTitleGap != imageTitleGap) {
			_imageTitleGap = imageTitleGap;
			invalidate();
		}
	}
	
	public void setTextColor(VLColor textColor) {
		if(textColor == null)
			textColor = new VLColor();
		if(!_textColor.equals(textColor)) {
			_textColor = new VLColor(textColor);
			this.invalidate();
		}
	}
	
	public void setShadowColor(VLColor shadowColor) {
		if(shadowColor == null)
			shadowColor = new VLColor();
		if(!_shadowColor.equals(shadowColor)) {
			_shadowColor = new VLColor(shadowColor);
			this.invalidate();
		}
	}

	public void setShadowOffset(VLSizeF shadowOffset) {
		if(shadowOffset == null)
			shadowOffset = new VLSizeF();
		if(!_shadowOffset.equals(shadowOffset)) {
			_shadowOffset = new VLSizeF(shadowOffset);
			this.invalidate();
		}
	}
	
	public VLFontInfo font() {
		return _font;
	}
	
	public void setFont(VLFontInfo font) {
		if(font == null)
			font = new VLFontInfo();
		if(!_font.equals(font)) {
			_font = font;
			this.invalidate();
		}
	}
	
	public void setFontHeightRatio(float heightRatio) {
		if(_font.heightRatio != heightRatio) {
			VLFontInfo font = (VLFontInfo)_font.clone();
			font.heightRatio = heightRatio;
			this.setFont(font);
		}
	}

	public int getTextGravity() {
		return _textGravity;
	}
	
	public void setTextGravity(int textGravity) {
		if(_textGravity != textGravity) {
			_textGravity = textGravity;
			this.invalidate();
		}
	}
}
