package com.greatchange.app.utils;

import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * کلاس کمکی برای محاسبات پیشرفت و روزها
 */
public class ProgressCalculator {
    private SharedPreferences prefs;
    private static final int TOTAL_DAYS = 90;

    public ProgressCalculator(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    /**
     * گرفتن تاریخ امروز به صورت yyyyMMdd
     */
    public String getTodayDate() {
        return new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
    }

    /**
     * گرفتن شماره روز برنامه
     */
    public int getCurrentDay() {
        long startTime = prefs.getLong("program_start", System.currentTimeMillis());
        prefs.edit().putLong("program_start", startTime).apply();
        
        long daysPassed = (System.currentTimeMillis() - startTime) / (86400000L);
        return Math.min(TOTAL_DAYS, (int) daysPassed + 1);
    }

    /**
     * گرفتن روزهای باقی‌مانده
     */
    public int getRemainingDays() {
        return TOTAL_DAYS - getCurrentDay();
    }

    /**
     * محاسبه درصد پیشرفت کلی برنامه
     */
    public int getProgramProgressPercentage() {
        return (getCurrentDay() * 100) / TOTAL_DAYS;
    }

    /**
     * شمارش روزهای کامل‌شده
     */
    public int getCompletedDaysCount() {
        int count = 0;
        long startTime = prefs.getLong("program_start", System.currentTimeMillis());
        
        for (int i = 0; i < getCurrentDay(); i++) {
            long dayTime = startTime + (i * 86400000L);
            String dayDate = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date(dayTime));
            
            String progressKey = "progress_" + dayDate;
            String progressJson = prefs.getString(progressKey, null);
            
            // اگر روز پر‌شده بود، بزن یک
            if (progressJson != null) {
                count++;
            }
        }
        
        return count;
    }

    /**
     * بررسی زنجیره روزهای پی‌درپی
     */
    public int getCurrentStreak() {
        int streak = 0;
        long startTime = prefs.getLong("program_start", System.currentTimeMillis());
        
        for (int i = getCurrentDay() - 1; i >= 0; i--) {
            long dayTime = startTime + (i * 86400000L);
            String dayDate = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date(dayTime));
            
            String progressKey = "progress_" + dayDate;
            String progressJson = prefs.getString(progressKey, null);
            
            if (progressJson != null) {
                streak++;
            } else {
                break;
            }
        }
        
        return streak;
    }

    /**
     * محاسبه وضعیت امروز (0: شروع نشده، 1: جزئی، 2: کامل)
     */
    public int getTodayWorkoutStatus() {
        String today = getTodayDate();
        
        int completedSets = 0;
        int totalSets = 0;
        
        // بررسی تمام تمرین‌های امروز
        for (int i = 0; i < 7; i++) {
            String workoutKey = "workout_" + today + "_" + i;
            String json = prefs.getString(workoutKey, null);
            
            if (json != null) {
                // شمارش ست‌های انجام‌شده
                // این محاسبه ساده است و می‌تواند بهتر شود
                totalSets++;
                if (json.contains("\"completed\":true")) {
                    completedSets++;
                }
            }
        }
        
        if (totalSets == 0) return 0;
        if (completedSets == totalSets) return 2;
        return 1;
    }

    /**
     * محاسبه وضعیت ذهنیت امروز
     */
    public int getTodayMentalStatus() {
        String today = getTodayDate();
        
        int completedMental = 0;
        int totalMental = 0;
        
        // بررسی تمام تمرین‌های ذهنی
        for (int i = 0; i < 6; i++) {
            String mentalKey = "mental_" + today + "_" + i;
            String json = prefs.getString(mentalKey, null);
            
            if (json != null) {
                totalMental++;
                if (json.contains("\"completed\":true")) {
                    completedMental++;
                }
            }
        }
        
        if (totalMental == 0) return 0;
        if (completedMental == totalMental) return 2;
        return 1;
    }

    /**
     * محاسبه وضعیت تغ��یه امروز
     */
    public int getTodayNutritionStatus() {
        String today = getTodayDate();
        String nutritionKey = "nutrition_" + today;
        String status = prefs.getString(nutritionKey, "0");
        return Integer.parseInt(status);
    }

    /**
     * بررسی آیا وزن امروز ثبت شده
     */
    public boolean isWeightLoggedToday() {
        String today = getTodayDate();
        return prefs.getString("weight_" + today, null) != null;
    }

    /**
     * گرفتن وزن فعلی
     */
    public double getCurrentWeight() {
        String today = getTodayDate();
        String weightJson = prefs.getString("weight_" + today, null);
        
        if (weightJson != null) {
            try {
                int startIdx = weightJson.indexOf("\"weight\":") + 9;
                int endIdx = weightJson.indexOf(",", startIdx);
                if (endIdx == -1) {
                    endIdx = weightJson.indexOf("}", startIdx);
                }
                String weightStr = weightJson.substring(startIdx, endIdx).trim();
                return Double.parseDouble(weightStr);
            } catch (Exception e) {
                return 0;
            }
        }
        
        return 0;
    }

    /**
     * گرفتن وزن دیروز
     */
    public double getPreviousDayWeight() {
        long yesterday = System.currentTimeMillis() - 86400000L;
        String yesterdayDate = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date(yesterday));
        String weightJson = prefs.getString("weight_" + yesterdayDate, null);
        
        if (weightJson != null) {
            try {
                int startIdx = weightJson.indexOf("\"weight\":") + 9;
                int endIdx = weightJson.indexOf(",", startIdx);
                if (endIdx == -1) {
                    endIdx = weightJson.indexOf("}", startIdx);
                }
                String weightStr = weightJson.substring(startIdx, endIdx).trim();
                return Double.parseDouble(weightStr);
            } catch (Exception e) {
                return 0;
            }
        }
        
        return getCurrentWeight();
    }
}
