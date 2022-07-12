package com.example.eloquent;

import android.telephony.IccOpenLogicalChannelResponse;

public class Content {
    public String font;
    public String style;
    public int size;
    public int colour;
    public String message;


    public Content(String font, String style, int size, int colour, String message) {
        this.font = font;
        this.style = style;
        this.size = size;
        this.colour = colour;
        this.message = message;
    }

    public Content(int colour, String message) {

        this.colour = colour;
        this.message = message;
    }

    public int getColor() {return colour;}

    public String getMessage() {return message; }

    public void setColor(int color) {
        this.colour = colour;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
