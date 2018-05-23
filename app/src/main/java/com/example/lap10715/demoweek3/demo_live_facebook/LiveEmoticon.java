package com.example.lap10715.demoweek3.demo_live_facebook;

public class LiveEmoticon {
    private Emoticons emoticons;
    private int x;
    private int y;

    public LiveEmoticon(Emoticons emoticons, int x, int y) {
        this.emoticons = emoticons;
        this.x = x;
        this.y = y;
    }

    public Emoticons getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(Emoticons emoticons) {
        this.emoticons = emoticons;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
