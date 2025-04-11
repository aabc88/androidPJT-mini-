package com.example.androidpjt.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidpjt.R;
import com.example.androidpjt.databinding.ItemMainRvBinding;
import com.example.androidpjt.model.Student;
import com.example.androidpjt.util.DialogUtil;

import java.util.ArrayList;

class MainViewHolder extends RecyclerView.ViewHolder {
    ItemMainRvBinding binding;

    MainViewHolder(ItemMainRvBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

}

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {
    ArrayList<Student> datas;
    Activity context;

    public MainAdapter(Activity context, ArrayList<Student> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMainRvBinding binding = ItemMainRvBinding.inflate(context.getLayoutInflater(), parent, false);
        return new MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Student student = datas.get(position);
        holder.binding.itemNameTv.setText(student.getName());
        holder.binding.itemIv.setOnClickListener(v -> {
            DialogUtil.showCustomDialog(context, R.drawable.ic_student_large);
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}

