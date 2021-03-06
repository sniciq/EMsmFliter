package com.eddy.emsmfliter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.eddy.emsmfliter.db.FliterDBInfo;
import com.eddy.emsmfliter.db.FliterDao;
import com.eddy.emsmfliter.db.FliterEty;

public class FliterSettingAct extends ListActivity {

	private SimpleCursorAdapter filterAdapter;
	private FliterDao fliterDao;
	private ArrayAdapter<String> spinner_type_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setOnCreateContextMenuListener(this);
		
		fliterDao = new FliterDao(this);
		Cursor c = fliterDao.getAllCursor();
		filterAdapter = new SimpleCursorAdapter(this, R.layout.fliterlist_item, 
				c, 
				new String[] { FliterDBInfo.column_name_type, FliterDBInfo.column_name_filterInfo}, 
				new int[] { R.id.fliter_type, R.id.fliter_info}, 
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
			@Override
			public void setViewText(TextView v, String text) {
				if(v.getId() == R.id.fliter_type) {
					text = FliterEty.convertType(Integer.parseInt(text));
				}
				super.setViewText(v, text);
			}
		}; 
		setListAdapter(filterAdapter);
		
		spinner_type_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, FliterEty.Type_Array);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fliterlist_action, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_fliter:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.title_addfliter));
			LayoutInflater lif = LayoutInflater.from(this);
			final View dialogView = lif.inflate(R.layout.fliter_dialog, null);
			Spinner spinner = (Spinner) dialogView.findViewById(R.id.dialog_flitertype);
			spinner.setAdapter(spinner_type_adapter);
			builder.setView(dialogView);
			builder.setPositiveButton(getResources().getString(R.string.confirm_ok), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					EditText intxt =  (EditText) dialogView.findViewById(R.id.dialog_fliterinfo);
					Spinner spinner = (Spinner) dialogView.findViewById(R.id.dialog_flitertype);
					
					FliterEty ety = new FliterEty();
					String typeStr = spinner.getSelectedItem().toString();
					ety.setType(FliterEty.convertTypeStr(typeStr));
					
					ety.setFilterInfo(intxt.getText().toString());
					fliterDao.insert(ety);
					filterAdapter.changeCursor(fliterDao.getAllCursor());
					dialog.dismiss();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.confirm_cancel), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.options_fliter, menu);
		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		menu.setHeaderTitle(cursor.getString(cursor.getColumnIndex(FliterDBInfo.column_name_filterInfo)));
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new ComponentName(this, FliterSettingAct.class), null, intent, 0, null);		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		final int id = cursor.getInt(cursor.getColumnIndex(FliterDBInfo.column_name_id));
		
		switch (item.getItemId()) {
			case R.id.delete_fliter:
				fliterDao.deleteById(id);
				filterAdapter.changeCursor(fliterDao.getAllCursor());
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
}
