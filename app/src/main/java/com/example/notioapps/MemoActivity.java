package com.example.notioapps;

import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {

    private EditText memoEditText;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoEditText = findViewById(R.id.memoEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button voiceButton = findViewById(R.id.voiceButton); // 音声入力ボタン

        // 音声認識の設定
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) { }

            @Override
            public void onBeginningOfSpeech() { }

            @Override
            public void onRmsChanged(float rmsdB) { }

            @Override
            public void onBufferReceived(byte[] buffer) { }

            @Override
            public void onEndOfSpeech() { }

            @Override
            public void onError(int error) {
                Toast.makeText(MemoActivity.this, "音声認識エラー: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                // 音声認識の結果を取得
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);
                    memoEditText.setText(recognizedText); // 認識したテキストをメモEditTextに設定
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) { }

            @Override
            public void onEvent(int eventType, Bundle params) { }
        });

        // 音声認識のインテント設定
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP"); // 日本語で認識

        // 音声入力ボタンが押されたときに音声認識を開始
        voiceButton.setOnClickListener(v -> speechRecognizer.startListening(speechRecognizerIntent));

        // 保存ボタンの処理
        saveButton.setOnClickListener(v -> {
            String memoText = memoEditText.getText().toString();
            // メモを保存する処理を追加（例：データベースに保存、ファイルに書き込む等）
            Toast.makeText(MemoActivity.this, "メモを保存しました: " + memoText, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy(); // 音声認識のリソースを解放
        }
    }
}

