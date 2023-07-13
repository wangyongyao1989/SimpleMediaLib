package com.mmrobot.mediafacer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mmrobot.mediafacer.mediaHolders.AudioFolderContent;
import com.mmrobot.mediafacer.mediaHolders.AudioContent;

import java.io.File;
import java.util.ArrayList;

public class AudioGet {

    private static AudioGet audioGet;
    private final Context mAudioContext;
    public static final Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final Uri internalContentUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
    public static final String externalVolume = "external";
    public static final String internalVolume = "internal";
    private static Cursor cursor;

    private AudioGet(Context context) {
        mAudioContext = context.getApplicationContext();
    }

    static AudioGet getInstance(Context context) {
        if (audioGet == null) {
            audioGet = new AudioGet(context);
        }
        return audioGet;
    }

    String Selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    String[] projection =
            new String[]{
                    MediaStore.Audio.Media._ID
                    , MediaStore.Audio.Media.DATA
                    , MediaStore.Audio.Media.TITLE
                    , MediaStore.Audio.Media.SIZE
                    , MediaStore.Audio.Media.DISPLAY_NAME
                    , MediaStore.Audio.Media.TITLE
                    , MediaStore.Audio.Media.DURATION
                    , MediaStore.Audio.Media.ALBUM
                    , MediaStore.Audio.Media.DATE_MODIFIED
                    , MediaStore.Audio.Media.ALBUM_ID
                    , MediaStore.Audio.Media.ARTIST
                    , MediaStore.Files.FileColumns.MIME_TYPE};

    public ArrayList<AudioFolderContent> getAllAudioFolder(Uri contentLocation) {
        ArrayList<AudioFolderContent> musicFolders = new ArrayList<>();
        cursor = mAudioContext.getContentResolver().query(contentLocation
                , projection
                , null
                , null
                , "LOWER (" + MediaStore.Audio.Media.TITLE + ") ASC");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    AudioFolderContent audioFolder = new AudioFolderContent();
                    AudioContent audioContent = new AudioContent();

                    audioContent.setModifiedData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)));

                    String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    File path = new File(dataPath);
                    File parent = new File(path.getParent());
                    String parentName = parent.getName();
                    String parentPath = parent.getAbsolutePath();
                    audioContent.setFilePath(dataPath);
                    audioContent.setMusicSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                    audioContent.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                    audioContent.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    audioContent.setMusicId(id);

                    audioContent.setMusicUri(Uri.withAppendedPath(externalContentUri, String.valueOf(id)).toString());

                    audioContent.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));

                    long dur = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    audioContent.setDuration(dur);

                    long album_id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri imageUri = Uri.withAppendedPath(sArtworkUri, String.valueOf(album_id));
                    audioContent.setArt_uri(imageUri);
                    audioContent.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    audioFolder.setFolderName(parentName);
                    audioFolder.setFolderPath(path.getPath());
                    audioFolder.getMusicFiles().add(audioContent);
                    musicFolders.add(audioFolder);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        for (int i = 0; i < musicFolders.size(); i++) {
            Log.d("audio folders", musicFolders.get(i).getFolderName() + " and path = " + musicFolders.get(i).getFolderPath() + " " + musicFolders.get(i).getNumberOfSongs());
        }
        return musicFolders;
    }

    /**
     * 移除audioUri音频项目
     *
     * @param audioUri
     */
    public boolean deleteAudio(Uri audioUri) {
        ContentResolver resolver = mAudioContext.getContentResolver();
        String selection = null;
        String[] selectionArgs = null;
        int numImagesRemoved = resolver.delete(
                audioUri,
                selection,
                selectionArgs);
        return numImagesRemoved == 1;
    }

    /**
     * 以MediaStore.Video.Media.ARTIST作为收藏的标志
     *
     * @param uri
     * @param artist
     */
    public void favoriteAudio(Uri uri, String artist) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.ARTIST, artist);
        ContentResolver resolver = mAudioContext.getContentResolver();
        resolver.update(uri, values, null, null);
        //发送广播。通知此媒体文件已经可以用啦
        mAudioContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

}
