package be.evoliris.android.musicapp.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.evoliris.android.musicapp.db.DbHelper;
import be.evoliris.android.musicapp.model.Album;

public class AlbumDAO {

    public static final String TABLE_NAME = "album";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_OUT_DATE = "out_date";
    public static final String COL_GENRE = "genre";
    public static final String COL_IMAGE_URL = "image_url";
    public static final String COL_FAVORITE = "favorite";
    public static final String COL_RATING = "rating";

    public static final String CREATE_REQUEST = "CREATE TABLE " + TABLE_NAME
            + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITLE + " TEXT NOT NULL, "
            + COL_OUT_DATE + " DATE, "
            + COL_GENRE + " TEXT, "
            + COL_IMAGE_URL + " TEXT, "
            + COL_FAVORITE + " BOOLEAN NOT NULL, "
            + COL_RATING + " FLOAT "
            + ");";

    public static final String DROP_REQUEST = "DROP TABLE " + TABLE_NAME;

    private DbHelper helper;
    private Context context;
    private SQLiteDatabase db;

    public AlbumDAO(Context context) {
        this.context = context;
    }

    public AlbumDAO openWritable() {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
        return this;
    }

    public AlbumDAO openReadable() {
        helper = new DbHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    public void close() {
        helper.close();
        db.close();
    }

    // region CRUD

    public boolean create(Album album) {

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, album.getTitle());
        values.put(COL_OUT_DATE, album.getOutDate().getTime());
        values.put(COL_IMAGE_URL, album.getImageUrl());
        values.put(COL_GENRE, album.getGenre());
        values.put(COL_FAVORITE, album.isFavorite());
        values.put(COL_RATING, album.getRating());
        long id = db.insert(TABLE_NAME, null, values);
        if (id == -1) {
            return false;
        } else {
            album.setId(id);
            return true;
        }
    }

    public Album readById(long id) {

        Cursor c = readCursorById(id);

        if (c != null) {
            return cursorToAlbum(c);
        } else {
            return null;
        }
    }

    public Cursor readCursorById(long id) {
        Cursor c = db.query(TABLE_NAME,
                null, COL_ID + " = " + id,
                null, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            return c;
        } else {
            c.close();
            return null;
        }
    }

    public List<Album> readAll() {
        ArrayList<Album> albums = new ArrayList<>();

        Cursor c = readAllCursor();

        if (c != null) {

            do {
                albums.add(cursorToAlbum(c));
            } while (c.moveToNext());
            c.close();
        }
        return albums;
    }

    public Cursor readAllCursor() {
        Cursor c = db.query(TABLE_NAME,
                null, null,
                null, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            return c;
        } else {
            c.close();
            return null;
        }
    }

    public void deleteByTitle(String title) {
        db.delete(TABLE_NAME, COL_TITLE + " = ?", new String[] { title });
    }

    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }

    public static Album cursorToAlbum(Cursor c) {
        Album a = new Album();
        a.setId(c.getLong(c.getColumnIndex(COL_ID)));
        a.setTitle(c.getString(c.getColumnIndex(COL_TITLE)));
        a.setOutDate(new Date(c.getLong(c.getColumnIndex(COL_OUT_DATE))));
        a.setGenre(c.getString(c.getColumnIndex(COL_GENRE)));
        a.setImageUrl(c.getString(c.getColumnIndex(COL_IMAGE_URL)));
        a.setFavorite(c.getInt(c.getColumnIndex(COL_FAVORITE)) == 1);
        a.setRating(c.getFloat(c.getColumnIndex(COL_RATING)));
        return a;
    }



    // endregion
}
