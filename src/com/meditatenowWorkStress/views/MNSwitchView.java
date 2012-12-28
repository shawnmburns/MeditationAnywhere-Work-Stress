package com.meditatenowWorkStress.views;

import android.content.Context;
import com.meditatenowWorkStress.R;
import com.vl.ctrls.VLSwitchView;
import com.vl.drawing.VLImagesCache;

public class MNSwitchView extends VLSwitchView {

	public MNSwitchView(Context context) {
		super(context, VLImagesCache.getInstance().getImageByResource(R.drawable.switch_thumb, context.getResources()),
				R.drawable.switch_back, "ON", "OFF");
	}

}
