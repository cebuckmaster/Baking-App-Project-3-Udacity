package com.example.android.bakingapp.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.MainBakingActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.FavRecipeDBContract;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the RecipeFragment class that implements an OnClickHandler for the Step RecyclerView.
 */

public class RecipeFragment extends Fragment implements StepAdapter.StepAdapaterOnClickHandler {

    private static final String TAG = RecipeFragment.class.getSimpleName();
;


    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    @BindView(R.id.rv_recipe_ingredients) RecyclerView mrvIngredients;
    @BindView(R.id.rv_recipe_steps) RecyclerView mrvSteps;
    @BindView(R.id.tv_detail_recipe_name) TextView mRecipeName;
    @BindView(R.id.chkbox_working_recipe) CheckBox mWorkingRecipeCheckbox;
    @BindView(R.id.iv_recipe_image) ImageView mRecipeImage;
    @BindView(R.id.recipe_image_divider) View mRecipeImageDivider;


    OnRecipeStepClickListener mCallback;

    public interface OnRecipeStepClickListener {
        void OnRecipeStepSelected(int position);
    }

    //required constructor
    public RecipeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Recipe")) {
                mRecipe = savedInstanceState.getParcelable("Recipe");
            }
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //SharedPreferneces is used to store the Recipe name that is selected as the "Working Recipe"
        String savedRecipeName = preferences.getString(MainBakingActivity.RECIPE_NAME, null);
        if (savedRecipeName != null && savedRecipeName.equals(mRecipe.getName())) {
            mWorkingRecipeCheckbox.toggle();
        }

        final SharedPreferences.Editor prefEditor = preferences.edit();

        mRecipeName.setText(mRecipe.getName()+" Ingredients");
        mIngredients = mRecipe.getIngredients();

        mWorkingRecipeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWorkingRecipeCheckbox.isChecked()) {
                    prefEditor.putString(MainBakingActivity.RECIPE_NAME, mRecipe.getName());
                    prefEditor.apply();
                    //when the checkbox is clicked for the "Working Recipe" this deletes any old ingredents and adds the current to the DB
                    // to be displayed in the widget
                    deleteDBRecipeIngredients();
                    addDBRecipeIngredients();
                } else {
                    //this removes any ingredients from the DB if the "Working Recipe" is unchecked.
                    prefEditor.putString(MainBakingActivity.RECIPE_NAME, null);
                    prefEditor.apply();
                    deleteDBRecipeIngredients();
                }
            }
        });

        if (!TextUtils.isEmpty(mRecipe.getImage())) {
            mRecipeImage.setVisibility(View.VISIBLE);
            mRecipeImageDivider.setVisibility(View.VISIBLE);
            Picasso.with(view.getContext()).load(mRecipe.getImage()).into(mRecipeImage);
        } else {
            mRecipeImage.setVisibility(View.GONE);
            mRecipeImageDivider.setVisibility(View.GONE);
        }



        IngredientAdapter ingredientAdapter = new IngredientAdapter(view.getContext(), mIngredients);

        mrvIngredients.setAdapter(ingredientAdapter);
        mrvIngredients.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mSteps = mRecipe.getSteps();

        StepAdapter stepAdapter = new StepAdapter(view.getContext(), this);
        stepAdapter.setSteps(mSteps);

        mrvSteps.setAdapter(stepAdapter);
        mrvSteps.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("Recipe", mRecipe);
    }


    public void addDBRecipeIngredients() {

        ContentValues values = new ContentValues();
        Uri insertUri = null;

        for (int cntr = 0; cntr < mRecipe.getIngredients().size(); cntr++) {
            values.put(FavRecipeDBContract.FavRecipeDBEntry.COLUMN_QUANTITY, mRecipe.getIngredients().get(cntr).getQuantity());
            values.put(FavRecipeDBContract.FavRecipeDBEntry.COLUMN_MEASUREMENTTYPE, mRecipe.getIngredients().get(cntr).getMeasurementType());
            values.put(FavRecipeDBContract.FavRecipeDBEntry.COLUMN_INGREDIENT, mRecipe.getIngredients().get(cntr).getIngredient());
            values.put(FavRecipeDBContract.FavRecipeDBEntry.COLUMN_RECIPE_NAME, mRecipe.getName());
            insertUri = getContext().getContentResolver().insert(FavRecipeDBContract.FavRecipeDBEntry.CONTENT_URI, values);
        }
        if (insertUri == null) {
            Log.d(TAG, "Failed to insert recipe ingredients into database");
        } else {
            Log.d(TAG,"Successfully inserted ingredients into the database");
        }
    }

    public void deleteDBRecipeIngredients() {
        Uri deleteRecipe = FavRecipeDBContract.FavRecipeDBEntry.CONTENT_URI;
        int rowsDeleted = getContext().getContentResolver().delete(deleteRecipe, null, null);
        if (rowsDeleted == 0) {
            Log.d(TAG, "No ingredients found in the database to delete");
        } else {
            Log.d(TAG, "Total number of rows delete - " + rowsDeleted);
        }
    }

    @Override
    public void onClick(int postion) {
        //when a step is clicked we send the click back to the Recipe activity using the callback.
        //this will allow us to decide if we should update a StepDetailFragement with the new step or call an Intent for the StepDetailsActivity
        mCallback.OnRecipeStepSelected(postion);
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

}
