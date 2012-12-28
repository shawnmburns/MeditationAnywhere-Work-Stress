package com.meditatenowWorkStress.ui;

import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.meditatenowWorkStress.api.MNReminderTypeInfo;
import com.meditatenowWorkStress.api.MNSettingManager;
import com.meditatenowWorkStress.views.MNSwitchView;
import com.meditatenowWorkStress.views.MNTabView;
import com.vl.common.VLDate;
import com.vl.common.VLGeometry;
import com.vl.common.VLRectF;
import com.vl.ctrls.VLBaseButton;
import com.vl.ctrls.VLCtrlsUtils;
import com.vl.ctrls.VLTextView;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;
import com.vl.logic.VLBinder;
import com.vl.logic.VLMessageListener;

public class MNSettingsView extends MNTabView {
	
	private VLTextView _tvPlayInstructions;
	private VLTextView _tvPlayIntro;
	private MNSwitchView _svPlayInstructions;
	private MNSwitchView _svPlayIntro;
	
	private VLTextView _tvSetReminder;
	private VLTextView _tvReminderName;
	private TimePicker _timePicker;
	private VLBaseButton _bnCancel;
	private VLBaseButton _bnSet;
	
	private MNReminderTypeInfo _curReminderTypeInfo = MNReminderTypeInfo.infoByType(MNSettingManager.getInstance().reminderType());

	public MNSettingsView(Context context) {
		super(context);
		
		VLFontInfo fontTV = new VLFontInfo("Helvetica", false, false, 0, 0.055f);
		
		_tvPlayInstructions = new VLTextView(context);
		_tvPlayInstructions.setFont(fontTV);
		_tvPlayInstructions.setGravity(Gravity.CENTER_VERTICAL);
		_tvPlayInstructions.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_tvPlayInstructions);
		_tvPlayInstructions.setText("Play Instructions");
		
		_tvPlayIntro = new VLTextView(context);
		_tvPlayIntro.setFont(fontTV);
		_tvPlayIntro.setGravity(Gravity.CENTER_VERTICAL);
		_tvPlayIntro.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_tvPlayIntro);
		_tvPlayIntro.setText("Play Intro");
		
		VLFontInfo fontSwitch = new VLFontInfo("Helvetica", false, false, 0, 0.045f);
		
		_svPlayInstructions = new MNSwitchView(context);
		_svPlayInstructions.setFont(fontSwitch);
		this.addView(_svPlayInstructions);
		
		_svPlayIntro = new MNSwitchView(context);
		_svPlayIntro.setFont(fontSwitch);
		this.addView(_svPlayIntro);
		
		_tvSetReminder = new VLTextView(context);
		_tvSetReminder.setFont(fontTV);
		_tvSetReminder.setGravity(Gravity.CENTER_VERTICAL);
		_tvSetReminder.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_tvSetReminder);
		_tvSetReminder.setText("Set Reminder");
		
		_tvReminderName = new VLTextView(context);
		_tvReminderName.setFont(fontTV);
		_tvReminderName.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		_tvReminderName.setTextColor(VLColor.ColorsCommon.White);
		this.addView(_tvReminderName);
		
		_timePicker = new TimePicker(context);
		String clockType = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.System.TIME_12_24);
		_timePicker.setIs24HourView(!(clockType != null && clockType.equals("12")));
		_timePicker.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addView(_timePicker);
		
		VLFontInfo fontBn = new VLFontInfo("Helvetica", false, false, 0, 0.055f);
		
		_bnCancel = new VLBaseButton(context);
		_bnCancel.setGravity(Gravity.CENTER);
		_bnCancel.setTextColor(VLColor.ColorsCommon.White);
		_bnCancel.setFont(fontBn);
		this.addView(_bnCancel);
		_bnCancel.setTitle("Cancel");
		
		_bnSet = new VLBaseButton(context);
		_bnSet.setGravity(Gravity.CENTER);
		_bnSet.setTextColor(VLColor.ColorsCommon.White);
		_bnSet.setFont(fontBn);
		this.addView(_bnSet);
		_bnSet.setTitle("Set");
		
		_svPlayInstructions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MNSettingManager.getInstance().setIsPlayInstructions(!_svPlayInstructions.isOn());
			}
		});
		
		_svPlayIntro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MNSettingManager.getInstance().setIsPlayIntro(!_svPlayIntro.isOn());
			}
		});
		
		_tvSetReminder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				List<MNReminderTypeInfo> types = MNReminderTypeInfo.allTypes();
				CharSequence[] choices = new CharSequence[types.size()];
				for(int i = 0; i < types.size(); i++)
					choices[i] = types.get(i).name();
				builder.setSingleChoiceItems(choices, _curReminderTypeInfo.type(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MNReminderTypeInfo type = MNReminderTypeInfo.allTypes().get(which);
						_curReminderTypeInfo = type;
						_tvReminderName.setText(_curReminderTypeInfo.name());
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});
		
		_bnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_timePicker.clearFocus();
				updateView();
				Toast.makeText(getContext(), "Reminder set to the previous settings", Toast.LENGTH_SHORT).show();
			}
		});
		
		_bnSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_timePicker.clearFocus();
				int hours = _timePicker.getCurrentHour();
				int minutes = _timePicker.getCurrentMinute();
				
				VLDate dtCur = VLDate.getCurrent();
				TimeZone tz = TimeZone.getDefault();
				long offsetMS = tz.getOffset((dtCur.getTicks() -  VLDate.kTicksUntil1970) / VLDate.kTicksPerMillisecond);
				
				int allDays = (int)((dtCur.getTicks() + offsetMS) / VLDate.kTicksPerDay);
				
				MNSettingManager manr = MNSettingManager.getInstance();
				manr.setReminderDateInDays(allDays);
				manr.setReminderTimeInSeconds(hours*3600 + minutes*60);
				
				int reminderType = _curReminderTypeInfo.type();
				MNSettingManager.getInstance().setReminderType(reminderType);
				
				if(reminderType != 0) {
					String msg = "Reminder set ";
					if(reminderType == MNReminderTypeInfo.REMINDER_TYPE_DAILY)
						msg += "daily";
					else if(reminderType == MNReminderTypeInfo.REMINDER_TYPE_EVERYOTHERDAY)
						msg += "every other day";
					else if(reminderType == MNReminderTypeInfo.REMINDER_TYPE_WEEKLY)
						msg += "weekly";
					msg += " at ";
					VLDate date = new VLDate(2001, 1, 1, hours, minutes, 0, TimeZone.getDefault());
					java.text.DateFormat timeFormat = DateFormat.getTimeFormat(getContext());
					msg += timeFormat.format(date.getJavaDate());
					Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(getContext(), "Reminder disabled", Toast.LENGTH_SHORT).show();
			}
		});
		
		MNSettingManager.getInstance().getMsgrVersionChanged().addListener(_settingsChanged);
		
		this.updateView();
	}
	
	private VLMessageListener _settingsChanged = new VLMessageListener() {
		@Override
		public void onMessage(VLBinder sender, Object args) {
			updateView();
		}
	};
	
	public void onUpdateView() {
		super.onUpdateView();
		_svPlayInstructions.setIsOn(MNSettingManager.getInstance().isPlayInstructions());
		_svPlayIntro.setIsOn(MNSettingManager.getInstance().isPlayIntro());

		_curReminderTypeInfo = MNReminderTypeInfo.infoByType(MNSettingManager.getInstance().reminderType());
		_tvReminderName.setText(_curReminderTypeInfo.name());
		
		int reminderTimeInSeconds = MNSettingManager.getInstance().reminderTimeInSeconds();
		_timePicker.setCurrentHour(reminderTimeInSeconds/3600);
		_timePicker.setCurrentMinute((reminderTimeInSeconds % 3600)/60);
	};
	
	private int border() {
		int border = VLCtrlsUtils.partOfDisplayMinSideRounded(0.045f);
		return border;
	}
	
	private VLRectF rectCtrls() {
		VLRectF rcBnds = getBounds();
		int border = border();
		VLRectF rcCtrls = rcBnds.rectWithInsets(border, border);
		return rcCtrls;
	}
	
	private VLRectF rectSection0() {
		VLRectF rcCtrls = rectCtrls();
		VLRectF rect = new VLRectF(rcCtrls);
		rect.setHeight((int)rcCtrls.getHeight()*0.24f);
		return rect;
	}
	
	private VLRectF rectSection1() {
		VLRectF rcCtrls = rectCtrls();
		VLRectF rcSec0 = rectSection0();
		int border = border();
		VLRectF rect = new VLRectF(rcCtrls);
		rect.top = rcSec0.bottom + border;
		return rect;
	}
	
	private float section1RowHeight() {
		VLRectF rcSec1 = rectSection1();
		float res = rcSec1.getHeight() / 5;
		return res;
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		VLRectF rcSec0 = rectSection0();
		VLRectF rcSec1 = rectSection1();
		
		VLColor colSecBack = new VLColor(39/255f, 40/255f, 39/255f);
		int corner = VLCtrlsUtils.partOfDisplayMinSideRounded(0.015f);
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		
		VLGraphicsUtils.drawRoundedRect(canvas, paint, rcSec0, corner, colSecBack);
		VLGraphicsUtils.drawRoundedRect(canvas, paint, rcSec1, corner, colSecBack);
		
		VLColor colDivider = new VLColor(60/255f, 63/255f, 62/255f);
		int dividerHeight = 1;
		VLGraphicsUtils.drawLine(canvas, rcSec0.left, (int)rcSec0.getMiddleY(), rcSec0.right, (int)rcSec0.getMiddleY(), colDivider, dividerHeight);
		
		VLColor colDividerBlue = new VLColor(56/255f, 192/255f, 239/255f);
		VLColor colDividerGray = new VLColor(59/255f, 60/255f, 60/255f);
		float sec1RowH = this.section1RowHeight();
		VLGraphicsUtils.drawLine(canvas, rcSec1.left, (int)(rcSec1.top + sec1RowH), rcSec1.right, (int)(rcSec1.top + sec1RowH), colDividerBlue, dividerHeight);
		VLGraphicsUtils.drawLine(canvas, rcSec1.left, (int)(rcSec1.bottom - sec1RowH), rcSec1.right, (int)(rcSec1.bottom - sec1RowH), colDividerGray, dividerHeight);
		VLGraphicsUtils.drawLine(canvas, rcSec1.getMiddleX(), (int)(rcSec1.bottom - sec1RowH), rcSec1.getMiddleX(), rcSec1.bottom, colDividerGray, dividerHeight);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		VLRectF rcSec0 = rectSection0();
		int borderRow = VLCtrlsUtils.partOfDisplayMinSideRounded(0.012f);
		int switchWidth = VLCtrlsUtils.partOfDisplayMinSideRounded(0.25f);
		_tvPlayInstructions.setFrame(VLGeometry.roundRect(
				new VLRectF(rcSec0.left + borderRow*2, rcSec0.top, rcSec0.right, rcSec0.top + rcSec0.getHeight()/2)));
		_svPlayInstructions.setFrame(VLGeometry.roundRect(
				new VLRectF(rcSec0.right - borderRow - switchWidth, rcSec0.top + borderRow,
						rcSec0.right - borderRow, rcSec0.top + rcSec0.getHeight()/2 - borderRow)));
		_tvPlayIntro.setFrame(VLGeometry.roundRect(
				new VLRectF(rcSec0.left + borderRow*2, rcSec0.top + rcSec0.getHeight()/2, rcSec0.right, rcSec0.bottom)));
		_svPlayIntro.setFrame(VLGeometry.roundRect(
				new VLRectF(rcSec0.right - borderRow - switchWidth, rcSec0.top + rcSec0.getHeight()/2 + borderRow,
						rcSec0.right - borderRow, rcSec0.bottom - borderRow)));
		
		VLRectF rcSec1 = rectSection1();
		float sec1RowH = this.section1RowHeight();
		
		VLRectF rcSec1Row0 = new VLRectF(rcSec1.left, (int)(rcSec1.top), rcSec1.right, (int)(rcSec1.top + sec1RowH));
		VLRectF rcSec1Row2 = new VLRectF(rcSec1.left, (int)(rcSec1.bottom - sec1RowH), rcSec1.right, (int)(rcSec1.bottom));
		VLRectF rcSec1Row1 = new VLRectF(rcSec1.left, rcSec1Row0.bottom, rcSec1.right, rcSec1Row2.top);
		rcSec1Row0 = VLGeometry.roundRect(rcSec1Row0.rectWithInsets(borderRow, borderRow));
		rcSec1Row2 = VLGeometry.roundRect(rcSec1Row2.rectWithInsets(borderRow, borderRow));
		rcSec1Row1 = VLGeometry.roundRect(rcSec1Row1.rectWithInsets(borderRow, borderRow));

		_tvSetReminder.setFrame(new VLRectF(rcSec1Row0.left + borderRow, rcSec1Row0.top, rcSec1Row0.right - borderRow*3, rcSec1Row0.bottom));
		_tvReminderName.setFrame(_tvSetReminder.getFrame());
		
		VLRectF rcPicker = new VLRectF(rcSec1Row1);
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int)(rcPicker.getWidth()), MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)(rcPicker.getHeight()), MeasureSpec.AT_MOST);
		_timePicker.measure(widthMeasureSpec, heightMeasureSpec);
		rcPicker.setWidth(_timePicker.getMeasuredWidth());
		rcPicker.setHeight(_timePicker.getMeasuredHeight());
		rcPicker = VLGeometry.rectOfFitToRectWithSize(rcSec1Row1, rcPicker.getSize());
		rcPicker = VLGeometry.roundRect(rcPicker);
		VLCtrlsUtils.setFrameToView(_timePicker, rcPicker);
		
		_bnCancel.setFrame(VLGeometry.roundRect(new VLRectF(rcSec1Row2.left, rcSec1Row2.top, rcSec1Row2.right - rcSec1Row2.getWidth()/2, rcSec1Row2.bottom)));
		_bnSet.setFrame(VLGeometry.roundRect(new VLRectF(rcSec1Row2.left + rcSec1Row2.getWidth()/2, rcSec1Row2.top, rcSec1Row2.right, rcSec1Row2.bottom)));
	}
}
