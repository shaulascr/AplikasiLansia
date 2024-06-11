package com.alya.aplikasilansia.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alya.aplikasilansia.R;
import com.google.android.flexbox.FlexboxLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileHealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileHealthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FlexboxLayout flexboxLayout;

    public ProfileHealthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileHealthFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_health, container, false);

        // Inflate the layout for this fragment

        flexboxLayout = view.findViewById(R.id.flexbox_disease);

        // Example data to be displayed in TextViews
        String[] data = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

        // Create and add TextViews dynamically
        for (String item : data) {
            TextView textView = new TextView(getContext());
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT // Wrap content for height
            );
            layoutParams.setMargins(6, 6, 6, 6); // Set margins as needed

            textView.setLayoutParams(layoutParams);
            textView.setText(item);
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 18); // Set text size as needed
            textView.setBackground(getResources().getDrawable(R.drawable.item_disease));
            textView.setPadding(12, 0, 12, 0); // Set horizontal padding as needed

            flexboxLayout.addView(textView);
        }

        return view;

    }
}