package com.example.androidpjt.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidpjt.R;
import com.example.androidpjt.databinding.ActivityAddStudentBinding;
import com.example.androidpjt.db.DBHelper;
import com.example.androidpjt.util.DialogUtil;

public class AddStudentActivity extends AppCompatActivity {
    ActivityAddStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.tb1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_save) {
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String name = binding.etName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String phone = binding.etPhone.getText().toString();
        String memo = binding.etMemo.getText().toString();
        //유효성 검증 유저 입력 데이터가 우리가 원하는 데이터인지 판단.
        if (name == null || name.equals("")) {
            DialogUtil.showToast(this, getString(R.string.add_name_null));
        } else {
            //DB에 저장하는 코드 작성

            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("email", email);
            values.put("phone", phone);
            values.put("memo", memo);

            long newRowId = db.insert("tb_student", null, values);
            db.close();

            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            intent.putExtra("id", newRowId);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            intent.putExtra("memo", memo);
            finish();
        }
    }
}