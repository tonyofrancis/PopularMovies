package com.tonyostudio.popularmovies.model;

import java.util.List;

/**
 * Created by tonyofrancis on 3/22/16.
 */
public class MovieResults {

    private int page;
    private List<Movie> results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
