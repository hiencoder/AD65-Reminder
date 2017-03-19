package com.example.mrphonglinh.ad65_reminder.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrphonglinh.ad65_reminder.R;
import com.example.mrphonglinh.ad65_reminder.model.Reminder;

import java.util.ArrayList;

/**
 * Created by MyPC on 19/03/2017.
 */

public class ReminderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Reminder> reminders;

    public ReminderAdapter(Context mContext, ArrayList<Reminder> reminders) {
        this.mContext = mContext;
        this.reminders = reminders;
    }

    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Reminder getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_reminder,parent,false);
        }
        View txtTab = convertView.findViewById(R.id.tabRow);
        TextView txtContent = (TextView) convertView.findViewById(R.id.txtRemind);
        Reminder reminder = getItem(position);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"android_7.ttf");
        txtContent.setTypeface(typeface);
        txtContent.setText(reminder.getContent());
        if (reminder.getImportant() == 1){
            //Nếu nhắc nhở là quan trọng
            txtTab.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        }else {
            txtTab.setBackgroundColor(mContext.getResources().getColor(R.color.color_green));
        }
        return convertView;
    }

    class ViewHolder{

    }
}
