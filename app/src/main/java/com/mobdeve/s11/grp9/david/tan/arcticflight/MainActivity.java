package com.mobdeve.s11.grp9.david.tan.arcticflight;
//test push
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.HomeBinding;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private HomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("MainActivity", "Play button clicked");
                Intent intent = new Intent(MainActivity.this, GameplayActivity.class);
                startActivity(intent);
            }
        });

    }

}
