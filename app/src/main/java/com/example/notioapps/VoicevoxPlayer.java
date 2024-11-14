package com.example.notioapps;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class VoicevoxPlayer {
    private static final String TAG = "VoicevoxPlayer";
    private static final String BASE_URL = "http://www2.sohome.net:50021";
    private final OkHttpClient client;
    private MediaPlayer mediaPlayer;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public VoicevoxPlayer() {
        this.client = new OkHttpClient();
        this.mediaPlayer = new MediaPlayer();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    // デフォルトのコールバックを定義
    private final VoicevoxCallback defaultCallback = new VoicevoxCallback() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "再生が開始されました");
        }

        @Override
        public void onPlayComplete() {
            Log.d(TAG, "再生が完了しました");
        }

        @Override
        public void onFailure(String error) {
            Log.e(TAG, "エラーが発生しました: " + error);
        }
    };

    // 音声合成と再生を実行
    public void synthesizeAndPlay(String text, int speakerId, VoicevoxCallback callback) {
        // コールバックが指定されていない場合はデフォルトのコールバックを使用
        VoicevoxCallback actualCallback = callback != null ? callback : defaultCallback;

        executor.execute(() -> {
            try {

                // バックグラウンドで音声合成を実行
                // 1. audio_query エンドポイントで音声合成用のクエリを作成
                Log.d("VoicevoxPlayer.synthesizeAndPlay", "Step 1");
                String queryJson = createAudioQuery(text, speakerId);
                if (queryJson == null) {
                    handleError(actualCallback, "Failed to create audio query");
                    return;
                }

                // 2. synthesis エンドポイントで音声を合成
                Log.d("VoicevoxPlayer.synthesizeAndPlay", "Step 2");
                byte[] audioData = synthesize(queryJson, speakerId);
                if (audioData == null) {
                    handleError(actualCallback, "Failed to synthesize audio");
                    return;
                }

                // メインスレッドで音声を再生
                Log.d("VoicevoxPlayer.synthesizeAndPlay", "Play audio");
                mainHandler.post(() -> playAudioData(audioData, actualCallback));
            } catch (Exception e) {
                Log.e(TAG, "Error during synthesis", e);
                handleError(actualCallback, "音声合成に失敗しました: " + e.getMessage());
            }
        });
    }

    // エラーをメインスレッドで処理
    private void handleError(VoicevoxCallback callback, String message) {
        mainHandler.post(() -> callback.onFailure(message));
    }

    // 音声データを再生
    private void playAudioData(byte[] audioData, VoicevoxCallback callback) {
        try {
            // 既存の再生を停止
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();

            // 音声データをセット
            mediaPlayer.setDataSource(new ByteArrayMediaDataSource(audioData));

            // 再生の準備
            mediaPlayer.prepareAsync();

            // リスナーの設定
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                callback.onSuccess();
            });

            mediaPlayer.setOnCompletionListener(mp -> callback.onPlayComplete());

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                callback.onFailure("再生中にエラーが発生しました");
                return true;
            });

        } catch (Exception e) {
            Log.e(TAG, "Error playing audio", e);
            callback.onFailure("音声の再生準備に失敗しました");
        }
    }

    // audio_queryエンドポイントにリクエストを送信
    private String createAudioQuery(String text, int speakerId) throws IOException {
        String url = BASE_URL + "/audio_query?text=" + text + "&speaker=" + speakerId;

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return null;
            return response.body().string();
        }
    }

    // synthesisエンドポイントで音声を合成
    private byte[] synthesize(String queryJson, int speakerId) throws IOException {
        String url = BASE_URL + "/synthesis?speaker=" + speakerId;

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                queryJson
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return null;
            return response.body().bytes();
        }
    }

    // リソースの解放
    public void release() {
        executor.shutdown();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    // 再生を停止
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    // コールバックインターフェース
    public interface VoicevoxCallback {
        void onSuccess();        // 再生開始時
        void onPlayComplete();   // 再生完了時
        void onFailure(String error);
    }

    // ByteArray用のMediaDataSource
    private static class ByteArrayMediaDataSource extends MediaDataSource {
        private byte[] data;

        public ByteArrayMediaDataSource(byte[] data) {
            this.data = data;
        }

        @Override
        public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
            if (position >= data.length) return -1;

            int remaining = (int) (data.length - position);
            int copyLength = Math.min(size, remaining);
            System.arraycopy(data, (int) position, buffer, offset, copyLength);
            return copyLength;
        }

        @Override
        public long getSize() throws IOException {
            return data.length;
        }

        @Override
        public void close() throws IOException {
            data = null;
        }
    }
}