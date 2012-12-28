package com.vl.ctrls;

import com.vl.common.VLGeometry;
import com.vl.common.VLPointF;
import com.vl.common.VLSizeF;
import com.vl.system.VLMessageCenter;
import com.vl.system.VLSystemMediator;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class VLBaseListViewItem extends VLBaseLayout {
	private boolean _isFirstItem = false;
	private boolean _isLastItem = false;
	private boolean _isSelected = false;
	private boolean _isTouched = false;
	private VLPointF _ptTouch;
	private VLPointF _ptTouchScr;
	private VLPointF _ptTouchUpScr;
	private static final int _delayBeforeTouchSelect = 100;
	private float _touchSizeMaxRatio = 0.02f;
	private View _contentView;
	
	public VLBaseListViewItem(Context context) {
		super(context);
		setBackgroundColor(Color.TRANSPARENT);
	}
	
	public VLBaseListViewItem(Context context, View contentView) {
		this(context);
		this.setContentView(contentView);
	}
	
	protected int onGetTableItemHeight() {
		return (int)VLCtrlsUtils.partOfDisplayMinSideRounded(0.1f);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		ListView listView = (ListView)VLCtrlsUtils.findInParents(this, ListView.class);
		if(listView != null) {
			int maxWidth = listView.getWidth();
			if(widthMeasureSpec <= 0 || widthMeasureSpec > maxWidth)
				widthMeasureSpec = maxWidth;
		} else {
			VLSizeF szDspl = VLSystemMediator.getInstance().getDisplaySize();
			int maxWidth = VLCtrlsUtils.widthAsPartOfDisplayRounded(0.95);
			if(widthMeasureSpec <= 0 || widthMeasureSpec >= szDspl.width)
				widthMeasureSpec = maxWidth;
		}
		heightMeasureSpec = this.onGetTableItemHeight();
		this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//super.onLayout(changed, l, t, r, b);
		if(_contentView != null) {
			VLCtrlsUtils.setFrameToView(_contentView, getBounds());
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		VLPointF pt = new VLPointF(event.getX(), event.getY());
		if(action == MotionEvent.ACTION_DOWN) {
			_ptTouch = pt;
			_ptTouchScr = this.convertPointToScreen(_ptTouch);
			_isTouched = true;
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run()
				{
					if(_isTouched)
						setIsSelected(true);
				}
			}, _delayBeforeTouchSelect);
			return true;
		} else if(action == MotionEvent.ACTION_MOVE) {
			float dist = VLCtrlsUtils.partOfDisplayMinSideRounded(_touchSizeMaxRatio);
			if(VLGeometry.getDistanceBetweenPoints(_ptTouch, pt) > dist) {
				_isTouched = false;
				this.setIsSelected(false);
			}
			return true;
		} else if(action == MotionEvent.ACTION_UP) {
			_ptTouchUpScr = this.convertPointToScreen(pt);
			_isTouched = false;
			if(_isSelected) {
				this.setIsSelected(false);
				this.onClick();
			} else {
				float dist = VLCtrlsUtils.partOfDisplayMinSideRounded(_touchSizeMaxRatio);
				if(VLGeometry.getDistanceBetweenPoints(_ptTouchScr, _ptTouchUpScr) < dist) {
					VLMessageCenter.startIgnoringTouches();
					setIsSelected(true);
					Handler handler1 = new Handler();
					handler1.postDelayed(new Runnable() {
						@Override
						public void run() {
							VLMessageCenter.stopIgnoringTouches();
							setIsSelected(false);
							onClick();
						}
					}, _delayBeforeTouchSelect);
				}
			}
			return true;
		} else if(action == MotionEvent.ACTION_OUTSIDE) {
			_isTouched = false;
			this.setIsSelected(false);
			return true;
		} else if(action == MotionEvent.ACTION_CANCEL) {
			_isTouched = false;
			this.setIsSelected(false);
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	protected void onClick() {
		
	}
	
	public boolean isSelected() {
		return _isSelected;
	}
	
	protected void setIsSelected(boolean value) {
		if(_isSelected != value) {
			_isSelected = value;
			this.invalidate();
		}
	}

	public boolean isFirstItem() {
		return _isFirstItem;
	}
	
	public void setIsFirstItem(boolean isFirstItem) {
		if(_isFirstItem != isFirstItem) {
			_isFirstItem = isFirstItem;
			invalidate();
		}
	}

	public boolean isLastItem() {
		return _isLastItem;
	}
	
	public void setIsLastItem(boolean isLastItem) {
		if(_isLastItem != isLastItem) {
			_isLastItem = isLastItem;
			invalidate();
		}
	}

	public View getContentView() {
		return _contentView;
	}

	public void setContentView(View contentView) {
		if(_contentView != contentView) {
			if(_contentView != null)
				this.removeView(_contentView);
			_contentView = contentView;
			if(_contentView != null) {
				this.addView(_contentView);
				this.forceLayout();
				//this.layoutSubviews();
			}
		}
	}
}
