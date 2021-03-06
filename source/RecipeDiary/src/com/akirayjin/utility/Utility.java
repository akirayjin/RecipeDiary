package com.akirayjin.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class Utility {
	
	public static Bitmap decodeFile(File f){
		try {
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			Matrix matrix = new Matrix();
			if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
				matrix.postRotate(ExifInterface.ORIENTATION_ROTATE_90);
			}else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
				matrix.postRotate(ExifInterface.ORIENTATION_ROTATE_180);
			}else if(orientation == ExifInterface.ORIENTATION_ROTATE_270){
				matrix.postRotate(-ExifInterface.ORIENTATION_ROTATE_90);
			}

			//Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f),null,o);

			//The new size we want to scale to
			final int REQUIRED_SIZE=100;

			//Find the correct scale value. It should be the power of 2.
			int scale=1;
			while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
				scale*=2;

			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			Bitmap sourceBitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
			return rotatedBitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void showDeleteDialog(final Context context, String recipeTitle, OnClickListener clickListener){
		AlertDialog alert = new AlertDialog.Builder(context).create();
		alert.setCancelable(false);
		alert.setTitle(String.format(ConstantVariable.DIALOG_TITLE, recipeTitle));
		alert.setMessage(String.format(ConstantVariable.DIALOG_MESSAGE, recipeTitle));
		alert.setButton(DialogInterface.BUTTON_POSITIVE,ConstantVariable.DIALOG_BUTTON_POSITIVE, clickListener);
		alert.setButton(DialogInterface.BUTTON_NEGATIVE,ConstantVariable.DIALOG_BUTTON_NEGATIVE, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}
}
