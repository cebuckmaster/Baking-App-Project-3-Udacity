package com.example.android.bakingapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * This is the DB Contract class for the sqlLite DB
 */

public class FavRecipeDBContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INGREDIENTDB = "ingredientDB";

    public static final class FavRecipeDBEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTDB;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTDB;

        public static final String TABLE_NAME = "IngredientList";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASUREMENTTYPE = "measurementType";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_RECIPE_NAME = "recipeName";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENTDB)
                .build();

    }

}
