package com.akirayjin.recipediary;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akirayjin.utility.Utility;

public class RecipeEditActivity extends Activity {
	private ImageView recipePicture;
	private EditText recipeTitle;
	private EditText recipeIngridient;
	private EditText recipeStepCook;
	private Bitmap image;
	private ImageView saveButton;
	private RecipeDatabaseSource rdbs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_edit);
		setAllView();
		setPictureClick();
		rdbs = new RecipeDatabaseSource(this);
	}

	private void setAllView(){
		recipePicture = (ImageView)findViewById(R.id.recipe_picture);
		recipeTitle = (EditText)findViewById(R.id.recipe_title);
		recipeIngridient = (EditText)findViewById(R.id.recipe_ingredient);
		recipeStepCook = (EditText)findViewById(R.id.recipe_cook);
		saveButton = (ImageView)findViewById(R.id.save_button);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveToDatabase();
			}
		});
	}

	private void setPictureClick(){
		recipePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
	}

	private void selectImage() {
		final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(RecipeEditActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take Photo"))
				{
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, 1);
				}
				else if (options[item].equals("Choose from Gallery"))
				{
					Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 2);
				}
				else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				image = (Bitmap) data.getExtras().get("data");
				recipePicture.setImageBitmap(image);
			} else if (requestCode == 2) {
				Uri selectedImage = data.getData();
				String[] filePath = { MediaStore.Images.Media.DATA };
				Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				File f = new File(picturePath);
				image = Utility.decodeFile(f);
				recipePicture.setImageBitmap(image);
			}
		}
	}
	
	private void saveToDatabase(){
		String title = recipeTitle.getText().toString();
		String ingredient = recipeIngridient.getText().toString();
		String cookProcess = recipeStepCook.getText().toString();
		if(image == null){
			image = BitmapFactory.decodeResource(this.getResources(),R.drawable.camera_icon);
		}
		rdbs.open();
		rdbs.addRecipe(title, image, ingredient, cookProcess);
		rdbs.close();
		Toast.makeText(this, "Resep sudah tersimpan", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, RecipeMainList.class);
		intent.putExtra("fromEdit", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
		finish();
	}
}
