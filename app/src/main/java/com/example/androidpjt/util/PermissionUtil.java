package com.example.androidpjt.util;

import android.os.Build;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.androidpjt.callback.PermissionCallback;

import java.util.HashSet;

public class PermissionUtil {

    public static void checkAllPermission(ComponentActivity activity, PermissionCallback callback) {
        HashSet<String> permissionSet = new HashSet<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionSet.add("android.permission.READ_MEDIA_IMAGES");
        } else {
            permissionSet.add("android.permission.READ_EXTERNAL_STORAGE");
            permissionSet.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }

        permissionSet.add("android.permission.CALL_PHONE");


        ActivityResultLauncher<String[]> launcher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean isAllGranted = true;
                    for (String permission : permissionSet) {
                        if (!result.get(permission)) {
                            isAllGranted = false;
                            break;
                        }
                    }
                    callback.onPermissionResult(isAllGranted);
                }
        );
        launcher.launch(permissionSet.toArray(new String[permissionSet.size()]));
    }
}
