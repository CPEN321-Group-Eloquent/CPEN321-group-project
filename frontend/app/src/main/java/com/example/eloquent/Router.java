package com.example.eloquent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private static RequestQueue requestQueue;
    private static Router self;

    private static String TAG = "Router";
    private static String BACKEND_HOST_AND_PORT = "http://20.104.77.70:8081";

    public static Router getInstance(Context applicationContext) {
        if (self == null) {
            self = new Router();
            requestQueue = Volley.newRequestQueue(applicationContext);
        }
        return self;
    }

    public void createUser(String IdToken, String userID, String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = BACKEND_HOST_AND_PORT + "/api/login";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                User user = User.getInstance();
                user.setData(response);
                Log.d(TAG, user.getData().getUserID() + " " + user.getData().getUsername());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", IdToken);
                params.put("userID", userID);
                params.put("username", username);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void getPresentation(String userID, String presID) {
        String url = BACKEND_HOST_AND_PORT + "/api/presentation";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);
                params.put("presID", presID);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void createEmptyPresentation(String userID, String title, Presentation presentation) {
        String url = BACKEND_HOST_AND_PORT + "/api/presentation"; // BACKEND_HOST_AND_PORT doesn't end with a "/"!
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                presentation.setPresentationID(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.toString());
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);
                params.put("title", title);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void importPresentation(String userID, String presentationTextRep) {
        String url = BACKEND_HOST_AND_PORT + "/api/import"; // BACKEND_HOST_AND_PORT doesn't end with a "/"!
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                ObjectMapper objectMapper = new ObjectMapper();
                Presentation pres;
                try {
                    pres = objectMapper.readValue(response, Presentation.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.toString());
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);
                params.put("text", presentationTextRep);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void savePresentation(String presID, String jsonString) {

        String url = BACKEND_HOST_AND_PORT + "/api/presentation"; // BACKEND_HOST_AND_PORT doesn't end with a "/"!
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.toString());
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("presID", presID);
                params.put("json", jsonString);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

}
