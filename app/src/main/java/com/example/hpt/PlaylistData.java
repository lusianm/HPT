package com.example.hpt;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;

import java.util.ArrayList;

public class PlaylistData implements Parcelable {
    int image;
    String part;
    String healthname;
    Button button;

    public PlaylistData(int image, String part, String healthname){
        this.image = image;
        this.part = part;
        this.healthname = healthname;
    }

    protected PlaylistData(Parcel in) {
        image = in.readInt();
        part = in.readString();
        healthname = in.readString();
    }

    public static final Creator<PlaylistData> CREATOR = new Creator<PlaylistData>() {
        @Override
        public PlaylistData createFromParcel(Parcel in) {
            return new PlaylistData(in);
        }

        @Override
        public PlaylistData[] newArray(int size) {
            return new PlaylistData[size];
        }
    };

    public int getImage() {
        return this.image;
    }

    public String getPart() {
        return this.part;
    }

    public String getHealthname() {
        return this.healthname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.image);
        dest.writeString(this.part);
        dest.writeString(this.healthname);
    }
}
