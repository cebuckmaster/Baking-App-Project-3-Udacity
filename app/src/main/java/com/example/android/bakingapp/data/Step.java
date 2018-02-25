package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is the Step class that stores all the steps for an individual recipe.
 */

public class Step implements Parcelable {

    private int mStepNum;
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;

    public Step(int stepNum, String shortDescription, String description, String videoURL, String thumbnameURL) {

        mStepNum = stepNum;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL = thumbnameURL;
    }

    private Step(Parcel in) {
        this.mStepNum = in.readInt();
        this.mShortDescription = in.readString();
        this.mDescription = in.readString();
        this.mVideoURL = in.readString();
        this.mThumbnailURL = in.readString();
    }

    public int getStepNum() {
        return mStepNum;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoURL() {
        return mVideoURL;
    }

    public String getThumbNameURL() {
        return mThumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mStepNum);
        parcel.writeString(mShortDescription);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoURL);
        parcel.writeString(mThumbnailURL);
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {

        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }
        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };


}
