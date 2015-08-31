package com.example.willians.testgeoposition.rest.models;

import com.example.willians.testgeoposition.domains.Booking;

import java.util.ArrayList;

/**
 * Created by willians on 30/08/15.
 */
public class BookingListResponse {

    //@SerializedName(JsonKey.BOOKING_RESULTS)
    ArrayList<Booking> resultsBooking;

    public ArrayList<Booking> getBookings(){
        return resultsBooking;
    }
}
