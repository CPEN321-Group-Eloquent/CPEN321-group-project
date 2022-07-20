package com.example.eloquent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class UserData {
    private String userID;
    private String username;
    private String[] presentations;
    private String[] presentationTitles;

    public String getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }

    public String[] getPresentations() {
        return presentations;
    }

    public String[] getPresentationTitles() {
        return presentationTitles;
    }
}
