package com.akirayjin.recipediary;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeListAdapter extends BaseAdapter{
	private List<RecipeModel> recipeArray;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	private RecipeDatabaseSource rdbs;

	public RecipeListAdapter(Context context, List<RecipeModel> recipeArray, RecipeDatabaseSource rdbs){
		this.rdbs = rdbs;
		this.mInflater = LayoutInflater.from(context);
		this.recipeArray = recipeArray;
	}

	@Override
	public int getCount() {
		return recipeArray.size();
	}

	@Override
	public Object getItem(int position) {
		return recipeArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View currentView = null;
		currentView = mInflater.inflate(R.layout.recipe_list_row, null);

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = currentView;
			holder.title = (TextView)convertView.findViewById(R.id.recipe_title);
			holder.photo = (ImageView)convertView.findViewById(R.id.recipe_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		RecipeModel currentRecipe = recipeArray.get(position);
		holder.title.setText(currentRecipe.getTitle());
		holder.photo.setImageBitmap(currentRecipe.getImage());
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		ImageView photo;
	}
	
	public void deleteRecipe(int position){
		RecipeModel currentRecipe = recipeArray.get(position);
		rdbs.open();
		rdbs.deleteRecipe(currentRecipe);
		recipeArray = rdbs.getAllRecipe();
		rdbs.close();
		notifyDataSetChanged();
	}
	
	public void setNewData(ArrayList<RecipeModel> newdata){
		recipeArray = newdata;
		notifyDataSetChanged();
	}
}
