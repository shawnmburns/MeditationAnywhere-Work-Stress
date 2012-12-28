package com.meditatenowWorkStress.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import com.meditatenowWorkStress.R;
import com.meditatenowWorkStress.api.MNCommon;
import com.meditatenowWorkStress.views.MNTabView;
import com.vl.common.VLRectF;
import com.vl.ctrls.VLBaseButton;
import com.vl.ctrls.VLCtrlsUtils;
import com.vl.ctrls.VLTextView;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLImagesCache;
import com.vl.system.VLSystemMediator;

public class MNAboutView extends MNTabView {
	
	VLTextView _tvInfo;
	VLBaseButton _bnFAQ;
	VLBaseButton _bnSite;

	public MNAboutView(Context context) {
		super(context);
		
		VLFontInfo font = new VLFontInfo("Helvetica", false, false, 0, 0.046f);
		
		_tvInfo = new VLTextView(context);
		_tvInfo.setText("MeditationAnywhere is designed to bring age-old, proven meditation practice to everyone through the use of modern-day, easy-to-use technology.\n\nHugh Byrne, a meditation expert with more than 20 years of experience in the art and science of teaching meditation, partnered with a team of seasoned technology start-up entrepreneurs to develop these practice lessons.");
		_tvInfo.setSingleLine(false);
		_tvInfo.setFont(font);
		_tvInfo.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_tvInfo);

		Bitmap bnImage = VLImagesCache.getInstance().getImageByResource(R.drawable.button, getResources());
		Bitmap bnImageH = VLImagesCache.getInstance().getImageByResource(R.drawable.button_sel, getResources());
		
		VLFontInfo bnFont = new VLFontInfo("Helvetica", true, false, 0, 0.05f);
		
		_bnFAQ = new VLBaseButton(context);
		_bnFAQ.setBackImage(bnImage);
		_bnFAQ.setBackImageTouched(bnImageH);
		_bnFAQ.setGravity(Gravity.CENTER);
		_bnFAQ.setFont(bnFont);
		_bnFAQ.setTitle("Common Questions");
		_bnFAQ.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_bnFAQ);
		
		_bnSite = new VLBaseButton(context);
		_bnSite.setBackImage(bnImage);
		_bnSite.setBackImageTouched(bnImageH);
		_bnSite.setGravity(Gravity.CENTER);
		_bnSite.setFont(bnFont);
		_bnSite.setTitle("MeditationAnywhere.com");
		_bnSite.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_bnSite);
		
		_bnFAQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(MNCommon.UrlFAQ));
				VLSystemMediator.getInstance().getCurrentActivity().startActivity(intent);
			}
		});
		
		_bnSite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(MNCommon.UrlMeditationAnywhere));
				VLSystemMediator.getInstance().getCurrentActivity().startActivity(intent);
			}
		});
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//super.onLayout(changed, l, t, r, b);
		
		VLRectF rcBnds = this.getBounds();
		int border = VLCtrlsUtils.partOfDisplayMinSideRounded(0.045f);
		VLRectF rcCtrls = rcBnds.rectWithInsets(border, border);
		
		VLRectF rcInfo = new VLRectF(rcCtrls);
		rcInfo.setHeight((int)(rcCtrls.getHeight()*0.65));
		VLCtrlsUtils.setFrameToView(_tvInfo, rcInfo);
		
		int bnHeight = VLCtrlsUtils.partOfDisplayMinSideRounded(0.125f);
		VLRectF rcBnFAQ = new VLRectF(rcCtrls);
		rcBnFAQ.setHeight(bnHeight);
		rcBnFAQ.setWidth(VLCtrlsUtils.partOfDisplayMinSideRounded(0.6f));
		rcBnFAQ.moveToY(rcInfo.bottom);
		rcBnFAQ.moveToX((int)(rcCtrls.getMiddleX() - rcBnFAQ.getWidth()/2));
		_bnFAQ.setFrame(rcBnFAQ);
		
		VLRectF rcBnSite = new VLRectF(rcCtrls);
		rcBnSite.setHeight(bnHeight);
		rcBnSite.setWidth(VLCtrlsUtils.partOfDisplayMinSideRounded(0.8f));
		rcBnSite.moveToY(rcBnFAQ.bottom + border/2);
		rcBnSite.moveToX((int)(rcCtrls.getMiddleX() - rcBnSite.getWidth()/2));
		_bnSite.setFrame(rcBnSite);
	}
}
