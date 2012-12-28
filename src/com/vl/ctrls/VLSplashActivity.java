package com.vl.ctrls;

import com.vl.common.VLRectF;
import com.vl.drawing.VLColor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

public class VLSplashActivity extends VLBaseActivity {
	
	private class VLSplashActivity_View extends VLBaseLayout {
		public VLSplashActivity_View(Context context) {
			super(context);
		}
		
		@SuppressLint("DrawAllocation")
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			if(imageScale != 0) {
				VLRectF rcBnds = this.getBounds();
				float thisScale = rcBnds.getWidth() / rcBnds.getHeight();
				VLRectF rcImage = new VLRectF(rcBnds);
				if(scaleToFitAll) {
					if(imageScale < thisScale)
						rcImage.setHeight(rcImage.getWidth() / imageScale);
					else
						rcImage.setWidth(rcImage.getHeight() * imageScale);
				} else {
					if(imageScale < thisScale)
						rcImage.setHeight(rcImage.getWidth() / imageScale);
					else
						rcImage.setWidth(rcImage.getHeight() * imageScale);
					rcImage.moveToX(rcBnds.getMiddleX() - rcImage.getWidth()/2);
					rcImage.moveToY(rcBnds.getMiddleY() - rcImage.getHeight()/2);
				}
				VLCtrlsUtils.setFrameToView(imageView, rcImage);
				return;
			}
			super.onLayout(changed, l, t, r, b);
		}
		
		public ImageView imageView;
		public float imageScale = 0;
		public boolean scaleToFitAll = false;
	}
	
	VLSplashActivity_View _view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_view = new VLSplashActivity_View(this);
		this.setContentView(_view);
	}
	
	protected void setSplashImageResId(int imageResId, Resources resources) {
		_view.imageView = new ImageView(this);
		_view.imageView.setBackgroundResource(imageResId);
        ViewGroup.LayoutParams layouts = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        _view.addView(_view.imageView, layouts);
	}
	
	protected void setSplashImageResId(int imageResId, Resources resources, VLColor colorBack,
			boolean stretchable, boolean scaleToFitAll) {
		if(stretchable) {
			setSplashImageResId(imageResId, resources);
			return;
		}
		_view.setBackgroundColor(colorBack.toIntColor());
		Drawable drawable = resources.getDrawable(imageResId);
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		_view.imageScale = width / (float)height;
		_view.scaleToFitAll = scaleToFitAll;
		
		_view.imageView = new ImageView(this);
		_view.imageView.setBackgroundDrawable(drawable);
		_view.addView(_view.imageView);
	}
}
