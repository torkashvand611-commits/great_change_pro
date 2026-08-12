package com.greatchange.app.ai;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class QwenClient {

    public static final String API_KEY = "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";

    private static final String URL_API = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "qwen/qwen3-32b";

    public interface Callback {
        void onSuccess(String answer);
        void onError(String error);
    }

    public static boolean isAvailable() {
        return API_KEY != null && API_KEY.trim().length() > 0;
    }

    public static void ask(final String prompt, final Callback cb) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(URL_API).openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Authorization", "Bearer " + API_KEY);
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setConnectTimeout(15000);
                    con.setReadTimeout(30000);
                    con.setDoOutput(true);

                    JSONObject body = new JSONObject();
                    body.put("model", MODEL);
                    JSONArray messages = new JSONArray();

                    JSONObject sys = new JSONObject();
                    sys.put("role", "system");
                    sys.put("content", "تو مربی هوشمند بدنسازی، تغذیه و ذهنیت هستی. همیشه کوتاه، دوستانه و به زبان فارسی جواب بده. پاسخ‌هایت را با ایموجی‌های مناسب زیباتر کن.");
                    messages.put(sys);

                    JSONObject user = new JSONObject();
                    user.put("role", "user");
                    user.put("content", prompt);
                    messages.put(user);
                    body.put("messages", messages);
                    body.put("temperature", 0.7);

                    OutputStream os = con.getOutputStream();
                    os.write(body.toString().getBytes("UTF-8"));
                    os.close();

                    int code = con.getResponseCode();
                    BufferedReader br;
                    if (code == 200) {
                        br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    } else {
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                    }
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                    br.close();

                    if (code == 200) {
                        JSONObject res = new JSONObject(sb.toString());
                        String answer = res.getJSONArray("choices").getJSONObject(0)
                                .getJSONObject("message").getString("content");
                        cb.onSuccess(answer);
                    } else {
                        cb.onError("خطای سرور: " + code);
                    }
                } catch (Exception e) {
                    cb.onError("خطا: " + e.getMessage());
                }
            }
        }).start();
    }
}
