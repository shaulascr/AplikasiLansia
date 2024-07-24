package com.alya.aplikasilansia.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.LoginActivity;
import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.QuizHistoryItem;
import com.alya.aplikasilansia.ui.quiz.QuizActivity;
import com.alya.aplikasilansia.ui.quiz.QuizResultAdapter;
import com.alya.aplikasilansia.ui.quiz.QuizViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CheckFragment extends Fragment {
    private Button btnCheck;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private QuizResultAdapter adapter;
    private QuizViewModel quizViewModel;
    private String userId;

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
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid(); // Store userId
        }
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
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), QuizActivity.class);
                    startActivity(intent);
                }
            });

            recyclerView = view.findViewById(R.id.rv_check);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            adapter = new QuizResultAdapter(getContext(), new ArrayList<>());
            recyclerView.setAdapter(adapter);

            quizViewModel.getQuizHistoryLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizHistoryItem>>() {
                @Override
                public void onChanged(List<QuizHistoryItem> quizHistoryItems) {
                    adapter.setQuizHistoryItems(quizHistoryItems);
                }

            });

            if (userId != null) {
                quizViewModel.fetchQuizHistory(userId);
            }
        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Refresh the quiz history data
        if (userId != null) {
            quizViewModel.fetchQuizHistory(userId);
        }
    }

}