package com.example.yelpapp;

public class ReviewDetail {
    private String author;
    private String rating;
    private String content;
    private String date;
    public ReviewDetail(String author, String rating, String content, String date) {
        this.author = author;
        this.rating = rating;
        this.content = content;
        this.date = date;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getRating() {
        return this.rating;
    }
    public String getContent() {
        return this.content;
    }
    public String getDate() {
        return this.date;
    }
}
