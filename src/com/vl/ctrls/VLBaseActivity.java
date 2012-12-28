package com.vl.ctrls;

import com.vl.common.VLRectF;
import com.vl.system.VLSystemMediator;

import android.app.Activity;
import android.content.Context;

public class VLBaseActivity extends Activity {
	
	private MainLayout _mainLayout;
	
	protected class MainLayout extends VLBaseLayout {

		public MainLayout(Context context) {
			super(context);
		}
		
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			VLRectF rcBnds = this.getBounds();
			VLBaseActivity.this.onMainLayout(rcBnds);
		}
	}
	
	protected void addMainLayout() {
		if(_mainLayout == null) {
			_mainLayout = new MainLayout(this);
			this.setContentView(_mainLayout);
		}
	}
	
	protected MainLayout mainLayout() {
		if(_mainLayout == null)
			addMainLayout();
		return _mainLayout;
	}
	
	protected void onMainLayout(VLRectF rcContent) {
		
	}
	
	@Override
	protected void onResume() {
		VLSystemMediator.getInstance().pushCurrentActivity(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		VLSystemMediator.getInstance().popCurrentActivity(this);
		super.onPause();
	}
	
	public void onUpdateView() {
	}
	
	public void updateView() {
		this.onUpdateView();
	}
}
