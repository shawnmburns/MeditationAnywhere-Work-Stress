package com.meditatenowWorkStress.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.vl.common.VLRectF;
import com.vl.ctrls.VLBaseButton;
import com.vl.drawing.VLPaint;

public class MNPlayButton extends VLBaseButton {
	
	private boolean _paused = true;

	public MNPlayButton(Context context) {
		super(context);
	}

	@Override
	protected void onDrawBackground(Canvas canvas) {
		VLRectF rcBnds = getBounds();
		Path path = new Path();
		if(!_paused) {
			float w = rcBnds.getWidth()/5 * 2;
			path.moveTo(rcBnds.left + w, rcBnds.top);
			path.lineTo(rcBnds.left, rcBnds.top);
			path.lineTo(rcBnds.left, rcBnds.bottom);
			path.lineTo(rcBnds.left + w, rcBnds.bottom);
			path.lineTo(rcBnds.left + w, rcBnds.top);
			path.lineTo(rcBnds.right - w, rcBnds.top);
			path.lineTo(rcBnds.right - w, rcBnds.bottom);
			path.lineTo(rcBnds.right, rcBnds.bottom);
			path.lineTo(rcBnds.right, rcBnds.top);
			path.close();
		} else {
			float left = rcBnds.left + rcBnds.getWidth()/12;
			path.moveTo(left, rcBnds.top);
			path.lineTo(rcBnds.right, rcBnds.getMiddleY());
			path.lineTo(left, rcBnds.bottom);
			path.close();
		}
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		canvas.drawPath(path, paint);
	}
	
	public void setPaused(boolean paused) {
		if(_paused != paused) {
			_paused = paused;
			invalidate();
		}
	}
}
