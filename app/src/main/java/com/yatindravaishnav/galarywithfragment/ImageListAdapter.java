package com.yatindravaishnav.galarywithfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Yatindra Vaishnav on 8/15/2016.
 */
public class ImageListAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<String> itemList;
    Integer imgSizeX, imgSizeY;

    ImageListAdapter(Context mCtx, int imageWd, int imageHi) {
        mContext=mCtx;
        itemList = new ArrayList<String>();
        imgSizeX = imageWd;
        imgSizeY = imageHi;
    }

    public void add(String path){
        itemList.add(path);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    public int getCountPerPage(int maxX, int maxY) {
        int hori_image_cnt = (maxX/imgSizeX);
        int ver_img_cnt = (maxY/imgSizeY);

        hori_image_cnt++;
        ver_img_cnt++;

        if ((hori_image_cnt*ver_img_cnt) < getCount()) {
            return getCount();
        } else {
            return (hori_image_cnt * ver_img_cnt);
        }
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(imgSizeX, imgSizeY));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        BitmapImageHandler imgHandler = new BitmapImageHandler(itemList.get(position), imgSizeX, imgSizeY);
        Bitmap bm = imgHandler.getBitmapImageWithOrientation();

        imageView.setImageBitmap(bm);
        return imageView;
    }

}
