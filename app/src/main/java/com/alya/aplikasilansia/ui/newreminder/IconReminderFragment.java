package com.alya.aplikasilansia.ui.newreminder;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class IconReminderFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private IconReminderAdapter adapter;
    private List<IconReminder> iconReminderList;
    private MaterialButton btnCloseReminderDialog;
    private OnIconSelectedListener mListener; // Define mListener

    public interface OnIconSelectedListener {
        void onIconSelected(int iconResId);
    }

    // Setter method for mListener
    public void setOnIconSelectedListener(OnIconSelectedListener listener) {
        mListener = listener;
    }
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public IconReminderFragment() {
        // Required empty public constructor
    }

    public static IconReminderFragment newInstance(String param1, String param2) {
        IconReminderFragment fragment = new IconReminderFragment();
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

//            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icon_reminder, container, false);
        btnCloseReminderDialog = view.findViewById(R.id.closeReminderDialog);
        btnCloseReminderDialog.setOnClickListener(v -> dismiss());

        recyclerView = view.findViewById(R.id.rv_icon_reminder);
        recyclerView.setHasFixedSize(true);

        // Set up FlexboxLayoutManager
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        recyclerView.setLayoutManager(layoutManager);

        iconReminderList = new ArrayList<>();
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_pumpkin));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_med));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_dinner));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_brain));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_clean));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_business));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_cooking));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_dance));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_dice));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_eat));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_knit));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_meditate));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_farmer));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_exercise));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_sleep));
        iconReminderList.add(new IconReminder(R.drawable.ic_remind_read));
        // Add more items as needed

        adapter = new IconReminderAdapter(getContext(), iconReminderList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new IconReminderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                IconReminder clickedItem = iconReminderList.get(position);
                int drawableId = clickedItem.getSrcIcon();
                if (mListener != null) {
                    mListener.onIconSelected(drawableId); // Notify listener of selected icon
                }
                dismiss(); // Close the fragment dialog
            }
        });


        btnCloseReminderDialog = view.findViewById(R.id.closeReminderDialog);
        btnCloseReminderDialog.setOnClickListener(v -> dismiss());
        return view;
    }


}