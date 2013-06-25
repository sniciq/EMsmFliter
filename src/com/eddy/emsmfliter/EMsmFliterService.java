package com.eddy.emsmfliter;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class EMsmFliterService extends Service {
	
	private EMsmReceiver emsmReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		emsmReceiver = new com.eddy.emsmfliter.EMsmReceiver();
		IntentFilter filter =  new  IntentFilter();
		filter.setPriority(100);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(emsmReceiver, filter);
		Toast.makeText(getApplicationContext(), "短信拦截已经启动", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
