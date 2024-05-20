package com.alya.aplikasilansia;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private android.text.InputType InputType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText passwordInput = findViewById(R.id.regPassword);
        final ImageView viewPasswordBtn = findViewById(R.id.view_password_btn);

        viewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(passwordInput, viewPasswordBtn);
            }
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView imageView) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.ic_hide_password);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.view_password);
        }
        // Memindahkan kursor ke akhir teks
        editText.setSelection(editText.getText().length());
    }
}
