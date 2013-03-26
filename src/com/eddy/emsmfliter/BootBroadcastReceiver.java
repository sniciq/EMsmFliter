package com.eddy.emsmfliter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)){
			Intent emsmIntent = new Intent(context, EMsmFliterService.class);
			context.startService(emsmIntent);
		}
	}

}
