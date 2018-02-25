package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * This is the Recipe class to store all the recipe information.
 */

public class Recipe implements Parcelable {

    private String mName;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private int mServingSize;
    private String mImage;

    public Recipe(String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servingSize, String image) {

        mName = name;
        mIngredients = new ArrayList<Ingredient>(ingredients);
        mSteps = new ArrayList<Step>(steps);
        mServingSize = servingSize;
        mImage = image;
    }

    private Recipe(Parcel in) {
        this.mName = in.readString();

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        in.readList(ingredients, Ingredient.class.getClassLoader());
        this.mIngredients = new ArrayList<Ingredient>(ingredients);

        ArrayList<Step> steps = new ArrayList<Step>();
        in.readList(steps, Step.class.getClassLoader());
        this.mSteps = new ArrayList<Step>(steps);

        this.mServingSize = in.readInt();
        this.mImage = in.readString();
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public int getServingSize() {
        return mServingSize;
    }

    public String getImage() {
        //For future use if Images are ever added to the recipes.
        return mImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeList(mIngredients);
        parcel.writeList(mSteps);
        parcel.writeInt(mServingSize);
        parcel.writeString(mImage);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
