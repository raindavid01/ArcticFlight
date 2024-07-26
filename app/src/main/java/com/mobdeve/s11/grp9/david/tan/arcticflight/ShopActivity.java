package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.DatabaseHelper;

import java.io.IOException;

public class ShopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter myAdapter;
    private TextView tvTotalCoins;
    private DatabaseHelper dbHelper;
    private MediaPlayer mediaPlayer;

    int[] arr = {R.drawable.santa, R.drawable.tophat, R.drawable.cap};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);
        recyclerView = findViewById(R.id.recyclerView);
        tvTotalCoins = findViewById(R.id.coinTv);

        dbHelper = new DatabaseHelper(this);

        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(this, arr);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);

        updateCoinDisplay();

        // Initialize and start the media player
        mediaPlayer = MediaPlayer.create(this, R.raw.shop);
        mediaPlayer.setLooping(true);
        mediaPlayer.start(); mediaPlayer.start();


        Button shopbackBtn = findViewById(R.id.shopbackBtn);
        shopbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateCoinDisplay() {
        int totalCoins = dbHelper.getTotalCoins();
        tvTotalCoins.setText(totalCoins + " Coins"); // Update the text to reflect the total coins
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCoinDisplay();  // Update coins every time the activity resumes

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop and release the media player when the activity is paused
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        if (myAdapter != null) {
            myAdapter.cleanup();
            myAdapter = null;
        }
        layoutManager = null;
        recyclerView = null;
        tvTotalCoins = null;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
