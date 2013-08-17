package com.akirayjin.recipediary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.akirayjin.utility.ConstantVariable;

public class RecipeMainList extends Activity {
	private ImageView addButton;
	private ListView recipeList;
	private LinearLayout emptyList;
	private RecipeDatabaseSource rdbs;
	private List<RecipeModel> array;
	private RecipeListAdapter adapter;
	private int selectedItem;
	private boolean isGoToOtherActivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rdbs = new RecipeDatabaseSource(this);
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
				isGoToOtherActivity = true;
				Intent intent = new Intent(RecipeMainList.this, RecipeEditActivity.class);
				startActivity(intent);
			}
		});
	}

	private void setRecipeList(){
		refreshRecipeArray();
		if(array.size() > 0){
			adapter = new RecipeListAdapter(this, array, rdbs);
			recipeList.setAdapter(adapter);
			recipeList.setOnItemClickListener(listClick);
			registerForContextMenu(recipeList);
			showDataList(true);
		}else{
			showDataList(false);
		}
	}

	private OnItemClickListener listClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adap, View v, int position,
				long id) {
			isGoToOtherActivity = true;
			Intent intent = new Intent(RecipeMainList.this, RecipeViewActivity.class);
			intent.putExtra(ConstantVariable.PUT_EXTRA_POSITION, position);
			startActivity(intent);
		}
	};

	private void showDataList(boolean state){
		if(state){
			emptyList.setVisibility(View.GONE);
			recipeList.setVisibility(View.VISIBLE);
		}else{
			emptyList.setVisibility(View.VISIBLE);
			recipeList.setVisibility(View.GONE);
		}
	}

	private void refreshRecipeArray(){
		rdbs.open();
		array = rdbs.getAllRecipe();
		rdbs.close();
		if(array.isEmpty()){
			showDataList(false);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		selectedItem = info.position;
		RecipeModel currentItem = (RecipeModel)adapter.getItem(selectedItem);
		menu.setHeaderTitle(currentItem.getTitle());  
		menu.add(0, v.getId(), 0, ConstantVariable.EDIT);  
		menu.add(0, v.getId(), 0, ConstantVariable.DELETE);
	}

	@Override  
	public boolean onContextItemSelected(MenuItem item) {  
		if(item.getTitle() == ConstantVariable.EDIT){
			isGoToOtherActivity = true;
			Intent intent = new Intent(RecipeMainList.this, RecipeEditActivity.class);
			intent.putExtra(ConstantVariable.PUT_EXTRA_EDIT_RECIPE, true);
			intent.putExtra(ConstantVariable.PUT_EXTRA_POSITION, selectedItem);
			startActivity(intent);
		}  
		else if(item.getTitle() == ConstantVariable.DELETE){
			adapter.deleteRecipe(selectedItem);
			refreshRecipeArray();
		}  
		else {
			return false;
		}  
		return true;
	}
	
	@Override
	protected void onResume() {
		if(isGoToOtherActivity){
			isGoToOtherActivity = false;
			refreshRecipeArray();
			adapter.setNewData((ArrayList<RecipeModel>)array);
		}
		super.onResume();
	}
}
