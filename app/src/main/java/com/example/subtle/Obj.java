package com.example.subtle;

import android.net.Uri;

public class Obj {
    private String name;
    private String description;
    private String imgID;
    private String initDate;
    private String loop;

    public Obj(String name, String description, String imgID, String initDate, String loop){
        this.name = name;
        this.description = description;
        this.imgID = imgID;
        this.initDate = initDate;
        this.loop = loop;
    }


    public String getName(){
        return this.name;
    }
    public String  getDescription(){
        return this.description;
    }

    public String getImgID() {
        return this.imgID;
    }
    public String getInitDate(){
        return this.initDate;
    }
    public String getLoop(){
        return this.loop;
    }
}
