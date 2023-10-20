package com.example.calorie;

import static com.example.calorie.MainActivity.preferences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class Info_page extends AppCompatActivity {

    //Toolbar
    Toolbar toolbar = null;

    //Import widgets
    //Calendar
    CalendarView calendar = null;

    //Text watcher
    TextWatcher reset_result_text = null;
    AdapterView.OnItemSelectedListener reset_result_spinner = null;


    //All Entries
    Spinner spinner_gender = null;
    EditText date_birth = null;
    EditText height = null;
    EditText weight = null;
    SwitchCompat unit = null;
    Spinner spinner_activity = null;
    CheckBox check_show = null;

    //Buttons
    Button compute = null;
    Button reset = null;

    //Result
    TextView result_content = null;
    Double result = null;

    public Info_page (){
        reset_result_text = resetAll();
        reset_result_spinner = reset_result_spinner();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);

        //Import Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Import widgets
        //Calendar
        calendar = findViewById(R.id.calendar);
        calendar.setVisibility(CalendarView.INVISIBLE);

        //All Entries
        spinner_gender = findViewById(R.id.spinner_gender);
        date_birth = findViewById(R.id.date_birth);
        weight = findViewById(R.id.number_weight);
        height = findViewById(R.id.number_height);
        unit = findViewById(R.id.switch_unit);
        spinner_activity = findViewById(R.id.spinner_act);
        check_show = findViewById(R.id.check_affichage);

        //Buttons
        compute = findViewById(R.id.button_compute);
        reset = findViewById(R.id.button_reset);

        //Result
        result_content = findViewById(R.id.result_content);
        //Set the default result content
        result_content.setText(R.string.waiting_compute);

        //Set the listeners for every field to reset the result on change
        //For text related widget
        date_birth.addTextChangedListener(reset_result_text);
        weight.addTextChangedListener(reset_result_text);
        height.addTextChangedListener(reset_result_text);
        unit.setOnClickListener((v)->{reset_result_text.afterTextChanged(null);});
        check_show.setOnClickListener((v)->{reset_result_text.afterTextChanged(null);});
        //for spinner
        //event on every change on the spinner
        spinner_gender.setOnItemSelectedListener(reset_result_spinner);
        spinner_activity.setOnItemSelectedListener(reset_result_spinner);

        //Set the listener for the button compute and reset
        compute.setOnClickListener((v)->{onCompute();});
        reset.setOnClickListener((v)->{onReset();});

        //We want to check for previous data to be able to send a mail with the result is there is any.
        //But we don't want them to be seen.
        readyForMail();
    }

    private void onCompute(){
        //Gather all the data
        Gender gender;
        Integer age_years;
        Float weight_var;
        Float height_var; //In cm
        Physical_activity activity_level;

        //The gender
        gender = Gender.getGender(spinner_gender.getSelectedItemPosition());
        //Get the date and check for format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        formatter.setLenient(false); //Set lenient to false to prevent misstyped date to be accepted
        try{
            //get the string
            String dob_var=(date_birth.getText().toString());
            //parse the string
            Date birth_date = formatter.parse(dob_var);
            //Periode between the date and now
            Period period = Period.between(birth_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            age_years = period.getYears();
        } catch (Exception e) {
            Log.d("Compute", "error Gender: "+e.toString());
            //Toast to warn the user in case of error
            Toast.makeText(this, getText(R.string.error_date), Toast.LENGTH_SHORT).show();
            return;
        }
        //Get the weight and height
        try {
            weight_var = Float.parseFloat(weight.getText().toString());
            //Check if the height is in cm or m
            height_var = Float.parseFloat(height.getText().toString());
            if (unit.isChecked()) {
                //If the height is in m, convert it to cm
                height_var *= 100;
            }
        } catch (NullPointerException e) {
            Log.d("Compute", "Error Weight&Height: "+e.toString());
            //Toast to warn the user in case of error
            Toast.makeText(this, getText(R.string.error_number), Toast.LENGTH_SHORT).show();
            return;
        } catch (NumberFormatException e) {
            Log.d("Compute", "Error Weight&Height: "+e.toString());
            //Toast to warn the user in case of error
            Toast.makeText(this, getText(R.string.error_number), Toast.LENGTH_SHORT).show();
            return;
        }
        //Get the activity level
        try {
            activity_level = Physical_activity.getActive(spinner_activity.getSelectedItemPosition());
        } catch (IllegalArgumentException e) {
            Log.d("Compute", "Error Activity: "+e.toString());
            //Toast to warn the user in case of error
            Toast.makeText(this, getText(R.string.error_global), Toast.LENGTH_SHORT).show();
            return;
        }

        //Base
        result = (weight_var*10 + (height_var*6.25) - (age_years*5));
        // Gender influence
        if (gender.equals(Gender.MEN)){
            result += 5;
        } else {
            result -= 161;
        }
        //Activity influence
        switch (activity_level){
            case SEDENTARY:
                result *= 1.37;
                break;
            case ACTIVE:
                result *= 1.55;
                break;
            case SPORTIVE:
                result *= 1.8;
                break;
        }

        //Show result depending on the checkbox
        if (check_show.isChecked()){
            result_content.setText(getString(R.string.result_complex,gender.getPronom(),activity_level.toString() , result));
        } else {
            result_content.setText(getString(R.string.result_simple, result));
        }

        //Save the data
        saveData(getIntent().getStringExtra(MainActivity.EXTRA_NAME),result);
        return;
    }

    protected void onReset(){
        saveData("0",0.0);
        Toast.makeText(this, R.string.data_removed, Toast.LENGTH_SHORT).show();
        finish();
    }


    /**
     * Save the data in the shared preferences (can be used with empty string and 0.0 to reset the data)
     * @param name The name of the user
     * @param cal  The number of calories
     */
    private void saveData(String name,Double cal){
        //Save the data
        preferences = getSharedPreferences(MainActivity.SHAREDDATA_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MainActivity.EXTRA_NAME, name);
        editor.putFloat(MainActivity.EXTRA_CAL,cal.floatValue());
        if (!editor.commit()){;
            // Show a message
            Toast.makeText(this, R.string.error_global, Toast.LENGTH_SHORT).show();
            Log.d("Compute", "ERROR : Data Not saved");
        }else {
            Log.d("Compute", "Data saved");
        }
    }

    /**
     * We want to store the result in a variable to be able to send it by mail
     */
    private void readyForMail(){
        Float cal = getIntent().getFloatExtra(MainActivity.EXTRA_CAL, 0);
        if (cal > 0){
            result = cal.doubleValue();
        }
    }

    //Create the menu in the toolbar
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //If the user clicks on the button "Language"
        if (item.getItemId() == R.id.action_lang){
            //Change the language
            //MainActivity.switchLocal(this, getResources().getConfiguration().locale.getLanguage());
            return true;
        } else if (item.getItemId() == R.id.action_calendar) {
            Log.d("Calendar", "onOptionsItemSelected: ");
            openCalendar();
            return true;
        } else if (item.getItemId() == R.id.action_mail) {
            if (this.result != null) {
                sendMail("", this.result);
            } else {
                Toast.makeText(this, R.string.error_no_result, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void sendMail(String adresse, Double result_to_send){
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // Only email apps handle this.
            intent.putExtra(Intent.EXTRA_EMAIL, adresse);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Calories");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_content,result_to_send));
            this.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            Log.d("Mail", "sendMail: "+e.toString());
            Toast.makeText(this, R.string.error_no_mail, Toast.LENGTH_SHORT).show();
        }
    }

    public void openCalendar() {
        Log.d("Calendar", "openCalendar: ");
        calendar.setVisibility((calendar.getVisibility()==CalendarView.VISIBLE)?CalendarView.INVISIBLE:CalendarView.VISIBLE);
        calendar.setDate(System.currentTimeMillis(), false, true);
    }

    private void setReset_result() {
        Log.d("Reset", "setReset_result: ");
        result_content.setText(R.string.waiting_compute);
    }

    private TextWatcher resetAll(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("Reset", "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Reset", "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                Log.d("Reset", "afterTextChanged: ");
                setReset_result();
            }
        };
    }
    private AdapterView.OnItemSelectedListener reset_result_spinner(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Log.d("Reset", "onItemSelected: ");
                setReset_result();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Reset", "onNothingSelected: ");
            }
        };
    }
}