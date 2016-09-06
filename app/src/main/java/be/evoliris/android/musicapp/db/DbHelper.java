package be.evoliris.android.musicapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.evoliris.android.musicapp.db.dao.AlbumDAO;

public class DbHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "music_db";
    public static int DB_VERSION = 3;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AlbumDAO.CREATE_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(AlbumDAO.DROP_REQUEST);
        sqLiteDatabase.execSQL(AlbumDAO.CREATE_REQUEST);
    }
}
