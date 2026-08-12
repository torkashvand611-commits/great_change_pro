package com.greatchange.app.models;

/**
 * نمایش یک تمرین ذهنی
 */
public class MentalExercise {
    public int id;              // شناسه
    public String name;         // نام تمرین
    public String description;  // توضیحات اجرا
    public String date;         // تاریخ (yyyyMMdd)
    public boolean completed;   // انجام شده؟
    public long timestamp;      // زمان ثبت

    public MentalExercise(int id, String name, String description, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.completed = false;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return name + (completed ? " ✅" : " ⬜");
    }
}
