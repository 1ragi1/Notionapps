package com.example.notioapps;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private Context context;
    private List<ListItem> memoList;
    private DBHelper dbHelper;

    public MemoAdapter(Context context, List<ListItem> memoList) {
        this.context = context;
        this.memoList = memoList;
        dbHelper = new DBHelper(context);
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        ListItem listItem = memoList.get(position);
        holder.titleTextView.setText(listItem.getTitle());

        // 削除ボタンのクリックイベント処理
        holder.deleteButton.setOnClickListener(v -> {
            // メモ削除
            dbHelper.deleteMemo(listItem.getId());

            // リストから削除
            memoList.remove(position);
            notifyItemRemoved(position);
        });

        // メモのクリックイベント（MemoActivityへの遷移）
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MemoActivity.class);
            intent.putExtra("memoId", listItem.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        Button deleteButton;

        public MemoViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            deleteButton = itemView.findViewById(R.id.delete_button); // ボタンIDを設定
        }
    }
}
