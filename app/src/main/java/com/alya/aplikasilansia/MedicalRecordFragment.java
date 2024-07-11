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

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.data.inputMedHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicalRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalRecordFragment extends DialogFragment {
    public List<inputMedHistory> inputDataList = new ArrayList<>();
//    private FrameLayout parentLayout;
    private RegisterViewModel registerViewModel;
    private LinearLayout firstInputLayout, parentLayout;
    private Button btnSave;
    private Button btnCancel;
    private Button btnAdd;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Listener interface to communicate with host activity
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
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

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

        btnSave = view.findViewById(R.id.btn_add_med_save);
        btnCancel = view.findViewById(R.id.btn_add_med_cancel);
        btnAdd = view.findViewById(R.id.btn_add_medhistory_dialog);
        parentLayout = view.findViewById(R.id.input_medhistory);
        firstInputLayout = view.findViewById(R.id.first_med_input);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputData();
                registerViewModel.registerHealth1(inputDataList);
                // Notify the listener with updated data list

                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> {dismiss();});

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewInputField();
            }
        });

        return view;
    }

    private void saveInputData() {
        // Clear the existing data list
        inputDataList.clear();

        // Save the first input field data
        EditText editTextPenyakit = firstInputLayout.findViewById(R.id.et_penyakit);
        EditText editTextLamanya = firstInputLayout.findViewById(R.id.et_med_years);
        EditText editTextLamanya2 = firstInputLayout.findViewById(R.id.et_med_months);
        String penyakit = editTextPenyakit.getText().toString();
        String lamanya = editTextLamanya.getText().toString();
        String lamanya2 = editTextLamanya2.getText().toString();
        inputDataList.add(new inputMedHistory(penyakit, lamanya, lamanya2));

        // Iterate through all child views in the parent layout
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View childView = parentLayout.getChildAt(i);
            editTextPenyakit = childView.findViewById(R.id.et_penyakit);
            editTextLamanya = childView.findViewById(R.id.et_med_years);
            editTextLamanya2 = childView.findViewById(R.id.et_med_months);

            // Retrieve the input values
            penyakit = editTextPenyakit.getText().toString();
            lamanya = editTextLamanya.getText().toString();
            lamanya2 = editTextLamanya2.getText().toString();

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

        if (onDataSavedListener != null) {
            onDataSavedListener.onDataSaved(inputDataList);
        }
    }
    private void addNewInputField() {
        // Inflate the input field layout and add it to the parent layout
        LayoutInflater inflater = getLayoutInflater();
        View newInputView = inflater.inflate(R.layout.register_input_medhistory, parentLayout, false);
        parentLayout.addView(newInputView);
    }


}