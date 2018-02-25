package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



/**
 * Used to test the initial MainBaking Activity is calling the first Recipe and displays the Recipe details.
 */
@RunWith(AndroidJUnit4.class)
public class MainBackingActivityScreenTest {

    public static final String RECIPE_NAME = "Nutella Pie Ingredients";

    @Rule public ActivityTestRule<MainBakingActivity> mRecipeTestRule = new ActivityTestRule<MainBakingActivity>(MainBakingActivity.class);

    @Test
    public void clickRecyclerviewItem_OpenRecipeDetailsActivity() {

        onView(withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.tv_detail_recipe_name)).check(matches(withText(RECIPE_NAME)));


    }


}
