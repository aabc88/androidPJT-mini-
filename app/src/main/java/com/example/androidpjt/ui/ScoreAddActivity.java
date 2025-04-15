package com.example.androidpjt.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidpjt.R;
import com.example.androidpjt.databinding.ActivityScoreAddBinding;
import com.example.androidpjt.db.DBHelper;

public class ScoreAddActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityScoreAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreAddBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnKey0.setOnClickListener(this);
        binding.btnKey1.setOnClickListener(this);
        binding.btnKey2.setOnClickListener(this);
        binding.btnKey3.setOnClickListener(this);
        binding.btnKey4.setOnClickListener(this);
        binding.btnKey5.setOnClickListener(this);
        binding.btnKey6.setOnClickListener(this);
        binding.btnKey7.setOnClickListener(this);
        binding.btnKey8.setOnClickListener(this);
        binding.btnKey9.setOnClickListener(this);
        binding.btnKeyDel.setOnClickListener(this);
        binding.btnKeyAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnKeyAdd) {
            //DetailActivity에 의해 이 액티비티가 실행되었을 것이고
            //전달 받은 id값을 추출한다.
            Intent intent = getIntent();
            int id = intent.getIntExtra("id", 0);
            long date = System.currentTimeMillis();
            String score = binding.tvKeyEdit.getText().toString();

            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("insert into tb_score (student_id, date, score) " +
                    "values(?, ?, ?)", new String[]{String.valueOf(id), String.valueOf(date), score});
            db.close();

            //화면은 CRUD의 create화면이다 목록 화면으로 자동 전환. 데이터를 넘기면서
            intent.putExtra("score", score);
            intent.putExtra("date", date);
            setResult(RESULT_OK, intent);
            finish();
        } else if (v == binding.btnKeyDel) {
            String text = binding.tvKeyEdit.getText().toString();
            if (text.length() == 1) {
                binding.tvKeyEdit.setText("0");
            } else {
                text = text.substring(0, text.length() - 1);
                binding.tvKeyEdit.setText(text);

            }
        } else {
            Button btn = (Button) v;
            String txt = btn.getText().toString();

            String score = binding.tvKeyEdit.getText().toString();
            if(score.equals("0")){
                //현재 화면에 0이 찍혀 있다면.. 유저가 클릭한 문자열을 그대로 출력..
                binding.tvKeyEdit.setText(txt);
            }else {
                String newScore = score + txt;
                //숫자로 계산하려고.. int 타입으로 변형..
                int intScore = Integer.parseInt(newScore);
                if(intScore > 100){
                    Toast.makeText(this, R.string.read_add_score_over_, Toast.LENGTH_SHORT)
                            .show();
                }else {
                    binding.tvKeyEdit.setText(newScore);
                }
            }
        }
    }
}