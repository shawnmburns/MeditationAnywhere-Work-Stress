package com.meditatenowWorkStress;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.meditatenowWorkStress.R;
import com.meditatenowWorkStress.api.MNCommon;
import com.meditatenowWorkStress.api.MNSettingManager;
import com.meditatenowWorkStress.ui.MNAboutView;
import com.meditatenowWorkStress.ui.MNAudioView;
import com.meditatenowWorkStress.ui.MNCommunityView;
import com.meditatenowWorkStress.ui.MNPracticeView;
import com.meditatenowWorkStress.ui.MNSettingsView;
import com.meditatenowWorkStress.views.MNBaseActivity;
import com.meditatenowWorkStress.views.MNBaseView;
import com.meditatenowWorkStress.views.MNTabbarView;
import com.vl.common.VLRectF;
import com.vl.ctrls.VLCtrlsUtils;
import com.vl.drawing.VLImagesCache;
import com.vl.system.VLSystemMediator;
import com.vl.system.VLTimer;

public class MNHomeActivity extends MNBaseActivity {

	private MNTabbarView _tabbar;
	private MNPracticeView _practiceView;
	private MNSettingsView _settingsView;
	private MNCommunityView _communityView;
	private MNAboutView _aboutView;
	private ArrayList<MNBaseView> _tabViews = new ArrayList<MNBaseView>();
	private boolean _wasAudioPlaying = false;
	private int _lastSelTabIndex = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		VLSystemMediator.getInstance().Initialize(this);
		
		_tabbar = new MNTabbarView(this);
		_tabbar.addItem("Practice", VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_pracitce, getResources()),
				VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_practice_sel, getResources()));
		_tabbar.addItem("Settings", VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_settings, getResources()),
				VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_settings_sel, getResources()));
		_tabbar.addItem("Community", VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_community, getResources()),
				VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_community_sel, getResources()));
		_tabbar.addItem("About", VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_about, getResources()),
				VLImagesCache.getInstance().getImageByResource(R.drawable.tbi_about_sel, getResources()));
		this.mainLayout().addView(_tabbar);
		
		_practiceView = new MNPracticeView(this);
		_practiceView.setVisibility(View.GONE);
		this.mainLayout().addView(_practiceView);
		_tabViews.add(_practiceView);
		
		_settingsView = new MNSettingsView(this);
		_settingsView.setVisibility(View.GONE);
		this.mainLayout().addView(_settingsView);
		_tabViews.add(_settingsView);
		
		_communityView = new MNCommunityView(this);
		_communityView.setVisibility(View.GONE);
		this.mainLayout().addView(_communityView);
		_tabViews.add(_communityView);
		
		_aboutView = new MNAboutView(this);
		_aboutView.setVisibility(View.GONE);
		this.mainLayout().addView(_aboutView);
		_tabViews.add(_aboutView);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(MNCommon.ACTION_KEY_START_PLAYING_FROM_BEGIN);
		this.registerReceiver(_notificationReceiver, filter);
		
		_tabbar.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MNHomeActivity.this.setSelectedTabView(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		this.setSelectedTabView(0);
	}
	
	private BroadcastReceiver _notificationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(MNCommon.ACTION_KEY_START_PLAYING_FROM_BEGIN)) {
				_wasAudioPlaying = false;
				setSelectedTabView(0);
				MNPracticeView.getInstance().stopPlaying();
				MNPracticeView.getInstance().startPlaying();
			}
		}
	};
	
	@Override
	protected void onMainLayout(VLRectF rcContent) {
		VLRectF rcTabs = new VLRectF(rcContent);
		rcTabs.setHeight(VLCtrlsUtils.partOfDisplayMinSideRounded(0.22f));
		_tabbar.setFrame(rcTabs);
		
		VLRectF rcTab = new VLRectF(rcContent);
		rcTab.top = rcTabs.bottom;
		for(int i = 0; i < _tabViews.size(); i++) {
			MNBaseView view = _tabViews.get(i);
			view.setFrame(rcTab);
		}
	}
	
	@Override
	protected void onPause() {
		VLTimer.TimersManager.getInstance().suspendTimers();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		VLTimer.TimersManager.getInstance().resumeTimers();
		MNSettingManager.getInstance().updateReminderNotification();
	}
	
	private void setSelectedTabView(int index) {
		if(index == _lastSelTabIndex)
			return;
		if(_lastSelTabIndex == _tabViews.indexOf(_practiceView)) {
			_wasAudioPlaying = !MNAudioView.getInstance().paused();
			if(_wasAudioPlaying)
				MNAudioView.getInstance().setPaused(true);
		}
		_tabbar.setSelectedItemIndex(index);
		
		for(int i = 0; i < _tabViews.size(); i++) {
			MNBaseView view = _tabViews.get(i);
			if(i == index)
				view.setVisibility(View.VISIBLE);
			else
				view.setVisibility(View.GONE);
		}
		
		if(index == _tabViews.indexOf(_practiceView)) {
			if(_wasAudioPlaying)
				MNAudioView.getInstance().setPaused(false);
		}
		_lastSelTabIndex = index;
	}
}
