package com.example.katesudal.spikefirebase;

import android.provider.ContactsContract;
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
import java.util.TimeZone;

public class ChooseReservedRoom extends AppCompatActivity implements View.OnClickListener {
    Spinner spinnerFreeRoom;
    Button buttonSendReservation;
    private DatabaseReference mDatabase;
    String reservedDate;
    HashMap<String, Room> availableRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reserved_room);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        spinnerFreeRoom = (Spinner) findViewById(R.id.spinnerFreeRoom);
        buttonSendReservation = (Button) findViewById(R.id.buttonSendReservation);

        buttonSendReservation.setOnClickListener(this);

        reservedDate = getIntent().getExtras().getString("time");
//        mDatabase.child("Room").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<HashMap<String,String>> rooms;
//                Log.d("List", String.valueOf(dataSnapshot.getValue()));
//                rooms = (List<HashMap<String,String>>) dataSnapshot.getValue();
//                showRoomNameInSpinner(rooms);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        showAvailableRoomInSpinner();


    }

    private void showAvailableRoomInSpinner() {
        availableRoom = new HashMap<>();
        mDatabase.child("Room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot roomDataSnapShot : dataSnapshot.getChildren()) {
                    Room room = roomDataSnapShot.getValue(Room.class);
                    availableRoom.put(roomDataSnapShot.getKey(), room);
                }
                Log.d("AllRoom", String.valueOf(availableRoom));
                removeUnavailableRoomFromMap();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeUnavailableRoomFromMap() {
        mDatabase.child("ReservationDetail")
                .orderByChild("reservedDate")
                .equalTo(reservedDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()==null){
                            showRoomNameInSpinner(availableRoom);
                        }
                        else {
                            final long[] snapShotIndex = {0};
                            for (DataSnapshot roomDataSnapShot : dataSnapshot.getChildren()) {
                                final long childrenCount = dataSnapshot.getChildrenCount();
                                Log.d("SnapShotIndex", String.valueOf(roomDataSnapShot.getValue()));
                                mDatabase.child("Reservation")
                                        .child(roomDataSnapShot.getKey())
                                        .child("roomId")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                availableRoom.remove(dataSnapshot.getValue());
                                                Log.d("CheckOrder", "Remove room");
                                                snapShotIndex[0]++;
                                                if (snapShotIndex[0] >= childrenCount)
                                                    showRoomNameInSpinner(availableRoom);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void showRoomNameInSpinner(HashMap<String, Room> availableRoomInSpinner) {
        Log.d("CheckOrder","Add to spinner");
        List<String> nameList = new ArrayList<>();
        for (String key : availableRoomInSpinner.keySet()) {
            nameList.add(availableRoomInSpinner.get(key).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFreeRoom.setAdapter(dataAdapter);
    }

//    private void showRoomNameInSpinner(List<HashMap<String,String>> rooms) {
//        Log.d("Room", String.valueOf(rooms));
//        List<String> roomNames = new ArrayList<>();
//        for(int roomIndex =0 ;roomIndex<rooms.size(); roomIndex++){
//            roomNames.add(rooms.get(roomIndex).get("name"));
//            Log.d("RoomName=",rooms.get(roomIndex).get("name"));
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,roomNames);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerFreeRoom.setAdapter(dataAdapter);
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSendReservation) {
            String roomName = spinnerFreeRoom.getSelectedItem().toString();
            Query queryRoom = mDatabase.child("Room").orderByChild("name").equalTo(roomName);
            queryRoom.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot roomDataSnapshot1 : dataSnapshot.getChildren()) {
                        Room room = roomDataSnapshot1.getValue(Room.class);
                        Log.d("SnapValu2e=", room.toString());
                        Log.d("Key=", roomDataSnapshot1.getKey());
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
        Long tsLong = System.currentTimeMillis() / 1000;
        String timeStamp = getDateCurrentTimeZone(tsLong);
        String reservedType = "zzz";
        String key = mDatabase.child("ReservationDetail").push().getKey();
        Reservation reservation = new Reservation("0", roomId);
        ReservationDetail reservationDetail = new ReservationDetail(reservedDate, timeStamp, reservedType);
        Map<String, Object> reservationDetailValues = reservationDetail.toMap();
        Map<String, Object> reservationValue = reservation.toMap();

        saveReservationToFirebase(key, reservationDetailValues, reservationValue);
    }

    private void saveReservationToFirebase(String key, Map<String, Object> reservationDetailValues, Map<String, Object> reservationValue) {
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/ReservationDetail/" + key, reservationDetailValues);
        childUpdates.put("/Reservation/" + key, reservationValue);

        mDatabase.updateChildren(childUpdates);
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
