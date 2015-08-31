package com.example.willians.testgeoposition;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.willians.testgeoposition.rest.BookingListApiAdapter;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements Callback<JsonObject>, LocationListener {

    private TextView textView;
    private TextView latTxt;
    private TextView longTxt;
    private LocationManager locationManager;
    private String provider;

    private Criteria criteria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTxt = (TextView)findViewById(R.id.txt_current_lat);
        longTxt = (TextView)findViewById(R.id.txt_current_long);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        provider  = locationManager.getBestProvider(criteria, false);



        BookingListApiAdapter.getApiService().getBookingData(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void getCurrentPosition(View view){

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latTxt.setText("Location not available");
            longTxt.setText("Location not available");
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void success(JsonObject jsonObject, Response response) {
        textView = (TextView)findViewById(R.id.txt_json);
        textView.setText(jsonObject.getAsJsonObject().toString());

        //Log.e("Json response", jsonObject.getAsJsonObject().toString());
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int)(location.getLatitude());
        int lgn = (int)(location.getLongitude());

        latTxt.setText(String.valueOf(lat));
        longTxt.setText(String.valueOf(lgn));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
