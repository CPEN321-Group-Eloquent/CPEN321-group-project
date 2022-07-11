package com.example.present;

public class Card {
    int backgroundColour;
    String transitionPhrase;
    int endWithPause;
    Side front;
    Side back;

    public Card(int backgroundColour, String transitionPhrase, int endWithPause, Side front, Side back) {
        this.backgroundColour = backgroundColour;
        this.transitionPhrase = transitionPhrase;
        this.endWithPause = endWithPause;
        this.front = front;
        this.back = back;
    }
}
