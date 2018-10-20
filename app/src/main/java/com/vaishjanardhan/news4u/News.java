package com.vaishjanardhan.news4u;

public class News {

    private String section;
    private String title;
    private String date;
    private String author;
    private String url;
    private String imageUrl;
    private boolean isFavourite;

    News(String section, String title, String date, String author, String url, String imageUrl) {
        this.section = section;
        this.title = title;
        this.date = date;
        this.author = author;
        this.url = url;
        this.imageUrl = imageUrl;
        this.isFavourite = false;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }


    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavourite() {
        return isFavourite;
    }
}
