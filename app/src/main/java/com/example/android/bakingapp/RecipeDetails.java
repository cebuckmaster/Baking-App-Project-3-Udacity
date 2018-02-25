package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.bakingapp.data.Recipe;

import com.example.android.bakingapp.fragments.RecipeFragment;
import com.example.android.bakingapp.fragments.StepDetailFragment;

/**
 *  This is the Details Activity for Each Recipe.  This will use either a phone or a tablet Layout file depending on screen size.
 */
public class RecipeDetails extends AppCompatActivity implements RecipeFragment.OnRecipeStepClickListener {

    private static final String TAG = RecipeDetails.class.getSimpleName();

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("parcel_data")) {
            mRecipe = (Recipe) intentThatStartedThisActivity.getParcelableExtra("parcel_data");
        }


        mTwoPane = getResources().getBoolean(R.bool.is_two_pane);

        if (mTwoPane) {
            // for two panel layouts in Tablets it will use Fragments to display both the Recipe and the Steps
            if (savedInstanceState == null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                RecipeFragment recipeFragment = new RecipeFragment();
                recipeFragment.setRecipe(mRecipe);
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_container, recipeFragment)
                        .commit();

                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setCurrentStep(mRecipe.getSteps().get(0));
                fragmentManager.beginTransaction()
                        .add(R.id.step_container, stepDetailFragment)
                        .commit();
            }
        } else {
            // for single panel layouts it will only display the Recipe Fragment
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                RecipeFragment recipeFragment = new RecipeFragment();
                recipeFragment.setRecipe(mRecipe);
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_container, recipeFragment)
                        .commit();
            }
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void OnRecipeStepSelected(final int position) {

        if (mTwoPane) {
            //for two panel layout it will expand the step into the step fragment that was selected.
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setCurrentStep(mRecipe.getSteps().get(position));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, stepDetailFragment)
                    .commit();
        } else {
            //for single panel layout it will use an intent to call the StepDetailsActivity.class and pass the recipe steps
            Context context = this;
            Intent intentShowStepDetails = new Intent(context, StepDetailsActivity.class);
            intentShowStepDetails.putParcelableArrayListExtra("parcel_data_steps", mRecipe.getSteps());
            intentShowStepDetails.putExtra("int_current_step", position);
            startActivity(intentShowStepDetails);
        }
    }

}
