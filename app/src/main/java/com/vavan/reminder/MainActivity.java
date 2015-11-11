package com.vavan.reminder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    int hours = 0;
    int minutes = 0;

    EditText etReminderText;
    Button btEnterTime, btRemind;
    TextView tvMakeReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etReminderText = (EditText)findViewById(R.id.etReminderText);
        btEnterTime = (Button)findViewById(R.id.btEnterTime);
        btRemind = (Button)findViewById(R.id.btRemind);
        tvMakeReminder = (TextView)findViewById(R.id.tvMakeReminder);

        btEnterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        btRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartReminderService(v);
            }
        });

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        TimePickerDialog timePickerDialog =
                new TimePickerDialog(this,myCallBack, hours,minutes,true);
        return timePickerDialog;
    }

    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int _hours, int _minutes) {
            String time_str = "";
            hours = _hours;
            minutes = _minutes;

            if (hours<10){
                time_str = "0" ;
            }
            time_str = time_str + hours + ':';

            if (minutes<10){
                time_str = time_str + "0" ;
            }
            time_str = time_str + minutes;

            btEnterTime.setText(time_str);
        }
    };


    public void onStartReminderService(View v) {

        Calendar calendar = Calendar.getInstance();
        int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);

        int timeMinutes =  (hours*60+minutes)-(currentHours*60+currentMinutes);
        if (timeMinutes<0){
            timeMinutes = 24*60 + timeMinutes;
        }

        tvMakeReminder.setText(""+timeMinutes);

        startService(new Intent(this, ReminderService.class).putExtra("time", timeMinutes).putExtra("text", etReminderText.getText().toString()));
    }


    public void onStopReminderService(View v) {
        stopService(new Intent(this, ReminderService.class));
    }


}
