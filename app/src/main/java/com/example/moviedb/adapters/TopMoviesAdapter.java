package com.example.moviedb.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
//import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.DbHelper;
import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.databinding.ItemsRvTopMoviesBinding;
import com.example.moviedb.fragments.MovieDetailsFragment;
import com.example.moviedb.models.Result;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.moviedb.constant.Constant.IMAGE_SIZE;
import static com.example.moviedb.constant.Constant.IMAGE_URL;

public class TopMoviesAdapter extends PagedListAdapter<Result, TopMoviesAdapter.MViewModel> {

    private Context context;
    private DbHelper db;
    private Menu menu;

    public static DiffUtil.ItemCallback<Result> diffCallback = new DiffUtil.ItemCallback<Result>() {
        @Override
        public boolean areItemsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem.equals(newItem);
        }
    };

    public TopMoviesAdapter(Context context, Menu menu) {
        super(diffCallback);
        this.context = context;
        this.menu = menu;
        db = new DbHelper(context);
    }

    @NonNull
    @Override
    public MViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemsRvTopMoviesBinding itemsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.items_rv_top_movies, parent, false
        );
        return new MViewModel(itemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewModel holder, final int position) {
        holder.bind(getItem(position), position + 1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu != null) {
                    MenuItem itemSearch = menu.findItem(R.id.search);
                    SearchView searchView = (SearchView) itemSearch.getActionView();
                    searchView.clearFocus();
                    searchView.onActionViewCollapsed();
                }

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu, new MovieDetailsFragment(getItem(position), "Internet"));
                frag_trans.commit();
            }
        });
    }

    public class MViewModel extends RecyclerView.ViewHolder {
        private ItemsRvTopMoviesBinding itemsBinding;
        public MViewModel(ItemsRvTopMoviesBinding videoItemsBinding) {
            super(videoItemsBinding.getRoot());
            itemsBinding = videoItemsBinding;
        }

        public void bind(final Result item, int nr) {
            if(item != null){
                String posterUrl = IMAGE_URL + IMAGE_SIZE + item.getBackdropPath();
                Picasso.get().load(posterUrl).into(itemsBinding.ivPoster);

                String title =  nr + ". " + item.getTitle();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
                try {
                    if(item.getReleaseDate() == null){
                        Log.d("HIBA","exception null");
                        return;
                    }
                    Date date = simpleDateFormat.parse(item.getReleaseDate());
                    title += " (" + simpleDateFormat.format(date) + ")";
                } catch (ParseException ignored) {

                }
                itemsBinding.tvTitle.setText(title);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                final String username = sharedPref.getString(context.getString(R.string.active_user),"Active User");

            }
        }
    }
}
