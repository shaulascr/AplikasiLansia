package com.alya.aplikasilansia.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.alya.aplikasilansia.LoginActivity;
import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.ui.quiz.QuizActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CheckFragment extends Fragment {
    private Button btnCheck;
    private FirebaseAuth mAuth;

    public CheckFragment() {
        // Required empty public constructor
    }
    public static CheckFragment newInstance(String param1, String param2) {
        CheckFragment fragment = new CheckFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            // User is not logged in, redirect to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            // Optionally, close the current activity
//            getActivity().finish();
        } else {
            // User is logged in, proceed with loading the fragment
            btnCheck = view.findViewById(R.id.btn_mulai_tes);
            btnCheck.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), QuizActivity.class);
                    startActivity(intent);
                }
            });
        }
        return view;
    }
}