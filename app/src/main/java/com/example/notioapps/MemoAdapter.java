import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notioapps.R;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private List<ListItem> memoList;

    public MemoAdapter(List<ListItem> memoList) {
        this.memoList = memoList;
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item, parent, false);
        return new MemoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        ListItem memo = memoList.get(position);
        holder.memoTitle.setText(memo.getTitle());
        holder.memoContent.setText(memo.getContent());

        holder.deleteButton.setOnClickListener(v -> {
            // 削除処理をここに記述
        });
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder {
        public TextView memoTitle;
        public TextView memoContent;
        public Button deleteButton;

        public MemoViewHolder(View itemView) {
            super(itemView);
            memoTitle = itemView.findViewById(R.id.memoTitle);
            memoContent = itemView.findViewById(R.id.memoContent);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
