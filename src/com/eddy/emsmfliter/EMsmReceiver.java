package com.eddy.emsmfliter;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.eddy.emsmfliter.db.FilterMessageDao;
import com.eddy.emsmfliter.db.FilterMessageEty;
import com.eddy.emsmfliter.db.FliterDao;
import com.eddy.emsmfliter.db.FliterEty;

public class EMsmReceiver extends BroadcastReceiver {
	

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		FliterDao fliterDao = new FliterDao(context);
		
		if (action.equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")) {
			SmsMessage sms = getMessagesFromIntent(intent)[0];
			String number = sms.getOriginatingAddress();
			number = trimSmsNumber("+86", number); // 
			String messageBody = sms.getMessageBody();
			//号码过滤
			long count = fliterDao.selectCountByNumber(number);
			if(count > 0) {
				addToDB(context, number, messageBody);
				abortBroadcast(); //中断了广播的继续传递
			}
			
			//内容过滤：
			List<FliterEty> fliterList = fliterDao.getAllFliterByType(FliterEty.type_content);
			if(fliterList == null)
				return;
			
			for(FliterEty ety : fliterList) {
				String content = ety.getFilterInfo();
				Pattern pattern=Pattern.compile(content);
				Matcher matcher = pattern.matcher(sms.getMessageBody());
				if(matcher.find()) {
					addToDB(context, number, messageBody);
					abortBroadcast(); //中断了广播的继续传递
				}
			}
		}
	}
	
	private void addToDB(Context context, String number, String messageBody) {
		FilterMessageEty ety = new FilterMessageEty();
		ety.setNumber(number);
		ety.setMessageBody(messageBody);
		ety.setReceiveTime(new Date());
		FilterMessageDao filterMessageService = new FilterMessageDao(context);
		filterMessageService.insert(ety);
		
		Toast.makeText(context, "拦截到一条垃圾短信！", Toast.LENGTH_SHORT).show();
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
