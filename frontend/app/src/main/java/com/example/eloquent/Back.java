package com.example.eloquent;

import java.io.Serializable;

public class Back implements Serializable {
    public int backgroundColor;
    public Content content;

    public Back(int backgroundColor, Content content) {
        this.backgroundColor = backgroundColor;
        this.content = content;
    }

    public Back(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.content = new Content();
    }

    // empty constructor for Jackson
    public Back() {}

    public int getBackgroundColor() {return backgroundColor;}

    public Content getContent() {return content;}

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setContent(Content content, int index) {
        this.content = content;
    }
}
