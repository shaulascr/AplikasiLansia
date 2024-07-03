package com.alya.aplikasilansia.ui.reminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FilteredReminderAdapter extends RecyclerView.Adapter<FilteredReminderAdapter.ReminderViewHolder> {
    private List<FilteredReminder> reminderList;

    public FilteredReminderAdapter(List<FilteredReminder> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filtered_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredReminderAdapter.ReminderViewHolder holder, int position) {
        FilteredReminder filteredReminder = reminderList.get(position);
        holder.bind(filteredReminder);
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_titleReminder;
        TextView tv_timeReminder;
        TextView tv_dayReminder;
        TextView tv_dayTitleReminder;
        TextView tv_descReminder;
        ImageView img_IcReminder;
        public ReminderViewHolder(View itemView) {
            super(itemView);
            tv_titleReminder = itemView.findViewById(R.id.tv_reminder_name_filter);
            tv_timeReminder = itemView.findViewById(R.id.tv_reminder_date_filter);
//            tv_dayReminder = itemView.findViewById(R.id.);
            tv_dayTitleReminder = itemView.findViewById(R.id.tv_reminder_day_filter);
            tv_descReminder = itemView.findViewById(R.id.tv_desc_reminder_filter);
            img_IcReminder = itemView.findViewById(R.id.img_reminder_ic);

        }
        public void bind (FilteredReminder filteredReminder) {
            tv_titleReminder.setText(filteredReminder.getTitleReminder());
//            tv_timeReminder.setText(filteredReminder.getTimeReminder());
//            String dayLabel = getDayLabel(filteredReminder.getTimeReminder());
//            tv_dayTitleReminder.setText(dayLabel);
//            // Set the day label TextView
//            if (showTitleDay) {
//                tv_dayTitleReminder.setVisibility(View.VISIBLE);
//                tv_dayTitleReminder.setText(filteredReminder.getTitleDayReminder());
//            } else {
//                tv_dayTitleReminder.setVisibility(View.GONE);
//            }
            tv_dayTitleReminder.setText(filteredReminder.getTitleDayReminder());
            tv_descReminder.setText(filteredReminder.getDescReminder());
            img_IcReminder.setImageResource(filteredReminder.getImgReminder());
        }

        private String getDayLabel(String dayReminder) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date reminderDate = sdf.parse(dayReminder);

                Calendar today = Calendar.getInstance();
                Calendar reminderCalendar = Calendar.getInstance();
                reminderCalendar.setTime(reminderDate);

                if (isSameDay(today, reminderCalendar)) {
                    return "Hari Ini";
                } else if (isTomorrow(today, reminderCalendar)) {
                    return "Besok";
                } else {
                    Calendar tomorrow = (Calendar) today.clone();
                    tomorrow.add(Calendar.DAY_OF_YEAR, 1);
                    if (reminderCalendar.after(tomorrow)) {
                        return "Yang Akan Datang";
                    } else {
                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                        return dayFormat.format(reminderDate);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Label Default"; // Handle parsing exception or return default label
            }
        }

        private boolean isSameDay(Calendar cal1, Calendar cal2) {
            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        }

        private boolean isTomorrow(Calendar cal1, Calendar cal2) {
            Calendar tomorrow = (Calendar) cal1.clone();
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);
            return tomorrow.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    tomorrow.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        }

    }
}
