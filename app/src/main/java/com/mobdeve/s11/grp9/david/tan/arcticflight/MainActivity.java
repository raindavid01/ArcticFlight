package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.app.Dialog;
import android.view.Window;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


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

        binding.shopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("MainActivity", "Shop button clicked");
                Intent intent = new Intent(MainActivity.this, ShopActivity.class); // Ensure ShopActivity.class exists
                startActivity(intent);
            }
        });

        binding.settingsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("MainActivity", "Settings button clicked");
                showDialogSettingsPopup();
            }
        });

    }

    private void showDialogSettingsPopup() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.settings);
        Window window = dialog.getWindow();
        if (window!= null) {
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

}
