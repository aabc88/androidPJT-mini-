package com.example.androidpjt.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidpjt.R;
import com.example.androidpjt.adapter.DetailAdapter;
import com.example.androidpjt.databinding.ActivityDetailBinding;
import com.example.androidpjt.db.DBHelper;
import com.example.androidpjt.model.Student;
import com.example.androidpjt.util.BitmapUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    Student student;
    ArrayList<Map<String, String>> datas = new ArrayList<>();
    DetailAdapter adapter;
    ActivityResultLauncher<Intent> requestGalleryLauncher;
    int sum = 0;
    int idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //actionbar > toolbar
        setSupportActionBar(binding.tb1);

        binding.btnAddScore.setBackgroundTintList(null);

        //실행과 동시에 전달한 데이터 추출
        int id = getIntent().getIntExtra("id", 0);

        setInitStudentDate(id);

        //초기 액티비티가 실행되면서 db select해서 시험점수를 목록으로 출력
        setInitScoreData(id);

        binding.btnScoreChart.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChartActivity.class);
            intent.putExtra("name", binding.tvName.getText().toString());
            intent.putExtra("chartScore", datas);
            startActivity(intent);
        });

        binding.btnMemo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            System.out.println(binding.tvPhone.getText().toString());
            intent.setData(Uri.parse("smsto:"+binding.tvPhone.getText().toString()));
            intent.putExtra("sms_body","score : "+datas.get(datas.size() - 1).get("date")+"  "+datas.get(datas.size() - 1).get("score"));
            startActivity(intent);
        });

        //도넛뷰

        ActivityResultLauncher<Intent> addScoreLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //ScoreAddActivity에서 전달한 데이터획득
                    Intent intent = result.getData();
                    String score = intent.getStringExtra("score");
                    Long date = intent.getLongExtra("date", 0);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("score", score);
                    Date d = new Date(date);
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                    map.put("date", sd.format(date));
                    datas.add(map);
                    idx++;
                    sum += Integer.parseInt(score);
                    setDonutValue();
                    adapter.notifyDataSetChanged();
                }
        );

        binding.btnAddScore.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScoreAddActivity.class);
            intent.putExtra("id", student.getId());
            addScoreLauncher.launch(intent);
        });

        requestGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        //갤러리목록에서 선택 후 돌아온 상황
                        //유저가 선택한 사진을 식별할 수 있는 식별자 값을 Uri객체로 넘겨줌
                        Uri uri = result.getData().getData();
                        //uri로 식별되는 사진의 경로를 획득한다. adb에 저장했다가 나중에 이용하기위함
                        String[] proj = new String[]{MediaStore.Images.Media.DATA}; //사진경로를 지칭하는 상수
                        Cursor galleryCursor = getContentResolver().query(uri, proj, null, null, null);

                        if (galleryCursor != null) {
                            if (galleryCursor.moveToFirst()) {
                                //사진의 경로를 획득
                                String filePath = galleryCursor.getString(0);
                                //나중을 위해서 tb_student 테이블에 데이터 저장
                                DBHelper dbHelper = new DBHelper(this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.execSQL("update tb_student set photo = ? where _id = ?", new String[]{filePath, String.valueOf(id)});
                                db.close();
                            }
                        }
                        //되돌아오고 화면에 출력하기
                        Bitmap bitmap = BitmapUtil.getGalleryBitmapFromStream(this, uri);
                        if (bitmap != null) {
                            binding.ivDetail.setImageBitmap(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void setInitScoreData(int id) {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select score, date from tb_score where student_id = ?", new String[]{String.valueOf(id)});
        datas = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("score", c.getString(0));
            sum += Integer.parseInt(c.getString(0));
            idx++;
            Date d = new Date(Long.parseLong(c.getString(1)));
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            map.put("date", sd.format(d));
            datas.add(map);
        }
        db.close();


        setDonutValue();
        binding.rvScore.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DetailAdapter(this, datas);
        binding.rvScore.setAdapter(adapter);
        binding.rvScore.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setDonutValue() {
        if (idx > 0) {
            binding.dnScore.setValue(sum /idx);
        } else {
            binding.dnScore.setValue(0);
        }
    }

    private void setInitStudentDate(int id) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_student where _id = ?", new String[]{String.valueOf(id)});
        String photoFilePath = null;

        if (cursor.moveToFirst()) {
            //db에서 가져온 데이터
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String phone = cursor.getString(3);
            //화면 출력
            binding.tvName.setText(name);
            binding.tvEmail.setText(email);
            binding.tvPhone.setText(phone);

            photoFilePath = cursor.getString(4);
            student = new Student(cursor.getInt(0), name, email, phone, cursor.getString(5), photoFilePath);
        }
        db.close();
        //db에 저장된 photo filepath로 화면 출력
        Bitmap bitmap = BitmapUtil.getGalleryBitmapFromFile(this, photoFilePath);
        if (bitmap != null) {
            binding.ivDetail.setImageBitmap(bitmap);
        }

        binding.ivDetail.setOnClickListener(v -> {
            //갤러리에서 사진을 선택하기 위한 인텐트
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            requestGalleryLauncher.launch(intent);
        });
    }
}