package com.greatchange.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.greatchange.app.ai.AICoach;
import com.greatchange.app.ai.AssessmentEngine;
import com.greatchange.app.ai.ProgramGenerator;
import com.greatchange.app.models.PersonProfile;

public class AssessmentActivity extends Activity {

    // پالت رنگی روشن و آرام (هماهنگ با MainActivity)
    final int MINT   = Color.rgb(191, 227, 214);
    final int CREAM  = Color.rgb(250, 245, 234);
    final int TEAL   = Color.rgb(42, 157, 143);
    final int DARK   = Color.rgb(33, 45, 42);
    final int MUTED  = Color.rgb(125, 138, 133);
    final int SKY    = Color.rgb(178, 205, 218);

    LinearLayout content;
    TextView questionNumber, questionText, progressText;
    EditText answerInput;
    LinearLayout answersContainer;
    Button prevButton, nextButton;
    ProgressBar progressBar;
    SharedPreferences prefs;
    AssessmentEngine assessmentEngine;
    AICoach aiCoach;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("gc2", MODE_PRIVATE);
        assessmentEngine = new AssessmentEngine();
        aiCoach = new AICoach(prefs);
        shell();
        showQuestion();
    }

    int dp(int n) {
        return (int)(n * getResources().getDisplayMetrics().density + 0.5f);
    }

    TextView tv(String s, float z, int c, boolean bold) {
        TextView v = new TextView(this);
        v.setText(s);
        v.setTextSize(z);
        v.setTextColor(c);
        v.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        v.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
        v.setPadding(dp(12), dp(5), dp(12), dp(5));
        v.setTextDirection(View.TEXT_DIRECTION_RTL);
        return v;
    }

    Button button(String s, int color) {
        Button b = new Button(this);
        b.setText(s);
        b.setTextColor(color);
        b.setTextSize(13);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setBackgroundColor(Color.TRANSPARENT);
        return b;
    }

    void shell() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(MINT);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.VERTICAL);
        head.setPadding(dp(16), dp(14), dp(16), dp(10));

        TextView title = tv("🌿 ارزیابی شخصی", 22, DARK, true);
        title.setGravity(Gravity.CENTER);
        head.addView(title);

        questionNumber = tv("سؤال 1 از " + assessmentEngine.getTotalQuestions(), 13, TEAL, true);
        questionNumber.setGravity(Gravity.CENTER);
        head.addView(questionNumber);

        root.addView(head, new LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT));

        ScrollView sv = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(10), dp(16), dp(16));

        questionText = tv("", 18, DARK, true);
        content.addView(questionText);

        answersContainer = new LinearLayout(this);
        answersContainer.setOrientation(LinearLayout.VERTICAL);
        content.addView(answersContainer);

        sv.addView(content);
        root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1));

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(-1, dp(8)));
        root.addView(progressBar);

        progressText = tv("0%", 12, MUTED, false);
        progressText.setGravity(Gravity.CENTER);
        root.addView(progressText);

        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
        buttonContainer.setGravity(Gravity.CENTER);
        buttonContainer.setPadding(dp(16), dp(12), dp(16), dp(16));

        prevButton = button("⬅️ قبلی", TEAL);
        prevButton.setOnClickListener(v -> goToPreviousQuestion());
        buttonContainer.addView(prevButton, new LinearLayout.LayoutParams(0, dp(50), 1));

        nextButton = button("بعدی ➡️", CREAM);
        nextButton.setBackgroundColor(TEAL);
        nextButton.setOnClickListener(v -> goToNextQuestion());
        buttonContainer.addView(nextButton, new LinearLayout.LayoutParams(0, dp(50), 1));

        root.addView(buttonContainer);

        setContentView(root);
    }

    void showQuestion() {
        AssessmentEngine.Question q = assessmentEngine.getCurrentQuestion();

        if (q == null) {
            completeAssessment();
            return;
        }

        questionNumber.setText("سؤال " + assessmentEngine.getCurrentQuestionNumber() +
            " از " + assessmentEngine.getTotalQuestions());

        questionText.setText(q.text);

        int progress = assessmentEngine.getProgressPercentage();
        progressBar.setProgress(progress);
        progressText.setText(progress + "%");

        answersContainer.removeAllViews();
        answerInput = null;

        if (q.type.equals("text")) {
            answerInput = new EditText(this);
            answerInput.setHint("جواب خود را بنویسید");
            answerInput.setHintTextColor(MUTED);
            answerInput.setTextColor(DARK);
            answerInput.setTextSize(14);
            answerInput.setGravity(Gravity.RIGHT);

            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(dp(12));
            gd.setStroke(dp(1), TEAL);
            answerInput.setBackground(gd);
            answerInput.setPadding(dp(12), dp(12), dp(12), dp(12));
            answerInput.setLayoutParams(new LinearLayout.LayoutParams(-1, dp(100)));

            String savedAnswer = prefs.getString("assessment_" + q.key, "");
            answerInput.setText(savedAnswer);

            answersContainer.addView(answerInput);

        } else if (q.type.equals("number")) {
            answerInput = new EditText(this);
            answerInput.setHint("عدد را وارد کنید");
            answerInput.setHintTextColor(MUTED);
            answerInput.setTextColor(DARK);
            answerInput.setTextSize(14);
            answerInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            answerInput.setGravity(Gravity.CENTER);

            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(dp(12));
            gd.setStroke(dp(1), TEAL);
            answerInput.setBackground(gd);
            answerInput.setPadding(dp(12), dp(12), dp(12), dp(12));
            answerInput.setLayoutParams(new LinearLayout.LayoutParams(-1, dp(60)));

            String savedAnswer = prefs.getString("assessment_" + q.key, "");
            answerInput.setText(savedAnswer);

            answersContainer.addView(answerInput);

        } else if (q.type.equals("choice")) {
            String[] options = q.choices;
            if (options == null) {
                options = new String[]{"بله", "خیر"};
            }
            for (String option : options) {
                Button choiceBtn = button(option, DARK);

                GradientDrawable gd = new GradientDrawable();
                gd.setCornerRadius(dp(12));

                String savedAnswer = prefs.getString("assessment_" + q.key, "");
                if (option.equals(savedAnswer)) {
                    gd.setColor(TEAL);
                    choiceBtn.setTextColor(CREAM);
                } else {
                    gd.setColor(CREAM);
                    choiceBtn.setTextColor(DARK);
                }
                choiceBtn.setBackground(gd);
                choiceBtn.setPadding(dp(12), dp(12), dp(12), dp(12));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(50));
                lp.setMargins(0, dp(4), 0, dp(4));
                choiceBtn.setLayoutParams(lp);

                String finalOption = option;
                choiceBtn.setOnClickListener(v -> {
                    prefs.edit().putString("assessment_" + q.key, finalOption).apply();
                    goToNextQuestion();
                });

                answersContainer.addView(choiceBtn);
            }
        }

        prevButton.setEnabled(assessmentEngine.getCurrentQuestionNumber() > 1);
        nextButton.setText(assessmentEngine.isComplete() ? "تکمیل ✓" : "بعدی ➡️");
    }

    void goToNextQuestion() {
        AssessmentEngine.Question q = assessmentEngine.getCurrentQuestion();

        if (q != null && answerInput != null && !q.type.equals("choice")) {
            String answer = answerInput.getText().toString();
            aiCoach.saveAnswer(q.key, answer);
        }

        if (assessmentEngine.isComplete()) {
            completeAssessment();
        } else {
            assessmentEngine.nextQuestion();
            showQuestion();
        }
    }

    void goToPreviousQuestion() {
        if (assessmentEngine.previousQuestion()) {
            showQuestion();
        }
    }

    void completeAssessment() {
        AssessmentEngine.Question q = assessmentEngine.getCurrentQuestion();
        if (q != null && answerInput != null && !q.type.equals("choice")) {
            String answer = answerInput.getText().toString();
            aiCoach.saveAnswer(q.key, answer);
        }

        PersonProfile profile = aiCoach.buildProfileFromAnswers();
        aiCoach.saveProfile();

        ProgramGenerator generator = new ProgramGenerator(profile);
        String analysis = aiCoach.generatePersonalAnalysis();
        String recommendations = aiCoach.generateRecommendations();
        String motivational = generator.generateMotivationalMessage();

        prefs.edit()
                .putString("assessment_complete", "true")
                .putBoolean("assessment_complete_check", true)
                .putString("program_analysis", analysis)
                .putString("program_recommendations", recommendations)
                .putString("program_motivational", motivational)
                .putLong("program_start", System.currentTimeMillis())
                .apply();

        showResults(profile, analysis, recommendations, motivational);
    }

    void showResults(PersonProfile profile, String analysis, String recommendations, String motivational) {
        content.removeAllViews();

        TextView done = tv("🎉 برنامه‌ات آماده است!", 24, DARK, true);
        done.setGravity(Gravity.CENTER);
        content.addView(done);

        content.addView(tv(motivational, 14, DARK, false));

        LinearLayout card1 = createCard();
        card1.addView(tv("📊 تجزیه وضعیت", 17, TEAL, true));
        card1.addView(tv(analysis, 12, DARK, false));
        content.addView(card1);

        LinearLayout card2 = createCard();
        card2.addView(tv("💡 توصیه‌های شخصی", 17, TEAL, true));
        card2.addView(tv(recommendations, 12, DARK, false));
        content.addView(card2);

        Button startBtn = button("🚀 شروع برنامه", CREAM);
        startBtn.setBackgroundColor(TEAL);
        startBtn.setPadding(dp(12), dp(12), dp(12), dp(12));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(60));
        lp.setMargins(dp(12), dp(10), dp(12), dp(10));
        startBtn.setLayoutParams(lp);
        startBtn.setOnClickListener(v -> {
            startActivity(new android.content.Intent(AssessmentActivity.this, MainActivity.class));
            finish();
        });
        content.addView(startBtn);
    }

    LinearLayout createCard() {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(16), dp(14), dp(16), dp(14));

        GradientDrawable g = new GradientDrawable();
        g.setColor(CREAM);
        g.setCornerRadius(dp(18));
        c.setBackground(g);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(dp(12), dp(7), dp(12), dp(7));
        c.setLayoutParams(lp);

        return c;
    }
}
