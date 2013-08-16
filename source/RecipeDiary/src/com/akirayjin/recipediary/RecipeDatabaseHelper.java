package com.akirayjin.recipediary;

import com.akirayjin.utility.ConstantVariable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDatabaseHelper extends SQLiteOpenHelper{

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ ConstantVariable.TABLE_RECIPE + "(" + ConstantVariable.COLUMN_ID
			+ " integer primary key autoincrement, " + ConstantVariable.COLUMN_TITLE
			+ " text not null, " + ConstantVariable.COLUMN_IMAGE
			+ " blob, " + ConstantVariable.COLUMN_INGREDIENT
			+ " text not null, " + ConstantVariable.COLUMN_COOK_PROCESS
			+ " text not null);";

	public RecipeDatabaseHelper(Context context) {
		super(context, ConstantVariable.DATABASE_NAME, null, ConstantVariable.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ConstantVariable.TABLE_RECIPE);
	    onCreate(db);
	}
}
