package be.evoliris.android.musicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.evoliris.android.musicapp.db.dao.AlbumDAO;
import be.evoliris.android.musicapp.model.Album;

public class MainActivity extends AppCompatActivity {

    private Button btnMainAdd;
    private ListView lvMainAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // findViewById

        btnMainAdd = (Button) findViewById(R.id.btn_main_add);
        lvMainAlbums = (ListView) findViewById(R.id.lv_main_albums);

        // Listeners

        btnMainAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddAlbum = new Intent(MainActivity.this, AddAlbumActivity.class);
                startActivityForResult(toAddAlbum, AddAlbumActivity.REQUEST_CODE);
            }
        });

        lvMainAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                Toast.makeText(MainActivity.this,
                        "Click on " + tv.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ListView

        updateListView();

        registerForContextMenu(lvMainAlbums);

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (prefs.getBoolean("news", false)) {
            String email = prefs.getString("email", "");
            Toast.makeText(MainActivity.this, "Sending a mail to " + email,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case AddAlbumActivity.REQUEST_CODE:

                switch (resultCode) {
                    case RESULT_OK:
                        updateListView();
                        Toast.makeText(MainActivity.this,
                                "Album was successfully added",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "Canceled",
                                Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Log.e(getLocalClassName(), "Unknown resultCode");
                        break;
                }

                break;

            default:
                Log.e(getLocalClassName(), "Unknown requestCode");
                break;
        }
    }

    private void updateListView() {

        ArrayList<String> data = new ArrayList<>();

        AlbumDAO albumDAO = new AlbumDAO(MainActivity.this);
        List<Album> albums = albumDAO.openReadable().readAll();

        for (Album a : albums) {
            data.add(a.getTitle());
        }
/*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                data);
                */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.album_list_item,
                R.id.tv_listitem_title,
                data);

        lvMainAlbums.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all:
                AlbumDAO dao = new AlbumDAO(MainActivity.this);
                dao.openWritable().deleteAll();
                dao.close();
                updateListView();
                break;

            case R.id.settings:
                Intent toPreferences =
                        new Intent(MainActivity.this, AppPreferenceActivity.class);
                startActivity(toPreferences);
                break;

            default:
                // TODO
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_main_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        TextView tv = (TextView) info.targetView;

        Log.i(getLocalClassName(), tv.getText().toString());

        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                // TODO
                break;

            case R.id.context_menu_delete:
                AlbumDAO dao = new AlbumDAO(MainActivity.this);
                dao.openWritable().deleteByTitle(tv.getText().toString());
                dao.close();
                updateListView();
                break;
        }
        return true;
    }
}
