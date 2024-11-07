package com.example.notioapps;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        EditText memoEditText = findViewById(R.id.memoEditText);
        Button saveButton = findViewById(R.id.saveButton);

        // 保存ボタンをクリックしたときの処理
        saveButton.setOnClickListener(v -> {
            String title = memoEditText.getText().toString().trim();

            DBHelper helper = new DBHelper(this);
            long id = helper.saveMemo(title, title);

            if (id != -1) {
                Toast.makeText(this, "メモが保存されました", Toast.LENGTH_SHORT).show();
                finish(); // 保存後にアクティビティを終了
            } else {
                Toast.makeText(this, "メモの保存に失敗しました", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
