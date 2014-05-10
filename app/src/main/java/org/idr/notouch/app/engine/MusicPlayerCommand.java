package org.idr.notouch.app.engine;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.MainActivity;
import org.idr.notouch.app.analyzer.SpeechActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Derya on 26.04.2014. (müzik çal müzik derya)
 */
public class MusicPlayerCommand implements Command, TextToSpeech.OnInitListener {

    // REQUEST NAME IDs
    public static final int REQUEST_PLAY_MUSIC = R.string.play_music;

    public static final int REQUEST_PARAM_MUSIC = R.string.music;

    public static final String PARAM_NAME = "music_name";

    private SpeechActivity mActivity;
    private Map<String, String> mParams;
    private TextToSpeech mTts;

    final String TAG = "MusicRetriever";
    ContentResolver mContentResolver;

    List<Item> mItems = new ArrayList<Item>();
    Random mRandom = new Random();

    public MusicPlayerCommand(SpeechActivity activity, Map<String, String> params,
                              ContentResolver cr) {
        mActivity = activity;
        mParams = params;
        mContentResolver = cr;
    }

    @Override
    public void execute() {
        mTts = new TextToSpeech(mActivity, this);
    }

    @Override
    public void onInit(int status) {
        /*
        start music player
         */
        String musicName = mParams.get(PARAM_NAME);
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.i(TAG, "Querying media...");
        Log.i(TAG, "URI: " + uri.toString());

        // Perform a query on the content resolver. The URI we're passing specifies that we
        // want to query for all audio media on external storage (e.g. SD card)
        Cursor cur = mContentResolver.query(uri, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        Log.i(TAG, "Query finished. " + (cur == null ? "Returned NULL." : "Returned a cursor."));

        if (cur == null) {
            // Query failed...
            Log.e(TAG, "Failed to retrieve music: cursor is null :-(");
            return;
        }
        if (!cur.moveToFirst()) {
            // Nothing to query. There is no music on the device. How boring.
            Log.e(TAG, "Failed to move cursor to first row (no query results).");

            return;

        }

        Log.i(TAG, "Listing...");

        // retrieve the indices of the columns where the ID, title, etc. of the song are
        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);

        Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
        Log.i(TAG, "ID column index: " + String.valueOf(titleColumn));

        // add each song to mItems
        //String musicNumber = null;
        Item item = null;

        do {
            Log.i(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
            Item currentItem = new Item(
                    cur.getLong(idColumn),
                    cur.getString(artistColumn),
                    cur.getString(titleColumn),
                    cur.getString(albumColumn),
                    cur.getLong(durationColumn));
            mItems.add(currentItem);

            String name = cur.getString(titleColumn);
            if (name != null && name.equalsIgnoreCase(musicName)) {
                item = currentItem;
                break;
            }
        } while (cur.moveToNext());

        Log.i(TAG, "Done querying media. MusicRetriever is ready.");
        if (item != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, item.getURI());
            mActivity.startActivity(intent);
        } else {
            mTts.speak(mActivity.getString(R.string.music_could_not_be_found),
                    TextToSpeech.QUEUE_FLUSH, null);
            ((MainActivity) mActivity).writeToListView(R.string.music_could_not_be_found, false);
        }
    }

    private ContentResolver getContentResolver() {
        return mContentResolver;
    }

    /**
     * Returns a random Item. If there are no items available, returns null.
     */
    private Item getRandomItem() {
        if (mItems.size() <= 0) return null;
        return mItems.get(mRandom.nextInt(mItems.size()));
    }

    private static class Item {
        long id;
        String artist;
        String title;
        String album;
        long duration;

        public Item(long id, String artist, String title, String album, long duration) {
            this.id = id;
            this.artist = artist;
            this.title = title;
            this.album = album;
            this.duration = duration;
        }

        public long getId() {
            return id;
        }

        public String getArtist() {
            return artist;
        }

        public String getTitle() {
            return title;
        }

        public String getAlbum() {
            return album;
        }

        public long getDuration() {
            return duration;
        }

        public Uri getURI() {
            return ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        }
    }
}
