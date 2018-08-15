package com.vaishjanardhan.news4u;

public class News {

    private String section;
    private String title;
    private String date;
    private String author;
    private String url;

    News(String section, String title, String date, String author, String url) {
        this.section = section;
        this.title = title;
        this.date = date;
        this.author = author;
        this.url = url;
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
}
