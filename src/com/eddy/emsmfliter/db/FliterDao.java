package com.eddy.emsmfliter.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class FliterDao {
	
	private DatabaseHelper dbhelper;
	
	public FliterDao(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	
	public Cursor getAllCursor() {
		try {
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor c = db.rawQuery("select * from " + FliterDBInfo.tableName +" order by " + FliterDBInfo.column_name_id + " desc;", null);
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
		sb.append(FliterDBInfo.column_name_type + ",");
		sb.append(FliterDBInfo.column_name_filterInfo);
		sb.append(")values(?,?)");
		db.execSQL(sb.toString(),new Object[]{ety.getType(), ety.getFilterInfo()});  
	}

	public long selectCountByNumber(String number) {
		try {
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			String sql = "select count(1) from " + FliterDBInfo.tableName +" where " + FliterDBInfo.column_name_type + " = 0 and " + FliterDBInfo.column_name_filterInfo + " = '" + number + "'";
			SQLiteStatement s = db.compileStatement(sql);
			long c = s.simpleQueryForLong();
			return c;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 按类型得到所有的过滤器
	 * @param type
	 * @return
	 */
	public List<FliterEty> getAllFliterByType(int type) {
		Cursor c = null;
		try {
			List<FliterEty> retList = new ArrayList<FliterEty>();
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			String sql = "select * from " + FliterDBInfo.tableName +" where " + FliterDBInfo.column_name_type + "=" + type + " order by " + FliterDBInfo.column_name_id + " desc;";
			c = db.rawQuery(sql, null);
			
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				int id = c.getInt(c.getColumnIndex(FliterDBInfo.column_name_id));
				int ftype = c.getInt(c.getColumnIndex(FliterDBInfo.column_name_type));
				String context = c.getString(c.getColumnIndex(FliterDBInfo.column_name_filterInfo));
				
				FliterEty ety = new FliterEty();
				ety.setId(id);
				ety.setType(ftype);
				ety.setFilterInfo(context);
				retList.add(ety);
			}
			return retList;
		}
		catch(Exception e) {
			Log.i("FliterDao", e.getMessage(), e);
			return null;
		}
		finally {
			if(c != null) {
				c.close();
			}
		}
	}
}
