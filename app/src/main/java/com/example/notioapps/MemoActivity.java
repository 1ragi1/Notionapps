package com.example.notioapps;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MemoActivity extends AppCompatActivity {

    private EditText memoEditText;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoEditText = findViewById(R.id.memoEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button listenButton = findViewById(R.id.button_listen); // 音声認識ボタン

        // 音声認識の結果を受け取るためのランチャーを登録
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent resultData = result.getData();
                        if (resultData != null) {
                            ArrayList<String> candidates = resultData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            if (candidates != null && !candidates.isEmpty()) {
                                String recognizedText = candidates.get(0);
                                memoEditText.setText(recognizedText); // 認識結果をEditTextに表示
                            }
                        }
                    }
                });

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

        listenButton.setOnClickListener(v -> startVoiceRecognition());
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPANESE.toString()); // 日本語を指定
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力");
        resultLauncher.launch(intent);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(MemoActivity.this, MainActivity.class);
        startActivity(intent); // MainActivityに遷移
        finish(); // 現在のMemoActivityを終了
    }
}