package com.example.calorie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONObject;


public class FoodRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_recipe);
    }

    private void RequestAPI(){
        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.quotable.io/random";
        // Create a listener for the response
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Parse the JSON response
                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
                //Log
                Log.d("API Response", jsonElement.toString());
                Log.d("API Response", jsonElement.getAsJsonObject().get("content").getAsString());
            }
        };
        // Handle errors in the response
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
            }
        };
        // Create a GET request to the API
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                responseListener,
                errorListener);
        // Add the request to the queue to send it
        requestQueue.add(request);
    }
}