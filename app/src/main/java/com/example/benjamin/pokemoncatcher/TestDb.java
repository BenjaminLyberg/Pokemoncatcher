package com.example.benjamin.pokemoncatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class TestDb extends AppCompatActivity {
    private TextView dbView;
    private TextView counterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);

        dbView = (TextView) findViewById(R.id.dbView);
        counterView = (TextView) findViewById(R.id.counterView);

        DatabaseHandler db = new DatabaseHandler(this);



        Pokemon pokemon = new Pokemon("3", "Jynx", "linkydoodle");
        db.addPokemon(pokemon);





        //COUNTER
        int count = db.getPokemonCount();
        String counter = String.valueOf(count);
        counterView.setText(counter);



    }
}
