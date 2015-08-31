package com.example.willians.testgeoposition.rest.deserializer;

import android.util.Log;

import com.example.willians.testgeoposition.rest.models.BookingListResponse;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by willians on 30/08/15.
 */
public class BookingListDeserializer implements JsonDeserializer<BookingListResponse> {

    @Override
    public BookingListResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();

        BookingListResponse response = gson.fromJson(json, BookingListResponse.class);

        JsonObject bookingData = json.getAsJsonObject();

        Log.e("TEST DESERIALIZER", response.toString());

        return response;
    }
}
