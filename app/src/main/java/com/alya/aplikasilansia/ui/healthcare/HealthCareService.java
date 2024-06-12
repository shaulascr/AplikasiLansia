package com.alya.aplikasilansia.ui.healthcare;

public class HealthCareService {
    private String name;
    private String address;

    public HealthCareService(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
