package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;

public class SettingsActivity extends AppCompatActivity {

    private AudioManager audioManager; // AudioManager for controlling volume
    private Button oneBtn, twoBtn, threeBtn; // Buttons for selecting game difficulty
    private SharedPreferences sharedPreferences; // SharedPreferences for saving settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);

        // Show the settings dialog when the activity is created
        showDialogSettingsPopup();
    }

    // Method to show the settings dialog
    private void showDialogSettingsPopup() {
        Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.settings);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(dialogInterface -> finish()); // Finish activity when dialog is dismissed

        dialog.getWindow().setLayout(850, 1100);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button backBtn = dialog.findViewById(R.id.shopbackBtn);
        backBtn.setOnClickListener(v -> dialog.dismiss()); // Dismiss dialog on back button click

        SeekBar volumeSeekBar = dialog.findViewById(R.id.seekBar);
        setupVolumeControl(volumeSeekBar); // Set up volume control SeekBar

        oneBtn = dialog.findViewById(R.id.oneBtn);
        twoBtn = dialog.findViewById(R.id.twoBtn);
        threeBtn = dialog.findViewById(R.id.threebtn);

        // Set default selection based on saved preference
        float savedSpeed = sharedPreferences.getFloat("selectedSpeed", GameConstants.DIFFICULTY.two);
        if (savedSpeed == GameConstants.DIFFICULTY.one) {
            selectButton(oneBtn, GameConstants.DIFFICULTY.one);
        } else if (savedSpeed == GameConstants.DIFFICULTY.two) {
            selectButton(twoBtn, GameConstants.DIFFICULTY.two);
        } else if (savedSpeed == GameConstants.DIFFICULTY.three) {
            selectButton(threeBtn, GameConstants.DIFFICULTY.three);
        }

        // Set click listeners for difficulty buttons
        oneBtn.setOnClickListener(v -> selectButton(oneBtn, GameConstants.DIFFICULTY.one));
        twoBtn.setOnClickListener(v -> selectButton(twoBtn, GameConstants.DIFFICULTY.two));
        threeBtn.setOnClickListener(v -> selectButton(threeBtn, GameConstants.DIFFICULTY.three));

        dialog.show();
    }

    // Method to set up the volume control SeekBar
    private void setupVolumeControl(SeekBar seekBar) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // Get max volume level
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // Get current volume level

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);

        // Set SeekBar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0); // Change volume
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

    // Method to handle button selection and save the selected speed
    private void selectButton(Button selectedButton, float speed) {
        // Reset backgrounds to default
        oneBtn.setBackgroundResource(R.drawable.one);
        twoBtn.setBackgroundResource(R.drawable.two);
        threeBtn.setBackgroundResource(R.drawable.three);

        // Set the selected button's background
        if (selectedButton == oneBtn) {
            selectedButton.setBackgroundResource(R.drawable.selected_1);
        } else if (selectedButton == twoBtn) {
            selectedButton.setBackgroundResource(R.drawable.selected_2);
        } else if (selectedButton == threeBtn) {
            selectedButton.setBackgroundResource(R.drawable.selected_3);
        }

        // Save the selected speed in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("selectedSpeed", speed);
        editor.apply();
    }
}
