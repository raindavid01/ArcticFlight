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
import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.HomeBinding;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;

public class MainActivity extends AppCompatActivity {
    private HomeBinding binding;
    private MediaPlayer mediaPlayer;

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
                stop_music();
                Log.d("MainActivity", "Play button clicked");
                Intent intent = new Intent(MainActivity.this, GameplayActivity.class);
                startActivity(intent);
            }
        });

        binding.shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Shop button clicked");
                Intent intent = new Intent(MainActivity.this, ShopActivity.class); // Ensure ShopActivity.class exists
                startActivity(intent);
                finish();
            }
        });

        binding.settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Settings button clicked");
                showDialogSettingsPopup();
            }
        });
    }

    private void stop_music() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void showDialogSettingsPopup() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.settings);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(850, 1100);
            window.setBackgroundDrawableResource(R.drawable.rounded_corners);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();

        Button backBtn = dialog.findViewById(R.id.shopbackBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.d("MainActivity", "Back button clicked");
            }
        });
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop_music();
    }
}
