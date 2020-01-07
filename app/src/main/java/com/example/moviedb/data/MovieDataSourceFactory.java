package com.example.moviedb.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.moviedb.models.Result;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Result> {

    private String sort_criteria, search_key;
    private MutableLiveData<MovieDataSource> liveData;
    private MovieDataSource dataSource;

    public MovieDataSourceFactory(String sort_criteria, String search_key) {
        this.sort_criteria = sort_criteria;
        this.search_key = search_key;
        liveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, Result> create() {
        dataSource = new MovieDataSource(sort_criteria, search_key);
        liveData = new MutableLiveData<>();
        liveData.postValue(dataSource);
        return dataSource;
    }
}
