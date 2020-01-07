package com.example.moviedb.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moviedb.DbHelper;
import com.example.moviedb.MainActivity;
import com.example.moviedb.R;

public class RegisterFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        final EditText et_userName = view.findViewById(R.id.et_username);
        final EditText et_password = view.findViewById(R.id.et_password);
        final EditText et_confirmPassword = view.findViewById(R.id.et_confirm_password);
        Button btn_register = view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_userName.getText().toString();
                if(userName.isEmpty()){
                    et_userName.setError("Please enter a username");
                    return;
                }

                String password = et_password.getText().toString();
                if(password.isEmpty()){
                    et_password.setError("Please enter a password");
                    return;
                }

                String confirmPassword = et_confirmPassword.getText().toString();
                if(confirmPassword.isEmpty()){
                    et_confirmPassword.setError("Please repeat password");
                    return;
                }

                if(!password.equals(confirmPassword)){
                    et_confirmPassword.setError("Does not match previous password");
                    return;
                }

                DbHelper db = new DbHelper(context);
                if(!db.insertUser(userName,password)){
                    Toast.makeText(context, "Cannot insert new user", Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.active_user), userName);
                editor.apply();

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu,new ContainerWithMenuFragment());
                frag_trans.commit();
            }
        });


        return view;
    }

}
