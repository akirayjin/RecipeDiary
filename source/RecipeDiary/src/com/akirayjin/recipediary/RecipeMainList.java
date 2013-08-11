package com.akirayjin.recipediary;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RecipeMainList extends Activity {
	private ImageView addButton;
	private ListView recipeList;
	private LinearLayout emptyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setAllView();
		setAddOnClickListener();
		setRecipeList();
	}
	
	private void setAllView(){
		addButton = (ImageView)findViewById(R.id.add_button);
		recipeList = (ListView)findViewById(R.id.recipe_list);
		emptyList = (LinearLayout)findViewById(R.id.empty_list);
	}
	
	private void setAddOnClickListener(){
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	private void setRecipeList(){
		showDataList(false);
	}
	
	private void showDataList(boolean state){
		if(state){
			emptyList.setVisibility(View.GONE);
			recipeList.setVisibility(View.VISIBLE);
		}else{
			emptyList.setVisibility(View.VISIBLE);
			recipeList.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
