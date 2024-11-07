package com.example.notioapps;

public class ListItem {
    private long id;
    private String title;

    public ListItem(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
