package com.vl.ctrls;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;

public class VLSelectionDialog extends Dialog {
	
	private ListView _listView;
	
	
	public VLSelectionDialog(Context context) {
		super(context);
		
		_listView = new ListView(context);
		int border = VLCtrlsUtils.heightAsPartOfDisplayRounded(0.01f);
		LinearLayout.LayoutParams layouts = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layouts.gravity = Gravity.CENTER | Gravity.RIGHT;
		layouts.setMargins(border, border, border, border);
		this.setContentView(_listView, layouts);
	}
}
