package com.example.user.fypapp.Presenter;

/**
 * Created by user on 16/12/2016.
 */

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.user.fypapp.Model.App;


public class ImageLoader extends AsyncTask<App, Void, Bitmap> {

    ImageView imageView = null;
    App application = null;
    ProgressBar pb = null;

    public ImageLoader(App application, ImageView imgv, ProgressBar pb) {
        this.application = application;
        this.imageView = imgv;
        this.pb = pb;
    }

    protected Bitmap doInBackground(App... img) {
        return getBitmapDownloaded((String) application.getIcon());
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);  // hide the progressbar after downloading the image.
        application.setImgbit(result);
        imageView.setImageBitmap(result); //set the bitmap to the imageview.
    }

    /** This function downloads the image and returns the Bitmap **/
    private Bitmap getBitmapDownloaded(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url)
                    .getContent());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
        protected void onPreExecute() {
            imageView.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
        }


    /** decodes image and scales it to reduce memory consumption **/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}