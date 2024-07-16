package com.alya.aplikasilansia.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.LoginActivity;
import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.User;
import com.alya.aplikasilansia.ui.editprofile.EditProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private ProfileViewModel profileViewModel;
    private Button editProfile;
    private TextView personalProfile, healthProfile;
    private TextView userNameProfile;
    private ImageView imageProfile;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;

    private static final int FRAGMENT_PERSONAL_DATA = 1;
    private static final int FRAGMENT_HEALTH_DATA = 2;
    private String currentFragment;
//    private int currentFragment;

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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            // User is not logged in, redirect to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            // Optionally, close the current activity
//            getActivity().finish();
        } else {
            /// Find the button
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
                replaceFragment(new ProfilePersonalFragment(), "personal");
            }
            profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
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
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        profileViewModel.fetchUser();
        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    userNameProfile.setText(user.getUserName());
                    if (user.getProfileImageUrl() != null) {
//                        String imageUrl = user.getProfileImageUrl() + "?t=" + System.currentTimeMillis();
                        Glide.with(ProfileFragment.this)
                                .load(user.getProfileImageUrl())
                                .into(imageProfile);
                    } else {
                        // Handle no profile image case
                        imageProfile.setImageResource(R.drawable.img);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_editProfile) {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("FRAGMENT_TYPE", currentFragment);
//            startActivity(intent);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE); // Use startActivityForResult

        } else if (v.getId() == R.id.btnPersonalData) {
            replaceFragment(new ProfilePersonalFragment(), "personal");
            personalProfile.setBackgroundResource(R.drawable.text_blue_underlined);
            healthProfile.setBackgroundResource(R.drawable.text_transparant);
        } else if (v.getId() == R.id.btnHealthData) {
            replaceFragment(new ProfileHealthFragment(),"health");
            healthProfile.setBackgroundResource(R.drawable.text_blue_underlined);
            personalProfile.setBackgroundResource(R.drawable.text_transparant);
        }
        // Create an intent to start the EditProfileActivity

    }

    private void replaceFragment(Fragment fragment, String fragmentType) {
        currentFragment = fragmentType;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST_CODE) {
            profileViewModel.fetchUser();
            if (resultCode == FragmentActivity.RESULT_OK && data != null) {
                String fragmentType = data.getStringExtra("FRAGMENT_TYPE");
                if (fragmentType != null) {
                    Fragment fragmentToShow = fragmentType.equals("personal") ? new ProfilePersonalFragment() : new ProfileHealthFragment();
                    replaceFragment(fragmentToShow, fragmentType);
                }
            } else if (resultCode == FragmentActivity.RESULT_CANCELED && data != null) {
                String fragmentType = data.getStringExtra("FRAGMENT_TYPE");
                if (fragmentType != null) {
                    Fragment fragmentToShow = fragmentType.equals("personal") ? new ProfilePersonalFragment() : new ProfileHealthFragment();
                    replaceFragment(fragmentToShow, fragmentType);
                }
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == FragmentActivity.RESULT_CANCELED && data != null) {
//            String fragmentType = data.getStringExtra("FRAGMENT_TYPE");
//            if (resultCode == FragmentActivity.RESULT_OK) {
//                // Refresh the user data
//                profileViewModel.fetchUser();
//            } else {
//                // Handle other cases if necessary
//            }            if (fragmentType != null) {
//                Fragment fragmentToShow;
//                if (fragmentType.equals("personal")) {
//                    fragmentToShow = new ProfilePersonalFragment(); // Load PersonalProfileFragment
//                } else if (fragmentType.equals("health")) {
//                    fragmentToShow = new ProfileHealthFragment(); // Load HealthProfileFragment
//                } else {
//                    // Default to PersonalProfileFragment if fragmentType is unknown
//                    fragmentToShow = new ProfilePersonalFragment();
//                }
//                replaceFragment(fragmentToShow, fragmentType);
//            }
//        }
//    }
}