package com.alya.aplikasilansia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private android.text.InputType InputType;
    private EditText passwordInputReg;
    private EditText nameInputReg;
    private EditText emailInputReg;
    private EditText genderInputReg;
    private ImageView viewPasswordBtn;
    private TextView birthdateTextView;
    private Button btnRegister;
    private Button btnRegisterGoogle;
    private FirebaseAuth mAuth;
    private static final String TAG = "RegisterActivity";
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        birthdateTextView = findViewById(R.id.tv_birthDateReg);
        setPasswordToggle();
        loginFromRegister();
        setupDatePicker(birthdateTextView);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        nameInputReg = findViewById(R.id.name_input_reg);
        emailInputReg = findViewById(R.id.email_input_reg);
        birthdateTextView = findViewById(R.id.tv_birthDateReg);

        mAuth = FirebaseAuth.getInstance();
        Button registerBtn = findViewById(R.id.btn_regis);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInputReg.getText().toString().trim();
                String password = passwordInputReg.getText().toString().trim();
                String birthDate = birthdateTextView.getText().toString().trim();
                String userName = nameInputReg.getText().toString().trim();

                registerViewModel.register(email, password, birthDate, userName);
            }
        });

        registerViewModel.userLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Log.d(TAG, "createUserWithEmail:success");
                    Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent2);
                }
            }
        });

        registerViewModel.errorLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Log.w(TAG, "createUserWithEmail:failure: " + error);
                    Toast.makeText(RegisterActivity.this, "Authentication failed: " + error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setPasswordToggle () {
        passwordInputReg = findViewById(R.id.regPassword);
        viewPasswordBtn = findViewById(R.id.view_password_btn);
        viewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(passwordInputReg, viewPasswordBtn);
            }
        });
    }
    private void loginFromRegister () {
        TextView loginFromRegTv = findViewById(R.id.masukRegTv);
        loginFromRegTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent2);
        }
    }
}