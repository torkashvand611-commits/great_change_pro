package com.greatchange.app.models;

/**
 * نمایش یک ست تمرین
 * شامل: تکرار، وزن، وضعیت انجام
 */
public class ExerciseSet {
    public int setNumber;      // شماره ست (1, 2, 3, ...)
    public int reps;           // تعداد تکرار
    public double weight;      // وزن وزنه (کیلوگرم)
    public boolean completed;  // آیا انجام شده؟
    public long timestamp;     // زمان ثبت

    public ExerciseSet(int setNumber) {
        this.setNumber = setNumber;
        this.reps = 0;
        this.weight = 0.0;
        this.completed = false;
        this.timestamp = System.currentTimeMillis();
    }

    public ExerciseSet(int setNumber, int reps, double weight, boolean completed) {
        this.setNumber = setNumber;
        this.reps = reps;
        this.weight = weight;
        this.completed = completed;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return setNumber + " ست → " + reps + " تکرار → " + weight + " کیلو" + (completed ? " ✅" : " ⬜");
    }
}
