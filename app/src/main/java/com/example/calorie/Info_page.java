package com.example.calorie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

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

import java.util.Date;

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

        //Set the listener for the button compute
        compute.setOnClickListener((v)->{compute();});
    }

    private void compute(){
        //Gather all the data
        Boolean is_biologicaly_a_man = ((spinner_gender.getSelectedItem().hashCode() == 1)?true:false);

        return;
    }

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
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_calendar) {
            Log.d("Calendar", "onOptionsItemSelected: ");
            openCalendar();
            return true;
        } else if (item.getItemId() == R.id.action_mail) {

        }
        return super.onOptionsItemSelected(item);
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