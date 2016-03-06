package com.udacity.myappportfolio.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TrailerMainBean implements Parcelable {

    private Integer id;
    private List<Trailer> results = new ArrayList<Trailer>();

    protected TrailerMainBean(Parcel in) {

        id = in.readInt();
        results = in.createTypedArrayList(Trailer.CREATOR);
    }

    public static final Creator<TrailerMainBean> CREATOR = new Creator<TrailerMainBean>() {
        @Override
        public TrailerMainBean createFromParcel(Parcel in) {
            return new TrailerMainBean(in);
        }

        @Override
        public TrailerMainBean[] newArray(int size) {
            return new TrailerMainBean[size];
        }
    };

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<Trailer> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(results);

    }
}
