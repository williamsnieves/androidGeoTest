package com.example.willians.testgeoposition.rest;

import com.example.willians.testgeoposition.rest.deserializer.BookingListDeserializer;
import com.example.willians.testgeoposition.rest.models.BookingListResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by willians on 30/08/15.
 */
public class BookingListApiAdapter {

    private static TaxiListApiService API_SERVICE;

    public static TaxiListApiService getApiService(){
        if(API_SERVICE == null){
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ApiConstants.URL_BASE)
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setConverter(buildBookingApiGsonConverter())
                    .build();

            API_SERVICE = adapter.create(TaxiListApiService.class);
        }

        return API_SERVICE;
    }

    //custom config to add a custom deserializaer to retrofit
    private static GsonConverter buildBookingApiGsonConverter(){
        Gson gsonConf = new GsonBuilder()
                .registerTypeAdapter(BookingListResponse.class, new BookingListDeserializer())
                .create();

        return new GsonConverter(gsonConf);
    }
}
