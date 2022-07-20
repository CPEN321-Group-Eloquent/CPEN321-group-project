package com.example.eloquent;

import java.io.Serializable;

public class Front implements Serializable {

    public int backgroundColor;
    public Content content;

    public Front(int backgroundColor, Content content) {
        this.backgroundColor = backgroundColor;
        this.content = content;
    }

    public Front(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.content = new Content();
    }

    // empty constructor for Jackson
    public Front() {}

    public int getBackgroundColor() {return backgroundColor;}

    public Content getContent() {return content; }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setContent(Content content, int index) {
        this.content = content;
    }
}
