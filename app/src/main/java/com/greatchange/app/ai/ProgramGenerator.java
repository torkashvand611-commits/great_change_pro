package com.greatchange.app.ai;

import com.greatchange.app.models.PersonProfile;
import com.greatchange.app.models.WorkoutSession;
import com.greatchange.app.models.MentalExercise;
import java.util.ArrayList;
import java.util.List;

/**
 * تولیدکننده برنامه شخصی‌سازی‌شده بر اساس پروفایل کاربر
 */
public class ProgramGenerator {
    private PersonProfile profile;
    
    public ProgramGenerator(PersonProfile profile) {
        this.profile = profile;
    }
    
    /**
     * تولید برنامه تمرینی شخصی
     */
    public List<WorkoutSession> generateWorkoutProgram() {
        List<WorkoutSession> workouts = new ArrayList<>();
        
        // بر اساس سطح تجربه و وزن
        if (profile.experienceLevel == 1) {
            // برنامه مبتدی
            workouts.add(new WorkoutSession(0, "اسکوات", "3 ست × 8–10", "۲ دقیقه", "شروع ملایم - بدون وزنه", ""));
            workouts.add(new WorkoutSession(1, "پرس سینه", "3 ست × 8–10", "۲ دقیقه", "شروع با وزن کم", ""));
            workouts.add(new WorkoutSession(6, "Farmer Walk", "2 × 30 ثانیه", "۱ دقیقه", "راه رفتن ساده", ""));
        } else if (profile.experienceLevel == 2) {
            // برنامه مبتدی‌ام بهتر
            workouts.add(new WorkoutSession(0, "اسکوات", "3 ست × 6–8", "۲–۳ دقیقه", "افزایش وزنه آهسته", ""));
            workouts.add(new WorkoutSession(1, "پرس سینه", "3 ست × 6–8", "۲–۳ دقیقه", "افزایش وزنه", ""));
            workouts.add(new WorkoutSession(2, "بارفیکس", "3 ست × 6–8", "۲ دقیقه", "استفاده از تسمه کمکی", ""));
        } else {
            // برنامه پیشرفته
            workouts.add(new WorkoutSession(0, "اسکوات", "4 ست × 6–8", "۲–۳ دقیقه", "وزن سنگین", ""));
            workouts.add(new WorkoutSession(1, "پرس سینه", "4 ست × 6–8", "۲–۳ دقیقه", "وزن سنگین", ""));
            workouts.add(new WorkoutSession(3, "ددلیفت", "3 ست × 8", "۲–۳ دقیقه", "فنون پیشرفته", ""));
        }
        
        return workouts;
    }
    
    /**
     * تولید برنامه غذایی
     */
    public NutritionPlan generateNutritionPlan() {
        NutritionPlan plan = new NutritionPlan();
        
        // محاسبه کالری بر اساس هدف
        if (profile.dietType.equals("کاهش وزن")) {
            plan.dailyCalories = 1800; // برای کاهش وزن
            plan.proteinGrams = 150;
            plan.carbsPercentage = 35;
            plan.fatPercentage = 30;
        } else if (profile.dietType.equals("توسعه عضلات")) {
            plan.dailyCalories = 2500;
            plan.proteinGrams = 200;
            plan.carbsPercentage = 45;
            plan.fatPercentage = 25;
        } else {
            plan.dailyCalories = 2000;
            plan.proteinGrams = 150;
            plan.carbsPercentage = 40;
            plan.fatPercentage = 30;
        }
        
        plan.mealsPerDay = profile.mealsPerDay;
        plan.allergies = profile.allergies;
        
        // طراحی وعده‌ها
        plan.breakfast = "تخم‌مرغ + نان + میوه";
        plan.lunch = "مرغ / گوشت + برنج + سبزیجات";
        plan.dinner = "ماهی / لبنیات + سبزیجات";
        plan.snacks = "آجیل / پروتئین‌شیک";
        
        return plan;
    }
    
    /**
     * تولید برنامه ذهنیت و کاریزما
     */
    public List<MentalExercise> generateMentalProgram() {
        List<MentalExercise> exercises = new ArrayList<>();
        
        if (profile.confidenceLevel < 5) {
            // برنامه مبتدی اعتماد‌به‌نفس
            exercises.add(new MentalExercise(0, "تنفس آرام ۵ دقیقه‌ای", "۲ دقیقه تنفس گرفتن و ۳ دقیقه تصویرسازی نسخه بهتر خودت", ""));
            exercises.add(new MentalExercise(1, "مرور روزانه", "سه نقطه مثبت امروز را یادداشت کن", ""));
            exercises.add(new MentalExercise(2, "تمرین اراده", "یک کار ترسناک کوچک انجام بده (تماس با دوست، صحبت در گروه)", ""));
        } else if (profile.confidenceLevel < 8) {
            // برنامه متوسط
            exercises.add(new MentalExercise(0, "تصویرسازی پیشرفته", "خودت را به عنوان نسخه بهتر تصور کن و با آن حرکت کن", ""));
            exercises.add(new MentalExercise(1, "کاریزما - تماس چشمی", "در هر گفتگو با تماس چشمی و لبخند گفتگو کن", ""));
            exercises.add(new MentalExercise(2, "رهبری - تصمیم‌گیری", "یک تصمیم بزرگ برای دیگران بگیر و اجرا کن", ""));
        } else {
            // برنامه پیشرفته
            exercises.add(new MentalExercise(0, "مدیتیشن عمیق ۲۰ دقیقه‌ای", "ذهن را کاملاً آزاد کن", ""));
            exercises.add(new MentalExercise(1, "رهبری تیم", "یک تیم کوچک را هدایت کن", ""));
            exercises.add(new MentalExercise(2, "تأثیرگذاری اجتماعی", "برای دیگران الهام‌بخش باش", ""));
        }
        
        return exercises;
    }
    
    /**
     * تولید پیام انگیزشی شخصی‌شده
     */
    public String generateMotivationalMessage() {
        StringBuilder message = new StringBuilder();
        message.append("سلام ").append(profile.name).append("! 🔥\n\n");
        message.append("برنامه ۹۰ روزه تو آماده است:\n\n");
        
        message.append("🎯 هدف: ").append(profile.mainGoal).append("\n");
        message.append("💪 وزن: ").append(profile.currentWeight).append(" kg → ").append(profile.targetWeight).append(" kg\n");
        message.append("📅 مدت: ۹۰ روز (۱۲ هفته)\n");
        message.append("⏰ روزانه: ").append(calculateDailyTime()).append(" ساعت\n\n");
        
        message.append("برنامه شامل:\n");
        message.append("✅ تمرین شخصی‌شده\n");
        message.append("✅ برنامه غذایی\n");
        message.append("✅ تمرین‌های ذهنی\n");
        message.append("✅ توسعه کاریزما و اعتماد‌به‌نفس\n\n");
        
        message.append("\"کم، پیوسته، قدرتمند\"");
        
        return message.toString();
    }
    
    private String calculateDailyTime() {
        int trainingHours = profile.trainingDaysPerWeek / 2; // فرض: هر تمرین ۱ ساعت
        int mentalMinutes = 30;
        return trainingHours + " ساعت + ۳۰ دقیقه ذهنیت";
    }
    
    /**
     * کلاس برنامه غذایی
     */
    public static class NutritionPlan {
        public int dailyCalories;
        public int proteinGrams;
        public int carbsPercentage;
        public int fatPercentage;
        public int mealsPerDay;
        public String allergies;
        public String breakfast;
        public String lunch;
        public String dinner;
        public String snacks;
    }
}
