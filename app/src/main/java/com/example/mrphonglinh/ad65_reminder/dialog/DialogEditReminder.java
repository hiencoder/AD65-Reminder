package com.example.mrphonglinh.ad65_reminder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mrphonglinh.ad65_reminder.R;
import com.example.mrphonglinh.ad65_reminder.controller.EditController;
import com.example.mrphonglinh.ad65_reminder.model.Reminder;
import com.example.mrphonglinh.ad65_reminder.utils.ReminderDBAdapter;

/**
 * Created by MyPC on 19/03/2017.
 */

public class DialogEditReminder extends Dialog implements View.OnClickListener{
    private EditText txtContent;
    private ImageView btnCheckImportant;
    private Button btnCancel, btnCommit;
    private Context mContext;
    private Reminder rmSelected;
    private boolean checked;
    private int important = 0;
    private ReminderDBAdapter reminderDBAdapter;

    public DialogEditReminder(Context context, Reminder reminder, ReminderDBAdapter reminderDBAdapter) {
        super(context);
        setContentView(R.layout.lt_dialog_edit_remind);
        setTitle("Edit Reminder");
        this.mContext = context;
        this.rmSelected = reminder;
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

        //Set các thuộc tính
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"android_7.ttf");
        txtContent.setTypeface(typeface);
        txtContent.setText(rmSelected.getContent());
        if (rmSelected.getImportant() == 1){
            btnCheckImportant.setSelected(true);
            checked = true;
        }else {
            btnCheckImportant.setSelected(false);
            checked = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCheckImportant:
                checked = !checked;
                if (checked){
                    btnCheckImportant.setSelected(true);
                    important = 1;
                }else {
                    btnCheckImportant.setSelected(false);
                    important = 0;
                }
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnCommit:
                String content = txtContent.getText().toString();
                rmSelected.setContent(content);
                rmSelected.setImportant(important);
                reminderDBAdapter.updateReminder(rmSelected);
                dismiss();
                break;
        }
    }

}
