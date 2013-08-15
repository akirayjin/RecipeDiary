package com.akirayjin.recipediary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDatabaseHelper extends SQLiteOpenHelper{
	public static final String TABLE_RECIPE = "recipes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_IMAGE = "image";
	public static final String COLUMN_INGREDIENT = "ingredient";
	public static final String COLUMN_COOK_PROCESS = "cook_process";

	private static final String DATABASE_NAME = "recipe_database.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_RECIPE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_TITLE
			+ " text not null, " + COLUMN_IMAGE
			+ " blob, " + COLUMN_INGREDIENT
			+ " text not null, " + COLUMN_COOK_PROCESS
			+ " text not null);";

	public RecipeDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
	    onCreate(db);
	}
}
