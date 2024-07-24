package com.alya.aplikasilansia.ui.quiz;

import com.alya.aplikasilansia.data.Question;

public class ItemQuestionModel {
    private int id;
    private boolean isQuestionAnswered;
    private Question questionData;

    public ItemQuestionModel(int id, boolean isQuestionAnswered, Question questionData) {
        this.id = id;
        this.isQuestionAnswered = isQuestionAnswered;
        this.questionData = questionData;
    }

    public boolean isQuestionAnswered() {
        return isQuestionAnswered;
    }

    public void setQuestionAnswered(boolean questionAnswered) {
        isQuestionAnswered = questionAnswered;
    }

    public Question getQuestionData() {
        return questionData;
    }

    public void setQuestionData(Question questionData) {
        this.questionData = questionData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // Getter for answered status

}
