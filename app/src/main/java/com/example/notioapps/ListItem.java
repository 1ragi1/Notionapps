package com.example.notioapps;

public class ListItem {
    private long id;
    private String title;
    private String content;

    // コンストラクタに content を追加
    public ListItem(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
