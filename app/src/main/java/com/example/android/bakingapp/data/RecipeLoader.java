package com.example.android.bakingapp.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bakingapp.utils.NetworkUtils;
import com.example.android.bakingapp.utils.RecipeJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * This is the AsyncTaskLoader called from the MainBakingActivity to load the recipe data from the Internet.
 */

public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

    public RecipeLoader(final Context context) {
        super(context);
    }

    @Override
    protected void  onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {

        ArrayList<Recipe> recipes = new ArrayList<>();

        URL recipeUrl = NetworkUtils.buildRecipeURL();

        try {
            String recipeResponse = NetworkUtils.getResponseFromHttpUrl(recipeUrl);
            recipes = RecipeJsonUtils.getRecipeFromJson(recipeResponse);
            return recipes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
