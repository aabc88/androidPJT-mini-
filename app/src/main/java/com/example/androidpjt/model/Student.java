package com.example.androidpjt.model;

public class Student {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String memo;
    private String Photo;

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Student() {

    }

    public Student(int id, String name, String email, String phone, String memo, String photo) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memo = memo;
        this.Photo = photo;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
