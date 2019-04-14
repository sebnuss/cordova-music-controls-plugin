package com.homerours.musiccontrols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class BitmapUtils {

	public static final int NOTIFICATION_IMAGE_SIZE = 256;

	public static Bitmap resizeForNotification(Bitmap image) {
		return resize(image, NOTIFICATION_IMAGE_SIZE, NOTIFICATION_IMAGE_SIZE);
	}

	public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
		if (image == null) {
			return image;
		}
		int width = image.getWidth();
		int height = image.getHeight();
		if (maxWidth > 0  && maxHeight > 0 && (width > maxWidth || height > maxHeight)) {
			float ratioBitmap = (float) width / (float) height;
			float ratioMax = (float) maxWidth / (float) maxHeight;
	
			int finalWidth = maxWidth;
			int finalHeight = maxHeight;
			if (ratioMax > ratioBitmap) {
				finalWidth = (int) ((float)maxHeight * ratioBitmap);
			} else {
				finalHeight = (int) ((float)maxWidth / ratioBitmap);
			}
			image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
			return image;
		} else {
			return image;
		}
	}

	// Get image from url
	public static Bitmap getBitmap(Context context, String bitmapUrl){
		try{
			if(bitmapUrl.matches("^(https?|ftp)://.*$"))
				// Remote image
				return getBitmapFromURL(bitmapUrl);
			else {
				// Local image
				return getBitmapFromLocal(context, bitmapUrl);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// get Local image
	private static Bitmap getBitmapFromLocal(Context context, String localBitmapUrl){
		try {
			Uri uri = Uri.parse(localBitmapUrl);
			File file = new File(uri.getPath());
			FileInputStream fileStream = new FileInputStream(file);
			BufferedInputStream buf = new BufferedInputStream(fileStream);
			Bitmap myBitmap = BitmapFactory.decodeStream(buf);
			buf.close();
			return myBitmap;
		} catch (Exception ex) {
			try {
				InputStream fileStream = context.getAssets().open("www/" + localBitmapUrl);
				BufferedInputStream buf = new BufferedInputStream(fileStream);
				Bitmap myBitmap = BitmapFactory.decodeStream(buf);
				buf.close();
				return myBitmap;
			} catch (Exception ex2) {
				ex.printStackTrace();
				ex2.printStackTrace();
				return null;
			}
		}
	}

	// get Remote image
	private static Bitmap getBitmapFromURL(String remoteBitmapUrl) {
		try {
			URL url = new URL(remoteBitmapUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}