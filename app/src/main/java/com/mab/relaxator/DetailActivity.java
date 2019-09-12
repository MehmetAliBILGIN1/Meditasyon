package com.mab.relaxator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.concurrent.TimeUnit;



public class DetailActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    Bundle bundle;
    SQLiteHandler veritabani;
    Cursor imlec;

    private AdView mAdView;

    SeekBar seekBar;
    TextView basYazi;
    TextView sonYazi;
    Button playBtn;
    Button favoriBtn;
    MediaPlayer mediaPlayer;
    private int toplamSure;
    private final Handler handler = new Handler();

    String gelenID, id, baslik, aciklama, resim, ses, favori, tarih, kategori;

    @SuppressLint("ClickableViewAccessibility")

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        veritabani = new SQLiteHandler(getApplicationContext());
        seekBar = findViewById(R.id.seekbarPlay);
        basYazi = findViewById(R.id.textBas);
        sonYazi = findViewById(R.id.textSon);
        playBtn = findViewById(R.id.playButton);
        favoriBtn = findViewById(R.id.favButton);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


        playBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    mediaPlayer.setDataSource(ses);
                    mediaPlayer.prepare();

                } catch (Exception e) {
                    Log.e("Hata oluştu", String.valueOf(e));
                    //   Toast.makeText(DetailActivity.this, "Hata Oluştu", Toast.LENGTH_SHORT).show();
                }

                toplamSure = mediaPlayer.getDuration();

                @SuppressLint("DefaultLocale") String toplamSureYazisi = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(toplamSure) - TimeUnit.DAYS.toHours(TimeUnit.MICROSECONDS.toDays(toplamSure)),
                        TimeUnit.MILLISECONDS.toMinutes(toplamSure) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(toplamSure)),
                        TimeUnit.MILLISECONDS.toSeconds(toplamSure) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(toplamSure))

                );

                sonYazi.setText(toplamSureYazisi);


                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.pause);
                }

                seekBarGuncelle();

            }
        });

        bundle = getIntent().getExtras();
        gelenID = bundle.getString("anahtarDegeri");

        if (bundle != null) {

            imlec = veritabani.getWritableDatabase().rawQuery("SELECT * FROM veriler WHERE id = '" + gelenID + "'", null);


            while (imlec.moveToNext()) {
                id = imlec.getString(imlec.getColumnIndex("id"));
                baslik = imlec.getString(imlec.getColumnIndex("tile"));
                aciklama = imlec.getString(imlec.getColumnIndex("description"));
                resim = "http://mistikyol.com/mistikmobil/thumbnails/" + imlec.getString(imlec.getColumnIndex("image"));
                ses = "http://mistikyol.com/mistikmobil/audios/" + imlec.getString(imlec.getColumnIndex("ses"));
                favori = imlec.getString(imlec.getColumnIndex("favorite"));
                tarih = imlec.getString(imlec.getColumnIndex("date"));
                kategori = imlec.getString(imlec.getColumnIndex("category"));
            }
        }


        if (favori.equals("1")) {
            favoriBtn.setBackgroundResource(R.drawable.minus);
        } else {
            favoriBtn.setBackgroundResource(R.drawable.plus);
        }

        favoriBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (favori.equals("1")) {
                    veritabani.favoriDurumu(id, "0");
                    favoriBtn.setBackgroundResource(R.drawable.plus);
                    Toast.makeText(DetailActivity.this, "Favorilerden Çıkarıldı!", Toast.LENGTH_SHORT).show();
                    favori = "0";
                } else {
                    veritabani.favoriDurumu(id, "1");
                    favoriBtn.setBackgroundResource(R.drawable.minus);
                    Toast.makeText(DetailActivity.this, "Favorilere Eklendi!", Toast.LENGTH_SHORT).show();
                    favori = "1";
                }
            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mediaPlayer.isPlaying()) {
                    int sure = (toplamSure / 100) * seekBar.getProgress();
                    mediaPlayer.seekTo(sure);
                }
                return false;
            }
        });

    }

    private void seekBarGuncelle() {

        final int anlikUzunSure = mediaPlayer.getCurrentPosition();

        seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / toplamSure * 100)));
        if (mediaPlayer.isPlaying()) {
            Runnable hareket = new Runnable() {
                @Override
                public void run() {
                    seekBarGuncelle();

                    @SuppressLint("DefaultLocale") String anlikSureYazisi = String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(anlikUzunSure) - TimeUnit.DAYS.toHours(TimeUnit.MICROSECONDS.toDays(anlikUzunSure)),
                            TimeUnit.MILLISECONDS.toMinutes(anlikUzunSure) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(anlikUzunSure)),
                            TimeUnit.MILLISECONDS.toSeconds(anlikUzunSure) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(anlikUzunSure))

                    );

                    basYazi.setText(anlikSureYazisi);
                }
            };

            handler.postDelayed(hareket, 1000);
        }
    }


    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

        seekBar.setSecondaryProgress(i);

    }


    public void onCompletion(MediaPlayer mediaPlayer) {

        playBtn.setBackgroundResource(R.drawable.play);

    }


    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }
}
