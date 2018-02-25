package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.android.bakingapp.data.FavRecipeDBContract;
import com.example.android.bakingapp.data.Ingredient;
import java.util.ArrayList;



/**
 * This is the widget Provider used to get the ingredients for the "working Recipie" and display them in the Widget
 */

public class RecipeListProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Ingredient> mIngredients;
    private Context mContext = null;

    public RecipeListProvider(Context context, Intent intent) {
        mContext = context;

        loadIngredients();
    }

    private void loadIngredients() {
        Uri recipeIngredientUri = FavRecipeDBContract.FavRecipeDBEntry.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(
                recipeIngredientUri,
                null,
                null,
                null,
                null);

        mIngredients = new ArrayList<Ingredient>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int quantityIndex =  cursor.getColumnIndex(FavRecipeDBContract.FavRecipeDBEntry.COLUMN_QUANTITY);
            int measurementTypeIndex = cursor.getColumnIndex(FavRecipeDBContract.FavRecipeDBEntry.COLUMN_MEASUREMENTTYPE);
            int ingredientIndex = cursor.getColumnIndex(FavRecipeDBContract.FavRecipeDBEntry.COLUMN_INGREDIENT);
            mIngredients.add(new Ingredient(cursor.getInt(quantityIndex),
                                            cursor.getString(measurementTypeIndex),
                                            cursor.getString(ingredientIndex)));

            while (cursor.moveToNext()) {
                mIngredients.add(new Ingredient(cursor.getInt(quantityIndex),
                                                cursor.getString(measurementTypeIndex),
                                                cursor.getString(ingredientIndex)));
            }
            cursor.close();
        }

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);

        remoteView.setTextViewText(R.id.tv_ingredient_quantity, String.valueOf(mIngredients.get(position).getQuantity()));
        remoteView.setTextViewText(R.id.tv_measurement_type, mIngredients.get(position).getMeasurementType());
        remoteView.setTextViewText(R.id.tv_recipe_ingredient, mIngredients.get(position).getIngredient());

         return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
