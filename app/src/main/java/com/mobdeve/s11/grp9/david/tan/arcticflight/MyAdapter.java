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

    private int[] arr; // Array of drawable resource IDs for hats
    private Context context; // Application context
    private boolean[] owned; // Array to track owned hats
    private boolean[] equipped; // Array to track equipped hats
    private int coins; // Current coin count
    private DatabaseHelper dbHelper; // Database helper instance
    private SharedPreferences gamePrefs, shopPrefs; // Shared preferences for game and shop
    private HatSelectionListener hatSelectionListener; // Listener for hat selection events

    // Constructor to initialize the adapter
    public MyAdapter(Context context, int[] arr, int initialCoins, DatabaseHelper dbHelper) {
        this.context = context;
        this.arr = arr;
        this.owned = new boolean[arr.length];
        this.equipped = new boolean[arr.length];
        this.coins = initialCoins;
        this.dbHelper = dbHelper;
        this.shopPrefs = context.getSharedPreferences("ShopPrefs", Context.MODE_PRIVATE);
        this.gamePrefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        loadOwned(); // Load owned and equipped hats from shared preferences
    }

    // Method to set the hat selection listener
    public void setHatSelectionListener(HatSelectionListener listener) {
        this.hatSelectionListener = listener;
    }

    // Method to load owned and equipped hats from shared preferences
    private void loadOwned() {
        for (int i = 0; i < arr.length; i++) {
            owned[i] = shopPrefs.getBoolean("owned_" + i, false);
            equipped[i] = shopPrefs.getBoolean("equipped_" + i, false);
        }
    }

    // Method to save the owned state of a hat to shared preferences
    private void saveOwned(int position, boolean isOwned) {
        SharedPreferences.Editor editor = shopPrefs.edit();
        editor.putBoolean("owned_" + position, isOwned);
        editor.apply();
    }

    // Method to save the equipped state of hats to shared preferences
    private void saveEquip(int position, boolean isEquipped) {
        SharedPreferences.Editor editor = shopPrefs.edit();
        if (isEquipped) {
            // Unequip all hats
            for (int i = 0; i < equipped.length; i++) {
                equipped[i] = false; // Update internal state
                editor.putBoolean("equipped_" + i, false);
            }
            // Equip the selected hat
            equipped[position] = true; // Update internal state
            editor.putBoolean("equipped_" + position, true);
        } else {
            // Unequip the selected hat
            equipped[position] = false; // Update internal state
            editor.putBoolean("equipped_" + position, false);
        }
        editor.apply();
        notifyItemRangeChanged(0, equipped.length); // Update all items to reflect changes
    }

    // Method to inflate the layout for each item in the RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new MyViewHolder(view);
    }

    // Method to bind data to the view holder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), arr[position]);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth() / 2, originalBitmap.getHeight() / 2, true);

        holder.hatIv.setImageBitmap(scaledBitmap);

        Button buyBtn = holder.itemView.findViewById(R.id.buyBtn);
        updateButton(buyBtn, position);
    }

    // Method to update the button state and set its click listeners
    private void updateButton(Button buyBtn, int position) {
        buyBtn.setEnabled(true);

        if (owned[position]) {
            if (equipped[position]) {
                buyBtn.setBackgroundResource(R.drawable.equipped);
                buyBtn.setOnClickListener(v -> {
                    // Unequip this item
                    saveEquip(position, false);
                    hatSelectionListener.onHatSelected(0); // Notify hat selection with index 0 (no hat equipped)
                    notifyItemChanged(position); // Update this item only
                });
            } else {
                buyBtn.setBackgroundResource(R.drawable.ownbtn);
                buyBtn.setOnClickListener(v -> {
                    // Equip this item and unequip others
                    saveEquip(position, true);
                    hatSelectionListener.onHatSelected(position + 1); // Notify hat selection with correct index
                    notifyItemRangeChanged(0, arr.length); // Update all items to reflect changes
                });
            }
        } else {
            buyBtn.setBackgroundResource(R.drawable.buybtn);
            buyBtn.setOnClickListener(v -> {
                if (coins >= 20) {
                    coins -= 20; // Deduct coins
                    dbHelper.purchaseUpdate(20);
                    ((ShopActivity) context).updateCoinDisplay(coins); // Subtract 20 coins from the total in DB
                    owned[position] = true;
                    saveOwned(position, true);
                    // Equip this newly bought item and unequip others
                    saveEquip(position, true);
                    hatSelectionListener.onHatSelected(position + 1); // Notify hat selection with correct index
                    notifyItemRangeChanged(0, arr.length); // Update all items to reflect changes
                    Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Oops! Not enough coins.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Interface for hat selection listener
    public interface HatSelectionListener {
        void onHatSelected(int hatIndex);
    }

    // Method to return the number of items in the adapter
    @Override
    public int getItemCount() {
        return arr.length;
    }

    // Method to update coins from the activity if needed
    public void setCoins(int newCoins) {
        coins = newCoins;
    }

    // Method to cleanup resources if necessary
    public void cleanup() {
        // Cleanup resources if necessary
    }

    // ViewHolder class for the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView hatIv; // ImageView for displaying the hat

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hatIv = itemView.findViewById(R.id.hatIv);
        }
    }
}
