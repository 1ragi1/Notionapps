package com.example.notioapps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {

    private EditText memoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoEditText = findViewById(R.id.memoEditText);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String memoText = memoEditText.getText().toString();
            if (!memoText.isEmpty()) {
                DBHelper dbHelper = new DBHelper(MemoActivity.this);
                long memoId = dbHelper.saveMemo(memoText, ""); // 空のコンテンツで保存

                if (memoId != -1) {
                    Toast.makeText(MemoActivity.this, "メモを保存しました: " + memoText, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MemoActivity.this, "メモの保存に失敗しました", Toast.LENGTH_SHORT).show();
                }

                navigateToMainActivity();
            } else {
                Toast.makeText(MemoActivity.this, "メモが空です", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(MemoActivity.this, MainActivity.class);
        startActivity(intent); // MainActivityに遷移
        finish(); // 現在のMemoActivityを終了
    }
}
