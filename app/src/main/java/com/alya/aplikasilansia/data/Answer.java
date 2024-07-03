package com.alya.aplikasilansia.data;

public class Answer {
    private int questionIndex;
    private boolean answer;
    private int score;

    public Answer(int questionIndex, boolean answer, int score) {
        this.questionIndex = questionIndex;
        this.answer = answer;
        this.score = score;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public boolean isAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }
}
