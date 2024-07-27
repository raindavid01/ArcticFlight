package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.DatabaseHelper;

public class ShopActivity extends AppCompatActivity implements MyAdapter.HatSelectionListener {

    private RecyclerView recyclerView; // RecyclerView to display the shop items
    RecyclerView.LayoutManager layoutManager; // LayoutManager for RecyclerView
    MyAdapter myAdapter; // Adapter for RecyclerView
    private TextView tvTotalCoins; // TextView to display the total coins
    private DatabaseHelper dbHelper; // Database helper to interact with the local database
    private MediaPlayer mediaPlayer; // MediaPlayer for background music

    // Array of drawable resource IDs for hats
    int[] arr = {R.drawable.santa, R.drawable.tophat, R.drawable.cap};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop); // Set the content view to the shop layout
        recyclerView = findViewById(R.id.recyclerView); // Initialize RecyclerView
        tvTotalCoins = findViewById(R.id.coinTv); // Initialize TextView for coins

        // Initialize DatabaseHelper and get total coins
        dbHelper = new DatabaseHelper(this);
        int totalCoins = dbHelper.getTotalCoins();

        // Set up the RecyclerView with a GridLayoutManager
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize the adapter with the context, hat array, total coins, and database helper
        myAdapter = new MyAdapter(this, arr, totalCoins, dbHelper);
        myAdapter.setHatSelectionListener(this); // Set the hat selection listener
        Log.d("HatSelection", "Listener set in ShopActivity");

        recyclerView.setAdapter(myAdapter); // Set the adapter to the RecyclerView
        recyclerView.setHasFixedSize(true); // Set fixed size for RecyclerView

        updateCoinDisplay(totalCoins); // Update the coin display

        // Initialize and start the MediaPlayer for background music
        mediaPlayer = MediaPlayer.create(this, R.raw.shop);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Set up the back button to navigate back to MainActivity
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

    // Method to update the coin display
    public void updateCoinDisplay(int coins) {
        tvTotalCoins.setText(coins + " Coins");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the coin display and adapter's coin count when activity resumes
        int totalCoins = dbHelper.getTotalCoins();
        updateCoinDisplay(totalCoins);
        myAdapter.setCoins(totalCoins);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop and release the MediaPlayer when the activity is paused
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources when the activity is destroyed
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

    // Method to save the selected hat index in SharedPreferences
    public void saveHatSelection(int hatIndex) {
        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selectedHatIndex", hatIndex);
        editor.apply(); // Apply the changes asynchronously
    }

    // Callback method when a hat is selected
    @Override
    public void onHatSelected(int hatIndex) {
        Log.d("HatSelection", "Hat selected: " + hatIndex); // Log the selected hat index
        saveHatSelection(hatIndex); // Save the selected hat index
        Toast.makeText(this, "Hat selected: " + hatIndex, Toast.LENGTH_SHORT).show(); // Show a toast message
    }
}
