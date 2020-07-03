package com.example.hpt;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaylistData implements Parcelable {
    String playlistname;

    public PlaylistData(String playlistname){
        this.playlistname = playlistname;
    }

    protected PlaylistData(Parcel in) {
        playlistname = in.readString();
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

    public String getPlaylistname() {
        return this.playlistname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playlistname);
    }
}
