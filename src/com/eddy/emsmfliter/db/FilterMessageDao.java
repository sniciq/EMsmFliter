package com.eddy.emsmfliter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FilterMessageDao {
	
	private DatabaseHelper dbhelper;

	public FilterMessageDao(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	
	public Cursor getAllCursor() {
		try {
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor c = db.rawQuery("select * from " + MessageDBInfo.tableName +" order by " + MessageDBInfo.column_name_receiveTime + " desc;", null);
			return c;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void clear() {
		SQLiteDatabase db = dbhelper.getWritableDatabase(); 
		db.execSQL("delete from " + MessageDBInfo.tableName + " where 1=1;");
	}
	
	public void deleteById(int id) {
		SQLiteDatabase db = dbhelper.getWritableDatabase(); 
		db.execSQL("delete from " + MessageDBInfo.tableName + " where " + MessageDBInfo.column_name_id  + "=?;", new Object[] {id});
	}
	
	public void insert(FilterMessageEty ety) {
		SQLiteDatabase db = dbhelper.getWritableDatabase(); 
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(MessageDBInfo.tableName);
		sb.append("(");
		sb.append(MessageDBInfo.column_name_number + ",");
		sb.append(MessageDBInfo.column_name_messageBody + ",");
		sb.append(MessageDBInfo.column_name_receiveTime );
		sb.append(")values(?,?,?)");
		db.execSQL(sb.toString(),new Object[]{ety.getNumber(), ety.getMessageBody(), ety.getReceiveTime().getTime()});  
	}
	
}
