package com.greatchange.app.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * موتور پرسش‌های تشخیصی
 */
public class AssessmentEngine {

    private List<Question> questions;
    private int currentQuestionIndex = 0;

    public AssessmentEngine() {
        initializeQuestions();
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();

        // بخش 1: اطلاعات پایه
        questions.add(new Question("اسمت چیه؟", "text", "نام", null));
        questions.add(new Question("سنت چند سال است؟", "number", "سن", null));
        questions.add(new Question("جنسیت‌ات چیه؟", "choice", "جنسیت", 
            new String[]{"مرد", "زن"}));
        questions.add(new Question("قدت چند سانتی‌متر است؟", "number", "قد", null));
        questions.add(new Question("وزن فعلی‌ات چند کیلوگرم است؟", "number", "وزن فعلی", null));
        questions.add(new Question("وزن هدف‌ات چند کیلوگرم است؟", "number", "وزن هدف", null));

        // بخش 2: تمرین
        questions.add(new Question("تجربه تمرین‌ات چند سال است؟", "choice", "سطح تجربه", 
            new String[]{"بدون تجربه", "مبتدی", "متوسط", "پیشرفته"}));
        questions.add(new Question("هفته‌ای چند روز می‌تونی تمرین کنی؟", "number", "روزهای تمرینی", null));
        questions.add(new Question("آیا مصدومیت یا محدودیت بدنی داری؟", "text", "مصدومیت", null));

        // بخش 3: تغذیه
        questions.add(new Question("هدف‌ات کاهش وزن است یا توسعه عضلات یا توازن؟", "choice", "هدف تغذیه", 
            new String[]{"کاهش وزن", "توسعه عضلات", "توازن"}));
        questions.add(new Question("الرژی غذایی داری؟", "text", "الرژی", null));
        questions.add(new Question("معمولاً هفته‌ای چند وعده می‌خوری؟", "number", "وعده‌های غذایی", null));

        // بخش 4: ذهنیت و کاریزما
        questions.add(new Question("اعتماد‌به‌نفس‌ات از 1 تا 10 چند است؟", "number", "اعتماد‌به‌نفس", null));
        questions.add(new Question("ترس یا قلق اجتماعی‌ات از 1 تا 10 چند است؟ (1 = بدون ترس)", "number", "اضطراب اجتماعی", null));
        questions.add(new Question("در کاریزما و جذابیت چی می‌خوای بهتر شی؟", "choice", "اهداف کاریزما", 
            new String[]{"رهبری", "جذابیت", "تأثیرگذاری"}));

        // بخش 5: اراده و اعتماد‌به‌نفس
        questions.add(new Question("اراده و تحمل‌ات از 1 تا 10 چند است؟", "number", "اراده", null));
        questions.add(new Question("بزرگ‌ترین چالش‌ات برای تغییر چیه؟", "text", "بزرگ‌ترین چالش", null));        questions.add(new Question("قبلاً چند بار سعی کردی تغییر کنی و ناموفق شدی؟", "number", "تاریخچه شکست", null));

        // بخش 6: هدف کلی
        questions.add(new Question("هدف اصلی‌ات از این 90 روز چیه؟", "text", "هدف اصلی", null));
        questions.add(new Question("انگیزه‌ات برای شروع این برنامه از 1 تا 10 چند است؟", "number", "انگیزه", null));
        questions.add(new Question("روزانه چند ساعت می‌تونی برای این برنامه وقت بگذاری؟", "number", "ساعات روزانه", null));
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean nextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < questions.size();
    }

    public boolean previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            return true;
        }
        return false;
    }

    public int getProgressPercentage() {
        if (questions.isEmpty()) return 0;
        return (currentQuestionIndex * 100) / questions.size();
    }

    public boolean isComplete() {
        return currentQuestionIndex >= questions.size();
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    public static class Question {
        public String text;
        public String type; // text, number, choice
        public String key;
        public String[] choices; // برای choice type - حالا پر می‌شود!
        public Question(String text, String type, String key) {
            this.text = text;
            this.type = type;
            this.key = key;
            this.choices = null;
        }

        public Question(String text, String type, String key, String[] choices) {
            this.text = text;
            this.type = type;
            this.key = key;
            this.choices = choices;
        }
    }
}
