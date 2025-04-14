package com.example.androidpjt.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidpjt.databinding.ItemDetailScoreRvBinding;

import java.util.ArrayList;
import java.util.Map;

class DetailViewHolder extends RecyclerView.ViewHolder {
    ItemDetailScoreRvBinding binding;

    DetailViewHolder(ItemDetailScoreRvBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {
    ArrayList<Map<String, String>> datas;
    Activity context;

    public DetailAdapter(Activity context, ArrayList<Map<String, String>> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailScoreRvBinding binding = ItemDetailScoreRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new DetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Map<String, String> score = datas.get(position);
        holder.binding.rvDetailScore.setText(score.get("score"));
        holder.binding.rvDetailDate.setText(score.get("date"));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
