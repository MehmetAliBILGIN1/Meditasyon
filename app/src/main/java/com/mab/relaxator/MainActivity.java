package com.mab.relaxator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AdView mAdView;
    DrawerLayout drawer;
    Button menuButton;
    Button yenile;
    ListView listView;
    ListViewAdapter adapter;

    List<Meditasyon> liste = new ArrayList<>();
    private RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    String url = "http://mistikyol.com/mistikmobil/mobiljson.php";
    RequestQueue queue;
    SQLiteHandler veritabani;

    String[] names = new String[]{
            "Son Eklenenler",
            "Favorilerim",
            "İyi Bir Uyku",
            "Kişisel Gelişim",
            "Mistik İşler",
            "Olumlamalar",
            "Çekim Yasası Bilgileri",
    };

    String[] linkler = new String[]{
            "0",
            "00",
            "1",
            "2",
            "3",
            "4",
            "7",
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        queue = NetworkController.getInstance(this).getRequestQueue();
        queue.add(new JsonObjectRequest(0, url, null, new listener(), new error()));

        veritabani = new SQLiteHandler(getApplicationContext());

        drawer = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menu_button);
        listView = findViewById(R.id.left_drawer_child);
        yenile = findViewById(R.id.refreshBtn);


        yenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veritabani.verileriSil();
                queue.add(new JsonObjectRequest(0, url, null, new listener(), new error()));
                Toast.makeText(MainActivity.this, "Veriler Sunucu İle Senkronize Ediliyor", Toast.LENGTH_SHORT).show();

            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

                menuButton.setBackgroundResource(R.drawable.menu_open);

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                menuButton.setBackgroundResource(R.drawable.menu_close);

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        adapter = new ListViewAdapter(this, names, linkler);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                drawer.closeDrawer(GravityCompat.START);

                verileriGoster(linkler[position]);

            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        verileriGoster("0");

      /*  mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
    }


    private class listener implements Response.Listener<JSONObject> {
        public void onResponse(JSONObject response) {

            try {

                JSONArray meditasyonlar = response.getJSONArray("meditasyonlar");
                int length = meditasyonlar.length();

                for (int i = 0; i < length; i++) {
                    try {

                        JSONObject meditasyon = meditasyonlar.getJSONObject(i);

                        Cursor kayitlar = veritabani.getWritableDatabase().rawQuery("SELECT count(*) FROM veriler WHERE anahtar = '"
                                + meditasyon.getString("id") + "'", null);
                        kayitlar.moveToFirst();
                        int sayi = kayitlar.getInt(0);

                        if (sayi == 0) {
                            veritabani.veriEkle(meditasyon.getString("baslik"), meditasyon.getString("aciklama"),
                                    meditasyon.getString("thumbnail"), meditasyon.getString("sesdosyasi"),
                                    meditasyon.getString("tarih"), meditasyon.getString("kategori"), meditasyon.getString("id"));
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "For içinde Hata Oluştu" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                verileriGoster("0");

            } catch (Exception e) {

                Toast.makeText(MainActivity.this, "Hata Oluştu" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            Log.i("gelen cevap", response.toString());
        }
    }

    @SuppressLint("Recycle")
    public void verileriGoster(String kate) {
        liste.clear();

        Cursor kayitlar;
        switch (kate) {
            case "0":
                kayitlar = veritabani.getWritableDatabase().rawQuery("SELECT * FROM veriler ORDER BY anahtar DESC", null);
                break;

            case "00":
                kayitlar = veritabani.getWritableDatabase().rawQuery("SELECT * FROM veriler WHERE favorite = 1  ORDER BY anahtar DESC", null);
                break;

            default:
                kayitlar = veritabani.getWritableDatabase().rawQuery("SELECT * FROM veriler WHERE category = " + kate + " ORDER BY anahtar DESC", null);
                break;
        }


        while (kayitlar.moveToNext()) {
            String id = kayitlar.getString(kayitlar.getColumnIndex("id"));
            String baslik = kayitlar.getString(kayitlar.getColumnIndex("tile"));
            String aciklama = kayitlar.getString(kayitlar.getColumnIndex("description"));
            String resim = "http://mistikyol.com/mistikmobil/thumbnails/" + kayitlar.getString(kayitlar.getColumnIndex("image"));
            String ses = "http://mistikyol.com/mistikmobil/audios/" + kayitlar.getString(kayitlar.getColumnIndex("ses"));
            String favori = kayitlar.getString(kayitlar.getColumnIndex("favorite"));
            String tarih = kayitlar.getString(kayitlar.getColumnIndex("date"));
            String kategori = kayitlar.getString(kayitlar.getColumnIndex("category"));


            liste.add(new Meditasyon(id, baslik, aciklama, resim, ses, favori, tarih, kategori));

        }

        recyclerAdapter = new RecyclerAdapter(liste);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view1, int position) {
                Intent gecis = new Intent(MainActivity.this, DetailActivity.class);
                gecis.putExtra("anahtarDegeri", liste.get(position).getId());
                startActivity(gecis);
            }
        }));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private class error implements Response.ErrorListener {
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, "Hataaaa" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
    }

       /* @Override
        public void onBackPressed () {

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                super.onBackPressed();
            } else {
                super.onBackPressed();
            }
        }*/

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Uygulama kapatılıyor!")
                .setMessage("Uygulamayı Kapatmak İstediğinize Emin Misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                            finish();
                        } else {
                            finish();
                        }
                    }
                })
                .setNegativeButton("Hayır", null)
                .show();
    }

}
