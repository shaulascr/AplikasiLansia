package com.alya.aplikasilansia.ui.reminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FilteredReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_REMINDER = 1;
    private List<Object> items;
    public void updateList(List<Object> newItems) {
        items.clear(); // Clear the existing items
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public FilteredReminderAdapter(List<Object> items) {
        this.items = items;
    }
    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof String) {
            return VIEW_TYPE_HEADER;
        } else if (item instanceof Reminder) {
            return VIEW_TYPE_REMINDER;
        }
        return -1;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (items.get(position) instanceof String) {
//            return VIEW_TYPE_HEADER;
//        } else {
//            return VIEW_TYPE_REMINDER;
//        }
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_reminder, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filtered_reminder, parent, false);
            return new ReminderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            if (position == 0 || !(items.get(position - 1) instanceof String)) {
                ((HeaderViewHolder) holder).bind((String) items.get(position));
            } else {
                ((HeaderViewHolder) holder).hide();
            }
        } else if (holder instanceof ReminderViewHolder) {
            ((ReminderViewHolder) holder).bind((Reminder) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.tv_reminder_header);
        }

        public void bind(String title) {
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(title);
        }

        public void hide() {
            headerTitle.setVisibility(View.GONE);
        }
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_titleReminder;
        TextView tv_timeReminder;
        TextView tv_dayTitleReminder;
        TextView tv_descReminder;
        ImageView img_IcReminder;
        Button btnDelRemind;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            tv_titleReminder = itemView.findViewById(R.id.tv_reminder_name_filter);
            tv_timeReminder = itemView.findViewById(R.id.tv_reminder_time);
            tv_descReminder = itemView.findViewById(R.id.tv_desc_reminder_filter);
            img_IcReminder = itemView.findViewById(R.id.img_reminder_ic);

            btnDelRemind = itemView.findViewById(R.id.btn_del_remind);
            btnDelRemind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(Reminder reminder) {
            tv_titleReminder.setText(reminder.getTitle());
            tv_timeReminder.setText(formatDate(reminder.getTimestamp()));
//            tv_dayTitleReminder.setText(reminder.getDay());
            tv_descReminder.setText(reminder.getDesc());
            img_IcReminder.setImageResource(reminder.getIcon());
        }

        private String formatDate(String timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM 'pukul' HH:mm", new Locale("id", "ID"));
            try {
                Date date = sdf.parse(timestamp);
                return outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return timestamp;
            }
        }
    }

}
