package com.alya.aplikasilansia.ui.editprofile;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.inputMedHistory;

import java.util.ArrayList;
import java.util.List;

public class EditHealthFragment extends Fragment implements AddMedicalRecordFragment.OnMedicalRecordAddedListener {
    private Spinner caregiverSpinner, maritalStatSpinner;
    private LinearLayout medHistoryContainer;
    private EditProfileViewModel editProfileViewModel;
    private Button dialogAddMedButton, saveButton, cancelButton;
    private List<inputMedHistory> medHistoryList = new ArrayList<>();
    private OnSaveEditListener onSaveEditListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveEditListener) {
            onSaveEditListener = (OnSaveEditListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSaveEditListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveEditListener = null;
    }

    public EditHealthFragment() {
        // Required empty public constructor
    }
    public static EditHealthFragment newInstance(String param1, String param2) {
        EditHealthFragment fragment = new EditHealthFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_health, container, false);

        medHistoryContainer = view.findViewById(R.id.edit_input_medhistory);
        dialogAddMedButton = view.findViewById(R.id.btn_medhistory_dialog);
        addNewMedicalRecordDialog(dialogAddMedButton);

        caregiverSpinner = view.findViewById(R.id.edit_care_giver);
        maritalStatSpinner = view.findViewById(R.id.edit_marital);

        view.findViewById(R.id.btn_save_health).setOnClickListener(v -> {saveProfileChanges();});

        view.findViewById(R.id.btn_health_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        editProfileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                medHistoryList.addAll(user.getMedHistory()); // Add data from database
                medicalRecord(medHistoryList);
                setSpinnerCaregiver(user.getCaregiver());
                setSpinnerMarStat(user.getMaritalStatus());
            }
        });

        editProfileViewModel.getUpdateResultLiveData().observe(getViewLifecycleOwner(), updateResult -> {

        });

        return view;
    }

    private void addNewMedicalRecordDialog(Button dialogAddMedButton){
        dialogAddMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicalRecordFragment addMedicalRecordFragment = new AddMedicalRecordFragment();
                addMedicalRecordFragment.setOnMedicalRecordAddedListener(EditHealthFragment.this);
                addMedicalRecordFragment.show(getActivity().getSupportFragmentManager(), "AddMedicalRecordDialog");
            }
        });

    }

    private void saveProfileChanges(){
        String newCaregiver = caregiverSpinner.getSelectedItem().toString();
        String newStatus = maritalStatSpinner.getSelectedItem().toString();

        if (onSaveEditListener != null) {
            onSaveEditListener.onSaveHealthData(newCaregiver, newStatus, medHistoryList);
            requireActivity().onBackPressed();
        } else {
            Log.e("EditHealthFragment", "onSaveEditListener is not attached");
        }
    }

    public void onMedicalRecordAdded(inputMedHistory newMedHistory) {
        medHistoryList.add(newMedHistory);
        medicalRecord(medHistoryList);
        // Update ViewModel and database if necessary
    }
    private void medicalRecord(List<inputMedHistory> medHistory){
        if (medHistory == null || medHistory.isEmpty()) {
            Log.d("ProfileHealthFragment", "Medical history is null or empty");
            return;
        }

        medHistoryContainer.removeAllViews();

        for (inputMedHistory history : medHistory) {
            View itemView = getLayoutInflater().inflate(R.layout.register_input_medhistory, medHistoryContainer, false);

            EditText penyakitEditText = itemView.findViewById(R.id.et_penyakit);
            EditText tahunEditText = itemView.findViewById(R.id.et_med_years);
            EditText bulanEditText = itemView.findViewById(R.id.et_med_months);

            penyakitEditText.setText(history.getPenyakit());
            tahunEditText.setText(history.getLamanya());
            bulanEditText.setText(history.getLamanyaBulan());

            medHistoryContainer.addView(itemView);
        }
    }

    private void setSpinnerCaregiver(String selectedCaregiver) {
        String[] caregivers = getResources().getStringArray(R.array.care_giver);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, caregivers);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        caregiverSpinner.setAdapter(spinnerAdapter);

        // Set selected item
        if (selectedCaregiver != null) {
            int position = spinnerAdapter.getPosition(selectedCaregiver);
            caregiverSpinner.setSelection(position);
        }
    }

    private void setSpinnerMarStat(String selectedMaritalStatus) {
        String[] maritalStatuses = getResources().getStringArray(R.array.marital_status);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, maritalStatuses);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalStatSpinner.setAdapter(spinnerAdapter);

        // Set selected item
        if (selectedMaritalStatus != null) {
            int position = spinnerAdapter.getPosition(selectedMaritalStatus);
            maritalStatSpinner.setSelection(position);
        }
    }
}