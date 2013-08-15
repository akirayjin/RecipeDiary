package com.akirayjin.recipediary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeViewActivity extends Activity {
	private int selectedItem;
	private TextView title;
	private TextView ingredient;
	private TextView cookProcess;
	private ImageView picture;
	private ArrayList<RecipeModel> recipeArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_view);
		Intent intent = getIntent();
		selectedItem = intent.getIntExtra("position", 0);
		setAllView();
		setAllDataToView();
	}
	
	private void setAllView(){
		title = (TextView)findViewById(R.id.view_recipe_title);
		ingredient = (TextView)findViewById(R.id.view_ingredient);
		cookProcess = (TextView)findViewById(R.id.view_cook_process);
		picture = (ImageView)findViewById(R.id.view_picture);
	}
	
	private void setAllDataToView(){
		RecipeDatabaseSource rdbs = new RecipeDatabaseSource(this);
		rdbs.open();
		recipeArray = (ArrayList<RecipeModel>) rdbs.getAllRecipe();
		rdbs.close();
		RecipeModel currentRecipe = recipeArray.get(selectedItem);
		title.setText(currentRecipe.getTitle());
		ingredient.setText(currentRecipe.getIngredient());
		cookProcess.setText(currentRecipe.getCookProcess());
		picture.setImageBitmap(currentRecipe.getImage());
	}

}
