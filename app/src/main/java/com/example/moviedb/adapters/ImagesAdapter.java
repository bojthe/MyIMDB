package com.example.moviedb.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.example.moviedb.constant.Constant.IMAGE_SIZE;
import static com.example.moviedb.constant.Constant.IMAGE_URL;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder>{
    private List<ImageResult> images;
    private Context context;
    private View rootView;
    private String type;

    public ImagesAdapter(List<ImageResult> images, Context context, View rootView) {
        this.images = images;
        this.context = context;
        this.rootView = rootView;
        this.type = "internet";
    }

    public ImagesAdapter(List<ImageResult> images, Context context, View rootView, String type) {
        this.images = images;
        this.context = context;
        this.rootView = rootView;
        this.type = type;
    }

    @NonNull
    @Override
    public ImagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_rv_images, viewGroup, false);
        return new ImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImagesAdapter.MyViewHolder viewHolder, int i) {
        final ImageResult image = images.get(i);

        if(type.equals("internet")) {
            String imageUrl = IMAGE_URL + IMAGE_SIZE + image.getFilePath();
            Picasso.get().load(imageUrl).into(viewHolder.image);
        }else{
            try {
                String imageUrl = image.getFilePath();
                File f=new File(imageUrl);
                Bitmap selectedImage = BitmapFactory.decodeStream(new FileInputStream(f));
                viewHolder.image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("hiba", e.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById((R.id.image));
        }
    }
}
