package com.example.androidpjt.util;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;

import com.example.androidpjt.callback.PermissionCallback;

import java.util.HashSet;

public class PermissionUtil {

    public static void checkAllPermission(ComponentActivity activity, PermissionCallback callback) {
        HashSet<String> permissionSet = new HashSet<>();
        permissionSet.add("android.permission.CALL_PHONE");

        //ActivityResultLauncher<String>=activity.registerForActivityResult();
    }
}
