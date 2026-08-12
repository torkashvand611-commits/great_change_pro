package com.greatchange.app.models;

import java.util.ArrayList;
import java.util.List;

/**
 * نمایش یک تمرین کامل با تمام ست‌ها
 */
public class WorkoutSession {
    public int exerciseId;           // شناسه تمرین (0-6)
    public String name;              // نام تمرین (اسکوات، پرس سینه، ...)
    public String sets_reps;         // فرمت: "4 ست × 6–8"
    public String rest_time;         // زمان استراحت: "۲–۳ دقیقه"
    public String guide;             // راهنمای اجرا
    public List<ExerciseSet> sets;   // لیست ست‌ها
    public String date;              // تاریخ ثبت (yyyyMMdd)
    public long timestamp;           // زمان ثبت

    public WorkoutSession(int exerciseId, String name, String sets_reps, String rest_time, String guide, String date) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.sets_reps = sets_reps;
        this.rest_time = rest_time;
        this.guide = guide;
        this.date = date;
        this.sets = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();

        // ایجاد ست‌های خالی بر اساس تعداد ست‌ها
        int numSets = sets_reps.startsWith("4") ? 4 : 3;
        for (int i = 1; i <= numSets; i++) {
            this.sets.add(new ExerciseSet(i));
        }
    }

    /**
     * محاسبه درصد تکمیل تمرین
     */
    public int getCompletionPercentage() {
        if (sets.isEmpty()) return 0;
        int completed = 0;
        for (ExerciseSet set : sets) {
            if (set.completed) completed++;
        }
        return (completed * 100) / sets.size();
    }

    /**
     * بررسی آیا تمرین کامل انجام شده
     */
    public boolean isCompleted() {
        return getCompletionPercentage() == 100;
    }

    /**
     * گرفتن تعداد ست‌های انجام‌شده
     */
    public int getCompletedSets() {
        int count = 0;
        for (ExerciseSet set : sets) {
            if (set.completed) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return name + " - " + getCompletedSets() + "/" + sets.size() + " ست";
    }
}
