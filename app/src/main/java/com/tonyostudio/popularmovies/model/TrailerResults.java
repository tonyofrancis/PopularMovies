package com.tonyostudio.popularmovies.model;

import java.util.List;

/**
 * Created by tonyofrancis on 4/7/16.
 */
public class TrailerResults {

    private int id;
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
