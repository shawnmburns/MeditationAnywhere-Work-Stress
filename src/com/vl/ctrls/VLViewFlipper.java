package com.vl.ctrls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

public class VLViewFlipper extends ViewFlipper {
	
	int _animationDuration = 500;
	
	public VLViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VLViewFlipper(Context context) {
		super(context);
	}
	
	public int getAnimationDuration() {
		return _animationDuration;
	}
	
	public Animation getAnimationInFromRight() {
        Animation inFromRight = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, +1.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(this.getAnimationDuration());
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
	}
	
	public Animation getAnimationOutToLeft() {
        Animation outtoLeft = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, -1.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(this.getAnimationDuration());
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
	}
	
	public Animation getAnimationInFromLeft() {
        Animation inFromLeft = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, -1.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(this.getAnimationDuration());
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
	}
	
	public Animation getAnimationOutToRight() {
        Animation outtoRight = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, +1.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(this.getAnimationDuration());
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
	}
	
	public View getDisplayedChildView() {
		int index = this.getDisplayedChild();
		if(index < 0)
			return null;
		return this.getChildAt(index);
	}
	
	public void setDisplayedChild(View child) {
		int index = this.indexOfChild(child);
		if(index >= 0)
			this.setDisplayedChild(index);
	}
	
	public void slideRight() {
		Log.v("VLViewFlipper", "SlideRight()");
		if(this.getDisplayedChild() < this.getChildCount()-1) {
			this.setInAnimation(this.getAnimationInFromRight());
			this.setOutAnimation(this.getAnimationOutToLeft());
			this.setDisplayedChild(this.getDisplayedChild() + 1);
		}
	}
	
	public void slideLeft() {
		Log.v("VLViewFlipper", "SlideLeft()");
		if(this.getDisplayedChild() > 0) {
			this.setInAnimation(this.getAnimationInFromLeft());
			this.setOutAnimation(this.getAnimationOutToRight());
			this.setDisplayedChild(this.getDisplayedChild() - 1);
		}
	}
	
	public void slideToChildIndex(int newIndex) {
		if(newIndex < 0)
			newIndex = 0;
		if(newIndex >= getChildCount())
			newIndex = getChildCount()-1;
		if(newIndex < 0)
			return;
		int curIndex = getDisplayedChild();
		if(newIndex != curIndex) {
			if(newIndex > curIndex) {
				this.setInAnimation(this.getAnimationInFromRight());
				this.setOutAnimation(this.getAnimationOutToLeft());
				this.setDisplayedChild(newIndex);
			} else {
				this.setInAnimation(this.getAnimationInFromLeft());
				this.setOutAnimation(this.getAnimationOutToRight());
				this.setDisplayedChild(newIndex);
			}
		}
	}
}


