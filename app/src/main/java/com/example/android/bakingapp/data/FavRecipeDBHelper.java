package com.example.android.bakingapp.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is the helper class that defines the sqlite DB for the "Working Recipe" ingredients
 */

public class FavRecipeDBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_FAV_INGREDIENT_TABLE =
            "CREATE TABLE " + FavRecipeDBContract.FavRecipeDBEntry.TABLE_NAME + " (" +
                    FavRecipeDBContract.FavRecipeDBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FavRecipeDBContract.FavRecipeDBEntry.COLUMN_QUANTITY + " INTEGER DEFAULT 0, " +
                    FavRecipeDBContract.FavRecipeDBEntry.COLUMN_MEASUREMENTTYPE + " TEXT, " +
                    FavRecipeDBContract.FavRecipeDBEntry.COLUMN_INGREDIENT + " TEXT, " +
                    FavRecipeDBContract.FavRecipeDBEntry.COLUMN_RECIPE_NAME + " TEXT);";

    private static final String SQL_DELETE_FAV_INGREDIENT_TABLE = "DROP TABLE IF EXISTS " + FavRecipeDBContract.FavRecipeDBEntry.TABLE_NAME;

    private static final String DATABASE_NAME = "favRecipe.db";
    private static final int DATABASE_VER = 1;

    public FavRecipeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAV_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_FAV_INGREDIENT_TABLE);
        onCreate(db);
    }
}
