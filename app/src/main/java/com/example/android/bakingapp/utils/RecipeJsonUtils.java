package com.example.android.bakingapp.utils;

import android.text.TextUtils;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is used to parse the Json data into the Recipe Class
 */

public class RecipeJsonUtils {

    public static ArrayList<Recipe> getRecipeFromJson(String jsonString) throws JSONException {

        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray  recipesArray = new JSONArray(jsonString);

            for (int recipesCntr = 0; recipesCntr < recipesArray.length(); recipesCntr++) {
                JSONObject recipeDetailObj = recipesArray.getJSONObject(recipesCntr);
                String name = recipeDetailObj.getString("name");
                int servingSize = recipeDetailObj.getInt("servings");
                String image = recipeDetailObj.getString("image");

                JSONArray ingredientsArray = recipeDetailObj.getJSONArray("ingredients");
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                for (int ingredientCntr = 0; ingredientCntr < ingredientsArray.length(); ingredientCntr++) {
                    JSONObject ingredientDetailsObj = ingredientsArray.getJSONObject(ingredientCntr);
                    int quantity = ingredientDetailsObj.getInt("quantity");
                    String measurementType = ingredientDetailsObj.getString("measure");
                    String ingredientName = ingredientDetailsObj.getString("ingredient");
                    ingredients.add(new Ingredient(quantity, measurementType, ingredientName));
                }

                JSONArray stepsArray = recipeDetailObj.getJSONArray("steps");
                ArrayList<Step> steps = new ArrayList<>();
                for (int stepsCntr = 0; stepsCntr < stepsArray.length(); stepsCntr++) {
                    JSONObject stepsDetailsObj = stepsArray.getJSONObject(stepsCntr);
                    int orderNum = stepsDetailsObj.getInt("id");
                    String shortDescription = stepsDetailsObj.getString("shortDescription");
                    String description = stepsDetailsObj.getString("description");
                    String videoURL = stepsDetailsObj.getString("videoURL");
                    String thumbNameURL = stepsDetailsObj.getString("thumbnailURL");
                    steps.add(new Step(orderNum, shortDescription, description, videoURL, thumbNameURL));
                }

                recipes.add(new Recipe(name, ingredients, steps, servingSize, image));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;

    }
}
