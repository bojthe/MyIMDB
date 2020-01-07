package com.example.moviedb.api;

import com.example.moviedb.models.ImageResponse;
import com.example.moviedb.models.MovieResponse;
import com.example.moviedb.models.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie/{sort_criteria}")
    Call<MovieResponse> getAllMovies(
            @Path("sort_criteria") String sort_criteria,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MovieResponse> getSearchResults(
            @Query("query") String search_key,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String api_key
    );

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );


    @GET("movie/{movie_id}/images")
    Call<ImageResponse> getImages(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );

    @GET("movie/{movie_id}/similar")
    Call<MovieResponse> getSimilarMovies(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );

}
