package com.example.moviedb.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.moviedb.data.MovieDataSourceFactory;
import com.example.moviedb.models.Result;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.moviedb.constant.Constant.NUMBER_OF_THREAD_FIVE;
import static com.example.moviedb.constant.Constant.PAGE_LOAD_SIZE_HINT;
import static com.example.moviedb.constant.Constant.PAGE_SIZE;
import static com.example.moviedb.constant.Constant.Prefetch_Distance;

public class MainViewModel extends ViewModel {

    private String sort_criteria, search_key;
    private LiveData<PagedList<Result>> listLiveData;

    public MainViewModel(String sort_criteria, String search_key) {
        this.sort_criteria = sort_criteria;
        this.search_key = search_key;

        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD_FIVE);

        MovieDataSourceFactory sourceFactory = new MovieDataSourceFactory(sort_criteria, search_key);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(PAGE_LOAD_SIZE_HINT)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(Prefetch_Distance)
                .build();

        listLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Result>> getListLiveData() {
        return listLiveData;
    }
}
