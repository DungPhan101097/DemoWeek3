package com.example.lap10715.demoweek3.demo_live_fb;

public class LiveEmoticon {
    private Emoticons emoticons;
    private int xCordinate;
    private int yCorddinate;

    public LiveEmoticon(Emoticons emoticons, int xCordinate, int yCorddinate) {
        this.emoticons = emoticons;
        this.xCordinate = xCordinate;
        this.yCorddinate = yCorddinate;
    }

    public Emoticons getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(Emoticons emoticons) {
        this.emoticons = emoticons;
    }

    public int getxCordinate() {
        return xCordinate;
    }

    public void setxCordinate(int xCordinate) {
        this.xCordinate = xCordinate;
    }

    public int getyCordinate() {
        return yCorddinate;
    }

    public void setyCordinate(int yCorddinate) {
        this.yCorddinate = yCorddinate;
    }
}
