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
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLImagesCache;
import com.vl.system.VLSystemMediator;

public class MNCommunityView extends MNTabView {
	
	VLBaseButton _bnGetMore;
	VLBaseButton _bnSubmitReview;
	VLBaseButton _bnFacebook;
	VLBaseButton _bnTwitter;

	public MNCommunityView(Context context) {
		super(context);
		
		VLFontInfo bnFont = new VLFontInfo("Helvetica", false, false, 0, 0.07f);
		
		Bitmap bnImage = VLImagesCache.getInstance().getImageByResource(R.drawable.button, getResources());
		Bitmap bnImageH = VLImagesCache.getInstance().getImageByResource(R.drawable.button_sel, getResources());
		
		_bnGetMore = new VLBaseButton(context);
		_bnGetMore.setBackImage(bnImage);
		_bnGetMore.setBackImageTouched(bnImageH);
		_bnGetMore.setGravity(Gravity.CENTER);
		_bnGetMore.setFont(bnFont);
		_bnGetMore.setTitle("Get More from Us");
		_bnGetMore.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_bnGetMore);
		
		_bnSubmitReview = new VLBaseButton(context);
		_bnSubmitReview.setBackImage(bnImage);
		_bnSubmitReview.setBackImageTouched(bnImageH);
		_bnSubmitReview.setGravity(Gravity.CENTER);
		_bnSubmitReview.setFont(bnFont);
		_bnSubmitReview.setTitle("Submit a Review");
		_bnSubmitReview.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_bnSubmitReview);
		
		bnImage = VLImagesCache.getInstance().getImageByResource(R.drawable.fb_button, getResources());
		
		_bnFacebook = new VLBaseButton(context);
		_bnFacebook.setBackImage(bnImage);
		_bnFacebook.setGravity(Gravity.CENTER);
		_bnFacebook.setFont(bnFont);
		_bnFacebook.setTitle("Like us");
		_bnFacebook.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_bnFacebook);
		
		bnImage = VLImagesCache.getInstance().getImageByResource(R.drawable.twitter_button, getResources());
		
		_bnTwitter = new VLBaseButton(context);
		_bnTwitter.setBackImage(bnImage);
		_bnTwitter.setGravity(Gravity.CENTER);
		_bnTwitter.setFont(bnFont);
		_bnTwitter.setTitle("Follow us");
		_bnTwitter.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_bnTwitter);
		
		_bnGetMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(MNCommon.UrlGetMoreFromUs));
				VLSystemMediator.getInstance().getCurrentActivity().startActivity(intent);
			}
		});
		
		_bnSubmitReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=" + MNCommon.AppPackageName));
				VLSystemMediator.getInstance().getCurrentActivity().startActivity(intent);
			}
		});
		
		_bnFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(MNCommon.UrlLikeUs));
				VLSystemMediator.getInstance().getCurrentActivity().startActivity(intent);
			}
		});
		
		_bnTwitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(MNCommon.UrlFollowUs));
				VLSystemMediator.getInstance().getCurrentActivity().startActivity(intent);
			}
		});
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//super.onLayout(changed, l, t, r, b);
		
		VLRectF rcBnds = this.getBounds();
		int border = VLCtrlsUtils.partOfDisplayMinSideRounded(0.025f);
		VLRectF rcCtrls = rcBnds.rectWithInsets(border, border);
		
		int bnHeight = VLCtrlsUtils.partOfDisplayMinSideRounded(0.165f);
		
		VLRectF rcBnGetMore = new VLRectF(rcCtrls);
		rcBnGetMore.setHeight(bnHeight);
		rcBnGetMore.moveToY(rcCtrls.top + VLCtrlsUtils.partOfDisplayMinSideRounded(0.15f));
		_bnGetMore.setFrame(rcBnGetMore);
		
		VLRectF rcBnSubmitReview = new VLRectF(rcCtrls);
		rcBnSubmitReview.setHeight(bnHeight);
		rcBnSubmitReview.moveToY(rcBnGetMore.bottom + border);
		_bnSubmitReview.setFrame(rcBnSubmitReview);
		
		VLRectF rcBnFacebook = new VLRectF(rcCtrls);
		rcBnFacebook.setHeight(bnHeight);
		rcBnFacebook.moveToY(rcBnSubmitReview.bottom + border);
		_bnFacebook.setFrame(rcBnFacebook);
		
		VLRectF rcBnTwitter = new VLRectF(rcCtrls);
		rcBnTwitter.setHeight(bnHeight);
		rcBnTwitter.moveToY(rcBnFacebook.bottom + border);
		_bnTwitter.setFrame(rcBnTwitter);
	}
}
