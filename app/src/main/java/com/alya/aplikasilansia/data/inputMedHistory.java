package com.alya.aplikasilansia.data;

public class inputMedHistory {
    public String penyakit;
    public String lamanya;

    public inputMedHistory() {
        // Default constructor required by Firebase
    }

    public inputMedHistory (String penyakit, String lamanya) {
        this.penyakit = penyakit;
        this.lamanya = lamanya;
    }
}