package com.greatchange.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * کلاس کمکی برای ساخت اجزای UI
 */
public class UIHelper {
    private Context context;
    private float density;

    public UIHelper(Context context) {
        this.context = context;
        this.density = context.getResources().getDisplayMetrics().density;
    }

    /**
     * تبدیل dp به pixel
     */
    public int dp(int n) {
        return (int) (n * density + 0.5f);
    }

    /**
     * ایجاد TextView با استایل
     */
    public TextView createText(String text, float size, int color, boolean bold) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(color);
        tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        tv.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
        tv.setPadding(dp(12), dp(5), dp(12), dp(5));
        tv.setTextDirection(android.view.View.TEXT_DIRECTION_RTL);
        return tv;
    }

    /**
     * ایجاد کارت (Card) با رنگ لبه
     */
    public LinearLayout createCard(int accentColor) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(14), dp(10), dp(14), dp(10));

        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(Color.rgb(27, 27, 31));
        gradient.setCornerRadius(dp(18));
        gradient.setStroke(dp(2), accentColor);
        card.setBackground(gradient);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp(12), dp(7), dp(12), dp(7));
        card.setLayoutParams(lp);

        return card;
    }

    /**
     * ایجاد Progress Bar دایره‌ای
     */
    public LinearLayout createProgressCard(String title, int progress, int accentColor) {
        LinearLayout card = createCard(accentColor);

        // عنوان
        TextView titleText = createText(title, 16, accentColor, true);
        card.addView(titleText);

        // Progress Bar افقی
        LinearLayout progressContainer = new LinearLayout(context);
        progressContainer.setOrientation(LinearLayout.HORIZONTAL);
        progressContainer.setPadding(dp(8), dp(8), dp(8), dp(8));
        progressContainer.setGravity(Gravity.CENTER_VERTICAL);

        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgress(progress);
        progressBar.setMax(100);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(0, dp(20), 1));

        TextView percentText = createText(progress + "%", 14, accentColor, true);
        percentText.setLayoutParams(new LinearLayout.LayoutParams(dp(60), LinearLayout.LayoutParams.WRAP_CONTENT));
        percentText.setGravity(Gravity.CENTER);

        progressContainer.addView(progressBar);
        progressContainer.addView(percentText);

        card.addView(progressContainer);
        return card;
    }

    /**
     * ایجاد Streak Card (زنجیره روزها)
     */
    public LinearLayout createStreakCard(int dayNumber, int totalDays, int completedDays) {
        LinearLayout card = createCard(Color.rgb(201, 162, 46)); // طلایی

        // عنوان
        TextView titleText = createText("🔥 پیشرفت برنامه ۹۰ روزه", 18, Color.rgb(201, 162, 46), true);
        card.addView(titleText);

        // شماره روز
        LinearLayout dayInfo = new LinearLayout(context);
        dayInfo.setOrientation(LinearLayout.HORIZONTAL);
        dayInfo.setPadding(dp(8), dp(8), dp(8), dp(8));

        TextView dayText = createText("روز " + dayNumber + " از " + totalDays, 16, Color.WHITE, true);
        dayInfo.addView(dayText, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView completedText = createText(completedDays + " روز انجام شده", 14, Color.rgb(175, 175, 180), false);
        dayInfo.addView(completedText);

        card.addView(dayInfo);

        // Progress Bar
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgress((dayNumber * 100) / totalDays);
        progressBar.setMax(100);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(24)));
        card.addView(progressBar);

        return card;
    }

    /**
     * ایجاد Status Card برای وضعیت امروز
     */
    public LinearLayout createStatusCard(String title, int accentColor, int status) {
        LinearLayout card = createCard(accentColor);

        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(8), dp(8), dp(8), dp(8));

        TextView titleText = createText(title, 16, accentColor, true);
        row.addView(titleText, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        String statusStr = "";
        int statusColor = Color.WHITE;
        switch (status) {
            case 0:
                statusStr = "⬜ انجام نشده";
                statusColor = Color.rgb(100, 100, 100);
                break;
            case 1:
                statusStr = "⏳ جزئی";
                statusColor = Color.rgb(255, 193, 7);
                break;
            case 2:
                statusStr = "✅ کامل";
                statusColor = Color.rgb(93, 174, 120);
                break;
        }

        TextView statusText = createText(statusStr, 14, statusColor, true);
        row.addView(statusText);

        card.addView(row);
        return card;
    }

    /**
     * ایجاد Weight Card
     */
    public LinearLayout createWeightCard(double currentWeight, double previousWeight) {
        LinearLayout card = createCard(Color.rgb(74, 135, 190)); // آبی

        TextView titleText = createText("⚖️ وزن بدن", 18, Color.rgb(74, 135, 190), true);
        card.addView(titleText);

        LinearLayout weightRow = new LinearLayout(context);
        weightRow.setOrientation(LinearLayout.HORIZONTAL);
        weightRow.setPadding(dp(8), dp(8), dp(8), dp(8));
        weightRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView currentText = createText("وزن فعلی: " + String.format("%.1f", currentWeight) + " کیلو", 16, Color.WHITE, true);
        weightRow.addView(currentText, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        double diff = currentWeight - previousWeight;
        String diffStr = diff < 0 ? String.format("%.1f ⬇️", Math.abs(diff)) : String.format("%.1f ⬆️", diff);
        int diffColor = diff < 0 ? Color.rgb(93, 174, 120) : Color.rgb(244, 67, 54);

        TextView diffText = createText(diffStr, 14, diffColor, true);
        weightRow.addView(diffText);

        card.addView(weightRow);
        return card;
    }
}
