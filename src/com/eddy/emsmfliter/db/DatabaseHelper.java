package com.eddy.emsmfliter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, MessageDBInfo.dbName, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + MessageDBInfo.tableName + " ("
				+ MessageDBInfo.column_name_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MessageDBInfo.column_name_number + " TEXT,"
				+ MessageDBInfo.column_name_messageBody + " TEXT,"
				+ MessageDBInfo.column_name_receiveTime + " INTEGER "
				+ ");"
		);
		
		db.execSQL("create table " + FliterDBInfo.tableName + " ("
				+ FliterDBInfo.column_name_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FliterDBInfo.column_name_type + " INTEGER ,"
				+ FliterDBInfo.column_name_filterInfo + " TEXT"
				+ ");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + MessageDBInfo.tableName);
		db.execSQL("drop table if exists " + FliterDBInfo.tableName);
	}

}
