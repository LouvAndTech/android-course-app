package com.example.calorie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.RangeSlider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;


public class FoodRecipe extends AppCompatActivity {

    //Toolbar
    Toolbar toolbar = null;

    //Slider and buttons
    RangeSlider slider = null;
    Button search = null;

    //recipe_list
    LinearLayout recipe_list = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_recipe);

        //Import Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Import Slider and buttons
        slider = findViewById(R.id.calorie_slider);
        search = findViewById(R.id.search_button);

        //Import recipe_list
        recipe_list = findViewById(R.id.recipe_list);
        //Event listener on the search button
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the recipe list
                recipe_list.removeAllViews();
                //Get the values of the slider
                String calories_min = slider.getValues().get(0).toString();
                String calories_max = slider.getValues().get(1).toString();
                //Request the API
                RequestAPI(calories_min, calories_max);
            }
        });

    }

    //Add a recipe to the list
    protected void addRecipe(String img_url, String name, String calories){
        //Create a new recipe view
        View child = View.inflate(this, R.layout.recipe, null);
        //Using the url to load the image
        ImageView img = child.findViewById(R.id.img);
        //Remove the quotes from the url
        img_url=img_url.substring(1, img_url.length() - 1);
        //Download the image and add it
        Picasso.get().load(img_url).resize(200,200).centerCrop().into(img);
        //Get the fields
        TextView title = child.findViewById(R.id.title);
        TextView cal = child.findViewById(R.id.description);
        //Remove the quotes from the name
        name = name.substring(1, name.length() - 1);
        //Set the texts
        title.setText(name);
        cal.setText(getString(R.string.kcal, calories));
        //Add the recipe to the view
        recipe_list.addView(child);
    }

    //Create the menu in the toolbar
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.return_only, menu);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private void RequestAPI(String calories_min, String calories_max){
        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Integer number = 4;
        String url = "https://api.spoonacular.com/recipes/findByNutrients?minCalories="+calories_min+"&maxCalories="+calories_max+"&number="+number+"&random=true&apiKey="+API_keys.SPOONACULAR;
        // Create a listener for the response
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Parse the JSON response
                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
                //For each of the three recipes
                for (int i = 0; i < number; i++) {
                    //Get the title, image and calories
                    String title = jsonElement.getAsJsonArray().get(i).getAsJsonObject().get("title").toString();
                    String img_url = jsonElement.getAsJsonArray().get(i).getAsJsonObject().get("image").toString();
                    String calories = jsonElement.getAsJsonArray().get(i).getAsJsonObject().get("calories").toString();
                    //Add the recipe to the list
                    //img_url = "https://spoonacular.com/recipeImages/649411-312x231.jpg";
                    addRecipe(img_url, title, calories);
                }
            }
        };
        // Handle errors in the response
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
                Toast.makeText(FoodRecipe.this, R.string.error+": " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        // Create a GET request to the API
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                responseListener,
                errorListener);
        // Add the request to the queue to send it
        requestQueue.add(request);
    }
}