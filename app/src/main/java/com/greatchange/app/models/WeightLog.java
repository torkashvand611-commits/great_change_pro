package com.greatchange.app.models;

/**
 * نمایش یک ورودی وزن
 */
public class WeightLog {
    public double weight;      // وزن (کیلوگرم)
    public String date;        // تاریخ (yyyyMMdd)
    public long timestamp;     // زمان ثبت

    public WeightLog(double weight, String date) {
        this.weight = weight;
        this.date = date;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return weight + " کیلو - " + date;
    }
}
