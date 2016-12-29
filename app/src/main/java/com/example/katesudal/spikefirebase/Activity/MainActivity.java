package com.example.katesudal.spikefirebase.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.katesudal.spikefirebase.Model.Notification;
import com.example.katesudal.spikefirebase.Model.SendNotificationRequest;
import com.example.katesudal.spikefirebase.Model.SendNotificationResponse;
import com.example.katesudal.spikefirebase.NotificationCallAPI;
import com.example.katesudal.spikefirebase.R;
import com.google.firebase.auth.FirebaseAuth;
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
    Button buttonSendNotification;
    EditText editTextSendNotificationMessage;
    Button buttonGotoReservation;
    Button buttonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonSendNotification = (Button) findViewById(R.id.buttonSendNotification);
        editTextSendNotificationMessage = (EditText) findViewById(R.id.editTextSendNotificationMessage);
        buttonGotoReservation = (Button) findViewById(R.id.buttonGotoReservation);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);

        buttonSendNotification.setOnClickListener(this);
        buttonGotoReservation.setOnClickListener(this);
        buttonSignOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSendNotification) {
            sendNotification();
        }

        if(v.getId()==R.id.buttonGotoReservation){
            Intent intent = new Intent(this,ChooseReservedDateActivity.class);
            startActivity(intent);
        }

        if(v.getId()==R.id.buttonSignOut){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
        }
    }

    private void sendNotification() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(createHttLoggingInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NotificationCallAPI api = retrofit.create(NotificationCallAPI.class);

        SendNotificationRequest sendNotificationRequest = setSendNotificationRequest();

        callNotificationAPI(api, sendNotificationRequest);
    }

    private void callNotificationAPI(NotificationCallAPI api, SendNotificationRequest sendNotificationRequest) {
        Call<SendNotificationResponse> call = api.postNotification(sendNotificationRequest);
        call.enqueue(new Callback<SendNotificationResponse>() {
            @Override
            public void onResponse(Call<SendNotificationResponse> call, Response<SendNotificationResponse> response) {
                Log.d("Respone=","Request Success");

            }

            @Override
            public void onFailure(Call<SendNotificationResponse> call, Throwable t) {
                Log.d("Respone=","Request error",t);
            }
        });
    }

    @NonNull
    private SendNotificationRequest setSendNotificationRequest() {
        SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        String notiMessage = String.valueOf(editTextSendNotificationMessage.getText());
        Notification notification = new Notification("New Notification", notiMessage);
        sendNotificationRequest.setNotification(notification);
        sendNotificationRequest.setTo(FirebaseInstanceId.getInstance().getToken());
        return sendNotificationRequest;
    }

    private HttpLoggingInterceptor createHttLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
