package com.example.eloquent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Presentation {
    public String title;

    public int presentationID;
    public Cards[] cueCards;
    public Feedback[] feedback;

    public Presentation(String title, int presentationID) {
        this.title = title;
        this.presentationID = presentationID;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    String json = "{ \"title\" : \"Keyword\", \"Cards\" : \"[\"sdadsad\",\"sdadsad\",\"sdadsad\",\"sdadsad\"]\" }";
    Presentation presentation;

    {
        try {
            presentation = objectMapper.readValue(json, Presentation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Cards getCards(int index){
        return cueCards[index];
    }

    private String getTitle(){
        return title;
    }

    private int getPresentationID(){
        return presentationID;
    }

    public Feedback getFeedback(int index) {
        return feedback[index];
    }

    public void setPresentationID(int presentationID) {
        this.presentationID = presentationID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCueCards(Cards cueCards, int index) {
        this.cueCards[index] = cueCards;
    }

    public void setFeedback(Feedback feedback,int index) {
        this.feedback[index] = feedback;
    }
}
