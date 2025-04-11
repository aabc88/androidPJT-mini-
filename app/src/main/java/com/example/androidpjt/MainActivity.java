package com.example.androidpjt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidpjt.adapter.MainAdapter;
import com.example.androidpjt.databinding.ActivityMainBinding;
import com.example.androidpjt.db.DBHelper;
import com.example.androidpjt.model.Student;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    //AddStudentActivity 실행시키고 되돌아 올 때 callback실행
    ActivityResultLauncher<Intent> addLauncher;
    ArrayList<Student>  datas = new ArrayList<>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Toolbar는 ActionBar를 대체하는 UI로, 액티비티의 상단에 위치하여 앱의 제목과 탐색을 위한 아이콘을 표시합니다.
        //자동으로 actionBar에 나올 내용(title, navigation icon, menu)이 toolbar에 나오ㄴ지는 않는다.
        //코드에서 한줄 명령은 내려야 한다. toolbar가  개발자 뷰임으로 어느 뷰에 actionbar 내용이 나오면된다고
        setSupportActionBar(binding.tb1);
        addLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intent = result.getData();
                    int id = intent.getIntExtra("id", 0);
                    String name = intent.getStringExtra("name");
                    String email = intent.getStringExtra("email");
                    String phone = intent.getStringExtra("phone");
                    String memo = intent.getStringExtra("memo");
                    String photo = intent.getStringExtra("photo");

                    Student student = new Student(id, name, email, phone, memo, photo);
                    datas.add(student);
                    adapter.notifyDataSetChanged();
                    //리사이클러뷰에 데이터가 추가되면 어댑터에 notifyDataSetChanged()를 호출하여 리사이클러뷰를 갱신합니다.
                    
                }
        );
        makeRecyclerView();
    }

    private void makeRecyclerView() {
        getListData();
        adapter = new MainAdapter(this, datas);
        binding.rv1.setAdapter(adapter);
        binding.rv1.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    private void getListData() {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_student order by name", null);
        while (cursor.moveToNext()) {
            Student student= new Student();
            student.setId(cursor.getInt(0));
            student.setName(cursor.getString(1));
            student.setEmail(cursor.getString(2));
            student.setPhone(cursor.getString(3));
            student.setPhoto(cursor.getString(4));
            student.setMemo(cursor.getString(5));

            datas.add(student);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_add) {
            Intent intent = new Intent(this, AddStudentActivity.class);
            addLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}