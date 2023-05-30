package com.example.internalchat;

import java.util.ArrayList;

public class HomeRental {
    private String acreage;
    private String address;
    private String description;
    private String postId;
    private String Phone;
    private ArrayList<String> image;
    private String price;
    private String title;

    public  HomeRental(){}

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcreage() {
        return acreage;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPostId() {
        return postId;
    }

    public void setImage(ArrayList<String> arrayList) {
        this.image = arrayList;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

}
