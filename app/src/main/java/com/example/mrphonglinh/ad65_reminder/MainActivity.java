package com.example.mrphonglinh.ad65_reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mrphonglinh.ad65_reminder.adapter.ReminderAdapter;
import com.example.mrphonglinh.ad65_reminder.controller.EditController;
import com.example.mrphonglinh.ad65_reminder.dialog.DialogAddRemind;
import com.example.mrphonglinh.ad65_reminder.dialog.DialogDeleteReminder;
import com.example.mrphonglinh.ad65_reminder.dialog.DialogEditReminder;
import com.example.mrphonglinh.ad65_reminder.model.Reminder;
import com.example.mrphonglinh.ad65_reminder.receiver.ReminderAlarmReceiver;
import com.example.mrphonglinh.ad65_reminder.utils.ReminderDBAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    private ListView listView;
    private ReminderDBAdapter reminderDBAdapter;
    private ReminderAdapter reminderAdapter;
    private ArrayList<Reminder> reminders;

    private DialogAddRemind dialogAddRemind;
    private DialogEditReminder dialogEditReminder;
    private DialogDeleteReminder dialogDeleteReminder;
    private static String TAG = MainActivity.class.getSimpleName();

    private Reminder rmSelected;
    private EditController editController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Tạo actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        listView = (ListView) findViewById(R.id.lvReminders);
        reminderDBAdapter = new ReminderDBAdapter(this);
        reminderDBAdapter.open();
        reminders = reminderDBAdapter.fetchAllReminders();
        reminderAdapter = new ReminderAdapter(this,reminders);
        listView.setAdapter(reminderAdapter);

        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAddRemind:
                Toast.makeText(this, "Add new Reminder", Toast.LENGTH_SHORT).show();
                //Show ra dialog để tạo remind
                dialogAddRemind = new DialogAddRemind(this,reminderDBAdapter);
                dialogAddRemind.show();
                return true;
            case R.id.menuExit:
                finish();
                return true;
            default:
                return true;
        }
    }

    //Xử lý cho context menu

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        rmSelected = reminders.get(position);
        switch (item.getItemId()){
            case R.id.menu_edit:
                //Tạo thằng dialog
                dialogEditReminder = new DialogEditReminder(this,rmSelected,reminderDBAdapter);
                dialogEditReminder.show();
                break;
            case R.id.menu_delete:
                dialogDeleteReminder = new DialogDeleteReminder(this,rmSelected,reminderDBAdapter);
                dialogDeleteReminder.show();
                break;
            case R.id.menu_scheduler:
                //show ra timepicker dialog
                showTimePickerDialog();
                break;
        }
        return super.onContextItemSelected(item);
    }

    //Ham xu ly hien thi timepicker dialog
    private void showTimePickerDialog() {
        //Su dung ngay hom nay la thoi gian co ban de bao thuc
        //Hop thoai lua chon thoi gian cho phep chon gio, phut
        final Date today = new Date();
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date alarm = new Date(today.getYear(),today.getMonth(),today.getDate(), hourOfDay,minute);

                //Goi phuong thuc schedulerReminder() de hien thi noi dung bao thuc va thoi gian
                schedulerReminder(alarm.getTime(), rmSelected.getContent());
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                listener,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE,
                true);
        timePickerDialog.show();
    }

    private void schedulerReminder(long time, String content) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, ReminderAlarmReceiver.class);
        alarmIntent.putExtra(ReminderAlarmReceiver.REMINDER_TEXT,content);
        PendingIntent broadcast = PendingIntent.getBroadcast(this,0,alarmIntent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, broadcast);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        //Gọi lại hàm load lại database
        reminders.clear();
        reminders = reminderDBAdapter.fetchAllReminders();
        reminderAdapter = new ReminderAdapter(this,reminders);
        listView.setAdapter(reminderAdapter);
    }


}
