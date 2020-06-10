package com.example.myvideo;

public class Video {

    public String feedurl;
    public String nickname;
    public String description;
    public int likecount;

    public Video(String feedurl, String nickname, String description, int likecount){
        this.feedurl = feedurl;
        this.nickname = nickname;
        this.description = description;
        this.likecount = likecount;
   }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getLikecount() {
        return likecount;
    }

    public String getFeedurl() {
        return feedurl;
    }

    public String getDescription() {
        return description;
    }

    public String getNickname() {
        return nickname;
    }

    public String toString() {
        return nickname+","+description;
    }
}
