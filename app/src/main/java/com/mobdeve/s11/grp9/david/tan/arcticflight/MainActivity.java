package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.HomeBinding;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;

public class MainActivity extends AppCompatActivity {
    private HomeBinding binding; // View binding for the home screen
    private MediaPlayer mediaPlayer; // Media player for background music
    private boolean shouldStopMusic = true; // Flag to control whether music should stop on pause

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the activity to fullscreen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get screen dimensions and set game constants
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GameConstants.SCREEN_WIDTH = displayMetrics.widthPixels;
        GameConstants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        GameConstants.CONTEXT = getApplicationContext();

        // Initialize and start the media player for home screen background music
        mediaPlayer = MediaPlayer.create(this, R.raw.home_screen);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Set click listener for the play button
        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldStopMusic = true;
                stopMusic();
                Log.d("MainActivity", "Play button clicked");
                Intent intent = new Intent(MainActivity.this, GameplayActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the shop button
        binding.shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldStopMusic = true;
                stopMusic();
                Log.d("MainActivity", "Shop button clicked");
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set click listener for the settings button
        binding.settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldStopMusic = false;
                Log.d("MainActivity", "Settings button clicked");
                blurBackground();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to blur the background
    private void blurBackground() {
        ConstraintLayout mainLayout = findViewById(R.id.home_layout);
        mainLayout.setAlpha(0.5f);  // Reduce the alpha value to create a blurred effect
    }

    // Method to stop the background music
    private void stopMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reinitialize and start the media player if it was stopped
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.home_screen);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // Restore the alpha value of the main layout to remove the blur effect
        ConstraintLayout mainLayout = findViewById(R.id.home_layout);
        if (mainLayout != null) {
            mainLayout.setAlpha(1.0f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shouldStopMusic) {
            stopMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
        cleanup();
    }

    // Method to clean up resources
    private void cleanup() {
        if (binding != null) {
            binding = null;
        }
    }
}
