package com.alya.aplikasilansia.ui.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CardView headNews;
    private RecyclerView newsRV;

    public NewsFragment() {
        // Required empty public constructor
    }


    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        headNews = view.findViewById(R.id.headNews);
        newsRV = view.findViewById(R.id.rv_news_list);

        headNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsContentActivity.class);

                // Start the Activity
                startActivity(intent);
            }
        });

        newsRecyclerViewList();

        return view;
    }

    private void newsRecyclerViewList() {
        // Create a list of News objects
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("5 Cara Untuk Hidup Lebih Sehat","26-05-2034","Tips Kesehatan"));
        newsList.add(new News("5 Cara Untuk Hidup Lebih Sehat","26-05-2034","Tips Kesehatan"));
        newsList.add(new News("5 Cara Untuk Hidup Lebih Sehat","26-05-2034","Tips Kesehatan"));

        // Create an adapter for the RecyclerView and pass the list of News objects
        NewsAdapter adapter = new NewsAdapter(newsList);
        newsRV.setAdapter(adapter);

        newsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //
            }
        });
    }
}