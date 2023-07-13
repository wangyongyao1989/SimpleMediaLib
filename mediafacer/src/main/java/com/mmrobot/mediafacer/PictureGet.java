package com.mmrobot.mediafacer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import com.mmrobot.mediafacer.mediaHolders.PictureContent;
import com.mmrobot.mediafacer.mediaHolders.PictureFolderContent;

import java.util.ArrayList;

public class PictureGet {

    private static final String TAG = PictureGet.class.getSimpleName();
    private static PictureGet pictureGet;
    private final Context mPictureContext;
    public static final Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final Uri internalContentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    private static Cursor cursor;

    private PictureGet(Context context) {
        mPictureContext = context.getApplicationContext();
    }

    static PictureGet getInstance(Context context) {
        if (pictureGet == null) {
            pictureGet = new PictureGet(context);
        }
        return pictureGet;
    }

    @SuppressLint("InlinedApi")
    private final String[] Projections = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DESCRIPTION,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_MODIFIED,
    };


    /**
     * Returns an ArrayList of {@link PictureFolderContent}
     */
    @SuppressLint("InlinedApi")
    public ArrayList<PictureFolderContent> getAllPictureFolders() {
        ArrayList<PictureFolderContent> absolutePictureFolders = new ArrayList<>();
        ArrayList<Integer> picturePaths = new ArrayList<>();
        cursor = mPictureContext.getContentResolver().query(externalContentUri, Projections, null, null,
                "LOWER (" + MediaStore.Images.Media.DATE_TAKEN + ") DESC");
        try {
            cursor.moveToFirst();
            do {
                PictureFolderContent photoFolder = new PictureFolderContent();
                PictureContent pictureContent = new PictureContent();

                pictureContent.setPictureName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pictureContent.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pictureContent.setPictureSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                pictureContent.setModifiedData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)));
                pictureContent.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION)));

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                pictureContent.setPictureId(id);

                pictureContent.setPhotoUri(Uri.withAppendedPath(externalContentUri, String.valueOf(id)).toString());

                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                int bucket_id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));

                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));

                folderpaths = folderpaths + folder + "/";
                if (!picturePaths.contains(bucket_id)) {
                    picturePaths.add(bucket_id);
                    photoFolder.setBucket_id(bucket_id);
                    photoFolder.setFolderPath(folderpaths);
                    photoFolder.setFolderName(folder);
                    photoFolder.getPhotos().add(pictureContent);
                    absolutePictureFolders.add(photoFolder);
                } else {
                    for (PictureFolderContent folderX : absolutePictureFolders) {
                        if (folderX.getBucket_id() == bucket_id) {
                            folderX.getPhotos().add(pictureContent);
                        }
                    }
                }

            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return absolutePictureFolders;
    }

    /**
     * 移除audioUri音频项目
     *
     * @param pictureUri
     */
    public boolean deletePicture(Uri pictureUri) {
        ContentResolver resolver = mPictureContext.getContentResolver();
        String selection = null;
        String[] selectionArgs = null;
        int numImagesRemoved = resolver.delete(
                pictureUri,
                selection,
                selectionArgs);
        return numImagesRemoved == 1;
    }

    /**
     * 以MediaStore.Video.Media.BUCKET_ID作为收藏的标志
     *
     * @param uri
     * @param artist
     */
    public void favoritePicture(Uri uri, String artist) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DESCRIPTION, artist);
        ContentResolver resolver = mPictureContext.getContentResolver();
        resolver.update(uri, values, null, null);

        //发送广播。通知此媒体文件已经可以用啦
        mPictureContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }


}
