package com.meditatenowWorkStress.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.meditatenowWorkStress.views.MNTabView;
import com.vl.common.VLRectF;

public class MNPracticeView extends MNTabView {
	
	private MNTapToStartView _tapToStartView;
	private MNAudioView _audioView;
	private static MNPracticeView _instance;
	
	public static MNPracticeView getInstance() {
		return _instance;
	}

	public MNPracticeView(Context context) {
		super(context);
		_instance = this;
		
		_audioView = new MNAudioView(context);
		this.addView(_audioView);
		
		_tapToStartView = new MNTapToStartView(context);
		this.addView(_tapToStartView);
		
		_tapToStartView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTap();
			}
		});
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		VLRectF rcBnds = this.getBounds();
		_tapToStartView.setFrame(rcBnds);
		_audioView.setFrame(rcBnds);
	}
	
	public void onTap() {
		if(_tapToStartView.getVisibility() == View.VISIBLE) {
			_tapToStartView.setVisibility(View.GONE);
			_audioView.setPaused(false);
		} else {
			if(_audioView.paused()) {
				_audioView.setPaused(false);
			} else {
				_audioView.setPaused(true);
			}
		}
	}
	
	public void stopPlaying() {
		_tapToStartView.setVisibility(View.VISIBLE);
		_audioView.stopPlaying();
	}
	
	public void startPlaying() {
		stopPlaying();
		_tapToStartView.setVisibility(View.GONE);
		_audioView.setPaused(false);
	}
}
