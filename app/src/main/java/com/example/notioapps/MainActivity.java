package com.example.notioapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private List<ListItem> memoList;
    private MemoAdapter memoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DBHelper を初期化
        dbHelper = new DBHelper(this);

        // メモのリストを取得
        memoList = dbHelper.getAllMemos();

        // RecyclerView の設定
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memoAdapter = new MemoAdapter(this, memoList);
        recyclerView.setAdapter(memoAdapter);

        // 「追加」ボタンの設定
        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(v -> {
            // MemoActivity を開始
            startActivity(new Intent(MainActivity.this, MemoActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // メモのリストを更新
        memoList.clear();
        memoList.addAll(dbHelper.getAllMemos()); // DBからメモを再取得
        memoAdapter.notifyDataSetChanged(); // アダプターに変更を通知
    }
}
