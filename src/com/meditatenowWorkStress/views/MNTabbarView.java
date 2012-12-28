package com.meditatenowWorkStress.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.meditatenowWorkStress.R;
import com.vl.common.VLRectF;
import com.vl.ctrls.VLTabbarView;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLImagesCache;

public class MNTabbarView extends VLTabbarView {

	public MNTabbarView(Context context) {
		super(context);
		
		VLFontInfo font = new VLFontInfo("Helvetica", false, false, 0, 0.04f);
		this.setFont(font);
	}
	
	@Override
	protected void onDrawItemBackground(Canvas canvas, int itemIndex, Item item, VLRectF itemRect) {
		boolean selected = (itemIndex == this.getSelectedItemIndex());
		Bitmap image = null;
		if(selected)
			image = VLImagesCache.getInstance().getImageByResource(R.drawable.tab_item_bg_selected, getResources());
		else
			image = VLImagesCache.getInstance().getImageByResource(R.drawable.tab_item_bg_unselected, getResources());
		VLGraphicsUtils.drawBitmap(canvas, image, itemRect, true);
	}
}
