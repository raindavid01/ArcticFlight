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

    private GameplayBinding binding;
    private GameScene gameScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GameConstants.SCREEN_WIDTH = displayMetrics.widthPixels;
        GameConstants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        GameConstants.CONTEXT = getApplicationContext();

        binding = GameplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameScene = findViewById(R.id.game_scene);

        gameScene.start();
    }

    public void showGameOverDialog(int score, int bestScore) {
        Dialog dialog = new Dialog(GameplayActivity.this);
        dialog.setContentView(R.layout.gameover_popup);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(1100, 1500);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView scoreTextView = dialog.findViewById(R.id.score);
        scoreTextView.setText(String.valueOf(score));

        TextView bestScoreTextView = dialog.findViewById(R.id.best_score);
        bestScoreTextView.setText(String.valueOf(bestScore));

        Button retryButton = dialog.findViewById(R.id.retryBtn);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gameScene.restart();
            }
        });

        Button homeButton = dialog.findViewById(R.id.homedeathBtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(GameplayActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanup();
    }

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
