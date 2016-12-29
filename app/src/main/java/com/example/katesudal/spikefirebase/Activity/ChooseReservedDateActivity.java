package com.example.katesudal.spikefirebase.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.katesudal.spikefirebase.R;

import java.util.Calendar;

public class ChooseReservedDateActivity extends AppCompatActivity implements View.OnClickListener{
    CalendarView calendarViewPickDate;
    Button buttonGetNextToChooseRoom;
    String curDate;
    TextView textViewStartTime;
    TextView textViewEndTime;
    SeekBar seekBar;
    TextView textViewAmount;
    int amount;
    int startTime;
    int endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reserved_date);
        calendarViewPickDate = (CalendarView) findViewById(R.id.calendarViewPickDate);
        calendarViewPickDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month++;
                curDate =dayOfMonth+"/"+month+"/"+year;
            }
        });
        buttonGetNextToChooseRoom = (Button) findViewById(R.id.buttonGetNextToChooseRoom);
        textViewStartTime = (TextView) findViewById(R.id.textViewStarTime);
        textViewEndTime = (TextView) findViewById(R.id.textViewEndTime);
        seekBar = (SeekBar) findViewById(R.id.seekBarAmount);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
        buttonGetNextToChooseRoom.setOnClickListener(this);
        textViewStartTime.setOnClickListener(this);
        textViewEndTime.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String unit;
                amount = progress * 30 / 100;
                if(progress<1) unit = " person";
                else unit=" persons";
                textViewAmount.setText(amount+unit);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonGetNextToChooseRoom){
            Intent intent = new Intent(this,ChooseReservedRoomActivity.class);
            intent.putExtra("date",curDate);
            intent.putExtra("startTime",startTime);
            intent.putExtra("endTime",endTime);
            intent.putExtra("amount",amount);
            startActivity(intent);
        }
        if(v.getId()==R.id.textViewStarTime){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if(minute<10) {
                        textViewStartTime.setText( hourOfDay + ":0" + minute);
                        startTime =Integer.parseInt(""+hourOfDay+"0"+minute);
                    }
                    else {
                        textViewStartTime.setText( hourOfDay + ":" + minute);
                        startTime = Integer.parseInt(""+hourOfDay+""+minute);
                    }
                }
            }, hour, minute, true);
            timePickerDialog.setTitle("Select Start Time");
            timePickerDialog.show();
        }
        if(v.getId()==R.id.textViewEndTime){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if(minute<10) {
                        textViewEndTime.setText( hourOfDay + ":0" + minute);
                        endTime =Integer.parseInt(""+hourOfDay+"0"+minute);
                    }
                    else {
                        textViewEndTime.setText( hourOfDay + ":" + minute);
                        endTime = Integer.parseInt(""+hourOfDay+""+minute);
                    }
                }
            }, hour, minute, true);
            timePickerDialog.setTitle("Select End Time");
            timePickerDialog.show();
        }
    }
}
