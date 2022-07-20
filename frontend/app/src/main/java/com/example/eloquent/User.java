package com.example.eloquent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {
    private UserData data;
    private boolean isDataSet;

    private static User self;

    public static User getInstance() {
        if (self == null) {
            self = new User();
            self.isDataSet = false;
        }
        return self;
    }
    public UserData getData() {
        if (isDataSet) {
            return data;
        } else {
            return null;
        }
    }
    public void setData(String userJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            data = objectMapper.readValue(userJSON, UserData.class);
            isDataSet = true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
