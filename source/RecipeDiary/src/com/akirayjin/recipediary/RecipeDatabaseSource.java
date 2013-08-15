package com.akirayjin.recipediary;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class RecipeDatabaseSource {
	private SQLiteDatabase database;
	private RecipeDatabaseHelper dbHelper;
	private String[] allColumns = { RecipeDatabaseHelper.COLUMN_ID,
			RecipeDatabaseHelper.COLUMN_TITLE, RecipeDatabaseHelper.COLUMN_IMAGE,
			RecipeDatabaseHelper.COLUMN_INGREDIENT, RecipeDatabaseHelper.COLUMN_COOK_PROCESS};

	public RecipeDatabaseSource(Context context){
		dbHelper = new RecipeDatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public RecipeModel addRecipe(String title, Bitmap image, String ingdredient, String cookProcess) {
		ContentValues values = new ContentValues();
		values.put(RecipeDatabaseHelper.COLUMN_TITLE, title);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] bArray = bos.toByteArray();
		values.put(RecipeDatabaseHelper.COLUMN_IMAGE, bArray);
		values.put(RecipeDatabaseHelper.COLUMN_INGREDIENT, ingdredient);
		values.put(RecipeDatabaseHelper.COLUMN_COOK_PROCESS, cookProcess);
		long insertId = database.insert(RecipeDatabaseHelper.TABLE_RECIPE, null,
				values);
		Cursor cursor = database.query(RecipeDatabaseHelper.TABLE_RECIPE,
				allColumns, RecipeDatabaseHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		RecipeModel recipeModel = cursorToRecipeModel(cursor);
		cursor.close();
		return recipeModel;
	}

	public void deleteRecipe(RecipeModel recipeModel) {
		long id = recipeModel.getId();
		database.delete(RecipeDatabaseHelper.TABLE_RECIPE, RecipeDatabaseHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<RecipeModel> getAllRecipe() {
		List<RecipeModel> recipeModelArray = new ArrayList<RecipeModel>();

		Cursor cursor = database.query(RecipeDatabaseHelper.TABLE_RECIPE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RecipeModel recipeModel = cursorToRecipeModel(cursor);
			recipeModelArray.add(recipeModel);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return recipeModelArray;
	}

	private RecipeModel cursorToRecipeModel(Cursor cursor) {
		RecipeModel recipeModel = new RecipeModel();
		recipeModel.setId(cursor.getLong(0));
		recipeModel.setTitle(cursor.getString(1));
		recipeModel.setImage(cursor.getBlob(2));
		recipeModel.setIngredient(cursor.getString(3));
		recipeModel.setCookProcess(cursor.getString(4));
		return recipeModel;
	}
}
