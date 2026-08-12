package com.greatchange.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.view.*;
import android.widget.*;
import com.greatchange.app.utils.ProgressCalculator;
import com.greatchange.app.utils.UIHelper;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    final int BG=Color.rgb(15,15,18), TEXT=Color.WHITE, MUTED=Color.rgb(175,175,180);
    final int GOLD=Color.rgb(201,162,46), PURPLE=Color.rgb(155,126,222), GREEN=Color.rgb(93,174,120), BLUE=Color.rgb(74,135,190);
    LinearLayout content; 
    TextView clock, title; 
    SharedPreferences prefs; 
    String today;
    MediaPlayer player; 
    int currentTrack=-1;
    ProgressCalculator progressCalc;
    UIHelper uiHelper;

    String[][] exercises={
        {"اسکوات","4 ست × 6–8","۲–۳ دقیقه","پاها به عرض شانه، شکم سفت، زانو هم‌جهت پنجه. کنترل‌شده پایین برو و با فشار لگن بالا بیا."},
        {"پرس سینه","4 ست × 6–8","۲–۳ دقیقه","شانه‌ها عقب و پایین، پاها محکم روی زمین. میله را کنترل‌شده پایین بیاور و فشار بده."},
        {"بارفیکس / لت","4 ست × 6–10","۲ دقیقه","سینه را بالا نگه دار، آرنج‌ها را به سمت پایین بکش و از تاب دادن بدن خود جدا بیاور."},
        {"ددلیفت رومانیایی","3 ست × 8","۲–۳ دقیقه","زانو کمی خم، لگن به عقب، کمر خنثی. کشش پشت ران را حس کن و با فشار لگن بالا بیا."},
        {"پرس سرشانه دمبل","3 ست × 8–10","۹۰ ثانیه","مچ‌ها صاف، شکم سفت و مسیر دمبل‌ها کنترل‌شده. کمر را بیش از حد قوس ندار."},
        {"نشر جانب","4 ست × 12–15","۶۰–۹۰ ثانیه","آرنج کمی خم، دمبل‌ها تا حدود ارتفاع شانه. حرکت را آرام و بدون تاب دادن انجام بده."},
        {"Farmer Walk","3 × 30–45 ثانیه","۹۰ ثانیه","سینه باز، شانه‌ها پایین و عقب، شکم سفت و قدم‌های کنترل‌شده."}
    };
    
    String[] mentalDefault={"روتین صبح ۱۵ دقیقه","۵ دقیقه تصویرسازی","تمرین اراده","تمرین کاریزما","۱۰ دقیقه مطالعه","روتین شب"};
    String[] mentalDesc={
        "۲ دقیقه تنفس آرام، ۵ دقیقه تصویرسازی نسخه بهتر خودت، مرور هدف امروز و انتخاب یک کار سخت.",
        "چشم‌ها را ببند و خودت را در حالتی ببین که آرام، قوی، مرتب و با اعتمادبه‌نفس رفتار می‌کنی.",
        "یک کار کوچک که معمولاً عقب می‌اندازی انتخاب کن و همان لحظه انجام بده.",
        "در یک گفت‌وگوی کوتاه: تماس چشمی طبیعی، لبخند ملایم، شانه‌های باز و گوش دادن کامل را تمرین کن.",
        "حداقل ۱۰ صفحه یا ۱۰ دقیقه مطالعه بدون گوشی. بعد یک نکته مهم را ثبت کن.",
        "سه سؤال: امروز چه چیزی را خوب انجام دادم؟ کجا ضعیف بودم؟ فردا یک درصد بهتر چی می‌کنم؟"
    };

    @Override 
    public void onCreate(Bundle b){
        super.onCreate(b); 
        prefs = getSharedPreferences("gc2", MODE_PRIVATE);
        today = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
        progressCalc = new ProgressCalculator(prefs);
        uiHelper = new UIHelper(this);
        
        // بررسی اینکه آیا کاربر ارزیابی اولیه را انجام داده یا نه
        boolean assessmentComplete = prefs.getBoolean("assessment_complete_check", false);
        if (!assessmentComplete) {
            startActivity(new Intent(this, AssessmentActivity.class));
            finish();
            return;
        }
        
        shell(); 
        dashboard();
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

    LinearLayout card(int accent) {
        LinearLayout c = new LinearLayout(this); 
        c.setOrientation(LinearLayout.VERTICAL); 
        c.setPadding(dp(14), dp(10), dp(14), dp(10));
        GradientDrawable g = new GradientDrawable(); 
        g.setColor(Color.rgb(27, 27, 31)); 
        g.setCornerRadius(dp(18)); 
        g.setStroke(dp(2), accent); 
        c.setBackground(g);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); 
        lp.setMargins(dp(12), dp(7), dp(12), dp(7)); 
        c.setLayoutParams(lp); 
        return c;
    }

    void shell() {
        LinearLayout root = new LinearLayout(this); 
        root.setOrientation(LinearLayout.VERTICAL); 
        root.setBackgroundColor(BG); 
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        
        LinearLayout head = new LinearLayout(this); 
        head.setOrientation(LinearLayout.VERTICAL); 
        head.setPadding(dp(16), dp(7), dp(16), dp(7)); 
        head.setBackgroundColor(Color.rgb(11, 11, 13));
        
        title = tv("GREAT CHANGE PRO", 21, TEXT, true); 
        clock = tv("", 12, MUTED, false); 
        head.addView(title, new LinearLayout.LayoutParams(-1, dp(35))); 
        head.addView(clock, new LinearLayout.LayoutParams(-1, dp(23)));
        
        ScrollView sv = new ScrollView(this); 
        content = new LinearLayout(this); 
        content.setOrientation(LinearLayout.VERTICAL); 
        content.setPadding(0, dp(8), 0, dp(80)); 
        sv.addView(content); 
        root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1));
        
        root.addView(head, new LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT));
        
        LinearLayout nav = new LinearLayout(this); 
        nav.setGravity(Gravity.CENTER); 
        nav.setBackgroundColor(Color.rgb(24, 24, 28));
        
        String[] ns = {"امروز", "تمرین", "ذهنیت", "تقویم", "پیشرفت", "غذا"};
        for (String n : ns) {
            Button b = button(n, TEXT); 
            nav.addView(b, new LinearLayout.LayoutParams(0, dp(58), 1));
            if (n.equals("امروز")) b.setOnClickListener(v -> dashboard());
            if (n.equals("تمرین")) b.setOnClickListener(v -> workout());
            if (n.equals("ذهنیت")) b.setOnClickListener(v -> mind());
            if (n.equals("تقویم")) b.setOnClickListener(v -> calendar());
            if (n.equals("پیشرفت")) b.setOnClickListener(v -> progress());
            if (n.equals("غذا")) b.setOnClickListener(v -> food());
        }
        root.addView(nav);
        setContentView(root);
        
        Handler h = new Handler();
        h.post(new Runnable() {
            public void run() {
                clock.setText(new SimpleDateFormat("EEEE  d MMMM  |  HH:mm", new Locale("fa")).format(new Date()));
                h.postDelayed(this, 1000);
            }
        });
    }

    void clear() {
        content.removeAllViews();
    }

    void dashboard() {
        stopMusic();
        clear();
        
        content.addView(tv("📊 داشبورد امروز", 25, TEXT, true));
        
        int currentDay = progressCalc.getCurrentDay();
        int completedDays = progressCalc.getCompletedDaysCount();
        int streak = progressCalc.getCurrentStreak();
        int programProgress = progressCalc.getProgramProgressPercentage();
        
        LinearLayout streakCard = card(GOLD);
        streakCard.addView(tv("🔥 پیشرفت " + currentDay + " روزه", 18, GOLD, true));
        streakCard.addView(tv("روز " + currentDay + " از 90  •  " + completedDays + " روز انجام‌شده  •  زنجیره " + streak + " روز", 13, MUTED, false));
        
        ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pb.setProgress(programProgress);
        pb.setMax(100);
        pb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(24)));
        streakCard.addView(pb);
        
        TextView percentText = tv(programProgress + "%", 14, GOLD, true);
        percentText.setGravity(Gravity.CENTER);
        streakCard.addView(percentText);
        
        content.addView(streakCard);
        
        LinearLayout statusCard = card(BLUE);
        statusCard.addView(tv("📋 وضعیت امروز", 18, BLUE, true));
        
        int workoutStatus = progressCalc.getTodayWorkoutStatus();
        int mentalStatus = progressCalc.getTodayMentalStatus();
        int nutritionStatus = progressCalc.getTodayNutritionStatus();
        
        statusCard.addView(createStatusRow("💪 تمرین", workoutStatus));
        statusCard.addView(createStatusRow("🧠 ذهنیت", mentalStatus));
        statusCard.addView(createStatusRow("🍗 تغذیه", nutritionStatus));
        
        content.addView(statusCard);
        
        double currentWeight = progressCalc.getCurrentWeight();
        double previousWeight = progressCalc.getPreviousDayWeight();
        
        if (currentWeight > 0) {
            LinearLayout weightCard = card(GREEN);
            weightCard.addView(tv("⚖️ وزن بدن", 18, GREEN, true));
            
            double diff = currentWeight - previousWeight;
            String diffStr = diff < 0 ? String.format("%.1f کیلو کم", Math.abs(diff)) : String.format("%.1f کیلو بیش", diff);
            int diffColor = diff < 0 ? Color.rgb(93, 174, 120) : Color.rgb(244, 67, 54);
            
            weightCard.addView(tv("وزن فعلی: " + String.format("%.1f", currentWeight) + " کیلو", 16, TEXT, true));
            weightCard.addView(tv(diffStr, 14, diffColor, false));
            
            content.addView(weightCard);
        }
        
        checks("☑ چک‌لیست روزانه", new String[]{"آب کافی", "پروتئین روزانه", "تمرین برنامه‌ریزی‌شده", "مرور شبانه"}, GOLD);
    }

    private LinearLayout createStatusRow(String title, int status) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp(8), dp(8), dp(8), dp(8));
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView titleText = tv(title, 14, TEXT, true);
        row.addView(titleText, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        String statusStr = "";
        int statusColor = Color.WHITE;
        switch (status) {
            case 0:
                statusStr = "⬜ شروع نشده";
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

        TextView statusText = tv(statusStr, 12, statusColor, true);
        row.addView(statusText);

        return row;
    }

    void checks(String head, String[] items, int accent) {
        LinearLayout c = card(accent); 
        c.addView(tv(head, 18, accent, true));
        for (String x : items) {
            CheckBox cb = new CheckBox(this);
            cb.setText(x);
            cb.setTextColor(TEXT);
            cb.setTextSize(14);
            cb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            String k = "check_" + today + "_" + x;
            cb.setChecked(prefs.getBoolean(k, false));
            cb.setOnCheckedChangeListener((b, v) -> prefs.edit().putBoolean(k, v).apply());
            c.addView(cb, new LinearLayout.LayoutParams(-1, dp(48)));
        }
        content.addView(c);
    }

    void workout() {
        play(R.raw.workout_ambient); 
        clear(); 
        content.addView(tv("🏋️ تمرین امروز", 25, TEXT, true)); 
        content.addView(tv("ثبت ست + تکرار + وزنه", 13, MUTED, false));
        Button edit = button("✎ ویرایش برنامه تمرینی", GOLD);
        edit.setOnClickListener(v -> editWorkout());
        content.addView(edit);
        for (int i = 0; i < exercises.length; i++) exerciseCard(i);
    }

    void exerciseCard(int i) {
        String[] e = exercises[i]; 
        LinearLayout c = card(GOLD);
        LinearLayout top = new LinearLayout(this); 
        top.setOrientation(LinearLayout.HORIZONTAL);
        ImageView im = new ImageView(this);
        im.setImageResource(R.drawable.ic_dumbbell);
        top.addView(im, new LinearLayout.LayoutParams(dp(55), dp(55)));
        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.addView(tv(e[0], 18, TEXT, true));
        info.addView(tv(e[1] + "  •  استراحت " + e[2], 12, MUTED, false));
        top.addView(info, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        c.addView(top);
        Button guide = button("▣ توضیحات کامل", GOLD);
        guide.setOnClickListener(v -> guide(e[0], e[3]));
        c.addView(guide);
        int sets = e[1].startsWith("4") ? 4 : 3;
        for (int j = 1; j <= sets; j++) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            CheckBox done = new CheckBox(this);
            done.setText("ست " + j);
            done.setTextColor(TEXT);
            String base = "set_" + today + "_" + i + "_" + j;
            done.setChecked(prefs.getBoolean(base + "_done", false));
            done.setOnCheckedChangeListener((b, v) -> prefs.edit().putBoolean(base + "_done", v).apply());
            EditText reps = inp("تکرار");
            EditText wt = inp("kg");
            reps.setText(prefs.getString(base + "_r", ""));
            wt.setText(prefs.getString(base + "_w", ""));
            row.addView(done, new LinearLayout.LayoutParams(0, dp(48), 1));
            row.addView(reps, new LinearLayout.LayoutParams(dp(80), dp(48)));
            row.addView(wt, new LinearLayout.LayoutParams(dp(80), dp(48)));
            c.addView(row);
            saveFocus(reps, base + "_r");
            saveFocus(wt, base + "_w");
        }
        Button t = button("⏱ شروع استراحت ۹۰ ثانیه", GOLD);
        t.setOnClickListener(v -> timer(t, 90));
        c.addView(t);
        content.addView(c);
    }

    EditText inp(String h) {
        EditText e = new EditText(this);
        e.setHint(h);
        e.setHintTextColor(Color.rgb(120, 120, 125));
        e.setTextColor(TEXT);
        e.setTextSize(12);
        e.setInputType(2);
        e.setGravity(Gravity.CENTER);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.rgb(40, 40, 45));
        gd.setCornerRadius(dp(8));
        gd.setStroke(dp(1), Color.rgb(60, 60, 65));
        e.setBackground(gd);
        e.setPadding(dp(8), dp(8), dp(8), dp(8));
        return e;
    }

    void saveFocus(EditText e, String k) {
        e.setOnFocusChangeListener((v, h) -> {if (!h) prefs.edit().putString(k, e.getText().toString()).apply();});
    }

    void timer(Button b, int sec) {
        b.setEnabled(false);
        new CountDownTimer(sec * 1000L, 1000) {
            public void onTick(long m) {
                b.setText("⏱ " + (m / 1000) + " ثانیه");
            }

            public void onFinish() {
                b.setText("✓ آماده!");
                b.setEnabled(true);
            }
        }.start();
    }

    void guide(String name, String desc) {
        new AlertDialog.Builder(this)
                .setTitle("راهنمای " + name)
                .setMessage(desc + "\n\nنکات اجرا:\n• حرکت را کنترل‌شده انجام بده.\n• هر تکرار را با تمرکز کامل انجام بده.\n• استراحت بین ست‌ها لازمه!")
                .setPositiveButton("فهمیدم", null)
                .show();
    }

    void mind() {
        play(R.raw.relaxing_ambient); 
        clear(); 
        content.addView(tv("🧠 ذهنیت و کاریزما", 25, TEXT, true)); 
        content.addView(tv("فضای آرام • موسیقی پس‌زمینه • تمرین روزانه", 13, MUTED, false));
        LinearLayout music = card(PURPLE);
        music.addView(tv("♫ موسیقی آرام", 17, PURPLE, true));
        TextView st = tv("در حال پخش • برای توقف لمس کن", 13, MUTED, false);
        music.addView(st);
        Button stop = button("■ توقف موسیقی", PURPLE);
        stop.setOnClickListener(v -> {stopMusic(); st.setText("موسیقی متوقف شد");});
        music.addView(stop);
        content.addView(music);
        Button edit = button("✎ ویرایش تمرین‌های ذهنی", PURPLE);
        edit.setOnClickListener(v -> editMind());
        content.addView(edit);
        String[] names = loadMentalNames();
        for (int i = 0; i < names.length; i++) {
            int idx = i;
            LinearLayout c = card(PURPLE);
            c.addView(tv(names[i], 18, TEXT, true));
            c.addView(tv("تمرین پیشنهادی برای امروز", 12, MUTED, false));
            Button more = button("▶ توضیحات و اجرا", PURPLE);
            more.setOnClickListener(v -> mentalGuide(idx, names[idx]));
            c.addView(more);
            CheckBox done = new CheckBox(this);
            done.setText("انجام شد");
            done.setTextColor(TEXT);
            String k = "mental_" + today + "_" + i;
            done.setChecked(prefs.getBoolean(k, false));
            done.setOnCheckedChangeListener((b, v) -> prefs.edit().putBoolean(k, v).apply());
            c.addView(done, new LinearLayout.LayoutParams(-1, dp(48)));
            content.addView(c);
        }
    }

    String[] loadMentalNames() {
        String raw = prefs.getString("mental_names", "");
        if (raw.trim().isEmpty()) return mentalDefault;
        return raw.split("\\|", -1);
    }

    String[] loadMentalDescs() {
        String raw = prefs.getString("mental_descs", "");
        if (raw.trim().isEmpty()) return mentalDesc;
        return raw.split("\\|", -1);
    }

    void mentalGuide(int i, String name) {
        String[] ds = loadMentalDescs();
        String d = i < ds.length ? ds[i] : "برای این تمرین توضیحات بیشتری اضافه کن.";
        new AlertDialog.Builder(this)
                .setTitle("راهنمای " + name)
                .setMessage(d)
                .setPositiveButton("فهمیدم", null)
                .show();
    }

    void editMind() {
        clear();
        content.addView(tv("✎ ویرایش تمرین‌های ذهنی", 24, TEXT, true));
        content.addView(tv("نام و توضیحات هر تمرین را تغییر بده", 13, MUTED, false));
        String[] ns = loadMentalNames(), ds = loadMentalDescs();
        ArrayList<EditText> names = new ArrayList<>(), descs = new ArrayList<>();
        for (int i = 0; i < ns.length; i++) {
            LinearLayout c = card(PURPLE);
            EditText n = editField("نام تمرین", ns[i]);
            EditText d = editField("چگونه اجرا شود؟", i < ds.length ? ds[i] : "");
            d.setMinLines(3);
            c.addView(n);
            c.addView(d);
            names.add(n);
            descs.add(d);
            content.addView(c);
        }
        Button add = button("+ افزودن تمرین ذهنی", PURPLE);
        add.setOnClickListener(v -> {names.add(editField("نام تمرین", "تمرین جدید")); descs.add(editField("توضیحات", ""));});
        content.addView(add);
        Button save = button("✓ ذخیره تغییرات", GOLD);
        save.setOnClickListener(v -> {
            StringBuilder a = new StringBuilder(), b = new StringBuilder();
            for (int i = 0; i < names.size(); i++) {
                if (i > 0) {a.append("|"); b.append("|");}
                a.append(names.get(i).getText().toString());
                b.append(descs.get(i).getText().toString());
            }
            prefs.edit().putString("mental_names", a.toString()).putString("mental_descs", b.toString()).apply();
            mind();
        });
        content.addView(save);
    }

    EditText editField(String hint, String val) {
        EditText e = inp(hint);
        e.setText(val);
        e.setTextSize(14);
        e.setPadding(dp(12), dp(8), dp(12), dp(8));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp(8), dp(4), dp(8), dp(4));
        e.setLayoutParams(lp);
        return e;
    }

    void editWorkout() {
        clear();
        content.addView(tv("✎ ویرایش برنامه تمرینی", 24, TEXT, true));
        content.addView(tv("نام حرکت و دستور اجرای آن را شخصی‌سازی کن.", 13, MUTED, false));
        ArrayList<EditText> ns = new ArrayList<>(), ds = new ArrayList<>();
        for (String[] e : exercises) {
            LinearLayout c = card(GOLD);
            EditText n = editField("نام حرکت", e[0]);
            EditText d = editField("راهنمای اجرا", e[3]);
            d.setMinLines(3);
            c.addView(n);
            c.addView(d);
            ns.add(n);
            ds.add(d);
            content.addView(c);
        }
        Button save = button("✓ ذخیره برنامه", GOLD);
        save.setOnClickListener(v -> {
            for (int i = 0; i < exercises.length; i++) {
                exercises[i][0] = ns.get(i).getText().toString();
                exercises[i][3] = ds.get(i).getText().toString();
            }
            workout();
        });
        content.addView(save);
    }

    void calendar() {
        stopMusic();
        clear();
        content.addView(tv("📅 تقویم ۹۰ روزه", 25, TEXT, true));
        content.addView(tv("هر روز را کامل کن و زنجیره‌ات را حفظ کن.", 13, MUTED, false));
        LinearLayout c = card(BLUE);
        c.addView(tv("۱۶ مهر - روز " + progressCalc.getCurrentDay(), 16, BLUE, true));
        c.addView(tv("تقویم با جزئیات بیشتر به زودی اضافه خواهد شد", 12, MUTED, false));
        content.addView(c);
    }

    void progress() {
        stopMusic();
        clear();
        content.addView(tv("📈 پیشرفت", 25, TEXT, true));
        LinearLayout c = card(GREEN);
        c.addView(tv("وزن بدن", 18, GREEN, true));
        EditText w = editField("وزن فعلی (کیلوگرم)", "");
        Button save = button("✓ ثبت وزن", GREEN);
        save.setOnClickListener(v -> {
            String weight = w.getText().toString();
            if (!weight.isEmpty()) {
                prefs.edit().putString("weight_" + today, "{\"weight\":" + weight + "}").apply();
                progress();
            }
        });
        c.addView(w);
        c.addView(save);
        content.addView(c);
    }

    void food() {
        play(R.raw.focus_ambient);
        clear();
        content.addView(tv("🍗 تغذیه حجم تمیز", 25, TEXT, true));
        content.addView(tv("سبز و آرام • مناسب تمرکز روی تغذیه", 13, MUTED, false));
        LinearLayout c = card(GREEN);
        c.addView(tv("برنامه تغذیه امروز", 16, GREEN, true));
        c.addView(tv("صبحانه • ناهار • شام • اسنک", 12, MUTED, false));
        content.addView(c);
    }

    void play(int id) {
        if (currentTrack == id && player != null && player.isPlaying()) return;
        stopMusic();
        player = MediaPlayer.create(this, id);
        currentTrack = id;
        if (player != null) {
            player.setLooping(true);
            player.start();
        }
    }

    void stopMusic() {
        if (player != null) {
            try {
                player.stop();
            } catch (Exception e) {}
            player.release();
            player = null;
        }
        currentTrack = -1;
    }

    @Override 
    protected void onDestroy() {
        stopMusic();
        super.onDestroy();
    }
}
