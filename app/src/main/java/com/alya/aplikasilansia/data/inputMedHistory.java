package com.alya.aplikasilansia.data;

public class inputMedHistory {
    public String penyakit;

    public String getPenyakit() {
        return penyakit;
    }

    public String getLamanya() {
        return lamanya;
    }

    public String getLamanyaBulan() {
        return lamanyaBulan;
    }

    public String lamanya;
    public String lamanyaBulan;

    public inputMedHistory() {
        // Default constructor required by Firebase
    }

    public inputMedHistory (String penyakit, String lamanya, String lamanyaBulan) {
        this.penyakit = penyakit;
        this.lamanya = lamanya;
        this.lamanyaBulan = lamanyaBulan;
    }
}
