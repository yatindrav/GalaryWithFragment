package com.yatindravaishnav.galarywithfragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageListAdapter myImageListAdapter;
    Integer maxX, maxY;
    Integer currentImagePos = 0;
    Integer imagesPerPage = 0;
    boolean imageSelected = false;
    Bundle mSavedInstanceState;
    String targetPath;
    int gvColumnWidth, gvRowWidth;
    android.view.ViewParent parent;

    private int getMyColumnWidth(int maxX)
    {
        int imgCnt = 0;
        for(int i = 2; i < 10; i++) {
            int mod = (maxX % i);
            if (mod == 0) {
                imgCnt = i;
            }
        }
        return (maxX/imgCnt);
    }

    private int getMyRowWidth(int maxY)
    {
        int imgCnt = 0;
        for(int i = 2; i < 10; i++) {
            int mod = (maxY % i);
            if (mod == 0) {
                imgCnt = i;
            }
        }
        return (maxY/imgCnt);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = new Bundle();
        mSavedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        maxX = size.x;
        maxY = size.y;

        gvColumnWidth = getMyColumnWidth(maxX);
        gvRowWidth = getMyRowWidth(maxY);

        GridView gridview = (GridView) findViewById(R.id.thumb_nail_view);
        myImageListAdapter = new ImageListAdapter(this, gvColumnWidth, gvColumnWidth);
        gridview.setAdapter(myImageListAdapter);

        gridview.setColumnWidth(gvColumnWidth);

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        targetPath = ExternalStorageDirectoryPath + "/DCIM/Camera/";

//        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        try {
            File targetDirector = new File(targetPath);

            File[] files = targetDirector.listFiles();
            for (File file : files) {
                myImageListAdapter.add(file.getAbsolutePath());
            }

            imagesPerPage = myImageListAdapter.getCountPerPage(maxX, maxY);
        } catch (Exception e) {
            String fileError = e.toString();
            Toast.makeText(getApplicationContext(), fileError, Toast.LENGTH_LONG).show();
        }

        gridview.setOnItemClickListener(myOnItemClickListener);
        parent = gridview.getParent ();
    }


    AdapterView.OnItemClickListener myOnItemClickListener
            = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            currentImagePos = position;
            imageSelected = true;
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = (View) vi.inflate(R.layout.full_image, null);
            String prompt = (String)parent.getItemAtPosition(position);
/*            Toast.makeText(getApplicationContext(),
                    prompt,
                    Toast.LENGTH_LONG).show();*/
            BitmapImageHandler imgHandler = new BitmapImageHandler(prompt, maxX, maxY);
            ImageView imgView = (ImageView) v.findViewById(R.id.full_image_view);
            Bitmap bm = imgHandler.getBitmapImageWithOrientation();
            imgView.setImageBitmap(bm);
            setContentView(v);
        }
    };

    @Override
    public void onBackPressed() {
        if (imageSelected) {
            Intent a = new Intent(this, MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            currentImagePos = 0;
            imageSelected = false;
            startActivity(a);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
