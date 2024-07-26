package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private int[] arr;
    private Context context;

    public MyAdapter(Context context, int[] arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Scale down the bitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), arr[position]);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth() / 2, originalBitmap.getHeight() / 2, true);

        holder.hatIv.setImageBitmap(scaledBitmap);

        // Button click listener
        Button buyBtn = holder.itemView.findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(v -> {
            buyBtn.setBackgroundResource(R.drawable.ownbtn);
        });
    }

    @Override
    public int getItemCount() {
        return arr.length;
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
