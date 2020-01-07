package com.example.moviedb.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.DbHelper;
import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.adapters.FavouriteMoviesAdapter;
import com.example.moviedb.models.Result;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        ((MainActivity) context).setTitle("Favourites");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favourites, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_favourites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String username = sharedPref.getString(context.getString(R.string.active_user),"Active User");

        DbHelper db = new DbHelper(context);
        ArrayList<Result> favourites = db.getFavourites(username);

        FavouriteMoviesAdapter adapter = new FavouriteMoviesAdapter(favourites, context);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
