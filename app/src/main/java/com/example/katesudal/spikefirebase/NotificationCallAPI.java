package com.example.katesudal.spikefirebase;

import com.example.katesudal.spikefirebase.Model.SendNotiRequest;
import com.example.katesudal.spikefirebase.Model.SendNotiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by katesuda.l on 15/12/2559.
 */

public interface NotificationCallAPI {

    @Headers({"Content-Type:application/json",
    "Authorization:key=AAAAaWL5D4M:APA91bG0axup39dmwK980Dz9nsZSoRIW9qqgsV00Vb-1CtlRLirlTt9clCh5nudal-xSHjOvT4iEoyIMtDnWNPOvAJCotd0rU1LkxROX85XIrkjz5sO81jp6FI8Ip8K_d9v7msE6hl1uBmkNZOlgNYJGNgB4d4Sb9g"})
    @POST("fcm/send")
    Call<SendNotiResponse> postNotification(@Body SendNotiRequest sendNotiRequest);
}
