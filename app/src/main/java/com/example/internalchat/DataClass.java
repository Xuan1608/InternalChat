package com.example.internalchat;

public class DataClass {
    private String imageURL;
    private String title;
    private String price;
    private String location;
    private String timeItem;
    private  String postId;

    public DataClass(String imageURL, String title, String price,String timeItem, String location, String postId) {
        this.imageURL = imageURL;
        this.title = title;
        this.price = price;
        this.timeItem = timeItem;
        this.location = location;
        this.postId =postId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }
    public void setTimeItem(String timeItem) {
        this.timeItem = timeItem;
    }public String getTimeItem() {
        return timeItem;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.location = postId;
    }


}
