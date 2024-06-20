package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.GameplayBinding;

public class GameplayActivity extends AppCompatActivity {

    private GameplayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pauseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Gameplay Activity", "pause button clicked");
                Dialog dialog = new Dialog(GameplayActivity.this);
                dialog.setContentView(R.layout.pause_popup);
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawableResource(R.drawable.rounded_corners);
                    window.setLayout(700, 500);

                }
                dialog.show();

                binding.pauseBtn.setVisibility(View.INVISIBLE);

                Button quitBtn = dialog.findViewById(R.id.quitBtn);
                Button resumeBtn = dialog.findViewById(R.id.resumeBtn);// Assuming 'quit' is the ID of your quit button

                quitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Log.d("Gameplay Activity", "quit button clicked");
                        Intent intent = new Intent(GameplayActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                resumeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        binding.pauseBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        binding.character.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Gameplay Activity", "character clicked");
                Dialog dialog = new Dialog(GameplayActivity.this);
                dialog.setContentView(R.layout.gameover_popup);
                Window window = dialog.getWindow();
                if (window != null) {
                    //window.setBackgroundDrawableResource(R.drawable.rounded_corners);
                    window.setLayout(1100, 1100);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                }
                dialog.show();

                Button retryBtn = dialog.findViewById(R.id.retryBtn);
                Button homeBtn = dialog.findViewById(R.id.homedeathBtn);// Assuming 'quit' is the ID of your quit button

                retryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Log.d("Gameplay Activity", "quit button clicked");
                    }
                });

                homeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(GameplayActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });

    }
}
