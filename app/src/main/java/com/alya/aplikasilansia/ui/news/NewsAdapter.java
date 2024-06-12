package com.alya.aplikasilansia.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> news;

    public NewsAdapter(List<News> news){
        this.news = news;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Declare a member variable for the click listener
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);

        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News news1 = news.get(position);
        holder.bind(news1);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView newsNameTV;
        private TextView newsDate;
        private TextView newsCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsNameTV = itemView.findViewById(R.id.tv_newsTitle);
            newsCategory = itemView.findViewById(R.id.headNewsCat);
            newsDate = itemView.findViewById(R.id.newsDateList);
        }

        public void bind(News news1) {
            newsNameTV.setText(news1.getName());
            newsCategory.setText(news1.getCategory());
            newsDate.setText(news1.getDate());
        }
    }
}
