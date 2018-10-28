package com.example.android.bakingapp.room;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

public final class Step implements Parcelable {
    public int id;

    @ColumnInfo(name = "short_description")
    public String shortDescription;

    public String description;

    @ColumnInfo(name = "video_url")
    public String videoURL;

    @ColumnInfo(name = "thumbnail_url")
    public String thumbnailURL;

    private Step(Parcel parcel) {
        id = parcel.readInt();
        shortDescription = parcel.readString();
        description = parcel.readString();
        videoURL = parcel.readString();
        thumbnailURL = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}