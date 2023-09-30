package com.example.yelpapp;

public class BusinessDetail {
    private String id;
    private String image_url;
    private int index;
    private String rating;
    private double distance;
    private String name;
    private String yelp_link;
    BusinessDetail(String id, String image_url, String name, int index, String rating, double distance, String yelp_link) {
        this.id = id;
        this.image_url = image_url;
        this.index = index;
        this.rating = rating;
        this.distance = distance;
        this.name = name;
        this.yelp_link = yelp_link;
    }
    public String getId() {
        return this.id;
    }
    public int getIndex() {
        return this.index;
    }
    public String getRating() {
        return this.rating;
    }
    public String getImage_url() {
        return this.image_url;
    }
    public double getDistance() {
        return (this.distance * 0.000621371192);
    }
    public String getName() {
        return this.name;
    }
    public String getYelp_link() {
        return this.yelp_link;
    }
}
