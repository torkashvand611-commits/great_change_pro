package com.greatchange.app.ai;

import android.content.SharedPreferences;
import com.greatchange.app.models.PersonProfile;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * موتور AI برای تجزیه جوابات و تولید برنامه
 */
public class AICoach {
    private SharedPreferences prefs;
    private PersonProfile profile;
    
    public AICoach(SharedPreferences prefs) {
        this.prefs = prefs;
        this.profile = new PersonProfile();
    }
    
    /**
     * ذخیره جواب کاربر
     */
    public void saveAnswer(String key, String answer) {
        prefs.edit().putString("assessment_" + key, answer).apply();
    }
    
    /**
     * ساخت پروفایل از جوابات
     */
    public PersonProfile buildProfileFromAnswers() {
        profile.name = prefs.getString("assessment_نام", "دوست");
        profile.age = parseInt(prefs.getString("assessment_سن", "25"));
        profile.gender = prefs.getString("assessment_جنسیت", "مرد");
        profile.height = parseDouble(prefs.getString("assessment_قد", "175"));
        profile.currentWeight = parseDouble(prefs.getString("assessment_وزن فعلی", "80"));
        profile.targetWeight = parseDouble(prefs.getString("assessment_وزن هدف", "75"));
        
        String experienceStr = prefs.getString("assessment_سطح تجربه", "مبتدی");
        profile.experienceLevel = mapExperience(experienceStr);
        
        profile.trainingDaysPerWeek = parseInt(prefs.getString("assessment_روزهای تمرینی", "4"));
        profile.injuries = prefs.getString("assessment_مصدومیت", "ندارد");
        
        profile.dietType = prefs.getString("assessment_هدف تغذیه", "توازن");
        profile.allergies = prefs.getString("assessment_الرژی", "ندارد");
        profile.mealsPerDay = parseInt(prefs.getString("assessment_وعده‌های غذایی", "3"));
        
        profile.confidenceLevel = parseInt(prefs.getString("assessment_اعتماد‌به‌نفس", "5"));
        profile.socialAnxiety = parseInt(prefs.getString("assessment_اضطراب اجتماعی", "5"));
        profile.mentalGoals = prefs.getString("assessment_اهداف کاریزما", "جذابیت");
        
        profile.willpower = parseInt(prefs.getString("assessment_اراده", "5"));
        profile.biggestChallenge = prefs.getString("assessment_بزرگ‌ترین چالش", "حفظ انگیزه");
        profile.failureHistory = parseInt(prefs.getString("assessment_تاریخچه شکست", "2"));
        
        profile.mainGoal = prefs.getString("assessment_هدف اصلی", "بدن سالم و قوی");
        profile.motivationLevel = parseInt(prefs.getString("assessment_انگیزه", "8"));
        profile.timeAvailable = prefs.getString("assessment_ساعات روزانه", "2");
        
        return profile;
    }
    
    /**
     * ایجاد تجزیه شخصی‌شده
     */
    public String generatePersonalAnalysis() {
        StringBuilder analysis = new StringBuilder();
        
        // تجزیه وزن
        double weightDiff = profile.currentWeight - profile.targetWeight;
        analysis.append("📊 **تجزیه وضعیت شما:**\n\n");
        
        if (weightDiff > 0) {
            analysis.append("💪 نیاز به کاهش ").append(String.format("%.1f", weightDiff)).append(" کیلوگرم\n");
            analysis.append("📍 برنامه: تمرین ۶ روزه + کالری کم\n\n");
        } else {
            analysis.append("🏋️ نیاز به توسعه عضلات\n");
            analysis.append("📍 برنامه: تمرین سنگین + کالری بیش\n\n");
        }
        
        // تجزیه اعتماد‌به‌نفس
        analysis.append("🧠 **اعتماد‌به‌نفس و ذهنیت:**\n");
        if (profile.confidenceLevel < 5) {
            analysis.append("⚠️ اعتماد‌به‌نفس پایین - برنامه تمرین ذهنی مکثف\n");
        } else if (profile.confidenceLevel < 8) {
            analysis.append("✅ اعتماد‌به‌نفس متوسط - توسعه کاریزما\n");
        } else {
            analysis.append("🔥 اعتماد‌به‌نفس بالا - رهبری و تأثیرگذاری\n");
        }
        analysis.append("\n");
        
        // تجزیه اراده
        analysis.append("💎 **نقطه قوت شما:**\n");
        if (profile.willpower >= 8) {
            analysis.append("✨ اراده و تحمل بالا - می‌تونی برنامه سخت‌تری انجام بدی\n");
        } else if (profile.willpower >= 5) {
            analysis.append("✨ اراده متوسط - به یاد‌دهندگی و حمایت نیاز داری\n");
        } else {
            analysis.append("✨ نیاز به ساخت عادات آهسته اما محکم\n");
        }
        analysis.append("\n");
        
        // بزرگ‌ترین چالش
        analysis.append("⚡ **بزرگ‌ترین چالش شما: ").append(profile.biggestChallenge).append("\n");
        analysis.append("💡 راه‌حل: پیشگیری، یاد‌دهندگی روزانه، گروه‌های حمایتی\n\n");
        
        return analysis.toString();
    }
    
    /**
     * توصیه‌های شخصی‌شده
     */
    public String generateRecommendations() {
        StringBuilder recommendations = new StringBuilder();
        
        recommendations.append("🎯 **توصیه‌های شخصی برای ").append(profile.name).append(":**\n\n");
        
        // توصیه تمرین
        recommendations.append("1️⃣ **تمرین:**\n");
        recommendations.append("   • ").append(profile.trainingDaysPerWeek).append(" روز در هفته\n");
        recommendations.append("   • ۶۰ دقیقه در هر جلسه\n");
        recommendations.append("   • فوکوس بر ").append(profile.dietType.equals("کاهش وزن") ? "کاهش وزن" : "توسعه عضلات").append("\n\n");
        
        // توصیه غذایی
        recommendations.append("2️⃣ **تغذیه:**\n");
        recommendations.append("   • ").append(profile.mealsPerDay).append(" وعده در روز\n");
        recommendations.append("   • پروتئین در هر وعده\n");
        recommendations.append("   • آب: ۳ لیتر روزانه\n\n");
        
        // توصیه ذهنیت
        recommendations.append("3️⃣ **ذهنیت و کاریزما:**\n");
        if (profile.mentalGoals.contains("کاریزما")) {
            recommendations.append("   • تماس چشمی و لبخند\n");
            recommendations.append("   • گوش دادن فعال\n");
            recommendations.append("   • صدای آرام و اعتماد‌بر\n");
        }
        recommendations.append("   • ۳۰ دقیقه تمرین ذهنی روزانه\n\n");
        
        // توصیه اراده
        recommendations.append("4️⃣ **تقویت اراده:**\n");
        recommendations.append("   • شروع کن بدون فکر کردن\n");
        recommendations.append("   • بر ").append(profile.failureHistory).append(" شکست قبلی فکر نکن\n");
        recommendations.append("   • هر روز یک کار کوچک برای تمرین اراده\n\n");
        
        return recommendations.toString();
    }
    
    /**
     * ذخیره پروفایل کاملی
     */
    public void saveProfile() {
        try {
            JSONObject json = new JSONObject();
            json.put("name", profile.name);
            json.put("age", profile.age);
            json.put("gender", profile.gender);
            json.put("currentWeight", profile.currentWeight);
            json.put("targetWeight", profile.targetWeight);
            json.put("experienceLevel", profile.experienceLevel);
            json.put("trainingDaysPerWeek", profile.trainingDaysPerWeek);
            json.put("dietType", profile.dietType);
            json.put("confidenceLevel", profile.confidenceLevel);
            json.put("willpower", profile.willpower);
            json.put("mainGoal", profile.mainGoal);
            
            prefs.edit().putString("user_profile", json.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private int mapExperience(String str) {
        if (str.contains("بدون")) return 1;
        if (str.contains("مبتدی")) return 2;
        if (str.contains("متوسط")) return 3;
        return 4;
    }
    
    private int parseInt(String str) {
        try {
            return Integer.parseInt(str.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
    
    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
