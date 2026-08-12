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
