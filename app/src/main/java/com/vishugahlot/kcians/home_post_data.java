package com.vishugahlot.kcians;

public class home_post_data {
    String imageurl,post_text,post_link,date,name,profileurl,uid,token;
    Integer like,saves;


    public  home_post_data(){}
    public home_post_data(String imageurl, String post_text, String post_link, String date, String name, String profileurl, String uid, Integer like, Integer saves,String token) {
        this.imageurl = imageurl;
        this.post_text = post_text;
        this.post_link = post_link;
        this.date = date;
        this.name = name;
        this.profileurl = profileurl;
        this.uid = uid;
        this.like = like;
        this.saves = saves;
        this.token=token;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_link() {
        return post_link;
    }

    public void setPost_link(String post_link) {
        this.post_link = post_link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getSaves() {
        return saves;
    }

    public void setSaves(Integer saves) {
        this.saves = saves;
    }
    public String getToken()
    {
        return token;
    }
    public  void setToken(String token)
    {
        this.token=token;
    }


}
