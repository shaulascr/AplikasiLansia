package com.alya.aplikasilansia;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private android.text.InputType InputType;
    private EditText passwordInput;
    private ImageView viewPasswordBtn;
    private TextView birthdateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        birthdateTextView = findViewById(R.id.birthdate_textview);

        setPasswordToggle();
        loginFromRegister();
        setupDatePicker(birthdateTextView);
    }

    private void setPasswordToggle () {
        passwordInput = findViewById(R.id.regPassword);
        viewPasswordBtn = findViewById(R.id.view_password_btn);
        viewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(passwordInput, viewPasswordBtn);
            }
        });
    }

    private void loginFromRegister () {
        TextView loginFromRegTv = findViewById(R.id.masukRegTv);

        String text = "Sudah punya akun? Masuk";
        SpannableString spannableString = new SpannableString(text);

        int blueColor = ContextCompat.getColor(this, R.color.blue2);
        ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(blueColor);
        spannableString.setSpan(blueColorSpan, text.indexOf("Masuk"), text.indexOf("Masuk") + "Masuk".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make the word "Masuk" clickable
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, text.indexOf("Masuk"), text.indexOf("Masuk") + "Masuk".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginFromRegTv.setText(spannableString);
        loginFromRegTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void togglePasswordVisibility(EditText editText, ImageView imageView) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.view_password);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.hide_password);
        }
        // Memindahkan kursor ke akhir teks
        editText.setSelection(editText.getText().length());
    }

    private void setupDatePicker(TextView birthdateTextView) {
        birthdateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        // Create a date picker with constraints
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setOpenAt(calendar.getTimeInMillis());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select your birthdate")
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Format the selected date and set it to the TextView
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = format.format(selection);
            birthdateTextView.setText(formattedDate);
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }
}
