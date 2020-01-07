package com.example.moviedb.fragments;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContainerWithMenuFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_container_with_menu, container, false);

        final BottomNavigationView bottomNavBar = view.findViewById(R.id.bottomNavBar);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_top_movies: {
                        bottomNavBar.getMenu().getItem(0).setChecked(true);
                        FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container_with_menu,new TopMoviesFragment());
                        frag_trans.commit();
                        break;
                    }
                    case R.id.action_favourites: {
                        bottomNavBar.getMenu().getItem(1).setChecked(true);
                        FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container_with_menu,new FavouritesFragment());
                        frag_trans.commit();
                        break;
                    }
                    case R.id.action_profile: {
                        bottomNavBar.getMenu().getItem(2).setChecked(true);
                        FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container_with_menu,new ProfileFragment());
                        frag_trans.commit();
                        break;
                    }
                }
                return true;
            }
        });

        if(getArguments() != null) {
            String fragmentToOpen = getArguments().getString("fragmentToOpen");
            switch (fragmentToOpen){
                case "profile": {
                    bottomNavBar.setSelectedItemId(R.id.action_profile);
                    break;
                }
                case "favourites": {
                    bottomNavBar.setSelectedItemId(R.id.action_favourites);
                    break;
                }
                case "top_movies": {
                    bottomNavBar.setSelectedItemId(R.id.action_top_movies);
                    break;
                }
            }
        }
        else {
            bottomNavBar.setSelectedItemId(R.id.action_top_movies);
        }

        final ConstraintLayout constraintLayout = view.findViewById(R.id.rootView);
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    bottomNavBar.setVisibility(View.GONE);
                } else {
                    bottomNavBar.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

}
