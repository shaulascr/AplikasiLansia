package com.alya.aplikasilansia;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.alya.aplikasilansia.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);

    }

}