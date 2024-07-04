package com.alya.aplikasilansia.ui.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private final OnQuestionClickListener onQuestionClickListener;

    public interface OnQuestionClickListener {
        void onQuestionClick(int position);
    }

    public QuestionAdapter(List<Question> questions, OnQuestionClickListener onQuestionClickListener) {
        this.questions = questions;
        this.onQuestionClickListener = onQuestionClickListener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_item, parent, false);
        return new QuestionViewHolder(view, onQuestionClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.buttonQuestion.setText(String.valueOf(position + 1)); // This line sets the question number
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button buttonQuestion;
        OnQuestionClickListener onQuestionClickListener;

        public QuestionViewHolder(@NonNull View itemView, OnQuestionClickListener onQuestionClickListener) {
            super(itemView);
            buttonQuestion = itemView.findViewById(R.id.buttonQuestion);
            this.onQuestionClickListener = onQuestionClickListener;
            buttonQuestion.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuestionClickListener.onQuestionClick(getAdapterPosition());
        }
    }
}