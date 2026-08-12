package com.greatchange.app.models;

/**
 * پروفایل کاربر و اطلاعات شخصی
 */
public class PersonProfile {
    // اطلاعات پایه
    public String name;
    public int age;
    public String gender; // "مرد" یا "زن"
    public double currentWeight;
    public double targetWeight;
    public double height;
    
    // تمرین
    public int experienceLevel; // 1: بدون تجربه، 2: مبتدی، 3: متوسط، 4: پیشرفته
    public int trainingDaysPerWeek; // 3-6
    public String injuries; // مصدومیت‌ها
    
    // تغذیه
    public String dietType; // "کاهش وزن"، "توسعه عضلات"، "توازن"
    public String allergies; // الرژی‌ها
    public int mealsPerDay; // تعداد وعده‌های غذایی
    
    // ذهنیت
    public int confidenceLevel; // 1-10
    public int socialAnxiety; // 1-10 (کم = 1)
    public String mentalGoals; // "کاریزما"، "رهبری"، "جذابیت"
    
    // اراده و اعتماد‌به‌نفس
    public int willpower; // 1-10
    public String biggestChallenge; // بزرگ‌ترین چالش
    public int failureHistory; // تعداد شکست‌های قبلی
    
    // اهداف کلی
    public String mainGoal; // هدف اصلی
    public int motivationLevel; // 1-10
    public String timeAvailable; // "کم"، "متوسط"، "زیاد"
    
    public PersonProfile() {
    }

    @Override
    public String toString() {
        return name + " | " + age + " سال | " + currentWeight + " کیلو → " + targetWeight + " کیلو";
    }
}
