package com.alya.aplikasilansia.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.ui.editprofile.EditProfileActivity;
import com.bumptech.glide.Glide;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel profileViewModel;

    private Button editProfile;
    private TextView personalProfile, healthProfile;
    private TextView userNameProfile;
    private ImageView imageProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        // Find the button
        editProfile = view.findViewById(R.id.btn_editProfile); // Replace with your button's actual ID
        editProfile.setOnClickListener(this);
        personalProfile = view.findViewById(R.id.btnPersonalData);
        personalProfile.setOnClickListener(this);
        healthProfile = view.findViewById(R.id.btnHealthData);
        healthProfile.setOnClickListener(this);

        userNameProfile = view.findViewById(R.id.profile_userName);
        imageProfile = view.findViewById(R.id.profile_image);

        personalProfile.setBackgroundResource(R.drawable.text_blue_underlined);
        healthProfile.setBackgroundResource(R.drawable.text_transparant);

        if (savedInstanceState == null) {
            replaceFragment(new ProfilePersonalFragment());
        }
        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                userNameProfile.setText(user.getUserName());
                if (user.getProfileImageUrl() != null) {
                    Glide.with(ProfileFragment.this)
                            .load(user.getProfileImageUrl())
                            .into(imageProfile);
                } else {
                    // Handle no profile image case
                    imageProfile.setImageResource(R.drawable.img);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_editProfile) {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnPersonalData) {
            replaceFragment(new ProfilePersonalFragment());
            personalProfile.setBackgroundResource(R.drawable.text_blue_underlined);
            healthProfile.setBackgroundResource(R.drawable.text_transparant);
        } else if (v.getId() == R.id.btnHealthData) {
            replaceFragment(new ProfileHealthFragment());
            healthProfile.setBackgroundResource(R.drawable.text_blue_underlined);
            personalProfile.setBackgroundResource(R.drawable.text_transparant);
        }
        // Create an intent to start the EditProfileActivity

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}