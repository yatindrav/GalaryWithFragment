package com.yatindravaishnav.galarywithfragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by Yatindra Vaishnav on 8/16/2016.
 */
public class BitmapImageHandler {
    Bitmap mBitmap;
    String mPath;

    BitmapImageHandler(String path, int maxX, int maxY)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        mPath = path;
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateScaledSize(options, maxX, maxY);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(path, options);
    }

    private int calculateScaledSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inScaleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inScaleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inScaleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inScaleSize;
    }

    private void setBitmapImageOrientation(int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return ;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return;
        }

        try {
            Bitmap bmRotated = mBitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            mBitmap.recycle();
            mBitmap = bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            mBitmap = null;
        }
    }

    public Bitmap getBitmapImageWithOrientation() {
        // First decode with inJustDecodeBounds=true to check dimensions
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        setBitmapImageOrientation(orientation);

        return mBitmap;
    }

    public Bitmap getBitmapImage() {
        return mBitmap;
    }
}