package com.alya.aplikasilansia.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.QuizHistoryItem;
import com.alya.aplikasilansia.ui.quizResult.QuizResultActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class QuizResultAdapter extends RecyclerView.Adapter<QuizResultAdapter.QuizResultViewHolder> {
    private List<QuizHistoryItem> quizHistoryItems;
    private Context context;
    public QuizResultAdapter(Context context, List<QuizHistoryItem> quizHistoryItems) {
        this.context = context;
        this.quizHistoryItems = quizHistoryItems;
    }
    public void setQuizHistoryItems(List<QuizHistoryItem> quizHistoryItems) {
        this.quizHistoryItems = quizHistoryItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizResultAdapter.QuizResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cek_history, parent, false);
        return new QuizResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizResultAdapter.QuizResultViewHolder holder, int position) {
        QuizHistoryItem item = quizHistoryItems.get(position);
        int totalScore = item.getTotalScore();
        int maxScore = 15;
        int percentage = (int) ((totalScore / (float) maxScore) * 100);

        holder.statsProgressBar.setProgress(percentage);
        holder.numberScoreTextView.setText(String.valueOf(percentage + "%"));
        holder.testScoreTextView.setText(String.valueOf("Tingkat depresi Anda adalah : "+ item.getClassifiedScore()));
        holder.dateTestTextView.setText(item.getDate());
        holder.detailTestButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizResultActivity.class);
            intent.putExtra("classified_score", item.getClassifiedScore());
            intent.putExtra("dateQuiz", item.getDate());
            intent.putExtra("total_score", item.getTotalScore());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return quizHistoryItems.size();
    }

    static class QuizResultViewHolder extends RecyclerView.ViewHolder {
        TextView testScoreTextView;
        TextView dateTestTextView;
        MaterialButton detailTestButton;
        ProgressBar statsProgressBar;
        TextView numberScoreTextView;


        public QuizResultViewHolder(@NonNull View itemView) {
            super(itemView);
            testScoreTextView = itemView.findViewById(R.id.tv_testScore);
            numberScoreTextView = itemView.findViewById(R.id.number_score_kecil);
            dateTestTextView = itemView.findViewById(R.id.tv_dateTest);
            detailTestButton = itemView.findViewById(R.id.btn_detailTest);
            statsProgressBar = itemView.findViewById(R.id.stats_progressbar_kecil);

        }
    }
}
