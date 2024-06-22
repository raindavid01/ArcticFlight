package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    int []arr;

    public MyAdapter(int[] arr) {
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.hatIv.setImageResource(arr[position]);
        //holder.buyBtn
        Button buyBtn = holder.itemView.findViewById(R.id.buyBtn);

        buyBtn.setOnClickListener(v -> {
            buyBtn.setBackgroundResource(R.drawable.ownbtn);
        });
    }

    @Override
    public int getItemCount() {
        return arr.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView hatIv;
        //Button buyBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hatIv=itemView.findViewById(R.id.hatIv);
            //buyBtn=itemView.findViewById(R.id.buyBtn);

        }
    }
}
