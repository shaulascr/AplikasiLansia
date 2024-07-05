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
import com.alya.aplikasilansia.ui.bloodpressure.BloodPressureActivity;
import com.alya.aplikasilansia.ui.healthcare.HealthCareActivity;
import com.alya.aplikasilansia.ui.profile.ProfileViewModel;
import com.alya.aplikasilansia.ui.reminder.ReminderActivity;
import com.alya.aplikasilansia.ui.reminder.ReminderViewModel;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button tensiDarah;
    private ProfileViewModel profileViewModel;
    private ReminderViewModel reminderViewModel;

    private TextView userNameHome;
    private ImageView profileImage;
    private Button toHealthCare;
    private Button toReminder;
    private Button toBP;
    private TextView tvTitleRemind;
    private TextView tvTimeRemind;
    private ImageView imgRemind;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvTitleRemind = view.findViewById(R.id.txt_remind1);
        tvTimeRemind = view.findViewById(R.id.txt_remind2);
        imgRemind = view.findViewById(R.id.img_remind);

        // Now you can find your views within the inflated layout
        userNameHome = view.findViewById(R.id.txt_name);
        profileImage = view.findViewById(R.id.profile_image_home);

        toHealthCare = view.findViewById(R.id.btn_to_healthcare);
        toHealthCare.setOnClickListener(this);

        toReminder = view.findViewById(R.id.btn_to_reminder);
        toReminder.setOnClickListener(this);

        toBP = view.findViewById(R.id.btn_to_bloodpresure);
        toBP.setOnClickListener(this);

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

        reminderViewModel.getFirstReminderLiveData().observe(getViewLifecycleOwner(), firstReminder -> {
            if (firstReminder != null) {
                tvTitleRemind.setText(firstReminder.getTitle());
                tvTimeRemind.setText(formatDate(firstReminder.getTimestamp()));
                imgRemind.setImageResource(firstReminder.getIcon());
            }
        });


        return view;
    }
    private String formatDate(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("'Hari ini pukul' HH:mm", new Locale("id", "ID"));
        SimpleDateFormat tomorrowFormat = new SimpleDateFormat("'Besok pukul' HH:mm", new Locale("id", "ID"));
        SimpleDateFormat normalFormat = new SimpleDateFormat("EEEE, d MMMM 'pukul' HH:mm", new Locale("id", "ID"));

        try {
            Date date = sdf.parse(timestamp);
            Calendar calendar = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            calendar.setTime(date);

            if (isSameDay(calendar, today)) {
                return outputFormat.format(date);
            } else {
                Calendar tomorrow = (Calendar) today.clone();
                tomorrow.add(Calendar.DAY_OF_YEAR, 1);
                if (isSameDay(calendar, tomorrow)) {
                    return tomorrowFormat.format(date);
                } else {
                    return normalFormat.format(date);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return timestamp;
        }
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public void getRemindData(){

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_to_healthcare) {
            Intent intent = new Intent(getActivity(), HealthCareActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_to_reminder) {
            Intent intent = new Intent(getActivity(), ReminderActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_to_bloodpresure) {
            Intent intent = new Intent(getActivity(), BloodPressureActivity.class);
            startActivity(intent);
        }
    }
}