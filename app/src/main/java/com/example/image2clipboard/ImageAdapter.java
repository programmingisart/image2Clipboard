package com.example.image2clipboard;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MediaFileInfo> mediaList;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.ic_launcher_foreground,



    };

    // Constructor
    public ImageAdapter(Context c, ArrayList<MediaFileInfo> mediaList){
        mContext = c;
        this.mediaList = mediaList;
    }


    @Override
    public int getCount() {
        return mediaList.size();
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);



        imageView.setBackgroundColor(Color.BLACK);
        imageView.setImageResource(mThumbIds[0]);
        Uri u = mediaList.get(position).getFileUri();

        int boxSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, mContext.getResources().getDisplayMetrics());

        imageView.setImageBitmap(mediaList.get(position).getFileBitmap());

        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(boxSize, boxSize));
        return imageView;
    }

}