package com.alya.aplikasilansia.ui.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {
    NewsViewModel newsViewModel;
    private CardView headNews;
    private RecyclerView newsRV;
    NewsAdapter adapter;

    public NewsFragment() {
        // Required empty public constructor
    }


    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
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

        newsViewModel.getNewsLiveData().observe(getViewLifecycleOwner(), newsList -> {
            if (newsList != null) {
                adapter.updateList(newsList); // Update the adapter when the data changes
            }
        });

        return view;
    }

    private void newsRecyclerViewList() {
        newsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsAdapter(new ArrayList<>());
        adapter.setOnItemClickListener(this); // Set click listener
        newsRV.setAdapter(adapter);
    }

    @Override
    public void onItemClick(News newsItem) {
        // Handle item click here, navigate to NewsContentActivity with news details
        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
        // Pass necessary data to NewsContentActivity using intent extras
        intent.putExtra("news_name", newsItem.getName());
        intent.putExtra("news_date", newsItem.getDate());
        intent.putExtra("news_category", newsItem.getCategory());
        intent.putExtra("news_source", newsItem.getSource());
        intent.putExtra("news_image", newsItem.getImage().toString()); // Assuming Uri to String conversion
        intent.putExtra("news_content", newsItem.getNewsContent()); // Assuming Uri to String conversion
        startActivity(intent);
    }
}