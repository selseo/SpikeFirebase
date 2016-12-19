package com.example.katesudal.spikefirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ChooseReservedRoom extends AppCompatActivity implements View.OnClickListener{
    Spinner spinnerFreeRoom;
    Button buttonSendReservation;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reserved_room);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        spinnerFreeRoom = (Spinner) findViewById(R.id.spinnerFreeRoom);
        buttonSendReservation = (Button) findViewById(R.id.buttonSendReservation);

        buttonSendReservation.setOnClickListener(this);




        mDatabase.child("Room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HashMap<String,String>> rooms;
                Log.d("List", String.valueOf(dataSnapshot.getValue()));
                rooms = (List<HashMap<String,String>>) dataSnapshot.getValue();
                showRoomNameInSpinner(rooms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void showRoomNameInSpinner(List<HashMap<String,String>> rooms) {
        Log.d("Room", String.valueOf(rooms));
        List<String> roomNames = new ArrayList<>();
        for(int roomIndex =0 ;roomIndex<rooms.size(); roomIndex++){
            roomNames.add(rooms.get(roomIndex).get("name"));
            Log.d("RoomName=",rooms.get(roomIndex).get("name"));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,roomNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFreeRoom.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonSendReservation){
            String roomName = spinnerFreeRoom.getSelectedItem().toString();
            Query queryRoom = mDatabase.child("Room").orderByChild("name").equalTo(roomName);
//            Log.d("Query", String.valueOf(room.getRef()));
            queryRoom.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String rooms = (String) (((HashMap<String,Object>) dataSnapshot.getValue()).keySet().toArray())[0];
                    Log.d("Snapshot=", String.valueOf(rooms));
                    addNewReservation(rooms);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void addNewReservation(String roomId) {
        Reservation reservation = new Reservation("0",roomId);
        String timeStamp = "xxx";
        String reservedDate = "yyy";
        String reservedType = "zzz";
        String key = mDatabase.child("ReservationDetail").push().getKey();
        ReservationDetail reservationDetail = new ReservationDetail(reservedDate,timeStamp,reservedType);
        Map<String, Object> reservationDetailValues = reservationDetail.toMap();
        Map<String,Object> reservationValue = reservation.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/ReservationDetail/" + key, reservationDetailValues);
        childUpdates.put("/Reservation/"+key,reservationValue);
        mDatabase.updateChildren(childUpdates);
    }
}