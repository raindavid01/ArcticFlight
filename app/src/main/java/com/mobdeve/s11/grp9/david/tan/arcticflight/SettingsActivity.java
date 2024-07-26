package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private FrameLayout blurOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Show the settings dialog when the activity is created
        showDialogSettingsPopup();
    }

    private void showDialogSettingsPopup() {
        Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.settings);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(dialogInterface -> finish());

        dialog.getWindow().setLayout(850, 1100);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button backBtn = dialog.findViewById(R.id.shopbackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        SeekBar volumeSeekBar = dialog.findViewById(R.id.seekBar);
        setupVolumeControl(volumeSeekBar);

        dialog.show();
    }

    private void setupVolumeControl(SeekBar seekBar) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });
    }
}
