package com.alya.aplikasilansia;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.alya.aplikasilansia.data.inputMedHistory;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordFragment extends DialogFragment {
    public List<inputMedHistory> inputDataList = new ArrayList<>();
//    private FrameLayout parentLayout;
//    private RegisterViewModel registerViewModel;
    private LinearLayout firstInputLayout, parentLayout;
    private OnDataSavedListener onDataSavedListener;

    public interface OnDataSavedListener {
        void onDataSaved(List<inputMedHistory> updatedList);
    }


    // Method to set the listener
    public void setOnDataSavedListener(OnDataSavedListener listener) {
        onDataSavedListener = listener;
    }

    public MedicalRecordFragment() {
        // Required empty public constructor
    }
    public static MedicalRecordFragment newInstance(String param1, String param2) {
        MedicalRecordFragment fragment = new MedicalRecordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            // Get the screen width
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_corner_rounded);

            // Set the dialog's width to match the screen width
            dialog.getWindow().setLayout((int) (width * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_record, container, false);

        Button btnSave = view.findViewById(R.id.btn_add_med_save);
        Button btnCancel = view.findViewById(R.id.btn_add_med_cancel);
//        Button btnAdd = view.findViewById(R.id.btn_add_medhistory_dialog);
        parentLayout = view.findViewById(R.id.input_medhistory);
        firstInputLayout = view.findViewById(R.id.first_med_input);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputData();
                boolean isFirstInputValid = isValidInput(firstInputLayout);
                boolean isParentInputValid;

                if (parentLayout.getChildCount() > 0) {
                    isParentInputValid = isValidInput(parentLayout);
                    if (isFirstInputValid && isParentInputValid && !inputDataList.isEmpty()) {
                        if (onDataSavedListener != null) {
                            onDataSavedListener.onDataSaved(new ArrayList<>(inputDataList));
                        }
                        dismiss();
                    } else {
                        // Show a toast if any field is empty or no data
                        Toast.makeText(getActivity(), "Harap isi semua kolomW", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (isFirstInputValid && !inputDataList.isEmpty()) {
                        if (onDataSavedListener != null) {
                            onDataSavedListener.onDataSaved(new ArrayList<>(inputDataList));
                        }
                        dismiss();
                    } else {
                        // Show a toast if any field is empty or no data
                        Toast.makeText(getActivity(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


        btnCancel.setOnClickListener(v -> {dismiss();});

//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addNewInputField();
//            }
//        });

        return view;
    }

    private void saveInputData() {
        inputDataList.clear();

        // Clear the existing data list
        if (!isValidInput(firstInputLayout)) {
            Toast.makeText(getActivity(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }
        // Save the first input field data
        EditText editTextPenyakit = firstInputLayout.findViewById(R.id.et_penyakit);
        EditText editTextLamanya = firstInputLayout.findViewById(R.id.et_med_years);
        EditText editTextLamanya2 = firstInputLayout.findViewById(R.id.et_med_months);

        if (editTextPenyakit == null || editTextLamanya == null || editTextLamanya2 == null) {
            Toast.makeText(getActivity(), "Form tidak valid", Toast.LENGTH_SHORT).show();
            return; // Stop execution if views are not found
        }

        String penyakit = editTextPenyakit.getText().toString();
        String lamanya = editTextLamanya.getText().toString();
        String lamanya2 = editTextLamanya2.getText().toString();

        if (penyakit.isEmpty() || lamanya.isEmpty() || lamanya2.isEmpty()) {
            Toast.makeText(getActivity(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }

        inputDataList.add(new inputMedHistory(penyakit, lamanya, lamanya2));

        // Iterate through all child views in the parent layout
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View childView = parentLayout.getChildAt(i);
            editTextPenyakit = childView.findViewById(R.id.et_penyakit);
            editTextLamanya = childView.findViewById(R.id.et_med_years);
            editTextLamanya2 = childView.findViewById(R.id.et_med_months);
            if (editTextPenyakit == null || editTextLamanya == null || editTextLamanya2 == null) {
                continue; // Skip this view if any EditText is null
            }
            // Retrieve the input values
            penyakit = editTextPenyakit.getText().toString();
            lamanya = editTextLamanya.getText().toString();
            lamanya2 = editTextLamanya2.getText().toString();

            if (penyakit.isEmpty() || lamanya.isEmpty() || lamanya2.isEmpty()) {
                Toast.makeText(getActivity(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
//                return; // Stop execution if validation fails
            }
            // Add the input values to the data list
            inputDataList.add(new inputMedHistory(penyakit, lamanya, lamanya2));
//            Log.d("RegisterStep2Activity", "Input " + (i + 1) + " - Penyakit: " + penyakit + ", Lamanya: " + lamanya);

        }
        // Now inputDataList contains all the input data
        // You can use it as needed
        for (int i = 0; i < inputDataList.size(); i++) {
            inputMedHistory input = inputDataList.get(i);
            Log.d("RegisterStep2Activity", "Input " + (i + 1) + " - Penyakit: " + input.penyakit + ", Lamanya: " + input.lamanya);
        }

    }

    private boolean isValidInput(View view) {
        EditText editTextPenyakit = view.findViewById(R.id.et_penyakit);
        EditText editTextLamanya = view.findViewById(R.id.et_med_years);
        EditText editTextLamanya2 = view.findViewById(R.id.et_med_months);

        if (editTextPenyakit == null || editTextLamanya == null || editTextLamanya2 == null) {
            return false; // Return false if any EditText is not found
        }

        String penyakit = editTextPenyakit.getText().toString().trim();
        String lamanya = editTextLamanya.getText().toString().trim();
        String lamanya2 = editTextLamanya2.getText().toString().trim();

        return !penyakit.isEmpty() && !lamanya.isEmpty() && !lamanya2.isEmpty();
    }

    private void addNewInputField() {
        // Inflate the input field layout and add it to the parent layout
        LayoutInflater inflater = getLayoutInflater();
        View newInputView = inflater.inflate(R.layout.register_input_medhistory, parentLayout, false);
        parentLayout.addView(newInputView);
    }


}