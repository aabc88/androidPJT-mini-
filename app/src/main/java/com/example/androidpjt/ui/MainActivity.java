package com.example.androidpjt.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidpjt.R;
import com.example.androidpjt.adapter.MainAdapter;
import com.example.androidpjt.callback.DialogCallback;
import com.example.androidpjt.databinding.ActivityMainBinding;
import com.example.androidpjt.databinding.LayoutSearchViewBinding;
import com.example.androidpjt.db.DBHelper;
import com.example.androidpjt.model.Student;
import com.example.androidpjt.util.DialogUtil;
import com.example.androidpjt.util.PermissionUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    //AddStudentActivity 실행시키고 되돌아 올 때 callback실행
    ActivityResultLauncher<Intent> addLauncher;
    ArrayList<Student> datas = new ArrayList<>();
    MainAdapter adapter;
    MenuItem etSearch;
    LayoutSearchViewBinding searchViewBinding;

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
        //자동으로 actionBar에 나올 내용(title, navigation icon, menu)이 toolbar에 나오지는 않는다.
        //코드에서 한줄 명령은 내려야 한다. toolbar가  개발자 뷰임으로 어느 뷰에 actionbar 내용이 나오면된다고
        setSupportActionBar(binding.tb1);
        addLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intent = result.getData();
                    int id = (int) intent.getLongExtra("id", 0);
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
        PermissionUtil.checkAllPermission(this, isAllGranted -> {
            if (isAllGranted) {
                makeRecyclerView();
            } else {
                showDialog();
            }
        });
    }

// onCreate 종료

    private void makeRecyclerView() {
        getListData();
        adapter = new MainAdapter(this, datas);
        binding.rv1.setAdapter(adapter);
        binding.rv1.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    private void showDialog() {
        DialogUtil.showMessageDialog(this, getString(R.string.permission_denied),
                "확인", null, new DialogCallback() {
                    @Override
                    public void onPositiveCallback() {

                    }

                    @Override
                    public void onNegativeCallback() {

                    }
                });
    }

    private void getListData() {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_student order by name", null);
        while (cursor.moveToNext()) {
            Student student = new Student();
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
        etSearch = menu.findItem(R.id.menu_et_search);
        View actionView = etSearch.getActionView();
        searchViewBinding = LayoutSearchViewBinding.bind(actionView);

        searchViewBinding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().toLowerCase().trim();
                ArrayList<Student> filteredList = new ArrayList<>();

                for (Student student : datas) {
                    System.out.println(student.getName());
                    if (student.getName().toLowerCase().contains(keyword)) {
                        filteredList.add(student);
                    }
                }
                adapter.setFilteredData(filteredList);
                adapter.notifyDataSetChanged();

                if (keyword.isEmpty() || keyword.equals("")) {
                    adapter.setFilteredData(datas);  // 전체 리스트 복원
                    adapter.notifyDataSetChanged();
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        searchViewBinding.editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO) {
                hideKeyboard(v);
                //hideSearchEdit();
                v.clearFocus();
                return true;
            }
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_add) {
            Intent intent = new Intent(this, AddStudentActivity.class);
            addLauncher.launch(intent);
        }

        if (item.getItemId() == R.id.menu_main_search) {
            binding.tb1.getMenu().findItem(R.id.menu_main_search).setVisible(false);
            binding.tb1.getMenu().findItem(R.id.menu_main_close).setVisible(true);
            etSearch.setVisible(true);
            searchViewBinding.editSearch.requestFocus();
            showKeyboard(searchViewBinding.editSearch);
        }

        if (item.getItemId() == R.id.menu_main_close) {
            hideSearchEdit();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            IBinder windowToken = view.getWindowToken();
            if (windowToken != null) {
                imm.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    //EditText
    private void hideSearchEdit() {
        if (etSearch != null && etSearch.isVisible()) {
            etSearch.setVisible(false);
            binding.tb1.getMenu().findItem(R.id.menu_main_search).setVisible(true);
            binding.tb1.getMenu().findItem(R.id.menu_main_close).setVisible(false);
            searchViewBinding.editSearch.clearFocus();
            binding.main.requestFocus();
            hideKeyboard(searchViewBinding.editSearch);
            searchViewBinding.editSearch.setText("");
        }
    }

    //뒤로가기 버튼으로 EditText 포커스해제 o
    @Override
    public void onBackPressed() {
        if (etSearch != null && etSearch.isVisible()) {
            hideSearchEdit();
        } else {
            super.onBackPressed();
        }
    }
}