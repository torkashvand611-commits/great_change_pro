package com.greatchange.app.models;

/**
 * نمایش وضعیت روز
 */
public class DailyProgress {
    public String date;              // تاریخ (yyyyMMdd)
    public int dayNumber;            // شماره روز (1-90)
    public int workoutStatus;        // 0: انجام نشده، 1: جزئی، 2: کامل
    public int mentalStatus;         // 0: انجام نشده، 1: جزئی، 2: کامل
    public int nutritionStatus;      // 0: انجام نشده، 1: جزئی، 2: کامل
    public boolean weightLogged;     // وزن ثبت شد؟
    public boolean streakMaintained; // زنجیره حفظ شد؟
    public long timestamp;           // زمان ثبت

    public DailyProgress(String date, int dayNumber) {
        this.date = date;
        this.dayNumber = dayNumber;
        this.workoutStatus = 0;
        this.mentalStatus = 0;
        this.nutritionStatus = 0;
        this.weightLogged = false;
        this.streakMaintained = false;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * محاسبه درصد تکمیل روز
     */
    public int getDayCompletionPercentage() {
        int total = 0;
        if (workoutStatus > 0) total += 25;
        if (mentalStatus > 0) total += 25;
        if (nutritionStatus > 0) total += 25;
        if (weightLogged) total += 25;
        return total;
    }

    /**
     * آیا روز کامل انجام شده
     */
    public boolean isDayComplete() {
        return workoutStatus == 2 && mentalStatus == 2 && nutritionStatus == 2;
    }

    @Override
    public String toString() {
        return "روز " + dayNumber + " - " + getDayCompletionPercentage() + "%";
    }
}
