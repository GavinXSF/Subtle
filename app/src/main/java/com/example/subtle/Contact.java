package com.example.subtle;

public class Contact {

    private String name;
    private String hobby;
    private String photo;
    private String birthday;
    private String loop;

    public Contact(String name, String description, String imgID, String initDate, String loop){
        this.name = name;
        this.hobby = description;
        this.photo = imgID;
        this.birthday = initDate;
        this.loop = loop;
    }


    public String getName(){
        return this.name;
    }
    public String  getHobby(){
        return this.hobby;
    }
    public String getPhoto() {
        return this.photo;
    }
    public String getBirthday(){
        return this.birthday;
    }
    public String getLoop(){
        return this.loop;
    }
}
