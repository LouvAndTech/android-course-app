package com.example.calorie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Key for the intent and the data
    public static final String EXTRA_NAME = "com.example.calorie.NAME";
    public static final String EXTRA_CAL = "com.example.calorie.CAL";
    public static final String SHAREDDATA_STORE = "com.example.calorie.DATA";

    public static SharedPreferences preferences = null;

    //Toolbar
    Toolbar toolbar = null;

    //Name input
    EditText name = null;
    Button compute = null;

    //Dialog box
    TextView title = null;
    TextView content = null;
    //Image
    ImageView image = null;

    //Save the name and the calories
    String name_str = null;
    Float cal = null;

    //Button to switch to the recipe page
    Button recipe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Import Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*=== Import all the widgets ===*/
        //Name input
        name = findViewById(R.id.entry_name_id);
        compute = findViewById(R.id.compute);
        //Image
        image = findViewById(R.id.chad_img);
        //Dialog box
        title = findViewById(R.id.dialog_title);
        content = findViewById(R.id.dialog_content);
        //Button to switch to the recipe page
        recipe = findViewById(R.id.recipe_activity_button);

        /*=== Set the default values ===*/
        //Disable the button
        compute.setEnabled(false);
        compute.setClickable(false);

        //Hide the image
        image.setVisibility(ImageView.INVISIBLE);
        image.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim));
        image.setVisibility(ImageView.VISIBLE);

        /*==== Check for saved values ====*/

        //Check in the preferences if there is a saved name and data
        savedData();

        /*==== Set the listeners ====*/

        //On key press, change the text
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onNameChanges();
            }
        });

        //When the user clicks on the button "Compute"
        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the name and go to the next activity
                name_str = name.getText().toString();
                //Create the intent
                Intent intent = new Intent(MainActivity.this, Info_page.class);
                intent.putExtra(EXTRA_NAME, name_str);
                intent.putExtra(EXTRA_CAL, cal);
                //Start the activity
                startActivity(intent);
            }
        });

        //When the user clicks on the button "Recipe"
        recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the intent
                Intent intent = new Intent(MainActivity.this, FoodRecipe.class);
                //Start the activity
                startActivity(intent);
            }
        });
    }

    //Check is there is a saved name and data and if yes, display a dialog box
    protected void savedData(){
        preferences = getSharedPreferences(SHAREDDATA_STORE,MODE_PRIVATE);
        String saved_name = preferences.getString(EXTRA_NAME, "");
        Float saved_cal = preferences.getFloat(EXTRA_CAL, 0);
        //Store the values for later
        name_str = saved_name;
        cal = saved_cal;
        //If there is a saved name and data
        if (saved_name.length() > 0 && saved_cal > 0){
            //Display the dialog box
            title.setText(getString(R.string.dialog_title, saved_name));
            content.setText(getString(R.string.dialog_message, saved_cal));
            //Set the name and allow the user to click on the button
            name.setText(saved_name);
            compute.setEnabled(true);
            compute.setClickable(true);
        }
    }

    //When the user changes the name
    protected void onNameChanges(){
        //If the user has entered a name
        if (name.getText().toString().length() > 0) {
            //Enable the button
            compute.setEnabled(true);
            compute.setClickable(true);
        } else {
            //Disable the button
            compute.setEnabled(false);
            compute.setClickable(false);
        }
    }

    //Create the menu in the toolbar
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //If the user clicks on the button "Language"
        if (item.getItemId() == R.id.action_lang){
            //Change the language
            if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                setLocale(this, Locale.FRANCE);
            } else {
                setLocale(this, Locale.ENGLISH);
            }
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Change the language of the app
     * @info To use
     * @param activity The activity to change the language
     * @param locale The locale to change to
     */
    public static void setLocale(Activity activity, Locale locale) {
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}