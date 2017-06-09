package com.example.benjamin.pokemoncatcher;

/**
 * Created by Benjamin on 30.05.2016.
 */

public class Pokemon {
    public String id;
    public String _id;
    public String name;
    public String imageUrl;


    public Pokemon(){

    }


    public Pokemon(final String _id, final String name, final String imageUrl){
        this._id = _id;
        this.name = name;
        this.imageUrl = imageUrl;

    }

    public String getID(){
        return this._id;
    }

    public void setID(String id){
        this._id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImage(){
        return this.imageUrl;
    }

    public void setImage(String image){
        this.imageUrl = image;
    }

    @Override
    public String toString(){
        return  name;
    }
}
