package com.akirayjin.recipediary;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.akirayjin.utility.ConstantVariable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class RecipeDatabaseSource {
	private SQLiteDatabase database;
	private RecipeDatabaseHelper dbHelper;
	private String[] allColumns = { ConstantVariable.COLUMN_ID,
			ConstantVariable.COLUMN_TITLE, ConstantVariable.COLUMN_IMAGE,
			ConstantVariable.COLUMN_INGREDIENT, ConstantVariable.COLUMN_COOK_PROCESS};

	public RecipeDatabaseSource(Context context){
		dbHelper = new RecipeDatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public RecipeModel addRecipe(String title, Bitmap image, String ingredient, String cookProcess) {
		ContentValues values = new ContentValues();
		values.put(ConstantVariable.COLUMN_TITLE, title);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] bArray = bos.toByteArray();
		values.put(ConstantVariable.COLUMN_IMAGE, bArray);
		values.put(ConstantVariable.COLUMN_INGREDIENT, ingredient);
		values.put(ConstantVariable.COLUMN_COOK_PROCESS, cookProcess);
		long insertId = database.insert(ConstantVariable.TABLE_RECIPE, null,
				values);
		Cursor cursor = database.query(ConstantVariable.TABLE_RECIPE,
				allColumns, ConstantVariable.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		RecipeModel recipeModel = cursorToRecipeModel(cursor);
		cursor.close();
		return recipeModel;
	}
	
	public RecipeModel updateRecipe(String title, Bitmap image, String ingredient, String cookProcess, long recipeId) {
		ContentValues values = new ContentValues();
		values.put(ConstantVariable.COLUMN_TITLE, title);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] bArray = bos.toByteArray();
		values.put(ConstantVariable.COLUMN_IMAGE, bArray);
		values.put(ConstantVariable.COLUMN_INGREDIENT, ingredient);
		values.put(ConstantVariable.COLUMN_COOK_PROCESS, cookProcess);
		database.update(ConstantVariable.TABLE_RECIPE, values, "_id=" + recipeId, null);
		Cursor cursor = database.query(ConstantVariable.TABLE_RECIPE,
				allColumns, ConstantVariable.COLUMN_ID + " = " + recipeId, null,
				null, null, null);
		cursor.moveToFirst();
		RecipeModel recipeModel = cursorToRecipeModel(cursor);
		cursor.close();
		return recipeModel;
	}

	public void deleteRecipe(RecipeModel recipeModel) {
		long id = recipeModel.getId();
		database.delete(ConstantVariable.TABLE_RECIPE, ConstantVariable.COLUMN_ID
				+ " = " + id, null);
	}

	public List<RecipeModel> getAllRecipe() {
		List<RecipeModel> recipeModelArray = new ArrayList<RecipeModel>();

		Cursor cursor = database.query(ConstantVariable.TABLE_RECIPE,
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
