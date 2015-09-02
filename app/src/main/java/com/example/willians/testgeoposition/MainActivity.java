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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/* En la declaracion del activity implemento una interface que es el callback de la petición de retrofit y tambien
se implementa el LocationListener que contiene metodos para manejar el tema de localizaciones */
public class MainActivity extends AppCompatActivity implements Callback<JsonObject>, LocationListener {

    private TextView textView;
    private TextView latTxt;
    private TextView longTxt;
    private LocationManager locationManager;
    private String provider;

    private Criteria criteria;

    private LatLng currentPosition;

    private LatLng bookingCoord;
    private LatLng position1;
    private LatLng position2;
    private LatLng position3;

    private GoogleMap map;

    private ArrayList<LatLng> listCoords;

    private JsonObject jsonResponseData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map  = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

        latTxt = (TextView)findViewById(R.id.txt_current_lat);
        longTxt = (TextView)findViewById(R.id.txt_current_long);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        provider  = locationManager.getBestProvider(criteria, false);

        listCoords = new ArrayList<LatLng>();


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



    public void getServerData(View view){


        //Esto me permite hacer la peticion al servidor a traves de retrofit iniciando el service definido tanto en el adapter como en la interface
        BookingListApiAdapter.getApiService().getBookingData(this);

    }


    private LatLng getCentroid(ArrayList<LatLng> coordsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < coordsList.size() ; i++)
        {
            builder.include(coordsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
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


    /* En el metodo success recibo la respuesta de retrofit la cual me devuelve el json del servidor en un JsonObject
    * luego en este metodo se agrega a textview el string de la respuesta, es decir el json convertido en string
    *
    * */

    @Override
    public void success(JsonObject jsonObject, Response response) {

        jsonResponseData = jsonObject;
        textView = (TextView)findViewById(R.id.txt_json);
        textView.setText(jsonObject.getAsJsonObject().toString());

        //obtengo la posicion del telefono

        Location location = locationManager.getLastKnownLocation(provider);

        //si no es null llamo al metodo onLocationChanged donde se termina el proceso de mostrar en mapa y calcular el centroid
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latTxt.setText("Location not available");
            longTxt.setText("Location not available");
        }
        //textView.setText(jsonObject.getAsJsonObject().getAsJsonArray("bookings").get(0).getAsJsonObject().get("lat").getAsString());

        //Log.e("Json response", jsonObject.getAsJsonObject().toString());
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }

    @Override
    public void onLocationChanged(Location location) {
        float lat = (float)(location.getLatitude());
        float lgn = (float)(location.getLongitude());

        //creo un objeto de tipo LatLng que me permite tener una coordenada exacta

        currentPosition = new LatLng(lat, lgn);

        //agrego la coordenada al array list listCoords esto para calcular el centroid posteriormente

        listCoords.add(currentPosition);

        /* Del json obtenido tras la respuesta del servidor hacemos un parse y obtenemos el array de bookings
        * luego iteramos a traves de un for y creamos un objeto de tipo LatLng con cada una de las coordenadas que
        * se encuentran e el array de bookings. Seguidamente se agrega ese objeto a una lista de coordenadas
        * */

        JsonArray myBookings = jsonResponseData.getAsJsonObject().getAsJsonArray("bookings");

        for(int i = 0 ; i < myBookings.size() ; i++)
        {
            bookingCoord = new LatLng(myBookings.get(i).getAsJsonObject().get("lat").getAsFloat(), myBookings.get(i).getAsJsonObject().get("lon").getAsFloat());

            listCoords.add(bookingCoord);
        }

        /*position1 = new LatLng(4.703230, -74.028926);
        position2 = new LatLng(4.699359, -74.027681);
        position3 = new LatLng(4.699594, -74.034720);
        listCoords.add(currentPosition);
        listCoords.add(position1);
        listCoords.add(position2);
        listCoords.add(position3);*/


        //Aqui se llama al metodo getCentroid para obtener el centroid de un conjunto de coordenadas que se encuentran en la lista listCoords

        LatLng centroid = getCentroid(listCoords);

        //muestro el resultado del centroide en un Toast
        Toast.makeText(this, centroid.toString(), Toast.LENGTH_LONG).show();



        /*
        * Aqui utilizo google maps para mostrar la posicion actual del dispositivo
        * */
        latTxt.setText(String.valueOf(lat));
        longTxt.setText(String.valueOf(lgn));
        if(map != null){
            Marker current = map.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .title("Posicion actual")
                    .snippet("hola desde android maps")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 1));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


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
