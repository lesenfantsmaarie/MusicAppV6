package be.evoliris.android.musicapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import be.evoliris.android.musicapp.R;
import be.evoliris.android.musicapp.db.dao.AlbumDAO;
import be.evoliris.android.musicapp.model.Album;

/**
 * Created by Evoliris on 29/08/2016.
 */
public class AlbumCursorAdapter extends CursorAdapter{
    //va alimenter liste via cursor, objet complet, au lieu d'une liste avec seulement le nom de l album

    private LayoutInflater inflater;

    public AlbumCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return  inflater.inflate(R.layout.album_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_listitem_title);
        Album a = AlbumDAO.cursorToAlbum(cursor);
        tvTitle.setText(a.getTitle());

    }

}
