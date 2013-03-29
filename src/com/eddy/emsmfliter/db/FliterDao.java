package com.eddy.emsmfliter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class FliterDao {
	
	private DatabaseHelper dbhelper;
	
	public FliterDao(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	
	public Cursor getAllCursor() {
		try {
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor c = db.rawQuery("select * from " + FliterDBInfo.tableName +" order by " + FliterDBInfo.column_name_id + " desc;", null);
			while(c.moveToNext()) {
				int id = c.getInt(c.getColumnIndex(FliterDBInfo.column_name_id));
				String number = c.getString(c.getColumnIndex(FliterDBInfo.column_name_number));
				String context = c.getString(c.getColumnIndex(FliterDBInfo.column_name_filterInfo));
				
				System.out.println(id + " | " + number + " | " + context);
			}
			
			return c;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteById(int id) {
		SQLiteDatabase db = dbhelper.getWritableDatabase(); 
		db.execSQL("delete from " + FliterDBInfo.tableName + " where " + FliterDBInfo.column_name_id + "=?;", new Object[] {id});
	}
	
	public void insert(FliterEty ety) {
		SQLiteDatabase db = dbhelper.getWritableDatabase(); 
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(FliterDBInfo.tableName);
		sb.append("(");
		sb.append(FliterDBInfo.column_name_number + ",");
		sb.append(FliterDBInfo.column_name_filterInfo);
		sb.append(")values(?,?)");
		db.execSQL(sb.toString(),new Object[]{ety.getNumber(), ety.getFilterInfo()});  
	}

	public long selectCountByNumber(String number) {
		try {
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			String sql = "select count(1) from " + FliterDBInfo.tableName +" where " + FliterDBInfo.column_name_number + " = '" + number + "'";
			SQLiteStatement s = db.compileStatement(sql);
			long c = s.simpleQueryForLong();
			return c;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
