package com.alya.aplikasilansia.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.User;
import com.alya.aplikasilansia.ui.profile.ProfileViewModel;
import com.alya.aplikasilansia.ui.quiz.QuizActivity;
import com.bumptech.glide.Glide;

public class HomeFragment extends Fragment {

    private Button tensiDarah;
    private ProfileViewModel profileViewModel;
    private TextView userNameHome;
    private ImageView profileImage;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Now you can find your views within the inflated layout
        tensiDarah = view.findViewById(R.id.btn_to_bloodpresure);
        userNameHome = view.findViewById(R.id.txt_name);
        profileImage = view.findViewById(R.id.profile_image_home);

        tensiDarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent1);
            }
        });

        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    userNameHome.setText(user.getUserName());
                    if (user.getProfileImageUrl() != null) {
                        Glide.with(HomeFragment.this)
                                .load(user.getProfileImageUrl())
                                .into(profileImage);
                    } else {
                        // Handle no profile image case
                        profileImage.setImageResource(R.drawable.img);
                    }
                }
            }
        });

        return view;
    }
}