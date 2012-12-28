package com.vl.ctrls;

import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLImagesCache;
import com.vl.drawing.VLPaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

public class VLImageView extends VLBaseLayout {
	
	private int _imageResId = 0;
	private VLSizeF _shadowOffset = new VLSizeF();
	private ImageView.ScaleType _scaleType = ImageView.ScaleType.FIT_XY;
	
	public VLImageView(Context context) {
		super(context);
		this.setBackgroundColor(Color.TRANSPARENT);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		VLRectF rcBnds = this.getBounds();
		if(_imageResId != 0) {
			Bitmap bmp = VLImagesCache.getInstance().getImageByResource(_imageResId, this.getContext().getResources());
			if(bmp != null) {
				VLRectF rcImageArea = new VLRectF(rcBnds);
				float shadowRadius = 0;
				if(_shadowOffset.width != 0 || _shadowOffset.height != 0) {
					shadowRadius = (Math.abs(_shadowOffset.width) + Math.abs(_shadowOffset.height)) / 2;
					if(_shadowOffset.width > 0)
						rcImageArea.right -= _shadowOffset.width + shadowRadius;
					else if(_shadowOffset.width < 0)
						rcImageArea.left += -_shadowOffset.width + shadowRadius;
					if(_shadowOffset.height > 0)
						rcImageArea.bottom -= _shadowOffset.height + shadowRadius;
					else if(_shadowOffset.height < 0)
						rcImageArea.top += -_shadowOffset.height + shadowRadius;
				}
				VLRectF rcImage = new VLRectF(rcImageArea);
				if(_scaleType != ImageView.ScaleType.FIT_XY) {
					float width = bmp.getWidth();
					float height = bmp.getHeight();
					float scaleImage = width / height;
					float scaleThis = rcImageArea.getWidth() / rcImageArea.getHeight();
					if(_scaleType == ImageView.ScaleType.CENTER_INSIDE) {
						if(scaleImage > scaleThis) {
							rcImage.setHeight(rcImage.getWidth() / scaleImage);
						} else if(scaleImage < scaleThis) {
							rcImage.setWidth(rcImage.getHeight() * scaleImage);
						}
						rcImage.moveToX(rcImageArea.getMiddleX() - rcImage.getWidth()/2);
						rcImage.moveToY(rcImageArea.getMiddleY() - rcImage.getHeight()/2);
					}
				}
				VLPaint paint = new VLPaint();
				paint.setAntiAlias(true);
				if(_shadowOffset.width != 0 || _shadowOffset.height != 0) {
					VLPaint paintShadow = new VLPaint();
					paintShadow.setShadowLayer(shadowRadius, _shadowOffset.width, _shadowOffset.height, Color.BLACK);
					paintShadow.setColor(Color.GRAY);
					paintShadow.setAlpha(64);
					ColorMatrix cm = new ColorMatrix();
					cm.setSaturation(0);
					ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
					paintShadow.setColorFilter(filter);
					VLGraphicsUtils.drawBitmap(canvas, bmp, rcImage, paintShadow, true);
				}
				VLGraphicsUtils.drawBitmap(canvas, bmp, rcImage, paint, true);
			}
		}
	}

	public int getImageResource() {
		return _imageResId;
	}
	
	public void setImageResource(int imageResId) {
		if(_imageResId != imageResId) {
			_imageResId = imageResId;
			this.invalidate();
		}
	}

	public VLSizeF getShadowOffset() {
		return _shadowOffset;
	}
	
	public void setShadowOffset(VLSizeF shadowOffset) {
		if(shadowOffset == null)
			shadowOffset = new VLSizeF();
		if(!_shadowOffset.equals(shadowOffset))
		{
			_shadowOffset = shadowOffset;
			if(_imageResId != 0)
				this.invalidate();
		}
	}

	public ImageView.ScaleType getScaleType() {
		return _scaleType;
	}
	
	public void setScaleType(ImageView.ScaleType scaleType) {
		if(_scaleType != scaleType) {
			_scaleType = scaleType;
			if(_imageResId != 0)
				this.invalidate();
		}
	}
}
