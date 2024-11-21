package com.example.notioapps;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {

    private EditText memoEditText;

    // ActivityResultLauncherを使って音声認識結果を取得
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent resultData = result.getData();
                    if (resultData != null) {
                        ArrayList<String> matches = resultData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            String recognizedText = matches.get(0);
                            memoEditText.setText(recognizedText); // 認識結果を EditText にセット
                            Toast.makeText(MemoActivity.this, "音声認識成功: " + recognizedText, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MemoActivity.this, "音声認識結果がありません", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoEditText = findViewById(R.id.memoEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button voiceButton = findViewById(R.id.voiceButton); // 音声入力ボタン

        // 音声認識インテントを作成
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP"); // 日本語で認識

        // 音声入力ボタンが押されたときに音声認識を開始
        voiceButton.setOnClickListener(v -> {
            try {
                resultLauncher.launch(speechRecognizerIntent); // 音声認識を開始
                Toast.makeText(MemoActivity.this, "音声認識を開始しました", Toast.LENGTH_SHORT).show();
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MemoActivity.this, "音声認識がサポートされていません", Toast.LENGTH_SHORT).show();
            }
        });

        // 保存ボタンの処理
        saveButton.setOnClickListener(v -> {
            String memoText = memoEditText.getText().toString();
            if (!memoText.isEmpty()) {
                // メモを保存する処理を追加（例：データベースに保存、ファイルに書き込む等）
                Toast.makeText(MemoActivity.this, "メモを保存しました: " + memoText, Toast.LENGTH_SHORT).show();
                navigateToMainActivity(); // メモ保存後にMainActivityに遷移
            } else {
                Toast.makeText(MemoActivity.this, "メモが空です", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // メイン画面（MainActivity）に遷移するメソッド
    private void navigateToMainActivity() {
        Intent intent = new Intent(MemoActivity.this, MainActivity.class);
        startActivity(intent); // MainActivityに遷移
        finish(); // 現在のMemoActivityを終了
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
