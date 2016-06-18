package com.tonyostudio.popularmovies.model;

import java.util.List;

/**
 * Created by tonyofrancis on 4/8/16.
 */
public class ReviewResults {

    private int id;
    private int page;
    private List<Review> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
