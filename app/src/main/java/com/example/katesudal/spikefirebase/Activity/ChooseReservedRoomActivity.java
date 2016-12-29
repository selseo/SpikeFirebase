package com.example.katesudal.spikefirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.katesudal.spikefirebase.Model.Reservation;
import com.example.katesudal.spikefirebase.Model.Room;
import com.example.katesudal.spikefirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.TimeZone;

public class ChooseReservedRoomActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spinnerFreeRoom;
    Button buttonSendReservation;
    EditText editTextType;
    private DatabaseReference mDatabase;
    String reservedDate;
    int startTime;
    int endTime;
    int amount;
    HashMap<String, Room> availableRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reserved_room);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        spinnerFreeRoom = (Spinner) findViewById(R.id.spinnerFreeRoom);
        buttonSendReservation = (Button) findViewById(R.id.buttonSendReservation);
        editTextType = (EditText) findViewById(R.id.editTextType);

        buttonSendReservation.setOnClickListener(this);

        reservedDate = getIntent().getExtras().getString("date");
        startTime = getIntent().getExtras().getInt("startTime");
        endTime = getIntent().getExtras().getInt("endTime");
        amount = getIntent().getExtras().getInt("amount");

        getAllRoomOnAmount();
    }

    private void getAllRoomOnAmount() {
        mDatabase.child("Room")
                .orderByChild("maxCapacity")
                .startAt(amount)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Room> roomsMap = new HashMap<>();
                for(DataSnapshot roomDataSnapshot : dataSnapshot.getChildren()){
                    roomsMap.put(roomDataSnapshot.getKey(),roomDataSnapshot.getValue(Room.class));
                }
                getReservationOnDate(roomsMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getReservationOnDate(final HashMap<String,Room> roomsMap) {
        mDatabase.child("Reservation")
                .orderByChild("reservedDate")
                .equalTo(reservedDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Reservation> reservationListOnDate = new ArrayList<>();
                        for(DataSnapshot reservationDataSnapshot : dataSnapshot.getChildren()){
                            Reservation reservation = reservationDataSnapshot.getValue(Reservation.class);
                            reservationListOnDate.add(reservation);
                        }
                        removeReservedRoom(roomsMap,reservationListOnDate);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void removeReservedRoom(HashMap<String,Room> roomsMap,
                                    List<Reservation> reservationListOnDate) {
        for(Reservation reservation:reservationListOnDate){
            if(!(startTime>=reservation.getEndTime()||
                    endTime<=reservation.getStartTime())){
                if(roomsMap.containsKey(reservation.getRoomId())){
                    roomsMap.remove(reservation.getRoomId());
                }
            }
        }
        showAvailableRoomInSpinner(roomsMap);

    }

    private void showAvailableRoomInSpinner(HashMap<String, Room> roomsMap) {
        List<String> nameList = new ArrayList<>();
        for (String key : roomsMap.keySet()) {
            nameList.add(roomsMap.get(key).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFreeRoom.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSendReservation) {
            String roomName = spinnerFreeRoom.getSelectedItem().toString();
            Query queryRoom = mDatabase.child("Room").orderByChild("name").equalTo(roomName);
            queryRoom.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot roomDataSnapshot1 : dataSnapshot.getChildren()) {
                        createReservationByRoomId(roomDataSnapshot1.getKey());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    private void createReservationByRoomId(String roomId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Long tsLong = System.currentTimeMillis() / 1000;
        String timeStamp = getDateCurrentTimeZone(tsLong);
        String reservedType = String.valueOf(editTextType.getText());
        String key = mDatabase.child("Reservation").push().getKey();
        Reservation reservation = new Reservation(user.getUid(), roomId,timeStamp,reservedDate,
                reservedType,startTime,endTime);
        Map<String, Object> reservationValue = reservation.toMap();

        saveReservationToFirebase(key, reservationValue);
    }

    private void saveReservationToFirebase(String key, Map<String, Object> reservationValue) {
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/Reservation/" + key, reservationValue);

        mDatabase.updateChildren(childUpdates);

        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }

    public String getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("UTC+07:00");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentTimeZone = calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
        }
        return "";
    }
}
