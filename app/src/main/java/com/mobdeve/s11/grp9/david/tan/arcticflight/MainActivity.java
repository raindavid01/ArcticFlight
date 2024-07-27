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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.HomeBinding;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;

public class MainActivity extends AppCompatActivity {
    private HomeBinding binding;
    private MediaPlayer mediaPlayer;
    private boolean shouldStopMusic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GameConstants.SCREEN_WIDTH = displayMetrics.widthPixels;
        GameConstants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        GameConstants.CONTEXT = getApplicationContext();

        // Initialize and start the media player
        mediaPlayer = MediaPlayer.create(this, R.raw.home_screen);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

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

    private void blurBackground() {
        ConstraintLayout mainLayout = findViewById(R.id.home_layout);
        mainLayout.setAlpha(0.5f);  // Reduce the alpha value to create a blurred effect
    }

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

    private void cleanup() {
        if (binding != null) {
            binding = null;
        }
    }
}
