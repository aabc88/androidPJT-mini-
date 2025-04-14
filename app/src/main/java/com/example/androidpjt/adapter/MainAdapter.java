package com.example.androidpjt.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidpjt.DetailActivity;
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
        holder.binding.itemCallBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                callPhone(student.getPhone());
            } else {
                DialogUtil.showToast(context, context.getString(R.string.permission_denied));
            }
        });

        holder.binding.itemNameTv.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", student.getId());

            context.startActivity(intent);
        });

    }

    private void callPhone(String phone) {
        if (phone != null && !phone.equals("")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
        } else {
            DialogUtil.showToast(context, context.getString(R.string.main_list_phone_error));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}

