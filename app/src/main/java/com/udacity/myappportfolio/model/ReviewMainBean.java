package com.udacity.myappportfolio.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ReviewMainBean implements Parcelable{

    private Integer id;
    private Integer page;
    private List<Review> results = new ArrayList<Review>();
    private Integer total_pages;
    private Integer total_results;

    protected ReviewMainBean(Parcel in) {
        results = in.createTypedArrayList(Review.CREATOR);

        id = in.readInt();
        page = in.readInt();

        total_pages = in.readInt();
        total_results = in.readInt();
    }

    public static final Creator<ReviewMainBean> CREATOR = new Creator<ReviewMainBean>() {
        @Override
        public ReviewMainBean createFromParcel(Parcel in) {
            return new ReviewMainBean(in);
        }

        @Override
        public ReviewMainBean[] newArray(int size) {
            return new ReviewMainBean[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<Review> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Review> results) {
        this.results = results;
    }

    /**
     * @return The total_pages
     */
    public Integer getTotal_pages() {
        return total_pages;
    }

    /**
     * @param total_pages The total_pages
     */
    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    /**
     * @return The total_results
     */
    public Integer getTotal_results() {
        return total_results;
    }

    /**
     * @param total_results The total_results
     */
    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeInt(id);
        dest.writeInt(page);
        dest.writeInt(total_pages);
        dest.writeInt(total_results);
    }
}
