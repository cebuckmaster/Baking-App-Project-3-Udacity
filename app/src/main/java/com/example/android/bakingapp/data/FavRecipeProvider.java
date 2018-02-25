package com.example.android.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * This is the RecipeProvider to access the ingredients for the "Working Recipe" in a sqlite DB.
 */

public class FavRecipeProvider extends ContentProvider {

    private static final int FAVRECIPE = 100;

    private static final UriMatcher sUriMather = buildUriMatcher();

    public static final String TAG = FavRecipeProvider.class.getSimpleName();

    private FavRecipeDBHelper mRecipeDBHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavRecipeDBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavRecipeDBContract.PATH_INGREDIENTDB, FAVRECIPE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mRecipeDBHelper = new FavRecipeDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mRecipeDBHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMather.match(uri);

        switch (match) {
            case FAVRECIPE:
                cursor = database.query(FavRecipeDBContract.FavRecipeDBEntry.TABLE_NAME,
                                        projection,
                                        selection,
                                        selectionArgs,
                                        null,
                                        null,
                                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown Uri - " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMather.match(uri);
        switch (match) {
            case FAVRECIPE:
                return FavRecipeDBContract.FavRecipeDBEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mRecipeDBHelper.getWritableDatabase();

        int match = sUriMather.match(uri);

        Uri returnUri = null;
        switch (match) {
            case FAVRECIPE:
                long id = db.insert(FavRecipeDBContract.FavRecipeDBEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavRecipeDBContract.FavRecipeDBEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Insertion is now support for - " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mRecipeDBHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMather.match(uri);
        switch (match) {
            case FAVRECIPE:
                rowsDeleted = db.delete(FavRecipeDBContract.FavRecipeDBEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
