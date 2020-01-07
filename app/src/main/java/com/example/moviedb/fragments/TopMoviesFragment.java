package com.example.moviedb.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.adapters.TopMoviesAdapter;
import com.example.moviedb.models.Result;
import com.example.moviedb.viewmodels.MainViewModel;
import com.example.moviedb.viewmodels.MainViewModelFactory;

public class TopMoviesFragment extends Fragment {

    private Context context;
    private Menu menu;
    private String search_key = "";

    public TopMoviesFragment() {
    }

    public TopMoviesFragment(String search_key) {
        this.search_key = search_key;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        ((MainActivity) context).setTitle("Top Movies");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_top_movies, container, false);

        String sort_criteria = "popular";
        //String sort_criteria = "top_rated";
        MainViewModel viewModel = ViewModelProviders.of(this, new MainViewModelFactory(sort_criteria, search_key)).get(MainViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.rv_top_movies);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final TopMoviesAdapter adapter = new TopMoviesAdapter(context, menu);
        recyclerView.setAdapter(adapter);

        viewModel.getListLiveData().observe(this, new Observer<PagedList<Result>>() {
            @Override
            public void onChanged(PagedList<Result> results) {
                if(results != null){
                    adapter.submitList(results);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, menu);
        //super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;

        MenuItem itemSearch = menu.findItem(R.id.search);
        MenuItem itemClose = menu.findItem(R.id.close);
        itemSearch.setVisible(true);
        itemClose.setVisible(false);

        final SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()){
                    return false;
                }
                searchView.onActionViewCollapsed();
                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_with_menu,new TopMoviesFragment(query));
                frag_trans.commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
