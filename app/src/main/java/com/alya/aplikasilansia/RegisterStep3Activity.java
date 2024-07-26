package com.alya.aplikasilansia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.alya.aplikasilansia.data.inputMedHistory;
import com.alya.aplikasilansia.ui.editprofile.EditProfileViewModel;
import com.alya.aplikasilansia.ui.reminder.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

public class RegisterStep3Activity extends AppCompatActivity {

    private Spinner spinnerCareGiver;
    private Spinner spinnerMaritalStatus;
    private Button btnSaveMedData;
    private RegisterViewModel registerViewModel;
    private EditProfileViewModel editProfileViewModel;
    private String careGiver, maritalStat;
    private List<inputMedHistory> medHistory;
    private Uri profileImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step3);

        spinnerCareGiver = findViewById(R.id.spinner_care_giver);
        spinnerMaritalStatus = findViewById(R.id.spinner_marital);
        btnSaveMedData = findViewById(R.id.btn_save_med);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

//        Intent intent = getIntent();
//        String email = intent.getStringExtra("email");
//        String password = intent.getStringExtra("password");
//        String birthDate = intent.getStringExtra("birthDate");
//        String userName = intent.getStringExtra("userName");
//        String gender = intent.getStringExtra("gender");
//        String profileImageUrlString = intent.getStringExtra("profileImageUrl");
//        Uri profileImageUri = Uri.parse(profileImageUrlString); // Convert String back to Uri
//        ArrayList<inputMedHistory> medHistory = (ArrayList<inputMedHistory>) intent.getSerializableExtra("medHistory");

        UserData userData = UserData.getInstance();

        // Get the saved data from UserData
        String email = userData.getEmail();
        String password = userData.getPassword(); // Make sure to add `password` in `UserData`
        String birthDate = userData.getBirthDate();
        String userName = userData.getUserName();
        String gender = userData.getGender();
        profileImageUrl = userData.getProfileImageUrl();
        medHistory = userData.getMedHistory();
        Log.d("RegisterStep3Activity", "profile image to save: " +profileImageUrl);


        btnSaveMedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                careGiver = spinnerCareGiver.getSelectedItem().toString();
                maritalStat = spinnerMaritalStatus.getSelectedItem().toString();

                if (careGiver.equals("--Pilih yang Merawat--") || maritalStat.equals("--Pilih Status--")) {
                    incompleteFormDialog();
                    return; // Exit if form is incomplete
                }

                // Check for null values in required fields
                if (email == null || password == null || birthDate == null || userName == null || gender == null) {
                    Log.d("RegisterStep3Activity", "Error: One or more required fields are null.");
                    return; // Exit if any required data is missing
                }

                registerViewModel.register(email, password, birthDate, userName, gender);

                observeData();
                // Register health data and navigate to the main activity


                // Navigate to MainActivity
//                Intent intent = new Intent(RegisterStep3Activity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        // Create an ArrayAdapter using the custom spinner adapter
        CustomSpinnerAdapter careGiverAdapter = new CustomSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.care_giver)
        );
        careGiverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CustomSpinnerAdapter maritalStatusAdapter = new CustomSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.marital_status)
        );
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapters to the spinners
        spinnerCareGiver.setAdapter(careGiverAdapter);
        spinnerMaritalStatus.setAdapter(maritalStatusAdapter);
    }

    private void observeData() {
        registerViewModel.userLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Log.d("RegisterStep3Activity", "createUserWithEmail:success");
                    sendEmailVerification(firebaseUser);
//                    if (profileImageUrl != null) {
//                    }
                    registerHealthData();
//                    FirebaseAuth.getInstance().signOut();
//                    verificationSentDialog(email);



                }
            }
        });

        registerViewModel.errorLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Log.w("RegisterStep3Activity", "createUserWithEmail:failure: " + error);
                    Toast.makeText(RegisterStep3Activity.this, "Authentication failed: " + error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerHealthData(){
        registerViewModel.registerHealth1(medHistory);
        registerViewModel.registerHealth2(careGiver, maritalStat);
//        Log.d("RegisterStep3Activity", "profile image to save2: " +profileImageUrl);
////        editProfileViewModel.updateProfile(null, null, null, profileImageUrl);
//        if (profileImageUrl != null) {
//            editProfileViewModel.updateProfile(null, null, null, profileImageUrl);
//        } else {
//            Log.d("RegisterStep3Activity", "No profile image to update.");
//        }
    }
    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            verificationSentDialog(user.getEmail());
//                            showCustomToast(user.getEmail());
                            verificationSentDialog(user.getEmail());
//                            Toast.makeText(RegisterActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("RegisterStep3Activity", "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterStep3Activity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
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

        Button btnLogin = dialogView.findViewById(R.id.btn_close_verif);
        TextView emailVerifDialogTv = dialogView.findViewById(R.id.tv_email_verif);
        emailVerifDialogTv.setText(emailSent);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intentReg2 = new Intent(RegisterStep3Activity.this, LoginActivity.class);
                startActivity(intentReg2);
                finish();            }
        });
    }

    private void incompleteFormDialog() {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.incomplete_form_dialog, null);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Set the custom background drawable with rounded corners before showing the dialog
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_corner_rounded);

        // Adjust dialog size programmatically
        dialog.setOnShowListener(dialogInterface -> {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 90% of screen width
            params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Adjust height as needed
            dialog.getWindow().setAttributes(params);
        });

        // Show the dialog
        dialog.show();

//        TextView textTv = dialogView.findViewById(R.id.tv_incomplete_form);
//        String text = "Mohon lengkapi data riwayat penyakit Anda";
//        textTv.setText(text);
        // Get the close button from the custom layout and set its click listener
        Button btnClose = dialogView.findViewById(R.id.btn_close_incomplete);
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }
}