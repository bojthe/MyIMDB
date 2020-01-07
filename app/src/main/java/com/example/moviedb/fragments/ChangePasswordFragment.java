package com.example.moviedb.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviedb.DbHelper;
import com.example.moviedb.MainActivity;
import com.example.moviedb.R;

public class ChangePasswordFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String userName = sharedPref.getString(getString(R.string.active_user),"Active User");

        final EditText et_password = view.findViewById(R.id.et_password);
        final EditText et_confirmPassword = view.findViewById(R.id.et_confirm_password);
        Button btn_savePassword = view.findViewById(R.id.btn_savePassword);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if(!db.changePassword(userName, password)){
                    Toast.makeText(context, "Cannot update password", Toast.LENGTH_LONG).show();
                    return;
                }

                ContainerWithMenuFragment containerWithMenuFragment = new ContainerWithMenuFragment();
                Bundle args = new Bundle();
                args.putString("fragmentToOpen", "profile");
                containerWithMenuFragment.setArguments(args);

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu, containerWithMenuFragment);
                frag_trans.commit();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerWithMenuFragment containerWithMenuFragment = new ContainerWithMenuFragment();
                Bundle args = new Bundle();
                args.putString("fragmentToOpen", "profile");
                containerWithMenuFragment.setArguments(args);

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu, containerWithMenuFragment);
                frag_trans.commit();
            }
        });

        return view;
    }



}
