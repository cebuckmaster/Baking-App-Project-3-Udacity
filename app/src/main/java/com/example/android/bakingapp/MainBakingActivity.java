package com.example.android.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainBakingActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {


    public static final String RECIPE_NAME = "RecipeName";

    private static final int RECIPE_LOADER = 0;

    //Using Butterknife Library to support access to Layout elements
    @BindView(R.id.recyclerview_recipelist) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_baking);
        ButterKnife.bind(this);

        int numberofColumns = 1;

        if(findViewById(R.id.tablet_frame_layout) != null) {
            numberofColumns = 3;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberofColumns);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        mLoadingIndicator.setVisibility(View.VISIBLE);

        if(savedInstanceState == null || !savedInstanceState.containsKey("Recipes")) {
            mRecipe = new ArrayList<Recipe>();
            loadRecipeData();
        } else {
            mRecipe = savedInstanceState.getParcelableArrayList("Recipes");
            showRecipeData();
            mRecipeAdapter.setRecipes(mRecipe);
        }

    }

    private void loadRecipeData() {
        //Calls AsyncTask to load recipe data from Internet API.
        getSupportLoaderManager().initLoader(RECIPE_LOADER, null, loaderCallbackRecipes);
    }

    private void showRecipeData() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //saves the recipes in a Parcelable so that it doesn't have to call the network each time.
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Recipes", mRecipe);
    }

    @Override
    public void onClick(Recipe recipe) {
        Context context = this;
        Intent intentShowRecipeDetails = new Intent(context, RecipeDetails.class);
        intentShowRecipeDetails.putExtra("parcel_data", recipe);
        startActivity(intentShowRecipeDetails);
    }

    private LoaderManager.LoaderCallbacks<ArrayList<Recipe>> loaderCallbackRecipes = new LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {
        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int i, Bundle bundle) {
            Context context = getApplicationContext();
            return new RecipeLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {

            if (recipes == null) {
                showErrorMessage();
                return;
            }

            if(!recipes.isEmpty()) {
                showRecipeData();
                mRecipe = recipes;
                mRecipeAdapter.setRecipes(mRecipe);
            } else {
                showErrorMessage();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

        }
    };


}
