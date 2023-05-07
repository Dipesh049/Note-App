package com.example.noteapp;

public class firebaseModel {
    private String title;
    private String desc;

    public firebaseModel(){

    }
    public firebaseModel(String title,String desc){
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
