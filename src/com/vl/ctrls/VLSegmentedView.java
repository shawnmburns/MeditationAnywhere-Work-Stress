package com.vl.ctrls;

import java.util.ArrayList;

import com.vl.common.VLGeometry;
import com.vl.common.VLPointF;
import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class VLSegmentedView extends VLBaseLayout {
	
	private class ItemInfo {
		public String title = "";
		public Bitmap image;
		public float widthWeight = 1.0f;
		public int textGravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
	}
	
	private ArrayList<ItemInfo> _items = new ArrayList<VLSegmentedView.ItemInfo>();
	private VLFontInfo _font = new VLFontInfo();
	private VLColor _textColor = new VLColor();
	private VLColor _shadowColor = new VLColor();
	private VLSizeF _shadowOffset = new VLSizeF();
	//private VLRectF _contentInsetsRel = new VLRectF();
	private VLColor _selectedColor = new VLColor(255/255f, 255/255f, 255/255f, 0.2f);
	private VLColor _selectedBorderColor = new VLColor(128/255f, 128/255f, 128/255f, 0.2f);
	private int _selectedIndex = -1;
	private boolean _selectionByTouchEnabled = true;
	private OnItemSelectedListener _onItemSelectedListener;
	private OnItemClickListener _onItemClickListener;
	
	public VLSegmentedView(Context context) {
		super(context);
		this.setBackgroundColor(Color.TRANSPARENT);
		
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	private VLRectF rectForItems() {
		return this.getBounds();
	}
	
	private VLRectF rectForItem(int index) {
		VLRectF rcItems = this.rectForItems();
		if(_items.size() <= 1)
			return rcItems;
		ItemInfo item = _items.get(index);
		float allWidthWeight = 0;
		float widthWeightBefore = 0;
		for(int i = 0; i < _items.size(); i++) {
			ItemInfo obj = _items.get(i);
			allWidthWeight += obj.widthWeight;
			if(i < index)
				widthWeightBefore += obj.widthWeight;
		}
		VLRectF rcItem = new VLRectF(rcItems);
		rcItem.left = rcItems.left + rcItems.getWidth() * widthWeightBefore / allWidthWeight;
		rcItem.setWidth(rcItems.getWidth() * item.widthWeight / allWidthWeight);
		return rcItem;
	}
	
	private int itemIndexByPoint(VLPointF pt) {
		for(int i = 0; i < _items.size(); i++) {
			VLRectF rcItem = rectForItem(i);
			if(rcItem.containsPoint(pt))
				return i;
		}
		return -1;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.isEnabled())
			return super.onTouchEvent(event);
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		VLPointF pt = new VLPointF(x, y);
		int itemIndex = this.itemIndexByPoint(pt);
		if(action == MotionEvent.ACTION_DOWN) {
			if(itemIndex >= 0) {
				boolean needClick = false;
				if(_onItemClickListener != null) {
					_onItemClickListener.onItemClick(null, this, itemIndex, 0);
					needClick = true;
				}
				if(itemIndex != _selectedIndex && _selectionByTouchEnabled) {
					this.setSelectedIndex(itemIndex);
					if(_onItemSelectedListener != null) {
						_onItemSelectedListener.onItemSelected(null, this, _selectedIndex, 0);
						needClick = true;
					}
				}
				if(needClick)
					this.performClick();
			}
			return true;
		} else if(action == MotionEvent.ACTION_MOVE) {
			return true;
		} else if(action == MotionEvent.ACTION_UP) {
			return true;
		} else if(action == MotionEvent.ACTION_CANCEL) {
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	private void drawImage(Canvas canvas, Bitmap image, VLRectF rect, float xMax, float yMax, VLPaint paint) {
		if(xMax >= rect.right && yMax >= rect.bottom) {
			VLGraphicsUtils.drawBitmap(canvas, image, rect, paint, true);
			return;
		}
		VLRectF rcClip = new VLRectF(rect);
		if(xMax < rect.right)
			rcClip.setWidth(xMax - rcClip.left);
		if(yMax < rect.bottom)
			rcClip.setHeight(yMax - rcClip.top);
		canvas.save();
		canvas.clipRect(rcClip.toRect());
		VLGraphicsUtils.drawBitmap(canvas, image, rect, paint, true);
		canvas.restore();
	}
	
	protected void drawTitle(Canvas canvas, String title, VLRectF rcText, VLPaint paint, int textGravity) {
		VLRectF rcContent = new VLRectF(rcText);
		rcText.setSize(VLGraphicsUtils.getTextSize(title, _font));
		rcText.moveToY(rcContent.getMiddleY() - rcText.getHeight() / 2);
		if((textGravity & Gravity.LEFT) == Gravity.LEFT)
			rcText.moveToX(rcContent.left);
		else if((textGravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL)
			rcText.moveToX(rcContent.getMiddleX() - rcText.getWidth() / 2);
		else if((textGravity & Gravity.RIGHT) == Gravity.RIGHT)
			rcText.moveToX(rcContent.right - rcText.getWidth());
		rcText = VLGeometry.roundRect(rcText);
		if(_shadowOffset.width != 0 || _shadowOffset.height != 0) {
			rcText.move(_shadowOffset.width, _shadowOffset.height);
			paint.setColor(_shadowColor.toIntColor());
			VLGraphicsUtils.drawText(title, _font, canvas, rcText.left, rcText.top, paint);
			rcText.move(-_shadowOffset.width, -_shadowOffset.height);
		}
		paint.setColor(_textColor.toIntColor());
		VLGraphicsUtils.drawText(title, _font, canvas, rcText.left, rcText.top, paint);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		VLRectF rcBnds = getBounds();
		if(rcBnds.getWidth() < 1 || rcBnds.getHeight() < 1)
			return;
		Bitmap imageL = this.getLeftImage();
		Bitmap imageM = this.getMiddleImage();
		Bitmap imageR = this.getRightImage();
		if(imageL == null || imageM == null || imageR == null)
			return;
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		VLRectF rcL = new VLRectF(rcBnds);
		rcL.setWidth(Math.round(rcL.getHeight() * imageL.getWidth() / imageL.getHeight()));
		VLRectF rcR = new VLRectF(rcBnds);
		rcR.setWidth(Math.round(rcR.getHeight() * imageR.getWidth() / imageR.getHeight()));
		rcR.moveToX(rcBnds.right - rcR.getWidth());
		VLGraphicsUtils.drawBitmap(canvas, imageL, rcL, paint, true);
		VLGraphicsUtils.drawBitmap(canvas, imageR, rcR, paint, true);
		VLSizeF szM = rcBnds.getSize();
		szM.width = Math.round(szM.height * imageM.getWidth() / imageM.getHeight());
		for(float x = rcL.right; szM.width > 0 ; x += szM.width) {
			float xMax = rcBnds.right - rcR.getWidth();
			if(x >= xMax)
				break;
			VLRectF rcM = new VLRectF(rcBnds);
			rcM.setSize(szM);
			rcM.moveToX(x);
			this.drawImage(canvas, imageM, rcM, xMax, rcBnds.bottom, paint);
		}
		
		Path clipPath = this.getContentClipPath();
		if(clipPath != null)
			canvas.clipPath(clipPath);
		
		for(int i = 0; i < _items.size(); i++) {
			ItemInfo item = _items.get(i);
			VLRectF rcItem = this.rectForItem(i);
			rcItem = VLGeometry.roundRect(rcItem);
			
			if(i == _selectedIndex)
			{
				paint.setColor(_selectedColor.toIntColor());
				canvas.drawRect(rcItem.toRectF(), paint);
				
				paint.setColor(_selectedBorderColor.toIntColor());
				canvas.drawLine(rcItem.left, rcItem.top, rcItem.left, rcItem.bottom, paint);
				canvas.drawLine(rcItem.right, rcItem.top, rcItem.right, rcItem.bottom, paint);
			}
			
			VLSizeF szText = VLGraphicsUtils.getTextSize(item.title, _font);
			VLRectF rcText = new VLRectF(rcItem);
			rcText.setSize(szText);
			rcText.moveToX(rcItem.getMiddleX() - rcText.getWidth()/2 + _shadowOffset.width);
			rcText.moveToY(rcItem.getMiddleY() - rcText.getHeight()/2 + _shadowOffset.height);
			VLRectF rcImage = new VLRectF();
			if(item.image != null) {
				float imageSide = Math.min(rcItem.getWidth(), rcItem.getHeight())/2;
				VLSizeF imageSize = new VLSizeF(imageSide, imageSide);
				rcImage.setSize(imageSize);
				rcImage.moveToY(rcItem.getMiddleY() - rcImage.getHeight()/2);
				if(item.title.length() == 0) {
					rcImage.moveToX(rcItem.getMiddleX() - rcImage.getWidth()/2);
				} else {
					float allItemsWidth = rcText.getWidth() + rcImage.getWidth();
					float border = (rcItem.getWidth() - allItemsWidth) / 3.0f;
					rcImage.moveToX(rcItem.left + border);
					rcText.moveToX(rcImage.right + border);
				}
				VLSizeF sizeOfImage = new VLSizeF(item.image.getWidth(), item.image.getHeight());
				rcImage = VLGeometry.rectOfFitToRectWithSize(rcImage, sizeOfImage);
				rcImage = VLGeometry.roundRect(rcImage);
				VLGraphicsUtils.drawBitmap(canvas, item.image, rcImage, true);
			}
			if(item.title.length() > 0) {
				rcText = VLGeometry.roundRect(rcText);
				this.drawTitle(canvas, item.title, rcText, paint, item.textGravity);
			}
		}
	}
	
	protected Bitmap getLeftImage() {
		return null;
	}
	
	protected Bitmap getMiddleImage() {
		return null;
	}
	
	protected Bitmap getRightImage() {
		return null;
	}
	
	protected Path getContentClipPath() {
		return null;
	}
	
	public int itemsCount() {
		return _items.size();
	}
	
	public void addItem(String title, Bitmap image, float widthWeight) {
		ItemInfo item = new ItemInfo();
		item.title = title;
		item.image = image;
		item.widthWeight = widthWeight;
		_items.add(item);
		this.invalidate();
	}
	
	public void addItem(String title, Bitmap image) {
		addItem(title, image, 1.0f);
	}
	
	public void addItem(String title) {
		addItem(title, null);
	}
	
	public void setItemTitle(int index, String title) {
		ItemInfo item = _items.get(index);
		if(!item.title.equals(title))
		{
			item.title = title;
			this.invalidate();
		}
	}
	
	public VLColor textColor() {
		return _textColor;
	}
	
	public void setTextColor(VLColor color) {
		if(color == null)
			color = VLColor.ColorsCommon.Black;
		if(!_textColor.equals(color)) {
			_textColor = color;
			this.invalidate();
		}
	}
	
	public void setShadowColor(VLColor shadowColor) {
		if(shadowColor == null)
			shadowColor = new VLColor();
		if(!_shadowColor.equals(shadowColor)) {
			_shadowColor = new VLColor(shadowColor);
			this.invalidate();
		}
	}

	public void setShadowOffset(VLSizeF shadowOffset) {
		if(shadowOffset == null)
			shadowOffset = new VLSizeF();
		if(!_shadowOffset.equals(shadowOffset)) {
			_shadowOffset = new VLSizeF(shadowOffset);
			this.invalidate();
		}
	}
	
	public int selectedIndex() {
		return _selectedIndex;
	}
	
	public void setSelectedIndex(int selectedIndex) {
		if(selectedIndex < -1)
			selectedIndex = -1;
		if(selectedIndex >= _items.size())
			selectedIndex = _items.size() - 1;
		if(_selectedIndex != selectedIndex) {
			_selectedIndex = selectedIndex;
			this.invalidate();
		}
	}

	public VLColor selectedColor() {
		return _selectedColor;
	}
	
	public void setSelectedColor(VLColor selectedColor) {
		if(!_selectedColor.equals(selectedColor)) {
			_selectedColor = selectedColor;
			this.invalidate();
		}
	}

	public VLColor selectedBorderColor() {
		return _selectedBorderColor;
	}
	
	public void setSelectedBorderColor(VLColor selectedBorderColor) {
		if(!_selectedBorderColor.equals(selectedBorderColor)) {
			_selectedBorderColor = selectedBorderColor;
			this.invalidate();
		}
	}
	
	public VLFontInfo font() {
		return _font;
	}
	
	public void setFont(VLFontInfo font) {
		if(font == null)
			font = new VLFontInfo();
		if(!_font.equals(font)) {
			_font = font;
			this.invalidate();
		}
	}
	
	public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
		_onItemSelectedListener = onItemSelectedListener;
	}

	public boolean selectionByTouchEnabled() {
		return _selectionByTouchEnabled;
	}
	
	public void setSelectionByTouchEnabled(boolean selectionByTouchEnabled) {
		_selectionByTouchEnabled = selectionByTouchEnabled;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		_onItemClickListener = onItemClickListener;
	}
}
