package com.eddy.emsmfliter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.eddy.emsmfliter.db.FilterMessageService;
import com.eddy.emsmfliter.db.MessageDBInfo;

public class MainActivity extends Activity {
	
	private ListView filteredMsmList;
	private SimpleCursorAdapter filteredMsmAdapter;
	private FilterMessageService filterMessageService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startService(new Intent(this, EMsmFliterService.class));
		
		filterMessageService = new FilterMessageService(this);
		filteredMsmList = (ListView) findViewById(R.id.filteredMsmList);
		filteredMsmAdapter = new SimpleCursorAdapter(this, R.layout.msm_item, 
				filterMessageService.getAllCursor(), 
				new String[] { MessageDBInfo.column_name_number, MessageDBInfo.column_name_receiveTime, MessageDBInfo.column_name_messageBody}, 
				new int[] { R.id.number, R.id.receiveTime, R.id.messageBody}, 
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
			@Override
			public void setViewText(TextView v, String text) {
				if(v.getId() == R.id.receiveTime) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					text = df.format(new Date(Long.parseLong(text)));
				}
				super.setViewText(v, text);
			}
		};
		filteredMsmList.setAdapter(filteredMsmAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
