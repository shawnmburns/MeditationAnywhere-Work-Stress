package com.meditatenowWorkStress.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import com.meditatenowWorkStress.R;
import com.meditatenowWorkStress.views.MNBaseView;
import com.vl.common.VLGeometry;
import com.vl.common.VLRectF;
import com.vl.ctrls.VLCtrlsUtils;
import com.vl.ctrls.VLTextView;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;

public class MNTapToStartView extends MNBaseView {

	ImageView _ivLotus;
	VLTextView _tvTapToStart;
	
	public MNTapToStartView(Context context) {
		super(context);
		
		this.setBackgroundResource(R.drawable.bg);
		
		_ivLotus = new ImageView(context);
		_ivLotus.setBackgroundResource(R.drawable.lotus);
		this.addView(_ivLotus);
		
		VLFontInfo font = new VLFontInfo("Helvetica", false, false, 0, 0.09f);
		
		_tvTapToStart = new VLTextView(context);
		_tvTapToStart.setFont(font);
		_tvTapToStart.setText("Tap to Start");
		_tvTapToStart.setTextColor(VLColor.ColorsCommon.White);
		_tvTapToStart.setGravity(Gravity.CENTER);
		this.addView(_tvTapToStart);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		VLRectF rcBnds = this.getBounds();
		
		VLRectF rcLotus = new VLRectF(rcBnds);
		rcLotus.setWidth(rcBnds.getWidth()*0.6f);
		Drawable image = _ivLotus.getBackground();
		rcLotus.setHeight(rcLotus.getWidth() / ((float)image.getIntrinsicWidth() / image.getIntrinsicHeight()));
		rcLotus.moveTo(rcBnds.getMiddleX() - rcLotus.getWidth()/2, rcBnds.getHeight()*0.1f);
		rcLotus = VLGeometry.roundRect(rcLotus);
		VLCtrlsUtils.setFrameToView(_ivLotus, rcLotus);
		
		VLRectF rcTvTap = new VLRectF(rcBnds);
		rcTvTap.setHeight(VLCtrlsUtils.partOfDisplayMinSideRounded(_tvTapToStart.getFont().heightRatio*1.1f));
		rcTvTap.moveToY(rcLotus.bottom);
		rcTvTap = VLGeometry.roundRect(rcTvTap);
		VLCtrlsUtils.setFrameToView(_tvTapToStart, rcTvTap);
	}
}
