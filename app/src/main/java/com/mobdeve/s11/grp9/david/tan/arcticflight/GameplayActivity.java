package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.GameplayBinding;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;

public class GameplayActivity extends AppCompatActivity {

    private GameplayBinding binding; // View binding for the layout
    private GameScene gameScene; // GameScene instance to manage game logic and rendering

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity to fullscreen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get screen dimensions and set them in GameConstants
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GameConstants.SCREEN_WIDTH = displayMetrics.widthPixels;
        GameConstants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        GameConstants.CONTEXT = getApplicationContext();

        // Inflate the layout using view binding
        binding = GameplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the GameScene view and start the game
        gameScene = findViewById(R.id.game_scene);
        gameScene.start();
    }

    // Show the game over dialog with score and best score
    public void showGameOverDialog(int score, int bestScore) {
        Dialog dialog = new Dialog(GameplayActivity.this);
        dialog.setContentView(R.layout.gameover_popup);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(1100, 1500); // Set the dialog size
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Set the score and best score in the dialog
        TextView scoreTextView = dialog.findViewById(R.id.score);
        scoreTextView.setText(String.valueOf(score));

        TextView bestScoreTextView = dialog.findViewById(R.id.best_score);
        bestScoreTextView.setText(String.valueOf(bestScore));

        // Set up retry button click listener
        Button retryButton = dialog.findViewById(R.id.retryBtn);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gameScene.restart(); // Restart the game
            }
        });

        // Set up home button click listener
        Button homeButton = dialog.findViewById(R.id.homedeathBtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(GameplayActivity.this, MainActivity.class);
                startActivity(intent); // Go to the main activity
                finish(); // Finish the current activity
            }
        });

        dialog.show(); // Show the dialog
    }

    // Play a sound using the game scene
    public void playSound(int soundId) {
        if (gameScene != null) {
            gameScene.playSound(soundId);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameScene != null) {
            gameScene.setShouldStopMusic(true);
            gameScene.pauseMusic(); // Pause the game music
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameScene != null) {
            gameScene.setShouldStopMusic(false);
            gameScene.resumeMusic(); // Resume the game music
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanup(); // Clean up resources
    }

    // Clean up resources
    private void cleanup() {
        if (binding != null) {
            binding = null;
        }
        if (gameScene != null) {
            gameScene.cleanup();
            gameScene = null;
        }
    }
}
