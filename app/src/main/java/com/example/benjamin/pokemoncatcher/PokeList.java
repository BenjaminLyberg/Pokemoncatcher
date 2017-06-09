package com.example.benjamin.pokemoncatcher;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class PokeList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);

        ListView listView1 = (ListView) findViewById(android.R.id.list);
        DatabaseHandler db = new DatabaseHandler(this);

        Pokemon pokemon = new Pokemon("1", "Mew", "linkydoodle");
        Pokemon pokemon2 = new Pokemon("2", "Charmander", "linkydoodle");

        List<Pokemon> pokelist = db.getAllPokemon();

        ArrayAdapter<Pokemon> adapter = new ArrayAdapter<Pokemon>(this,
                android.R.layout.simple_list_item_1, pokelist);

        listView1.setAdapter(adapter);
    }
}
