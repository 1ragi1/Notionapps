package com.example.notioapps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemoAdapter memoAdapter;
    private List<ListItem> memoList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        memoList = new ArrayList<>();
        memoList.addAll(dbHelper.getAllMemos());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memoAdapter = new MemoAdapter(this, memoList);
        recyclerView.setAdapter(memoAdapter);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MemoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // データベースから最新のメモリストを取得して更新
        memoList.clear();
        List<ListItem> updatedList = dbHelper.getAllMemos();
        memoList.addAll(updatedList);

        // デバッグ用ログ出力
        for (ListItem item : updatedList) {
            Log.d("MainActivity", "メモ: " + item.getTitle());
        }

        memoAdapter.notifyDataSetChanged(); // リスト更新通知
    }
}
