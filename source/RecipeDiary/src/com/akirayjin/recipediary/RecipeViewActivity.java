package com.akirayjin.recipediary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akirayjin.utility.ConstantVariable;

public class RecipeViewActivity extends Activity {
	private int selectedItem;
	private TextView title;
	private TextView ingredient;
	private TextView cookProcess;
	private ImageView picture;
	private ImageView editButton;
	private ImageView deleteButton;
	private ArrayList<RecipeModel> recipeArray;
	private RecipeDatabaseSource rdbs;
	private RecipeModel currentRecipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_view);
		Intent intent = getIntent();
		selectedItem = intent.getIntExtra(ConstantVariable.PUT_EXTRA_POSITION, 0);
		rdbs = new RecipeDatabaseSource(this);
		setAllView();
		setAllDataToView();
	}
	
	private void setAllView(){
		title = (TextView)findViewById(R.id.view_recipe_title);
		ingredient = (TextView)findViewById(R.id.view_ingredient);
		cookProcess = (TextView)findViewById(R.id.view_cook_process);
		picture = (ImageView)findViewById(R.id.view_picture);
		editButton = (ImageView)findViewById(R.id.view_edit_button);
		deleteButton = (ImageView)findViewById(R.id.view_delete_button);
		
		editButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecipeViewActivity.this, RecipeEditActivity.class);
				intent.putExtra(ConstantVariable.PUT_EXTRA_EDIT_RECIPE, true);
				intent.putExtra(ConstantVariable.PUT_EXTRA_POSITION, selectedItem);
				startActivity(intent);
				finish();
			}
		});
		
		deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteRecipe();
			}
		});
	}
	
	private void deleteRecipe(){
		rdbs.open();
		rdbs.deleteRecipe(currentRecipe);
		rdbs.close();
		Toast.makeText(this, ConstantVariable.RECIPE_HAS_DELETED, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, RecipeMainList.class);
		intent.putExtra(ConstantVariable.PUT_EXTRA_FROM_EDIT, true);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
		finish();
	}
	
	private void setAllDataToView(){
		rdbs.open();
		recipeArray = (ArrayList<RecipeModel>) rdbs.getAllRecipe();
		rdbs.close();
		currentRecipe = recipeArray.get(selectedItem);
		title.setText(currentRecipe.getTitle());
		ingredient.setText(currentRecipe.getIngredient());
		cookProcess.setText(currentRecipe.getCookProcess());
		picture.setImageBitmap(currentRecipe.getImage());
	}

}
