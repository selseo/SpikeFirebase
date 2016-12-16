package com.example.katesudal.spikefirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoomReservation extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;
    Button buttonNextToChooseRoom;
    DatePicker datePickerReservedDate;
    RadioButton radioButtonAM;
    RadioButton radioButtonPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_reservation);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child("0").child("id").setValue(0);
        mDatabase.child("User").child("0").child("name").setValue("Anna");
        mDatabase.child("User").child("0").child("email").setValue("email.firebase@email.com");

        buttonNextToChooseRoom = (Button) findViewById(R.id.buttonNextToChooseRoom);
        datePickerReservedDate = (DatePicker) findViewById(R.id.datePickerReservedDate);
        radioButtonAM = (RadioButton) findViewById(R.id.radioButtonAM);
        radioButtonPM = (RadioButton) findViewById(R.id.radioButtonPM);

        buttonNextToChooseRoom.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonNextToChooseRoom){
            String chooseTime;
            chooseTime = String.valueOf(datePickerReservedDate.getDayOfMonth());
            if(radioButtonAM.isChecked()) chooseTime = chooseTime+"-AM";
            else if(radioButtonPM.isChecked()) chooseTime = chooseTime+"-PM";

            Intent intent = new Intent(this,ChooseReservedRoom.class);
            intent.putExtra("time",chooseTime);
            startActivity(intent);
        }
    }
}
