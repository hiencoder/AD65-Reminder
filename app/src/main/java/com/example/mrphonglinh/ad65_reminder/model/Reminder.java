package com.example.mrphonglinh.ad65_reminder.model;

/**
 * Created by MyPC on 19/03/2017.
 */

public class Reminder {
    private int id;
    private String content;
    private int important;

    public Reminder(int id, String content, int important) {
        this.id = id;
        this.content = content;
        this.important = important;
    }

    public Reminder(String content, int important) {
        this.content = content;
        this.important = important;
    }

    public Reminder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }
}
