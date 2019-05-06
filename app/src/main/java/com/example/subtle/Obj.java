package com.example.subtle;

public class Obj {
    private String name;
    private String description;
    private int imgID;
    private String initDate;
    private String loop;

    public Obj(String name, String description, int imgID, String initDate, String loop){
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

    public int getImgID() {
        return this.imgID;
    }
    public String getInitDate(){
        return this.initDate;
    }
    public String getLoop(){
        return this.loop;
    }
}
