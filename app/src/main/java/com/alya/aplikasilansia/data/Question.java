package com.alya.aplikasilansia.data;

public class Question {

    private String text;
    private boolean correctAnswer;
    private int score;

    public Question() {
        // Default constructor required for calls to DataSnapshot.getValue(Question.class)
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public String toStrings() {
        return "Question{" +
                "text='" + text + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", score=" + score +
                '}';
    }
}





