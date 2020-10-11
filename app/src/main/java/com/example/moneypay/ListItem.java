package com.example.moneypay;

public class ListItem {
    private String id,Email,First_Name,Last_Name,img_url;

    public ListItem(String id, String email, String first_Name, String last_Name, String img_url) {
        this.id = id;
        Email = email;
        First_Name = first_Name;
        Last_Name = last_Name;
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return Email;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public String getImg_url() {
        return img_url;
    }
}
