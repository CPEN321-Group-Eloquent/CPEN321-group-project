package com.example.eloquent;

import java.io.Serializable;


public class Cards implements Serializable {

    public Front front;
    public Back back;
    public int backgroundColor;
    public String transitionPhrase;
    public int endWithPause;

    public Cards(int backgroundColor, String transitionPhrase, int endWithPause, Front front, Back back) {
        this.backgroundColor = backgroundColor;
        this.transitionPhrase = transitionPhrase;
        this.endWithPause = endWithPause;
        this.front = front;
        this.back = back;
    }

    public Cards(Front front, Back back, int backgroundColor) {
        this.front = front;
        this.back = back;
        this.backgroundColor = backgroundColor;
    }

    // empty constructor for Jackson
    public Cards() {}

    public Front getFront() {
        return front;
    }

    public Back getBack() {
        return back;
    }

    public int getBackground_color() {
        return backgroundColor;
    }

    public void setFront(Front front) {
        this.front = front;
    }

    public void setBack(Back back) {
        this.back = back;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
