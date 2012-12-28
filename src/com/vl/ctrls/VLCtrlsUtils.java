package com.vl.ctrls;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.system.VLSystemMediator;

public class VLCtrlsUtils {
	
	public static int heightAsPartOfDisplayRounded(double displayHeightPart) {
		VLSizeF szDspl = VLSystemMediator.getInstance().getDisplaySize();
		double pixels = szDspl.height * displayHeightPart;
		return (int)Math.round(pixels);
	}
	
	public static int widthAsPartOfDisplayRounded(double displayWidthPart) {
		VLSizeF szDspl = VLSystemMediator.getInstance().getDisplaySize();
		double pixels = szDspl.width * displayWidthPart;
		return (int)Math.round(pixels);
	}
	
	public static int partOfDisplayMinSideRounded(float ratio) {
		VLSizeF szDspl = VLSystemMediator.getInstance().getDisplaySize();
		float len = Math.min(szDspl.width, szDspl.height);
		float pixels = len * ratio;
		return (int)Math.round(pixels);
	}
	
	public static VLRectF getFrameOfView(View view) {
		return new VLRectF(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
	}
	
	public static void setFrameToView(View view, VLRectF rect) {
		view.layout((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);
	}
	
	static void getSubviewsTree(ViewGroup parentView, Class<?> classOfSubviews, ArrayList<View> result) {
		for(int i = 0; i < parentView.getChildCount(); i++) {
			View child = parentView.getChildAt(i);
			if(classOfSubviews.isAssignableFrom(child.getClass()))
				result.add(child);
			if(child instanceof ViewGroup) 
				getSubviewsTree((ViewGroup)child, classOfSubviews, result);
		}
	}
	
	public static ArrayList<View> getSubviewsTree(ViewGroup parentView, Class<?> classOfSubviews) {
		ArrayList<View> result = new ArrayList<View>();
		getSubviewsTree(parentView, classOfSubviews, result);
		return result;
	}
	
	public static ViewParent findInParents(View view, Class<?> parentClass) {
		ViewParent par = view.getParent();
		while(par != null)
		{
			if(parentClass.isAssignableFrom(par.getClass()))
				return par;
			par = par.getParent();
		}
		return null;
	}
	
	public static void hideKeyboard()
	{
		//view.setInputType(0);
		Context context = VLSystemMediator.getInstance().getCurrentContext();
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	public static void showKeyboard(EditText textEdit)
	{
		Context context = VLSystemMediator.getInstance().getCurrentContext();
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textEdit, InputMethodManager.SHOW_IMPLICIT);
	}
}
