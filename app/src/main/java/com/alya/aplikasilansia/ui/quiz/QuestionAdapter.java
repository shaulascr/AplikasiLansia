package com.alya.aplikasilansia.ui.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final List<Integer> questions;
    private final Boolean[] answers;
    private final OnQuestionClickListener onQuestionClickListener;

    public interface OnQuestionClickListener {
        void onQuestionClick(int position);
    }

    public QuestionAdapter(List<Integer> questions, Boolean[] answers, OnQuestionClickListener onQuestionClickListener) {
        this.questions = questions;
        this.answers = answers;
        this.onQuestionClickListener = onQuestionClickListener;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        Button buttonQuestion;

        public QuestionViewHolder(View view) {
            super(view);
            buttonQuestion = view.findViewById(R.id.buttonQuestion);
        }
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        int questionNumber = questions.get(position);
        holder.buttonQuestion.setText(String.valueOf(questionNumber));

        Boolean answer = answers[position];
        if (answer != null) {
            if (answer) {
                holder.buttonQuestion.setBackgroundResource(R.drawable.answered_yes_background);
            } else {
                holder.buttonQuestion.setBackgroundResource(R.drawable.answered_no_background);
            }
        } else {
            holder.buttonQuestion.setBackgroundResource(R.drawable.unanswered_background);
        }

        holder.buttonQuestion.setOnClickListener(v -> onQuestionClickListener.onQuestionClick(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}







