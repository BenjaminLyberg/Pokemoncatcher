package com.example.benjamin.pokemoncatcher;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Context context;
    private GoogleMap map;
    private Button pokeButton;
    private Button postButton;
    private Button testButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        db = new DatabaseHandler(this);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pokeButton = (Button) findViewById(R.id.pokeButton);
        postButton = (Button) findViewById(R.id.postButton);
        testButton = (Button) findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PokeList.class);
                startActivity(intent);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EnterCodeActivity.class);
                startActivity(intent);
            }
        });
        pokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPokeLocations();
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        LatLng starter = new LatLng(59.9116586, 10.7596282);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(starter,10));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);


    }


    private void addPokemonsToMap(final List<PokeLocation> pokemons){
        List<Pokemon> capturedListDB = db.getAllPokemon();
        List<PokeLocation> locationList = pokemons;

        Iterator<PokeLocation> i = locationList.iterator();

        while(i.hasNext()){
            PokeLocation loc = i.next();
            Iterator<Pokemon> j = capturedListDB.iterator();
            boolean empty = false;
            if(capturedListDB.isEmpty()){
                empty = true;
            }
                while(j.hasNext()){
                    Pokemon captured = j.next();

                if (loc.name.equals(captured.name)){
                    LatLng marker = new LatLng(loc.lat, loc.lng);
                    map.addMarker(new MarkerOptions().position(marker).title(loc.name).snippet("You've captured this Pokemon").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    i.remove();
                    j.remove();
                    capturedListDB.remove(captured);
                }

                }

            if(empty) {
                LatLng marker = new LatLng(loc.lat, loc.lng);
                map.addMarker(new MarkerOptions().position(marker).title(loc.name).snippet("Hint: " + loc.hint));
            }



        }



    }




    private List<PokeLocation> pokemonsFromJson(final String json) {
        final Gson gson = new Gson();
        final Type collectionType = new TypeToken<List<PokeLocation>>(){}.getType();
        return gson.fromJson(json, collectionType);
    }

    private void getPokeLocations() {
        new AsyncTask<Void, Void, String>() {
            private final ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Loading Pokemon locations");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(final Void... params) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Log.e("MainActivity", "Something went wrong while sleeping…", e);
                }


                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/locations").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    String json = "";

                    while (scanner.hasNextLine()) {
                        json += scanner.nextLine();
                    }

                    return json;
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while downloading Pokemon locations", e);
                }
            }

            @Override
            protected void onPostExecute(final String json) {
                super.onPostExecute(json);

                progressDialog.cancel();

                final List<PokeLocation> pokemons = pokemonsFromJson(json);
                addPokemonsToMap(pokemons);


            }
        }.execute();
    }
}

