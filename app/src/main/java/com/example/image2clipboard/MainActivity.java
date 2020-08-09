package com.example.image2clipboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {

    GridView gridView;
    ArrayList<MediaFileInfo> mediaList = new ArrayList<MediaFileInfo>();

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
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

    int i = 0;
    int off = 0;

    ImageAdapter imgAdap;
    private void parseAllImages() {

        try {
            String[] projection = {MediaStore.Images.Media.DATA};
           /* Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, // Which columns to return
                    null,       // Return all rows
                    null,
                    null); */
            Uri externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String orderBy = MediaStore.Images.Media.DATE_MODIFIED;

            Cursor cursor = getContentResolver().query(externalUri,
                            new String[] {MediaStore.MediaColumns._ID,
                            MediaStore.MediaColumns.DATE_MODIFIED},
                            null,
                            null,
                    orderBy + " DESC"
                    );


            int size = cursor.getCount();

            if (size == 0) {
                Toast.makeText(getApplicationContext(),
                        "No image found",
                        Toast.LENGTH_SHORT).show();
            } else {

                int thumbID = 0;
                while (i < cursor.getCount()) {
                    Log.e("COUNT",Integer.toString(i));
                    cursor.moveToPosition(i);


                    //Log.e("COUNT",Integer.toString(i));
                    MediaFileInfo mediaFileInfo = new MediaFileInfo();
                    //File imgFile = new File(path);

                    mediaFileInfo.setFilePath("Place h");
                    mediaFileInfo.setFileName("Place h");


                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                    Uri uri = ContentUris.withAppendedId(externalUri, Long.parseLong(id));

                    InputStream inputStream = null;

                    try {
                        inputStream = getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                    int newW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

                    int h = bmp.getHeight();
                    int w = bmp.getWidth();

                    int newH = (h/w)*newW;
                    if(newH == 0){
                        newH = 10;
                        newW = 10;
                    }
                    inputStream.close();
                    Bitmap resized = resize(bmp, 500, 500);
                    //Bitmap cropped = Bitmap.createBitmap(resized, 0, 0, newW, newW);
                    mediaFileInfo.setFileBitmap(resized);
                    mediaFileInfo.setFileUri(uri);
                    //mediaFileInfo.setFileType(type);
                    mediaList.add(mediaFileInfo);
                    i++;
                    imgAdap.notifyDataSetChanged();

                    if(i > 10 + off) {
                        off+=10;

                        break;

                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        imgAdap = new ImageAdapter(this, mediaList);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        parseAllImages();

                        sleep(30);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(imgAdap);


        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_image_click));

                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newUri(getContentResolver(), "URI", mediaList.get(position).getFileUri());

                //this.grantUriPermission(getPackageName(), imgURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    clipboard.setPrimaryClip(clip);
                }
                catch (Exception e){
                }

                Toast.makeText(getApplicationContext(),
                        mediaList.get(position).getFileUri().toString(),
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

}