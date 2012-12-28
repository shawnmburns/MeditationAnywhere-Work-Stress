package com.meditatenowWorkStress.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.meditatenowWorkStress.R;
import com.meditatenowWorkStress.api.MNAudioInfo;
import com.meditatenowWorkStress.api.MNAudioManager;
import com.meditatenowWorkStress.views.MNBaseView;
import com.meditatenowWorkStress.views.MNPlayButton;
import com.meditatenowWorkStress.views.MNProgressView;
import com.vl.common.VLGeometry;
import com.vl.common.VLPointF;
import com.vl.common.VLRectF;
import com.vl.ctrls.VLCtrlsUtils;
import com.vl.ctrls.VLTextView;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.logic.VLBinder;
import com.vl.logic.VLMessageListener;
import com.vl.system.VLTimer;

public class MNAudioView extends MNBaseView {
	
	private VLTextView _tvTitle;
	private MNProgressView _progressView;
	private MNPlayButton _playButton;
	private boolean _paused = true;
	private MediaPlayer _mediaPlayer;
	private VLTimer _timer;
	private MNAudioInfo _curAudio;
	private VLPointF _ptTouchStart;
	private boolean _isProgressTouched = false;
	private boolean _clickCanceled = false;
	private final int _timerIntervalStopped = 5000;
	private final int _timerIntervalPlaying = 150;
	private static MNAudioView _instance;
	
	public static MNAudioView getInstance() {
		return _instance;
	}

	public MNAudioView(Context context) {
		super(context);
		_instance = this;
		
		this.setBackgroundResource(R.drawable.bg);
		
		VLFontInfo font = new VLFontInfo("Helvetica", false, false, 0, 0.075f);
		
		_tvTitle = new VLTextView(context);
		_tvTitle.setFont(font);
		//_tvTitle.setText("Breathe...");
		_tvTitle.setTextColor(VLColor.ColorsCommon.White);
		_tvTitle.setGravity(Gravity.CENTER);
		this.addView(_tvTitle);
		
		_progressView = new MNProgressView(context);
		this.addView(_progressView);
		
		_playButton = new MNPlayButton(context);
		this.addView(_playButton);
		
		_timer = new VLTimer(_timerIntervalStopped);
		_timer.getMsgrTimerEvent().addListener(_onTime);
		_timer.start();
		
		_playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTap();	
			}
		});
		
		updateView();
	}
	
	private VLMessageListener _onTime = new VLMessageListener() {
		@Override
		public void onMessage(VLBinder sender, Object args) {
			updateView();
		}
	};
	
	public void onUpdateView() {
		super.onUpdateView();
		
		_playButton.setPaused(_paused);
		if(_curAudio != null)
			_tvTitle.setText(_curAudio.title);
		if(_mediaPlayer != null) {
			int pos = _mediaPlayer.getCurrentPosition();
			int duration = _mediaPlayer.getDuration();
			float value = 0;
			if(duration > 0)
				value = (float)pos/duration;
			_progressView.setValue(value);
		} else {
			_progressView.setValue(0);
		}
	};
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		VLRectF rcBnds = getBounds();
		int border = VLCtrlsUtils.partOfDisplayMinSideRounded(0.045f);
		VLRectF rcCtrls = rcBnds.rectWithInsets(border, border);
		
		VLRectF rcProgress = new VLRectF(rcCtrls);
		rcProgress.top = rcCtrls.top + rcCtrls.getHeight()*0.4f;
		rcProgress.setHeight(VLCtrlsUtils.partOfDisplayMinSideRounded(0.12f));
		VLCtrlsUtils.setFrameToView(_progressView, VLGeometry.roundRect(rcProgress));
		
		VLRectF rcTitle = new VLRectF(rcCtrls);
		rcTitle.setHeight(VLCtrlsUtils.partOfDisplayMinSideRounded(_tvTitle.getFont().heightRatio*1.1f));
		rcTitle.moveToY(rcProgress.top - border*1.5f - rcTitle.getHeight());
		VLCtrlsUtils.setFrameToView(_tvTitle, VLGeometry.roundRect(rcTitle));
		
		int btnPlayWidth = VLCtrlsUtils.partOfDisplayMinSideRounded(0.14f);
		int btnPlayHeight = btnPlayWidth;
		VLRectF rcBtnPlay = new VLRectF(rcCtrls);
		rcBtnPlay.top = rcProgress.bottom + border;
		rcBtnPlay.setWidth(btnPlayWidth);
		rcBtnPlay.setHeight(btnPlayHeight);
		rcBtnPlay.moveToX(rcCtrls.getMiddleX() - rcBtnPlay.getWidth()/2);
		_playButton.setFrame(VLGeometry.roundRect(rcBtnPlay));
	}
	
	public boolean paused() {
		return _paused;
	}
	
	public void setPaused(boolean paused) {
		if(_paused != paused || _mediaPlayer.isPlaying() != !_paused) {
			_paused = paused;
			if(_paused) {
				if(_mediaPlayer.isPlaying())
					_mediaPlayer.pause();
				_timer.setInterval(_timerIntervalStopped);
			} else {
				if(_curAudio == null) {
					_curAudio = MNAudioManager.getInstance().getFirstAudio();
				}
				if(_mediaPlayer == null) {
					_mediaPlayer = MediaPlayer.create(getContext(), _curAudio.resourceId);
					_mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							if(_curAudio != null) {
								MNAudioInfo nextCurAudio = MNAudioManager.getInstance().getNextAudio(_curAudio);
								if(nextCurAudio != null) {
									setPaused(true);
									releasePlayer();
									_curAudio = nextCurAudio;
									setPaused(false);
								} else {
									stopPlaying();
								}
							}
							updateView();
						}
					});
					_mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
						@Override
						public void onBufferingUpdate(MediaPlayer mp, int percent) {
							updateView();
						}
					});
					_mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
						@Override
						public boolean onInfo(MediaPlayer mp, int what, int extra) {
							updateView();
							return false;
						}
					});
					_mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mp) {
							updateView();
						}
					});
					_mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
						@Override
						public void onSeekComplete(MediaPlayer mp) {
							updateView();
						};
					});
				}
				if(!_mediaPlayer.isPlaying()) {
					Log.v(getClass().getCanonicalName(), "Playing: " + _curAudio.name);
					_mediaPlayer.start();
				}
				_timer.setInterval(_timerIntervalPlaying);
			}
			updateView();
		}
	}
	
	private void releasePlayer() {
		if(_mediaPlayer != null) {
			if(_mediaPlayer.isPlaying())
				_mediaPlayer.stop();
			_mediaPlayer.release();
			_mediaPlayer = null;
		}
	}
	
	private boolean _stoppingPlaying = false;
	public void stopPlaying() {
		if(_stoppingPlaying)
			return;
		_stoppingPlaying = true;
		_curAudio = null;
		releasePlayer();
		MNPracticeView.getInstance().stopPlaying();
		_paused = true;
		_stoppingPlaying = false;
		updateView();
		_timer.setInterval(_timerIntervalStopped);
	}
	
	private void onTap() {
		MNPracticeView.getInstance().onTap();
		updateView();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		VLRectF rcProgress = _progressView.getSliderArea();
		rcProgress.move(_progressView.getLeft(), _progressView.getTop());
		VLPointF ptOrig = new VLPointF(event.getX(), event.getY());
		int border = VLCtrlsUtils.partOfDisplayMinSideRounded(0.02f);
		VLRectF rectTouchArea = (_ptTouchStart != null) ? (new VLRectF(_ptTouchStart.x-border, _ptTouchStart.y-border,
				_ptTouchStart.x+border, _ptTouchStart.y+border)) : null;
		VLPointF pt = ptOrig;
		if(pt.x < rcProgress.left)
			pt.x = rcProgress.left;
		if(pt.x > rcProgress.right)
			pt.x = rcProgress.right;
		if(action == MotionEvent.ACTION_DOWN) {
			_clickCanceled = false;
			_ptTouchStart = pt;
			if(!_isProgressTouched && rcProgress.containsPoint(pt)) {
				_isProgressTouched = true;
			}
		}
		if(action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
			if(rectTouchArea != null && !rectTouchArea.containsPoint(ptOrig))
				_clickCanceled = true;
			if(_isProgressTouched && _curAudio != null && _mediaPlayer != null && rcProgress.containsPoint(ptOrig)) {
				int duration = _mediaPlayer.getDuration();
				float newPosF = (pt.x - rcProgress.left) / rcProgress.getWidth();
				int newPos = Math.round(newPosF * duration);
				if(newPos > duration)
					newPos = duration;
				if(newPos < 0)
					newPos = 0;
				_mediaPlayer.seekTo(newPos);
			}
			return true;
		} else if(action == MotionEvent.ACTION_UP) {
			_ptTouchStart = null;
			if(_isProgressTouched)
				_isProgressTouched = false;
			else if(rectTouchArea != null && rectTouchArea.containsPoint(ptOrig) && !_clickCanceled)
				onTap();
			return true;
		} else if(action == MotionEvent.ACTION_OUTSIDE) {
			_isProgressTouched = false;
			_ptTouchStart = null;
			return true;
		} else if(action == MotionEvent.ACTION_CANCEL) {
			_isProgressTouched = false;
			_ptTouchStart = null;
			return true;
		}
		return super.onTouchEvent(event);
	}
}
