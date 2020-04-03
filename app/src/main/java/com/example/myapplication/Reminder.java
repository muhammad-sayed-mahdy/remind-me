package com.example.myapplication;

/**
 * Created by engMa_000 on 2017-04-03.
 */

public class Reminder {


    private long mId;
    private String mContent;
    private int mImportant;
    public Reminder(long id, String content, int important) {
        mId = id;
        mImportant = important;
        mContent = content;
    }
    public long getId() {
        return mId;
    }
    public void setId(long id) {
        mId = id;
    }
    public int getImportant() {
        return mImportant;
    }
    public void setImportant(boolean important) {
        mImportant = important ? 1 : 0;
    }

    public void setImportant(int important) {
        mImportant = important;
    }
    public String getContent() {
        return mContent;
    }
    public void setContent(String content) {
        mContent = content;
    }

}
