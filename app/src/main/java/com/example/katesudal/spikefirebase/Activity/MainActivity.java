package com.example.katesudal.spikefirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.katesudal.spikefirebase.Model.Notification;
import com.example.katesudal.spikefirebase.Model.SendNotiRequest;
import com.example.katesudal.spikefirebase.Model.SendNotiResponse;
import com.example.katesudal.spikefirebase.NotificationCallAPI;
import com.example.katesudal.spikefirebase.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    Button buttonSendNoti;
    EditText editTextSendNotiMessage;
    Button buttonGotoReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonSendNoti = (Button) findViewById(R.id.buttonSendNoti);
        editTextSendNotiMessage = (EditText) findViewById(R.id.editTextSendNotiMessage);
        buttonGotoReservation = (Button) findViewById(R.id.buttonGotoReservation);

        buttonSendNoti.setOnClickListener(this);
        buttonGotoReservation.setOnClickListener(this);

//        Log.d("Token=", FirebaseInstanceId.getInstance().getToken());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSendNoti) {
            String notiMessage = String.valueOf(editTextSendNotiMessage.getText());

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(createHttLoggingInterceptor())
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl("https://fcm.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            NotificationCallAPI api = retrofit.create(NotificationCallAPI.class);

            SendNotiRequest sendNotiRequest = new SendNotiRequest();
            Notification notification = new Notification("New Notification", notiMessage);
            sendNotiRequest.setNotification(notification);
            sendNotiRequest.setTo(FirebaseInstanceId.getInstance().getToken());
//            sendNotiRequest.setTo("doQtcVavX08:APA91bGSQNm2jEgnefPOYsEaO1trFhMhl0ls6MALDwMfrUKtwEj6BdTtfdFM4SY2UFArbgXXjrleKIcez8Gnq-uuuYBl9_lFnTJjNR5--S91sbvwErGKj31x892GNGPBbc0LZMKkz1Vg");

            Call<SendNotiResponse> call = api.postNotification(sendNotiRequest);
            call.enqueue(new Callback<SendNotiResponse>() {
                @Override
                public void onResponse(Call<SendNotiResponse> call, Response<SendNotiResponse> response) {
                    Log.d("Respone=","Request Success");

                }

                @Override
                public void onFailure(Call<SendNotiResponse> call, Throwable t) {
                    Log.d("Respone=","Request error",t);
                }
            });

        }

        if(v.getId()==R.id.buttonGotoReservation){
            Intent intent = new Intent(this,RoomReservationActivity.class);
            startActivity(intent);
        }
    }

    private HttpLoggingInterceptor createHttLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
