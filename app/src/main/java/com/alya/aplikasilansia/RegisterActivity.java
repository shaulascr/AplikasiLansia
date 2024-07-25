package com.alya.aplikasilansia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.data.UserData;
import com.alya.aplikasilansia.ui.reminder.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private android.text.InputType InputType;
    private EditText passwordInputReg, nameInputReg, emailInputReg, genderInputReg;
    private ImageView viewPasswordBtn;

    private TextView birthdateTextView, emailVerifDialogTv;
    private Button btnRegister;
    private Button btnRegisterGoogle;
    private Spinner spinnerGender;
    private FirebaseAuth mAuth;
    private static final String TAG = "RegisterActivity";
    private RegisterViewModel registerViewModel;
    private String selectedGender, email;
    private boolean isDialogClosed = false; // Flag to check if dialog is closed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        birthdateTextView = findViewById(R.id.tv_birthDateReg);
        nameInputReg = findViewById(R.id.name_input_reg);
        emailInputReg = findViewById(R.id.email_input_reg);
        birthdateTextView = findViewById(R.id.tv_birthDateReg);
        spinnerGender = findViewById(R.id.spinner_gender);
        Button registerBtn = findViewById(R.id.btn_regis);

        setPasswordToggle();
        loginFromRegister();
        setupDatePicker(birthdateTextView);

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender_array)
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(spinnerAdapter);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailInputReg.getText().toString().trim();
                String password = passwordInputReg.getText().toString().trim();
                String birthDate = birthdateTextView.getText().toString().trim();
                String userName = nameInputReg.getText().toString().trim();
                selectedGender = spinnerGender.getSelectedItem().toString();

                if (email.isEmpty() || password.isEmpty() || birthDate.isEmpty() || userName.isEmpty() || selectedGender.equals("Jenis Kelamin") || selectedGender.isEmpty()) {
                    incompleteFormDialog();
                    return; // Exit the method without proceeding
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    String text = "Email tidak valid. Silahkan masukkan email yang valid.";
                    errorMessageToast(text);
                    return;
                }

                if (password.length() < 6) {
                    String text = "Kata sandi harus memiliki setidaknya 6 karakter.";
                    errorMessageToast(text);
                    return; // Exit the method without proceeding
                }

                UserData userData = UserData.getInstance();
                userData.setEmail(email);
                userData.setPassword(password);
                userData.setBirthDate(birthDate);
                userData.setUserName(userName);
                userData.setGender(selectedGender);

                // Proceed to RegisterStep2Activity
                Intent intentReg2 = new Intent(RegisterActivity.this, RegisterStep2Activity.class);
                startActivity(intentReg2);
                finish();

//                Intent intentReg2 = new Intent(RegisterActivity.this, RegisterStep2Activity.class);
//                intentReg2.putExtra("email", email);
//                intentReg2.putExtra("password", password);
//                intentReg2.putExtra("birthDate", birthDate);
//                intentReg2.putExtra("userName", userName);
//                intentReg2.putExtra("gender", selectedGender);
//
//                startActivity(intentReg2);
//                finish();
//                registerViewModel.register(email, password, birthDate, userName, selectedGender);
//
//                observeData();
            }
        });




    }

    private void observeData() {
        registerViewModel.userLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Log.d(TAG, "createUserWithEmail:success");
//                    sendEmailVerification(firebaseUser);
//                    FirebaseAuth.getInstance().signOut();
//                    verificationSentDialog(email);
//                    Intent intentReg2 = new Intent(RegisterActivity.this, RegisterStep2Activity.class);
//                    startActivity(intentReg2);
//                    finish();


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

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            verificationSentDialog(user.getEmail());
                            showCustomToast(user.getEmail());
//                            Toast.makeText(RegisterActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
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
                .setTitleText("Pilih Tanggal Lahir Anda")
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
    private void errorMessageToast (String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.error_custom_toast, null);

        ImageView toastIcon = layout.findViewById(R.id.img_error_ic);
        TextView toastText = layout.findViewById(R.id.tv_error_message);

        toastIcon.setImageResource(R.drawable.ic_warning); // Set your desired icon here
        toastText.setText(message);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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

    private void verificationSentDialog(String emailSent){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.email_verif_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_corner_rounded);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 80% of screen width
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Adjust height as needed
        dialog.getWindow().setAttributes(params);

//        Button btnLogin = dialogView.findViewById(R.id.btn_close_verif);
        emailVerifDialogTv = dialogView.findViewById(R.id.tv_email_verif);
        emailVerifDialogTv.setText(emailSent);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                isDialogClosed = true;
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//            }
//        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isDialogClosed) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.email_verif_dialog, null);

        ImageView toastIcon = layout.findViewById(R.id.img_verif_sent);
        TextView text = layout.findViewById(R.id.text_verif_sent);
        TextView email = layout.findViewById(R.id.tv_email_verif);
        TextView text2 = layout.findViewById(R.id.text_verif_sent_2);

        String text_text = "Verifikasi email telah dikirim ke";
        String text2_text = "Silahkan verifikasi email untuk mengaktifkan akun";

        toastIcon.setImageResource(R.drawable.ic_checkmark);
        text.setText(text_text);
        email.setText(message);
        text2.setText(text2_text);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
//            startActivity(intent2);
//        }
//    }
}