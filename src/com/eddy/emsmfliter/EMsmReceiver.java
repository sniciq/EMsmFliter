package com.eddy.emsmfliter;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.eddy.emsmfliter.db.FilterMessageEty;
import com.eddy.emsmfliter.db.FilterMessageService;

public class EMsmReceiver extends BroadcastReceiver {
	

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")) {
			SmsMessage sms = getMessagesFromIntent(intent)[0];
			String number = sms.getOriginatingAddress();
			number = trimSmsNumber("+86", number); // 把国家代码去除掉
			if (number.equals("1252013811481466")) {
				FilterMessageEty ety = new FilterMessageEty();
				ety.setNumber(number);
				ety.setMessageBody(sms.getMessageBody());
				ety.setReceiveTime(new Date());
				FilterMessageService filterMessageService = new FilterMessageService(context);
				filterMessageService.insert(ety);
				abortBroadcast(); //中断广播后，其他要接收短信的应用都没法收到短信广播了
			}
		}
	}

	public final static SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];
		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}

		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];

		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}

		return msgs;
	}

	public final static String trimSmsNumber(String prefix, String number) {
		String s = number;

		if (prefix.length() > 0 && number.startsWith(prefix)) {
			s = number.substring(prefix.length());
		}

		return s;
	}
}
