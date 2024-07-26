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
import android.widget.TextView;

import com.mobdeve.s11.grp9.david.tan.arcticflight.databinding.HomeBinding;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter myAdapter;
    private TextView tvTotalCoins;
    private DatabaseHelper dbHelper;

    int []arr={R.drawable.santa,R.drawable.tophat,R.drawable.cap};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);
        recyclerView = findViewById(R.id.recyclerView);
        tvTotalCoins = findViewById(R.id.coinTv); //

        dbHelper = new DatabaseHelper(this);

        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(arr);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);

        updateCoinDisplay();

        Button shopbackBtn = findViewById(R.id.shopbackBtn);
        shopbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateCoinDisplay() {
        int totalCoins = dbHelper.getTotalCoins();
        tvTotalCoins.setText(totalCoins + " Coins"); // Update the text to reflect the total coins
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCoinDisplay();  // Update coins every time the activity resumes
    }
}
