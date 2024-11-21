package com.example.notioapps;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MemoActivity extends Activity {

    private static final int REQ_CODE_SPEECH_INPUT = 100; // 音声認識リクエストコード
    private EditText memoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoEditText = findViewById(R.id.memoEditText); // メモ入力 EditText

        // 保存ボタンのクリックリスナー
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            // 保存ボタンが押されたときの処理
            String memoText = memoEditText.getText().toString();

            // SharedPreferences に保存
            getSharedPreferences("MemoPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("memoContent", memoText)  // 保存したいメモ内容をセット
                    .apply();

            // 保存完了メッセージを表示
            Toast.makeText(MemoActivity.this, "メモが保存されました", Toast.LENGTH_SHORT).show();

            // MainActivity に遷移
            Intent intent = new Intent(MemoActivity.this, MainActivity.class);
            startActivity(intent); // MainActivity に遷移
            finish(); // MemoActivity を終了
        });

        // 音声入力ボタンのクリックリスナー
        Button voiceButton = findViewById(R.id.voiceButton);
        voiceButton.setOnClickListener(v -> {
            // 音声認識のインテントを作成
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "話してください");

            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);  // 音声認識を開始
            } catch (ActivityNotFoundException a) {
                Toast.makeText(MemoActivity.this, "音声認識がサポートされていないデバイスです", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            // 音声認識の結果を取得
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0); // 最初の認識結果を取得
                memoEditText.setText(recognizedText); // 音声認識結果をメモに反映
            }
        }
    }
}
