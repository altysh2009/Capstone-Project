package com.example.home_.news;


public class MyObject {
    private String auther_Name;
    private String description;
    private String sourceName;
    private String title;
    private String image;
    private String date;
    private String url;

    public MyObject(String auther_Name, String date, String description, String image, String sourceName, String title, String url) {
        this.auther_Name = auther_Name;
        this.date = date;
        this.description = description;
        this.image = image;
        this.sourceName = sourceName;
        this.title = title;
        this.url = url;
    }

    public String getAuther_Name() {
        return auther_Name;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
