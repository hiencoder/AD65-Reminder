package com.example.mrphonglinh.ad65_reminder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mrphonglinh.ad65_reminder.R;
import com.example.mrphonglinh.ad65_reminder.model.Reminder;
import com.example.mrphonglinh.ad65_reminder.utils.ReminderDBAdapter;

/**
 * Created by MyPC on 19/03/2017.
 */

public class DialogDeleteReminder extends Dialog implements View.OnClickListener{
    private TextView txtContent;
    private Button btnCancel, btnOk;
    private Reminder rmSelected;
    private ReminderDBAdapter reminderDBAdapter;
    private Context mContext;
    public DialogDeleteReminder(Context context, Reminder reminder, ReminderDBAdapter reminderDBAdapter) {
        super(context);
        setContentView(R.layout.lt_dialog_delete_reminder);
        setTitle("Delete Reminder");
        this.mContext = context;
        this.rmSelected = reminder;
        this.reminderDBAdapter = reminderDBAdapter;

        initView();
        initEvent();
    }

    private void initView() {
        txtContent = (TextView) findViewById(R.id.txtContent);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"android_7.ttf");
        txtContent.setTypeface(typeface);
        txtContent.setText("Bạn muốn xóa " + rmSelected.getContent());
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk = (Button) findViewById(R.id.btnDelete);
    }

    private void initEvent() {
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnDelete:
                reminderDBAdapter.deleteReminderById(rmSelected.getId());
                dismiss();
                break;
        }
    }
}
