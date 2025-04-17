package com.example.androidpjt.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Person;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.androidpjt.callback.DialogCallback;
import com.example.androidpjt.databinding.DialogImageBinding;

import java.io.File;

public class DialogUtil {
    //이 클래스는 객체를 반복적으로 생성해서 각자의 메모리에 데이터를 유지하기 위한 목적이 아니고
    //여러곳에서 사용하는 코드의 중복을 피하기 위한 목적이므로 필요한곳에서 호출하겠단 의도
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void logD(String log, String activity) {
        Log.d(activity, "log: " + log);
    }

    public static void showCustomDialog(Context context, int defaultResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DialogImageBinding binding = DialogImageBinding.inflate(inflater);
        builder.setView(binding.dialogIv);
        AlertDialog dialog = builder.create();
        dialog.show();

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), defaultResId);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(bitmap.getWidth(), bitmap.getHeight());
        }
    }

    public static void showCustomDialog(Context context, String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DialogImageBinding binding = DialogImageBinding.inflate(inflater);
        builder.setView(binding.dialogIv);
        AlertDialog dialog = builder.create();
        dialog.show();

        File imgFile = new File(filePath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            binding.dialogIv.setImageBitmap(bitmap);
            Window window = dialog.getWindow();
            if (window != null) {
                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8); // 화면의 50%
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setLayout(width, height);
            }
        }
    }

    //메시지를 뿌리는 그리고 ok, cancel 버튼을 가진 다이얼로그를 띄우는 함수
    //ok, cancel 버튼을 누르면 이벤트처리를 이곳에선 못하고 띄우는 곳에서 callback만 호출함
    public static void showMessageDialog(Context context, String message, String
            positiveText, String negativeText, DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Message");
        builder.setMessage(message);
        if (positiveText != null) {
            builder.setPositiveButton(positiveText, (dialog, which) -> {
                callback.onPositiveCallback();
            });
        }

        if (negativeText != null) {
            builder.setNegativeButton(negativeText, (dialog, which) -> {
                callback.onNegativeCallback();
            });
        }

        builder.show();
    }
}
