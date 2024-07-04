package com.alya.aplikasilansia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.data.inputMedHistory;

import java.util.ArrayList;
import java.util.List;

public class RegisterStep2Activity extends AppCompatActivity {

    private FrameLayout parentLayout;
    private Button buttonAddInput, buttonSave;
    private List<inputMedHistory> inputDataList = new ArrayList<>();
    private LinearLayout firstInputLayout;
    private RegisterViewModel registerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        parentLayout = findViewById(R.id.input_medhistory);
        buttonAddInput = findViewById(R.id.btn_add_medhistory);
        buttonSave = findViewById(R.id.btn_save_medhistory);
        firstInputLayout = findViewById(R.id.first_input_medhistory);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);


        buttonAddInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewInputField();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputData();
                registerViewModel.registerHealth1(inputDataList);
                Intent register3 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                startActivity(register3);
            }
        });
    }

    private void addNewInputField() {
        // Inflate the input field layout and add it to the parent layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View newInputView = inflater.inflate(R.layout.register_input_medhistory, parentLayout, false);
        parentLayout.addView(newInputView);
    }
    private void saveInputData() {
        // Clear the existing data list
        inputDataList.clear();

        // Save the first input field data
        EditText editTextPenyakit = firstInputLayout.findViewById(R.id.et_penyakit);
        EditText editTextLamanya = firstInputLayout.findViewById(R.id.et_lamanya);
        String penyakit = editTextPenyakit.getText().toString();
        String lamanya = editTextLamanya.getText().toString();
        inputDataList.add(new inputMedHistory(penyakit, lamanya));

        // Iterate through all child views in the parent layout
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View childView = parentLayout.getChildAt(i);
            editTextPenyakit = childView.findViewById(R.id.et_penyakit);
            editTextLamanya = childView.findViewById(R.id.et_lamanya);

            // Retrieve the input values
            penyakit = editTextPenyakit.getText().toString();
            lamanya = editTextLamanya.getText().toString();

            // Add the input values to the data list
            inputDataList.add(new inputMedHistory(penyakit, lamanya));
//            Log.d("RegisterStep2Activity", "Input " + (i + 1) + " - Penyakit: " + penyakit + ", Lamanya: " + lamanya);

        }
        // Now inputDataList contains all the input data
        // You can use it as needed
        for (int i = 0; i < inputDataList.size(); i++) {
            inputMedHistory input = inputDataList.get(i);
            Log.d("RegisterStep2Activity", "Input " + (i + 1) + " - Penyakit: " + input.penyakit + ", Lamanya: " + input.lamanya);
        }
    }

}