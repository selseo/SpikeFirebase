package com.example.katesudal.spikefirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.katesudal.spikefirebase.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoomReservationActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;
    Button buttonNextToChooseRoom;
    DatePicker datePickerReservedDate;
    RadioButton radioButtonAM;
    RadioButton radioButtonPM;
    EditText editTextCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_reservation);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonNextToChooseRoom = (Button) findViewById(R.id.buttonNextToChooseRoom);
        datePickerReservedDate = (DatePicker) findViewById(R.id.datePickerReservedDate);
        radioButtonAM = (RadioButton) findViewById(R.id.radioButtonAM);
        radioButtonPM = (RadioButton) findViewById(R.id.radioButtonPM);
        editTextCapacity = (EditText) findViewById(R.id.editTextCapacity);

        buttonNextToChooseRoom.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonNextToChooseRoom){
            String capacity = String.valueOf(editTextCapacity.getText());
            String chooseTime;
            chooseTime = ""+datePickerReservedDate.getDayOfMonth()+"-"
                    +datePickerReservedDate.getMonth()+"-"
                    +datePickerReservedDate.getYear();
            if(radioButtonAM.isChecked()) chooseTime = chooseTime+"-AM";
            else if(radioButtonPM.isChecked()) chooseTime = chooseTime+"-PM";

            Intent intent = new Intent(this,ChooseReservedRoomActivity.class);
            intent.putExtra("time",chooseTime);
            intent.putExtra("capacity",capacity);
            startActivity(intent);
        }
    }
}
