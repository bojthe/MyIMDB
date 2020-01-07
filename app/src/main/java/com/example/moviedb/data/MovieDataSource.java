package com.example.moviedb.data;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.moviedb.api.ApiClient;
import com.example.moviedb.models.MovieResponse;
import com.example.moviedb.models.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviedb.constant.Constant.API_KEY;
import static com.example.moviedb.constant.Constant.LANGUAGE;
import static com.example.moviedb.constant.Constant.PAGE_ONE;
import static com.example.moviedb.constant.Constant.PREVIOUS_PAGE_KEY_ONE;
import static com.example.moviedb.constant.Constant.PREVIOUS_PAGE_KEY_TWO;

public class MovieDataSource extends PageKeyedDataSource<Integer, Result> {

    private String sort_criteria, search_key = "";

    /*public MovieDataSource(String sort_criteria) {
        this.sort_criteria = sort_criteria;
    }*/

    public MovieDataSource(String sort_criteria, String search_key) {
        this.sort_criteria = sort_criteria;
        this.search_key = search_key;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Result> callback) {
        if(search_key.isEmpty()) {
            ApiClient.getInstance().getApiService().getAllMovies(sort_criteria, API_KEY, LANGUAGE, PAGE_ONE)
                    .enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            if (response.isSuccessful()) {
                                callback.onResult(response.body().getResults(),
                                        PREVIOUS_PAGE_KEY_ONE, PREVIOUS_PAGE_KEY_TWO);
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {

                        }
                    });
        }
        else{
            ApiClient.getInstance().getApiService().getSearchResults(search_key, API_KEY, LANGUAGE, PAGE_ONE)
                    .enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            if (response.isSuccessful()) {
                                callback.onResult(response.body().getResults(),
                                        PREVIOUS_PAGE_KEY_ONE, PREVIOUS_PAGE_KEY_TWO);
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {

                        }
                    });
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Result> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Result> callback) {
        final int currentPage = params.key;
        if(search_key.isEmpty()) {
            ApiClient.getInstance().getApiService().getAllMovies(sort_criteria, API_KEY, LANGUAGE, currentPage)
                    .enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            if (response.isSuccessful()) {
                                int nextPage = currentPage + 1;
                                callback.onResult(response.body().getResults(), nextPage);
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {

                        }
                    });
        }
        else{
            ApiClient.getInstance().getApiService().getSearchResults(search_key, API_KEY, LANGUAGE, currentPage)
                    .enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            if (response.isSuccessful()) {
                                int nextPage = currentPage + 1;
                                callback.onResult(response.body().getResults(), nextPage);
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {

                        }
                    });
        }
    }
}
