package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.GameoverPopupBinding;

public class GameOverActivity extends AppCompatActivity {

    private GameoverPopupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameoverPopupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int score = getIntent().getIntExtra("score", 0);
        binding.score.setText("Score: " + score);

        binding.retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, GameplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.homedeathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
