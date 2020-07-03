package com.example.hpt;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainingData implements Parcelable {
    int image;
    String part;
    String healthname;

    public TrainingData(int image, String part, String healthname){
        this.image = image;
        this.part = part;
        this.healthname = healthname;
    }

    protected TrainingData(Parcel in) {
        image = in.readInt();
        part = in.readString();
        healthname = in.readString();
    }

    public static final Creator<TrainingData> CREATOR = new Creator<TrainingData>() {
        @Override
        public TrainingData createFromParcel(Parcel in) {
            return new TrainingData(in);
        }

        @Override
        public TrainingData[] newArray(int size) {
            return new TrainingData[size];
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
