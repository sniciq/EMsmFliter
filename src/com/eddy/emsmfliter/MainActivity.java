package com.eddy.emsmfliter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.eddy.emsmfliter.db.FilterMessageDao;
import com.eddy.emsmfliter.db.FliterDBInfo;
import com.eddy.emsmfliter.db.MessageDBInfo;

public class MainActivity extends Activity {
	
	private ListView filteredMsmList;
	private SimpleCursorAdapter filteredMsmAdapter;
	private FilterMessageDao filterMessageDao;
	private static final int requestcode_setting = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startService(new Intent(this, EMsmFliterService.class));
		
		filterMessageDao = new FilterMessageDao(this);
		filteredMsmList = (ListView) findViewById(R.id.filteredMsmList);
		filteredMsmList.setOnCreateContextMenuListener(this);
		filteredMsmAdapter = new SimpleCursorAdapter(this, R.layout.msm_item, 
				filterMessageDao.getAllCursor(), 
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
	protected void onResume() {
		filteredMsmAdapter.changeCursor(filterMessageDao.getAllCursor());
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_clear:
			AlertDialog.Builder build = new Builder(this);
			build.setTitle(getResources().getString(R.string.delete_confirm_title));
			build.setMessage("确定删除所有拦截到的短信? ");
			build.setPositiveButton(getResources().getString(R.string.confirm_ok), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					filterMessageDao.clear();
					filteredMsmAdapter.changeCursor(filterMessageDao.getAllCursor());
					dialog.dismiss();
				}
			});
			build.setNegativeButton(getResources().getString(R.string.confirm_cancel), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			build.create().show();
			break;
		case R.id.action_settings:
			Intent intent = new Intent(this, FliterSettingAct.class);
			startActivityForResult(intent, requestcode_setting);
			break;
		case R.id.action_refresh:
			filteredMsmAdapter.changeCursor(filterMessageDao.getAllCursor());
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.options_fliter, menu);
		
		Cursor cursor = (Cursor) filteredMsmList.getAdapter().getItem(info.position);
		menu.setHeaderTitle(cursor.getString(cursor.getColumnIndex(MessageDBInfo.column_name_number)));
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new ComponentName(this, FliterSettingAct.class), null, intent, 0, null);	
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Cursor cursor = (Cursor) filteredMsmList.getAdapter().getItem(info.position);
		final int id = cursor.getInt(cursor.getColumnIndex(MessageDBInfo.column_name_id));
		final int title = cursor.getInt(cursor.getColumnIndex(MessageDBInfo.column_name_number));
		
		switch (item.getItemId()) {
			case R.id.delete_fliter:
				AlertDialog.Builder build = new Builder(this);
				build.setTitle(getResources().getString(R.string.delete_confirm_title));
				build.setMessage(getResources().getString(R.string.delete_confirm_msg) + " " + title + " ? ");
				build.setPositiveButton(getResources().getString(R.string.confirm_ok), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						filterMessageDao.deleteById(id);
						filteredMsmAdapter.changeCursor(filterMessageDao.getAllCursor());
					}
				});
				build.setNegativeButton(getResources().getString(R.string.confirm_cancel), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				build.create().show();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
}
