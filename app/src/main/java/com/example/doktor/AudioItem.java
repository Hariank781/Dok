package com.example.doktor;

public class AudioItem {
    private String title;
    private String url;

    public AudioItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}