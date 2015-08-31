package com.example.willians.testgeoposition.rest;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by willians on 30/08/15.
 */
public interface TaxiListApiService {

    @Headers("Content-Type: application/json")
    @GET(ApiConstants.JSON_URL)
    //void getBookingData(Callback<BookingListResponse> serverResponse);
    void getBookingData(Callback<JsonObject> serverResponse);
}
