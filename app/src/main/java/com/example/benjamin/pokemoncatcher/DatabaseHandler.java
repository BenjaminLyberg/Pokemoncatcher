package com.example.benjamin.pokemoncatcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 02.06.2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pokeManager";
    private static final String TABLE_POKE = "pokemons";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "imageUrl";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POKE_TABLE = "CREATE TABLE " + TABLE_POKE + "("
                + KEY_ID + " TEXT," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_POKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POKE);
        onCreate(db);
    }

    public void addPokemon(Pokemon pokemon){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, pokemon.getID());
        values.put(KEY_NAME, pokemon.getName());
        values.put(KEY_IMAGE, pokemon.getImage());

        db.insert(TABLE_POKE, null, values);
        db.close();
    }

    public Pokemon getPokemon(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_POKE, new String[]{KEY_ID, KEY_NAME, KEY_IMAGE}, KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor!=null)
            cursor.moveToFirst();

        Pokemon pokemon = new Pokemon(cursor.getString(0),
                cursor.getString(1), cursor.getString(2));

        return pokemon;
    }

    public List<Pokemon> getAllPokemon(){
        List<Pokemon> pokelist = new ArrayList<Pokemon>();

        String query = "SELECT * FROM " +TABLE_POKE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                Pokemon pokemon = new Pokemon();
                pokemon.setID(cursor.getString(0));
                pokemon.setName(cursor.getString(1));
                pokemon.setImage(cursor.getString(2));

                pokelist.add(pokemon);
            } while(cursor.moveToNext());
        }
        return pokelist;
    }

    public int getPokemonCount(){
        String query = "SELECT * FROM " +TABLE_POKE;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        return cursor.getCount();

    }

    public int updatePokemon(Pokemon pokemon){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pokemon.getName());
        values.put(KEY_IMAGE, pokemon.getImage());

        return db.update(TABLE_POKE, values, KEY_ID + " = ? ",
                new String[] {pokemon.getID()});
    }

    public void deletePokemon(Pokemon pokemon){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POKE, KEY_ID + " = ?",
                new String[]{pokemon.getID()});
        db.close();
    }

    public void deleteDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POKE, null, null);
    }
}
