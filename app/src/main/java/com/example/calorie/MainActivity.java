package com.example.calorie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Key for the intent
    public static final String EXTRA_NAME = "com.example.calorie.NAME";

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

        /*=== Set the default values ===*/
        //Disable the button
        compute.setEnabled(false);
        compute.setClickable(false);

        //Hide the image
        image.setVisibility(ImageView.INVISIBLE);
        image.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim));
        image.setVisibility(ImageView.VISIBLE);

        /*==== Check for saved values ====*/


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
                String name_str = name.getText().toString();
                //Create the intent
                Intent intent = new Intent(MainActivity.this, Info_page.class);
                intent.putExtra(EXTRA_NAME, name_str);
                //Start the activity
                startActivity(intent);
            }
        });


    }

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
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
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