package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.DatabaseHelper;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private int[] arr;
    private Context context;
    private boolean[] owned;
    private boolean[] equipped;
    private int coins;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    public MyAdapter(Context context, int[] arr, int initialCoins, DatabaseHelper dbHelper) {
        this.context = context;
        this.arr = arr;
        this.owned = new boolean[arr.length];
        this.equipped = new boolean[arr.length];
        this.coins = initialCoins;
        this.dbHelper = dbHelper;
        this.prefs = context.getSharedPreferences("ShopPrefs", Context.MODE_PRIVATE);
        loadOwned();

    }

    private void loadOwned() {
        for (int i = 0; i < arr.length; i++) {
            owned[i] = prefs.getBoolean("owned_" + i, false);
            equipped[i] = prefs.getBoolean("equipped_" + i, false);
        }
    }

    private void saveOwned(int position, boolean isOwned) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("owned_" + position, isOwned);
        editor.apply();
    }

    private void saveEquip(int position, boolean isEquipped) {
        SharedPreferences.Editor editor = prefs.edit();
        if (isEquipped) {
            // First, unequip all items
            for (int i = 0; i < equipped.length; i++) {
                equipped[i] = false; // Update internal state
                editor.putBoolean("equipped_" + i, false);
            }
            // Then equip the selected item
            equipped[position] = true; // Update internal state
            editor.putBoolean("equipped_" + position, true);
        } else {
            // Unequip the selected item
            equipped[position] = false; // Update internal state
            editor.putBoolean("equipped_" + position, false);
        }
        editor.apply();
        notifyItemRangeChanged(0, equipped.length); // Update all items to reflect changes
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), arr[position]);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth() / 2, originalBitmap.getHeight() / 2, true);

        holder.hatIv.setImageBitmap(scaledBitmap);

        Button buyBtn = holder.itemView.findViewById(R.id.buyBtn);
        updateButton(buyBtn, position);
    }

    private void updateButton(Button buyBtn, int position) {
        buyBtn.setEnabled(true);

        if (owned[position]) {
            if (equipped[position]) {
                buyBtn.setBackgroundResource(R.drawable.equipped);
                buyBtn.setOnClickListener(v -> {
                    // Unequip this item
                    saveEquip(position, false);
                    notifyItemChanged(position); // Update this item only
                });
            } else {
                buyBtn.setBackgroundResource(R.drawable.ownbtn);
                buyBtn.setOnClickListener(v -> {
                    // Equip this item and unequip others
                    saveEquip(position, true);
                    notifyItemRangeChanged(0, arr.length); // Update all items to reflect changes
                });
            }
        } else {
            buyBtn.setBackgroundResource(R.drawable.buybtn);
            buyBtn.setOnClickListener(v -> {
                if (coins >= 20) {
                    coins -= 20; // Deduct coins
                    dbHelper.purchaseUpdate(20);
                    ((ShopActivity) context).updateCoinDisplay(coins);// Subtract 20 coins from the total in DB
                    owned[position] = true;
                    saveOwned(position, true);
                    // Equip this newly bought item and unequip others
                    saveEquip(position, true);
                    notifyItemRangeChanged(0, arr.length); // Update all items to reflect changes
                    Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Oops! Not enough coins.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arr.length;
    }

    public void setCoins(int newCoins) {
        coins = newCoins; // Update coins from the activity if needed
    }

    public void cleanup() {
        // Cleanup resources if necessary
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView hatIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hatIv = itemView.findViewById(R.id.hatIv);
        }
    }
}
