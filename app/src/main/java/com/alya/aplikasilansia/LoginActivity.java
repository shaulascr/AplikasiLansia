package com.alya.aplikasilansia;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.alya.aplikasilansia.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;
    private EditText passwordInput;
    private EditText emailInput;
    private ImageView viewPasswordBtn;
    private Button loginBtn;
    private TextView createAccBtn, forgotPassBtn;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    private LoginViewModel loginViewModel;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);
        setPasswordToggle();
        createAccountFromLogin();
        forgotPassword();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_login);

        Button loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    incompleteFormDialog();
                }
                loginViewModel.login(email, password);
            }
        });

        loginViewModel.userLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    if (firebaseUser.isEmailVerified()) {
                        Log.d(TAG, "signInWithEmail:success");
                        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                }
            }
        });

        loginViewModel.errorLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Log.w(TAG, "signInWithEmail:failure: " + error);
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void incompleteFormDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.incomplete_form_dialog, null);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();

        // Set the custom background drawable with rounded corners
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_corner_rounded);

        // Adjust dialog size programmatically after showing it
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 80% of screen width
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Adjust height as needed
        dialog.getWindow().setAttributes(params);

        // Get the buttons from the custom layout and set click listeners
        Button btnClose = dialogView.findViewById(R.id.btn_close_incomplete);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void setPasswordToggle () {
        passwordInput = findViewById(R.id.password_login);
        viewPasswordBtn = findViewById(R.id.view_password_btn);
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
            imageView.setImageResource(R.drawable.view_password);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.hide_password);
        }
        // Memindahkan kursor ke akhir teks
        editText.setSelection(editText.getText().length());
    }

    private void createAccountFromLogin () {
        TextView createAccBtn = findViewById(R.id.btnCreateAcc);
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void forgotPassword() {
        forgotPassBtn = findViewById(R.id.btn_forgot_pass);
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent1);
        }
    }
}