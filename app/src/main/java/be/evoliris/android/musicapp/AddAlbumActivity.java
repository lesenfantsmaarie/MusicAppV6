package be.evoliris.android.musicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;

import java.util.Calendar;

import be.evoliris.android.musicapp.db.dao.AlbumDAO;
import be.evoliris.android.musicapp.model.Album;

public class AddAlbumActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 205;

    private EditText etAddTitle;
    private DatePicker dpAddOut;
    private EditText etAddGenre;
    private EditText etAddImage;
    private CheckBox cbAddFavorite;
    private RatingBar rbAddRating;
    private Button btnAddSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        // FindViewById

        etAddTitle = (EditText) findViewById(R.id.et_add_title);
        dpAddOut = (DatePicker) findViewById(R.id.dp_add_out);
        etAddGenre = (EditText) findViewById(R.id.et_add_genre);
        etAddImage = (EditText) findViewById(R.id.et_add_image);
        cbAddFavorite = (CheckBox) findViewById(R.id.cb_add_favorite);
        rbAddRating = (RatingBar) findViewById(R.id.rb_add_rating);
        btnAddSubmit = (Button) findViewById(R.id.btn_add_submit);

        btnAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Album album = new Album();
                album.setTitle(etAddTitle.getText().toString());
                Calendar c = Calendar.getInstance();
                c.set(dpAddOut.getYear(), dpAddOut.getMonth() - 1, dpAddOut.getDayOfMonth());
                album.setOutDate(c.getTime());
                album.setGenre(etAddGenre.getText().toString());
                album.setImageUrl(etAddImage.getText().toString());
                album.setFavorite(cbAddFavorite.isChecked());
                album.setRating(rbAddRating.getRating());

                AlbumDAO albumDAO = new AlbumDAO(AddAlbumActivity.this);
                albumDAO.openWritable();
                if (albumDAO.create(album)) {
                    setResult(RESULT_OK);
                    albumDAO.close();
                    finish();
                }
                albumDAO.close();
            }
        });
    }
}
