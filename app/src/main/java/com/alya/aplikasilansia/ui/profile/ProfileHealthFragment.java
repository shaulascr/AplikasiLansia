package com.alya.aplikasilansia.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.User;
import com.alya.aplikasilansia.data.inputMedHistory;

import java.util.ArrayList;
import java.util.List;


public class ProfileHealthFragment extends Fragment {

    private LinearLayout profileMedHistory;
    private TextView tvCaregiver, tvMaritalStatus;
    private ProfileViewModel profileViewModel;
    private List<inputMedHistory> inputDataList = new ArrayList<>();


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileHealthFragment() {
        // Required empty public constructor
    }

    public static ProfileHealthFragment newInstance(String param1, String param2) {
        ProfileHealthFragment fragment = new ProfileHealthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_health, container, false);

        profileMedHistory = view.findViewById(R.id.profile_medhistory);
        tvCaregiver = view.findViewById(R.id.tv_caregiver);
        tvMaritalStatus = view.findViewById(R.id.tv_marital_stat);

        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Log.d("ProfileHealthFragment", "User data received: " + user.toString());
                    tvCaregiver.setText(user.getCaregiver());
                    tvMaritalStatus.setText(user.getMaritalStatus());
//                    profileMedHistory(user.getMedHistory());
                    List<inputMedHistory> medHistory = user.getMedHistory();

                    // You can then iterate over the list to access individual inputMedHistory objects
                    for (inputMedHistory history : medHistory) {
                        // Do something with med, e.g., log its values
                        Log.d("ProfileHealthFragment", "Med history: " + history.toString());
                        View itemView = getLayoutInflater().inflate(R.layout.profile_view_medhistory, profileMedHistory, false);
//                        profileMedHistory.removeAllViews();

                        TextView tvPenyakit = itemView.findViewById(R.id.tv_profile_penyakit);
                        TextView tvMedYears = itemView.findViewById(R.id.tv_profile_tahun);
                        TextView tvMedMonths = itemView.findViewById(R.id.tv_profile_bulan);

//                      if (tvPenyakit != null && tvMedYears != null && tvMedMonths != null) {
                        tvPenyakit.setText(history.getPenyakit());
                        tvMedYears.setText(history.getLamanya());
                        tvMedMonths.setText(history.getLamanyaBulan());


                        profileMedHistory.addView(itemView);
                    }
                } else {
                    Log.d("ProfileHealthFragment", "User data is null");
                }
            }
        });

        return view;

    }

    private void profileMedHistory(List<inputMedHistory> medHistory){
        if (medHistory == null || medHistory.isEmpty()) {
            Log.d("ProfileHealthFragment", "Medical history is null or empty");
            return;
        }

        profileMedHistory.removeAllViews();

        for (inputMedHistory history : medHistory) {
            Log.d("ProfileHealthFragment", "Adding medical history: " + history.toString());
            View itemView = getLayoutInflater().inflate(R.layout.register_view_medhistory, profileMedHistory, false);

            TextView tvPenyakit = itemView.findViewById(R.id.tv_penyakit);
            TextView tvMedYears = itemView.findViewById(R.id.tv_med_years);
            TextView tvMedMonths = itemView.findViewById(R.id.tv_med_months);

//            if (tvPenyakit != null && tvMedYears != null && tvMedMonths != null) {
                tvPenyakit.setText(history.getPenyakit());
                tvMedYears.setText(history.getLamanya());
                tvMedMonths.setText(history.getLamanyaBulan());


            profileMedHistory.addView(itemView);
        }
    }
}