package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.GameoverPopupBinding;
import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.GameplayBinding;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;

public class GameplayActivity extends AppCompatActivity {

    private GameplayBinding binding;
    private GameScene gameScene;
    private int record;

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

    public void onGameOverHandler(int score) {
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    private void storeData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("record", record);

        editor.apply();
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);

        record = sharedPreferences.getInt("record", 0);
    }
}
