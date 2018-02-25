package com.example.android.bakingapp;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;

import com.example.android.bakingapp.fragments.StepDetailFragment;

/**
 * This is the Step Detail Activity used to display the Step Detail Fragment in a single panel phone.
 */
public class StepDetailsActivity extends AppCompatActivity {

    private ArrayList<Step> mSteps;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("parcel_data_steps")) {
            mSteps = intentThatStartedThisActivity.getParcelableArrayListExtra("parcel_data_steps");
            mPosition = intentThatStartedThisActivity.getIntExtra("int_current_step", 0);
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            final StepDetailFragment stepDetailFragment = new StepDetailFragment();

            stepDetailFragment.setCurrentStep(mSteps.get(mPosition));
            stepDetailFragment.setSteps(mSteps);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_container_single_pane, stepDetailFragment)
                    .commit();
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
}
