package com.example.mrphonglinh.ad65_reminder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrphonglinh.ad65_reminder.R;
import com.example.mrphonglinh.ad65_reminder.model.Reminder;
import com.example.mrphonglinh.ad65_reminder.utils.ReminderDBAdapter;

/**
 * Created by MyPC on 19/03/2017.
 */

public class DialogAddRemind extends Dialog implements View.OnClickListener{
    private EditText txtContent;
    private ImageView btnCheckImportant;
    private Button btnCancel, btnCommit;
    private Context mContext;
    private ReminderDBAdapter reminderDBAdapter;
    //Biến kiểm tra trạng thái check
    private boolean check = false;

    //Biến important
    private int important = 0;

    public DialogAddRemind(Context context, ReminderDBAdapter reminderDBAdapter) {
        super(context);
        setContentView(R.layout.lt_dialog_add_remind);
        setTitle("Add new reminder");
        this.mContext = context;
        this.reminderDBAdapter = reminderDBAdapter;
        initView();
        initEvent();
    }

    private void initEvent() {
        btnCheckImportant.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    private void initView() {
        txtContent = (EditText) findViewById(R.id.txtContent);
        btnCheckImportant = (ImageView) findViewById(R.id.btnCheckImportant);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCommit = (Button) findViewById(R.id.btnCommit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                //Đóng dialog
                dismiss();
                break;
            case R.id.btnCommit:
                //Thực hiện lưu vào database
                String content = txtContent.getText().toString().trim();
                Reminder reminder = new Reminder(content,important);
                reminderDBAdapter.createReminder(reminder);
                //đóng dialog
                dismiss();
                break;
            case R.id.btnCheckImportant:
                check = !check;
                if (check){
                    //Nếu chưa check
                    btnCheckImportant.setSelected(true);
                    important = 1;
                }else {
                    btnCheckImportant.setSelected(false);
                    important = 0;
                }
                break;
        }
    }

    public void show(){
        if (!isShowing()){
            super.show();
        }
    }
}
