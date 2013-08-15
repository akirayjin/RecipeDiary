package com.akirayjin.recipediary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RecipeModel {
	private long id;
	private String title;
	private Bitmap image;
	private String ingredient;
	private String cookProcess;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(byte[] bb) {
		this.image = BitmapFactory.decodeByteArray(bb, 0, bb.length);
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getCookProcess() {
		return cookProcess;
	}
	public void setCookProcess(String cookProcess) {
		this.cookProcess = cookProcess;
	}
}
