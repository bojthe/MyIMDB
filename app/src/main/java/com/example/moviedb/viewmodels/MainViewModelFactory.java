package com.example.moviedb.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String sort_criteria, search_key;

    public MainViewModelFactory(String sort_criteria, String search_key) {
        this.sort_criteria = sort_criteria;
        this.search_key = search_key;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(sort_criteria, search_key);
    }
}
