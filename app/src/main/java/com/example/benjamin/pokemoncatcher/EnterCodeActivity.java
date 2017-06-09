package com.example.benjamin.pokemoncatcher;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class EnterCodeActivity extends AppCompatActivity {
    private Context context;
    private EditText code;
    private TextView header;
    private TextView response;
    private Button submit;
    private Button back;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        db = new DatabaseHandler(this);

        context = this;

        code = (EditText) findViewById(R.id.codeInput);
        header = (TextView) findViewById(R.id.codeHead);
        response = (TextView) findViewById(R.id.response);
        submit = (Button) findViewById(R.id.submitButton);
        back = (Button) findViewById(R.id.backButton);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse(code.getText().toString());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Pokemon pokemonsFromJson(final String json) {
        final Gson gson = new Gson();
        return gson.fromJson(json, Pokemon.class);
    }

    private void getResponse(final String s){
        new AsyncTask<String, Void, ReturnContent>(){
            private final ProgressDialog progressDialog = new ProgressDialog(EnterCodeActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Please wait");
                progressDialog.show();
            }

            @Override
            protected ReturnContent doInBackground(String... args){
                String input = s;
                int respCode = 0;

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/pokemon/"+input).openConnection();
                    connection.setRequestProperty("X-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.IlRlYW0gRmxhcmUi.9wjmcidHzjQoI07b2s69lyo8xnF8nzIsFcVCNSwjguk");
                    respCode = connection.getResponseCode();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    String json = "";

                    while (scanner.hasNextLine()) {
                        json += scanner.nextLine();
                    }
                    return new ReturnContent(json, respCode);

                } catch (IOException e) {
                    switch(respCode){
                        case 420: return new ReturnContent("You entered the wrong ID", 420);
                        case 401: return new ReturnContent("Invalid token", 401);
                    }


                    return new ReturnContent("something buggy happened", respCode);
                }



            }

            @Override
            protected void onPostExecute(final ReturnContent result){
                super.onPostExecute(result);
                progressDialog.cancel();


                int status = result.status;

                if (status == 201){
                    Pokemon pokemon = pokemonsFromJson(result.content);
                    db.addPokemon(pokemon);
                    response.setText("Congrats! You caught " +pokemon.name);

                } else if (status == 200){
                    Pokemon pokemon = pokemonsFromJson(result.content);
                    db.addPokemon(pokemon); //TEST-PURPOSES ONLY. REMEMBER TO REMOVE!!!
                    response.setText("You have already caught " +pokemon.name);

                } else {
                    response.setText(result.content + result.status);
                }


            }
        }.execute();



    }



    private class ReturnContent {
        private String content;
        private int status;

        public ReturnContent(String content, int status){
            this.content = content;
            this.status = status;
        }
    }
}
