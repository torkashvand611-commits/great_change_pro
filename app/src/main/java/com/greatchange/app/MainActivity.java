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
    // پالت رنگی روشن و آرام (طبق طرح جدید)
    final int MINT   = Color.rgb(191, 227, 214);
    final int CREAM  = Color.rgb(250, 245, 234);
    final int TEAL   = Color.rgb(42, 157, 143);
    final int DARK   = Color.rgb(33, 45, 42);
    final int MUTED  = Color.rgb(125, 138, 133);
    final int ORANGE = Color.rgb(240, 140, 90);
    final int SKY    = Color.rgb(178, 205, 218);

    LinearLayout content;
    SharedPreferences prefs;
    String today;
    MediaPlayer player;
    int currentTrack = -1;
    String currentTrackName = "";
    ProgressCalculator progressCalc;
    UIHelper uiHelper;

    LinearLayout navBar;
    final int NAV_COUNT = 6;
    Button[] navButtons = new Button[NAV_COUNT];

    int[] trackIds = {R.raw.workout_ambient, R.raw.focus_ambient, R.raw.relaxing_ambient};
    String[] trackNames = {"Cardio Power Mix", "Strength Beats", "Calm Mind"};
    String[] trackDesc = {"32 tracks", "25 tracks", "18 tracks"};

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

        boolean assessmentComplete = prefs.getBoolean("assessment_complete_check", false);
        if (!assessmentComplete) {
            startActivity(new Intent(this, AssessmentActivity.class));
            finish();
            return;
        }

        shell();
        setSection(2); // شروع از خانه
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

    LinearLayout card(int bgColor) {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(16), dp(14), dp(16), dp(14));
        GradientDrawable g = new GradientDrawable();
        g.setColor(bgColor);
        g.setCornerRadius(dp(18));
        c.setBackground(g);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(dp(12), dp(7), dp(12), dp(7));
        c.setLayoutParams(lp);
        return c;
    }

    void shell() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(MINT);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ScrollView sv = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(8), dp(16), dp(8), dp(16));
        sv.addView(content);
        root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1));

        // نوار پایین با ۶ قسمت
        navBar = new LinearLayout(this);
        navBar.setOrientation(LinearLayout.HORIZONTAL);
        navBar.setBackgroundColor(CREAM);
        navBar.setPadding(dp(4), dp(6), dp(4), dp(10));
        navBar.setGravity(Gravity.CENTER);

        String[] icons  = {"👤", "🏋️", "🏠", "🧠", "", "️"};
        String[] labels = {"پروفایل", "ورزش", "خانه", "ذهن", "موسیقی", "تنظیمات"};
        for (int i = 0; i < NAV_COUNT; i++) {
            final int idx = i;
            Button b = new Button(this);
            b.setText(icons[i] + "\n" + labels[i]);
            b.setTextSize(10);
            b.setAllCaps(false);
            b.setBackgroundColor(Color.TRANSPARENT);
            b.setPadding(0, dp(4), 0, 0);
            b.setOnClickListener(v -> setSection(idx));
            navButtons[i] = b;
            navBar.addView(b, new LinearLayout.LayoutParams(0, dp(58), 1));
        }
        root.addView(navBar);
        setContentView(root);
    }

    void setSection(int idx) {
        for (int i = 0; i < NAV_COUNT; i++) {
            navButtons[i].setTextColor(i == idx ? TEAL : MUTED);
            navButtons[i].setTypeface(Typeface.DEFAULT, i == idx ? Typeface.BOLD : Typeface.NORMAL);
        }
        if (idx == 0) profile();
        else if (idx == 1) workout();
        else if (idx == 2) home();
        else if (idx == 3) mind();
        else if (idx == 4) music();
        else if (idx == 5) settings();
    }

    void clear() {
        content.removeAllViews();
    }

    // ================= خانه =================
    void home() {
        stopMusic();
        clear();
        String name = prefs.getString("assessment_نام", "دوست");
        TextView t = tv("سلام، " + name + " 👋", 26, DARK, true);
        t.setGravity(Gravity.CENTER);
        content.addView(t);
        TextView st = tv("عادت‌ها را بساز، متعادل بمان", 13, MUTED, false);
        st.setGravity(Gravity.CENTER);
        content.addView(st);

        int currentDay = progressCalc.getCurrentDay();
        int streak = progressCalc.getCurrentStreak();
        int programProgress = progressCalc.getProgramProgressPercentage();

        // کارت امتیاز سلامت ذهن
        LinearLayout scoreCard = card(CREAM);
        scoreCard.addView(tv("سلامت ذهن", 14, DARK, true));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        TextView num = tv(String.format(Locale.US, "%.1f", programProgress * 1.0), 36, DARK, true);
        row.addView(num, new LinearLayout.LayoutParams(0, -2, 1));
        TextView flower = tv("🌸🌿", 40, TEAL, false);
        row.addView(flower);
        scoreCard.addView(row);
        scoreCard.addView(tv(programProgress >= 70 ? "عالی" : (programProgress >= 40 ? "متوسط" : "تازه شروع"), 12, MUTED, false));
        ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pb.setProgress(programProgress);
        pb.setMax(100);
        pb.setLayoutParams(new LinearLayout.LayoutParams(-1, dp(10)));
        scoreCard.addView(pb);
        scoreCard.addView(tv("روز " + currentDay + " از 90  •  زنجیره " + streak + " روز", 12, MUTED, false));
        content.addView(scoreCard);

        // کارت چک‌لیست عادت‌ها
        LinearLayout habits = card(CREAM);
        habits.addView(tv("چک‌لیست روزانه", 16, DARK, true));
        String[] items = {"آب کافی", "پروتئین روزانه", "تمرین برنامه‌ریزی‌شده", "مرور شبانه"};
        for (String x : items) {
            LinearLayout hr = new LinearLayout(this);
            hr.setOrientation(LinearLayout.HORIZONTAL);
            hr.setGravity(Gravity.CENTER_VERTICAL);
            CheckBox cb = new CheckBox(this);
            cb.setText(x);
            cb.setTextColor(DARK);
            cb.setTextSize(14);
            cb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            String k = "check_" + today + "_" + x;
            cb.setChecked(prefs.getBoolean(k, false));
            cb.setOnCheckedChangeListener((bb, v) -> prefs.edit().putBoolean(k, v).apply());
            hr.addView(cb, new LinearLayout.LayoutParams(0, dp(44), 1));
            hr.addView(tv("🔥", 16, ORANGE, false));
            habits.addView(hr);
        }
        content.addView(habits);

        // کارت مدیتیشن
        LinearLayout med = card(SKY);
        med.addView(tv("مدیتیشن شبانه", 16, DARK, true));
        med.addView(tv("۸ جلسه • آرامش قبل از خواب", 12, MUTED, false));
        med.setOnClickListener(v -> setSection(4));
        content.addView(med);
    }

    // ================= ورزش =================
    void workout() {
        stopMusic();
        clear();
        TextView t = tv("🏋️ تمرین امروز", 26, DARK, true);
        t.setGravity(Gravity.CENTER);
        content.addView(t);
        Button edit = button("✎ ویرایش برنامه تمرینی", TEAL);
        edit.setOnClickListener(v -> editWorkout());
        content.addView(edit);
        for (int i = 0; i < exercises.length; i++) exerciseCard(i);
    }

    void exerciseCard(int i) {
        String[] e = exercises[i];
        LinearLayout c = card(CREAM);
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        ImageView im = new ImageView(this);
        im.setImageResource(R.drawable.ic_dumbbell);
        top.addView(im, new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.addView(tv(e[0], 17, DARK, true));
        info.addView(tv(e[1] + "  •  استراحت " + e[2], 12, MUTED, false));
        top.addView(info, new LinearLayout.LayoutParams(0, -2, 1));
        c.addView(top);
        Button guide = button("▣ توضیحات کامل", TEAL);
        guide.setOnClickListener(v -> guide(e[0], e[3]));
        c.addView(guide);
        int sets = e[1].startsWith("4") ? 4 : 3;
        for (int j = 1; j <= sets; j++) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            CheckBox done = new CheckBox(this);
            done.setText("ست " + j);
            done.setTextColor(DARK);
            String base = "set_" + today + "_" + i + "_" + j;
            done.setChecked(prefs.getBoolean(base + "_done", false));
            done.setOnCheckedChangeListener((b, v) -> prefs.edit().putBoolean(base + "_done", v).apply());
            EditText reps = inp("تکرار");
            EditText wt = inp("kg");
            reps.setText(prefs.getString(base + "_r", ""));
            wt.setText(prefs.getString(base + "_w", ""));
            row.addView(done, new LinearLayout.LayoutParams(0, dp(48), 1));
            row.addView(reps, new LinearLayout.LayoutParams(dp(75), dp(48)));
            row.addView(wt, new LinearLayout.LayoutParams(dp(75), dp(48)));
            c.addView(row);
            saveFocus(reps, base + "_r");
            saveFocus(wt, base + "_w");
        }
        Button tm = button("⏱ شروع استراحت ۹۰ ثانیه", TEAL);
        tm.setOnClickListener(v -> timer(tm, 90));
        c.addView(tm);
        content.addView(c);
    }

    // ================= ذهن =================
    void mind() {
        stopMusic();
        clear();
        TextView t = tv("🧠 ذهنیت و کاریزما", 26, DARK, true);
        t.setGravity(Gravity.CENTER);
        content.addView(t);
        TextView st = tv("فضای آرام • تمرین روزانه", 13, MUTED, false);
        st.setGravity(Gravity.CENTER);
        content.addView(st);
        Button edit = button("✎ ویرایش تمرین‌های ذهنی", TEAL);
        edit.setOnClickListener(v -> editMind());
        content.addView(edit);
        String[] names = loadMentalNames();
        for (int i = 0; i < names.length; i++) {
            int idx = i;
            LinearLayout c = card(CREAM);
            c.addView(tv(names[i], 17, DARK, true));
            c.addView(tv("تمرین پیشنهادی برای امروز", 12, MUTED, false));
            Button more = button("▶ توضیحات و اجرا", TEAL);
            more.setOnClickListener(v -> mentalGuide(idx, names[idx]));
            c.addView(more);
            CheckBox done = new CheckBox(this);
            done.setText("انجام شد");
            done.setTextColor(DARK);
            String k = "mental_" + today + "_" + i;
            done.setChecked(prefs.getBoolean(k, false));
            done.setOnCheckedChangeListener((b, v) -> prefs.edit().putBoolean(k, v).apply());
            c.addView(done, new LinearLayout.LayoutParams(-1, dp(44)));
            content.addView(c);
        }
    }

    // ================= موسیقی =================
    void music() {
        clear();
        TextView t = tv("🎵 موسیقی", 26, DARK, true);
        t.setGravity(Gravity.CENTER);
        content.addView(t);

        // کارت در حال پخش
        LinearLayout np = card(CREAM);
        TextView npHead = tv("در حال پخش", 12, MUTED, false);
        npHead.setGravity(Gravity.CENTER);
        np.addView(npHead);
        TextView npTitle = tv(currentTrackName.isEmpty() ? "آهنگی انتخاب نشده" : currentTrackName, 18, DARK, true);
        npTitle.setGravity(Gravity.CENTER);
        np.addView(npTitle);
        TextView wave = tv("∿∿∿∿∿∿∿∿", 16, TEAL, false);
        wave.setGravity(Gravity.CENTER);
        np.addView(wave);

        LinearLayout ctrl = new LinearLayout(this);
        ctrl.setOrientation(LinearLayout.HORIZONTAL);
        ctrl.setGravity(Gravity.CENTER);
        Button prev = button("⏮", TEAL);
        prev.setOnClickListener(v -> playTrack((currentTrack + trackIds.length - 1) % trackIds.length));
        Button play = button((player != null && player.isPlaying()) ? "⏸ توقف" : "▶ پخش", TEAL);
        play.setTextSize(16);
        play.setOnClickListener(v -> {
            if (player != null && player.isPlaying()) { stopMusic(); music(); }
            else playTrack(currentTrack < 0 ? 0 : currentTrack);
        });
        Button next = button("⏭", TEAL);
        next.setOnClickListener(v -> playTrack((currentTrack + 1) % trackIds.length));
        ctrl.addView(prev, new LinearLayout.LayoutParams(0, dp(50), 1));
        ctrl.addView(play, new LinearLayout.LayoutParams(0, dp(50), 1));
        ctrl.addView(next, new LinearLayout.LayoutParams(0, dp(50), 1));
        np.addView(ctrl);
        content.addView(np);

        // پلی‌لیست‌ها
        for (int i = 0; i < trackIds.length; i++) {
            final int idx = i;
            LinearLayout c = card(i == currentTrack ? SKY : CREAM);
            c.addView(tv(trackNames[i], 16, DARK, true));
            c.addView(tv(trackDesc[i], 12, MUTED, false));
            c.setOnClickListener(v -> playTrack(idx));
            content.addView(c);
        }
    }

    void playTrack(int i) {
        stopMusic();
        player = MediaPlayer.create(this, trackIds[i]);
        currentTrack = i;
        currentTrackName = trackNames[i];
        if (player != null) {
            player.setLooping(true);
            player.start();
        }
        music();
    }

    // ================= پروفایل =================
    void profile() {
        stopMusic();
        clear();
        TextView t = tv("👤 پروفایل", 26, DARK, true);
        t.setGravity(Gravity.CENTER);
        content.addView(t);

        LinearLayout c = card(CREAM);
        TextView avatar = tv("👤", 44, TEAL, false);
        avatar.setGravity(Gravity.CENTER);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(Color.rgb(214, 235, 227));
        avatar.setBackground(circle);
        avatar.setPadding(dp(20), dp(14), dp(20), dp(14));
        LinearLayout.LayoutParams alp = new LinearLayout.LayoutParams(-1, -2);
        alp.setMargins(dp(60), 0, dp(60), dp(6));
        avatar.setLayoutParams(alp);
        c.addView(avatar);

        String name = prefs.getString("assessment_نام", "دوست");
        TextView nm = tv(name, 20, DARK, true);
        nm.setGravity(Gravity.CENTER);
        c.addView(nm);

        Button edit = button("ویرایش پروفایل", CREAM);
        edit.setBackgroundColor(TEAL);
        edit.setOnClickListener(v -> startActivity(new Intent(this, AssessmentActivity.class)));
        c.addView(edit);
        content.addView(c);

        // اطلاعات کاربر
        LinearLayout stats = card(CREAM);
        stats.addView(tv("اطلاعات من", 16, DARK, true));
        stats.addView(infoRow("سن", prefs.getString("assessment_سن", "-")));
        stats.addView(infoRow("قد", prefs.getString("assessment_قد", "-") + " سانتی‌متر"));
        stats.addView(infoRow("وزن فعلی", prefs.getString("assessment_وزن فعلی", "-") + " کیلوگرم"));
        stats.addView(infoRow("وزن هدف", prefs.getString("assessment_وزن هدف", "-") + " کیلوگرم"));
        stats.addView(infoRow("هدف اصلی", prefs.getString("assessment_هدف اصلی", "-")));
        content.addView(stats);

        // ثبت وزن
        LinearLayout wc = card(CREAM);
        wc.addView(tv("⚖️ ثبت وزن امروز", 16, DARK, true));
        EditText w = editField("وزن فعلی (کیلوگرم)", "");
        wc.addView(w);
        Button save = button("✓ ثبت وزن", TEAL);
        save.setOnClickListener(v -> {
            String weight = w.getText().toString();
            if (!weight.isEmpty()) {
                prefs.edit().putString("weight_" + today, "{\"weight\":" + weight + "}").apply();
                profile();
            }
        });
        wc.addView(save);
        content.addView(wc);
    }

    LinearLayout infoRow(String title, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(tv(title, 14, DARK, true), new LinearLayout.LayoutParams(0, -2, 1));
        row.addView(tv(value, 13, MUTED, false));
        return row;
    }

    // ================= تنظیمات =================
    void settings() {
        stopMusic();
        clear();
        TextView t = tv("⚙️ تنظیمات", 26, DARK, true);
        t.setGravity(Gravity.CENTER);
        content.addView(t);

        LinearLayout c = card(CREAM);
        c.addView(switchRow("اعلان‌ها", "set_notif", true));
        c.addView(switchRow("حالت تیره", "set_dark", false));
        c.addView(switchRow("همگام‌سازی سلامت", "set_sync", true));
        c.addView(infoRow("واحدها", "kg/km"));
        c.addView(infoRow("زبان", "فارسی"));
        c.addView(infoRow("اهداف روزانه", "فعال"));
        c.addView(infoRow("حریم خصوصی", "محافظت‌شده"));
        c.addView(infoRow("راهنما", "در دسترس"));

        Button logout = button("خروج و شروع دوباره", ORANGE);
        logout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, AssessmentActivity.class));
            finish();
        });
        c.addView(logout);
        content.addView(c);
    }

    LinearLayout switchRow(String title, String key, boolean def) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(tv(title, 14, DARK, true), new LinearLayout.LayoutParams(0, -2, 1));
        Switch sw = new Switch(this);
        sw.setChecked(prefs.getBoolean(key, def));
        sw.setOnCheckedChangeListener((b, v) -> prefs.edit().putBoolean(key, v).apply());
        row.addView(sw);
        return row;
    }

    // ================= ابزارها =================
    EditText inp(String h) {
        EditText e = new EditText(this);
        e.setHint(h);
        e.setHintTextColor(MUTED);
        e.setTextColor(DARK);
        e.setTextSize(12);
        e.setInputType(2);
        e.setGravity(Gravity.CENTER);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(dp(8));
        gd.setStroke(dp(1), TEAL);
        e.setBackground(gd);
        e.setPadding(dp(8), dp(8), dp(8), dp(8));
        return e;
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

    void mentalGuide(int i, String name) {
        String[] ds = loadMentalDescs();
        String d = i < ds.length ? ds[i] : "برای این تمرین توضیحات بیشتری اضافه کن.";
        new AlertDialog.Builder(this)
                .setTitle("راهنمای " + name)
                .setMessage(d)
                .setPositiveButton("فهمیدم", null)
                .show();
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

    void editMind() {
        clear();
        content.addView(tv("✎ ویرایش تمرین‌های ذهنی", 24, DARK, true));
        String[] ns = loadMentalNames(), ds = loadMentalDescs();
        ArrayList<EditText> names = new ArrayList<>(), descs = new ArrayList<>();
        for (int i = 0; i < ns.length; i++) {
            LinearLayout c = card(CREAM);
            EditText n = editField("نام تمرین", ns[i]);
            EditText d = editField("چگونه اجرا شود؟", i < ds.length ? ds[i] : "");
            d.setMinLines(3);
            c.addView(n);
            c.addView(d);
            names.add(n);
            descs.add(d);
            content.addView(c);
        }
        Button add = button("+ افزودن تمرین ذهنی", TEAL);
        add.setOnClickListener(v -> {names.add(editField("نام تمرین", "تمرین جدید")); descs.add(editField("توضیحات", ""));});
        content.addView(add);
        Button save = button("✓ ذخیره تغییرات", TEAL);
        save.setOnClickListener(v -> {
            StringBuilder a = new StringBuilder(), b = new StringBuilder();
            for (int i = 0; i < names.size(); i++) {
                if (i > 0) {a.append("|"); b.append("|");}
                a.append(names.get(i).getText().toString());
                b.append(descs.get(i).getText().toString());
            }
            prefs.edit().putString("mental_names", a.toString()).putString("mental_descs", b.toString()).apply();
            setSection(3);
        });
        content.addView(save);
    }

    void editWorkout() {
        clear();
        content.addView(tv("✎ ویرایش برنامه تمرینی", 24, DARK, true));
        ArrayList<EditText> ns = new ArrayList<>(), ds = new ArrayList<>();
        for (String[] e : exercises) {
            LinearLayout c = card(CREAM);
            EditText n = editField("نام حرکت", e[0]);
            EditText d = editField("راهنمای اجرا", e[3]);
            d.setMinLines(3);
            c.addView(n);
            c.addView(d);
            ns.add(n);
            ds.add(d);
            content.addView(c);
        }
        Button save = button("✓ ذخیره برنامه", TEAL);
        save.setOnClickListener(v -> {
            for (int i = 0; i < exercises.length; i++) {
                exercises[i][0] = ns.get(i).getText().toString();
                exercises[i][3] = ds.get(i).getText().toString();
            }
            setSection(1);
        });
        content.addView(save);
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
