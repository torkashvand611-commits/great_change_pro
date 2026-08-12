package com.greatchange.app.ai;

import com.greatchange.app.models.PersonProfile;
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
        questions.add(new Question("اسمت چیه؟", "text", "نام"));
        questions.add(new Question("سنت چند سال است؟", "number", "سن"));
        questions.add(new Question("جنسیت‌ات چیه؟\n(مرد / زن)", "choice", "جنسیت"));
        questions.add(new Question("قدت چند سانتی‌متر است؟", "number", "قد"));
        questions.add(new Question("وزن فعلی‌ات چند کیلوگرم است؟", "number", "وزن فعلی"));
        questions.add(new Question("وزن هدف‌ات چند کیلوگرم است؟", "number", "وزن هدف"));
        
        // بخش 2: تمرین
        questions.add(new Question("تجربه تمرین‌ات چند سال است؟\n(بدون تجربه / مبتدی / متوسط / پیشرفته)", "choice", "سطح تجربه"));
        questions.add(new Question("هفته‌ای چند روز می‌تونی تمرین کنی؟", "number", "روزهای تمرینی"));
        questions.add(new Question("آیا مصدومیت یا محدودیت بدنی داری؟", "text", "مصدومیت"));
        
        // بخش 3: تغذیه
        questions.add(new Question("هدف‌ات کاهش وزن است یا توسعه عضلات یا توازن؟", "choice", "هدف تغذیه"));
        questions.add(new Question("الرژی غذایی داری؟", "text", "الرژی"));
        questions.add(new Question("معمولاً هفته‌ای چند وعده می‌خوری؟", "number", "وعده‌های غذایی"));
        
        // بخش 4: ذهنیت و کاریزما
        questions.add(new Question("اعتماد‌به‌نفس‌ات از 1 تا 10 چند است؟", "number", "اعتماد‌به‌نفس"));
        questions.add(new Question("ترس یا قلق اجتماعی‌ات از 1 تا 10 چند است؟\n(1 = بدون ترس)", "number", "اضطراب اجتماعی"));
        questions.add(new Question("در کاریزما و جذابیت چی می‌خوای بهتر شی؟\n(رهبری / جذابیت / تأثیرگذاری)", "choice", "اهداف کاریزما"));
        
        // بخش 5: اراده و اعتماد‌به‌نفس
        questions.add(new Question("اراده و تحمل‌ات از 1 تا 10 چند است؟", "number", "اراده"));
        questions.add(new Question("بزرگ‌ترین چالش‌ات برای تغییر چیه؟", "text", "بزرگ‌ترین چالش"));
        questions.add(new Question("قبلاً چند بار سعی کردی تغییر کنی و ناموفق شدی؟", "number", "تاریخچه شکست"));
        
        // بخش 6: هدف کلی
        questions.add(new Question("هدف اصلی‌ات از این 90 روز چیه؟", "text", "هدف اصلی"));
        questions.add(new Question("انگیزه‌ات برای شروع این برنامه از 1 تا 10 چند است؟", "number", "انگیزه"));
        questions.add(new Question("روزانه چند ساعت می‌تونی برای این برنامه وقت بگذاری؟", "number", "ساعات روزانه"));
    }
    
    /**
     * گرفتن سؤال فعلی
     */
    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }
    
    /**
     * رفتن به سؤال بعدی
     */
    public boolean nextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < questions.size();
    }
    
    /**
     * برگشتن به سؤال قبلی
     */
    public boolean previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            return true;
        }
        return false;
    }
    
    /**
     * درصد پیشرفت تکمیل پرسش‌ها
     */
    public int getProgressPercentage() {
        return (currentQuestionIndex * 100) / questions.size();
    }
    
    /**
     * آیا تمام سؤالات تمام شده؟
     */
    public boolean isComplete() {
        return currentQuestionIndex >= questions.size();
    }
    
    /**
     * تعداد کل سؤالات
     */
    public int getTotalQuestions() {
        return questions.size();
    }
    
    /**
     * شماره سؤال فعلی
     */
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }
    
    /**
     * کلاس داخلی برای سؤالات
     */
    public static class Question {
        public String text;
        public String type; // text, number, choice
        public String key; // کلید برای ذخیره‌سازی
        public String[] choices; // برای choice type
        
        public Question(String text, String type, String key) {
            this.text = text;
            this.type = type;
            this.key = key;
        }
        
        public Question(String text, String type, String key, String[] choices) {
            this.text = text;
            this.type = type;
            this.key = key;
            this.choices = choices;
        }
    }
}
