package com.myapplication.Model.DataRepresentation;


import java.util.List;


public class MovieListShortInfo {
    private List<MovieShortInfo> result;

    private int numberOfPages;

    private int page;

    public MovieListShortInfo(List<MovieShortInfo> result,int numberOfPages, int page) {
        this.page = page;
        this.numberOfPages = numberOfPages;
        this.result = result;
    }


    public List<MovieShortInfo> getResult() {
        return result;
    }

    public int getPage() {
        return page;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }
}
