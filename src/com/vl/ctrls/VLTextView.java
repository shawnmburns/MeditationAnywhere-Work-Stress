package com.vl.ctrls;

import com.vl.common.VLGeometry;
import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Gravity;

public class VLTextView extends VLBaseLayout {
	
	private VLFontInfo _font = new VLFontInfo();
	private String _text = "";
	private VLColor _textColor = VLColor.ColorsCommon.Black;
	private int _gravity;
	private VLSizeF _shadowOffset = new VLSizeF();
	private VLColor _shadowColor = VLColor.ColorsCommon.Black;
	private boolean _singleLine = true;
	
	public VLTextView(Context context) {
		super(context);
		this.setBackgroundColor(Color.TRANSPARENT);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(_text.length() > 0) {
			VLRectF rcBnds = this.getBounds();
			VLRectF rcTextArea = new VLRectF(rcBnds);
			if(_singleLine) {
				if(_shadowOffset.width != 0 || _shadowOffset.height != 0) {
					if(_shadowOffset.width > 0)
						rcTextArea.right -= _shadowOffset.width;
					else if(_shadowOffset.width < 0)
						rcTextArea.left += -_shadowOffset.width;
					if(_shadowOffset.height > 0)
						rcTextArea.bottom -= _shadowOffset.height;
					else if(_shadowOffset.height < 0)
						rcTextArea.top += -_shadowOffset.height;
				}
				VLPaint paint = new VLPaint();
				paint.setColor(_textColor.toIntColor());
				VLSizeF szText = VLGraphicsUtils.getTextSize(_text, _font);
				VLRectF rcText = new VLRectF(rcTextArea);
				rcText.setSize(szText);
				if((_gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL)
					rcText.moveToY(rcTextArea.getMiddleY() - rcText.getHeight()/2);
				if((_gravity & Gravity.RIGHT) == Gravity.RIGHT)
					rcText.moveToX(rcTextArea.right - rcText.getWidth());
				else if((_gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL)
					rcText.moveToX(rcTextArea.getMiddleX() - rcText.getWidth()/2);
				rcText = VLGeometry.roundRect(rcText);
				if(_shadowOffset.width != 0 || _shadowOffset.height != 0) {
					VLRectF rcTextShadow = new VLRectF(rcText);
					rcTextShadow.left += _shadowOffset.width;
					rcTextShadow.top += _shadowOffset.height;
					VLPaint paintShadow = new VLPaint();
					paintShadow.setColor(_shadowColor.toIntColor());
					VLGraphicsUtils.drawText(_text, _font, canvas, rcTextShadow.left, rcTextShadow.top, paintShadow);
				}
				VLGraphicsUtils.drawText(_text, _font, canvas, rcText.left, rcText.top, paint);
			} else {
				TextPaint textPaint = new TextPaint();
				textPaint.setAntiAlias(true);
				textPaint.setColor(_textColor.toIntColor());
				VLGraphicsUtils.applyFontToPaint(textPaint, _font);
				StaticLayout textLayout = new StaticLayout(_text, textPaint, (int)rcTextArea.getWidth(),
						Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
				textLayout.draw(canvas);
			}
		}
	}
	
	public String getText() {
		return _text;
	}
	
	public void setText(String text) {
		if(text == null)
			text = "";
		if(!_text.equals(text)) {
			_text = text;
			this.invalidate();
		}
	}
	
	public VLColor getTextColor() {
		return _textColor;
	}
	
	public void setTextColor(VLColor color) {
		if(color == null)
			color = VLColor.ColorsCommon.Black;
		if(!_textColor.equals(color)) {
			_textColor = color;
			if(_text.length() > 0)
				this.invalidate();
		}
	}
	
	public VLFontInfo getFont() {
		return _font;
	}
	
	public void setFont(VLFontInfo font) {
		if(font == null)
			font = new VLFontInfo();
		if(!_font.equals(font)) {
			_font.assign(font);
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
	
	public int getGravity() {
		return _gravity;
	}
	
	@Override
	public void setGravity(int value) {
		super.setGravity(value);
		if(_gravity != value) {
			_gravity = value;
			if(_text.length() > 0)
				this.invalidate();
		}
	}
	
	public VLSizeF getShadowOffset() {
		return _shadowOffset;
	}
	
	public void setShadowOffset(VLSizeF shadowOffset) {
		if(shadowOffset == null)
			shadowOffset = new VLSizeF();
		if(!_shadowOffset.equals(shadowOffset)) {
			_shadowOffset = shadowOffset;
			if(_text.length() > 0)
				this.invalidate();
		}
	}
	
	public VLColor getShadowColor() {
		return _shadowColor;
	}
	
	public void setShadowColor(VLColor color) {
		if(color == null)
			color = VLColor.ColorsCommon.Black;
		if(!_shadowColor.equals(color)) {
			_shadowColor = color;
			if(_text.length() > 0)
				this.invalidate();
		}
	}
	
	public boolean singleLine() {
		return _singleLine;
	}
	
	public void setSingleLine(boolean singleLine) {
		if(_singleLine != singleLine) {
			_singleLine = singleLine;
			if(_text.length() > 0)
				this.invalidate();
		}
	}
}
