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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

public class ChooseReservedRoom extends AppCompatActivity implements View.OnClickListener{
    Spinner spinnerFreeRoom;
    Button buttonSendReservation;
    private DatabaseReference mDatabase;
    String reservedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reserved_room);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        spinnerFreeRoom = (Spinner) findViewById(R.id.spinnerFreeRoom);
        buttonSendReservation = (Button) findViewById(R.id.buttonSendReservation);

        buttonSendReservation.setOnClickListener(this);

        reservedDate = getIntent().getExtras().getString("time");
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
//        showAvailableRoomInSpinner();


    }

//    private void showAvailableRoomInSpinner() {
//        Query queryReservationDetail = mDatabase.child("ReservationDetail").orderByChild("reservedDate").equalTo(reservedDate);
//        queryReservationDetail.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("ReservesnapShot", String.valueOf(((HashMap<String,Object>) dataSnapshot.getValue()).keySet()));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

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
            queryRoom.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot roomDataSnapshot1: dataSnapshot.getChildren()) {
                        Room room = roomDataSnapshot1.getValue(Room.class);
                        Log.d("SnapValu2e=", room.toString());
                        Log.d("Key=", roomDataSnapshot1.getKey());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }
    }

    private void addNewReservation(String roomId) {
        Reservation reservation = new Reservation("0",roomId);
        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = getDateCurrentTimeZone(tsLong);
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

    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("UTC+07:00");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentTimeZone = calendar.getTime();
            return sdf.format(currentTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
}
