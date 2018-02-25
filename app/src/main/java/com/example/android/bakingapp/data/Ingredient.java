package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is the Ingredient class that is used to store all the ingredients for each recipe.
 */

public class Ingredient implements Parcelable{

    private int mQuantity;
    private String mMeasureType;
    private String mIngredient;

    public Ingredient(int quantity, String measureType, String ingredientName) {

        mQuantity = quantity;
        mMeasureType = measureType;
        mIngredient = ingredientName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasurementType() {
        return mMeasureType;
    }

    public String getIngredient() {
        return mIngredient;
    }

    private Ingredient(Parcel in) {
        this.mQuantity = in.readInt();
        this.mMeasureType = in.readString();
        this.mIngredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mQuantity);
        parcel.writeString(mMeasureType);
        parcel.writeString(mIngredient);

    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }
        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
