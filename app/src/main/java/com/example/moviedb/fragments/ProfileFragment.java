package com.example.moviedb.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviedb.DbHelper;
import com.example.moviedb.MainActivity;
import com.example.moviedb.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ProfileFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        ((MainActivity) context).setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView et_userName = view.findViewById(R.id.et_userName);

        Button btn_changePassword = view.findViewById(R.id.btn_changePassword);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String oldUsername = sharedPref.getString(getString(R.string.active_user),"Active User");
        et_userName.setText(oldUsername);


        ImageView iv_profilePicture = view.findViewById(R.id.img_profilePicture);

        DbHelper db = new DbHelper(context);
        String imagePath = db.getProfilePicture(oldUsername);
        if(!imagePath.equals("")){
            try {
                File f=new File(imagePath);
                Bitmap selectedImage = BitmapFactory.decodeStream(new FileInputStream(f));
                iv_profilePicture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("hiba", e.getMessage());
            }
        }
        else{
            Log.d("hiba", "nincs kep");
        }

        iv_profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                ((MainActivity) context).startActivityForResult(photoPickerIntent, 1);
            }
        });



        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu,new ChangePasswordFragment());
                frag_trans.commit();
            }
        });



        return view;
    }

}
