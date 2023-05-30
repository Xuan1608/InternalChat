package com.example.internalchat.widget;

public class newsItem {
    private String imageUrl;
    private String title;
    private String time;

    public newsItem(String imageUrl, String title, String time) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }
}
