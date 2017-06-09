package com.example.benjamin.pokemoncatcher;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static android.R.id.message;

public class PostActivity extends AppCompatActivity {
    private TextView response;
    private String test;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        context = this;
        response = (TextView) findViewById(R.id.textView);
        getResponse();



    }

    private Pokemon pokemonsFromJson(final String json) {
        final Gson gson = new Gson();
        return gson.fromJson(json, Pokemon.class);
    }

    private void getResponse(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(final Void... params){
                try{
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/pokemon/abcdefghijklmnopqrstuvwzyz").openConnection();
                    connection.setRequestProperty("X-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.IlRlYW0gRmxhcmUi.9wjmcidHzjQoI07b2s69lyo8xnF8nzIsFcVCNSwjguk");
                    Scanner scanner = new Scanner(connection.getInputStream());
                    String json = "";

                    while (scanner.hasNextLine()) {
                        json += scanner.nextLine();
                    }
                    return json;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(final String result){
                super.onPostExecute(result);
                Pokemon pokemon = pokemonsFromJson(result);
                response.setText(pokemon.toString());

            }
        }.execute();



}








}
