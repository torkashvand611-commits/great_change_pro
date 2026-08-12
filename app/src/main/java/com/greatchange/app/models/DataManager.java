package com.greatchange.app.models;

import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * مدیریت ذخیره‌سازی و بازیابی داده‌ها به صورت JSON
 */
public class DataManager {
    private SharedPreferences prefs;

    public DataManager(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    // ============ Workout Session ============

    /**
     * ذخیره‌سازی یک جلسه تمرینی
     */
    public void saveWorkoutSession(WorkoutSession session) {
        try {
            JSONObject json = new JSONObject();
            json.put("exerciseId", session.exerciseId);
            json.put("name", session.name);
            json.put("sets_reps", session.sets_reps);
            json.put("rest_time", session.rest_time);
            json.put("guide", session.guide);
            json.put("date", session.date);

            JSONArray setsArray = new JSONArray();
            for (ExerciseSet set : session.sets) {
                JSONObject setJson = new JSONObject();
                setJson.put("setNumber", set.setNumber);
                setJson.put("reps", set.reps);
                setJson.put("weight", set.weight);
                setJson.put("completed", set.completed);
                setJson.put("timestamp", set.timestamp);
                setsArray.put(setJson);
            }
            json.put("sets", setsArray);
            json.put("timestamp", session.timestamp);

            String key = "workout_" + session.date + "_" + session.exerciseId;
            prefs.edit().putString(key, json.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * بازیابی یک جلسه تمرینی
     */
    public WorkoutSession getWorkoutSession(String date, int exerciseId, String name, String sets_reps, String rest_time, String guide) {
        String key = "workout_" + date + "_" + exerciseId;
        String json = prefs.getString(key, null);

        WorkoutSession session = new WorkoutSession(exerciseId, name, sets_reps, rest_time, guide, date);

        if (json != null) {
            try {
                JSONObject obj = new JSONObject(json);
                JSONArray setsArray = obj.getJSONArray("sets");
                session.sets.clear();

                for (int i = 0; i < setsArray.length(); i++) {
                    JSONObject setJson = setsArray.getJSONObject(i);
                    ExerciseSet set = new ExerciseSet(
                            setJson.getInt("setNumber"),
                            setJson.getInt("reps"),
                            setJson.getDouble("weight"),
                            setJson.getBoolean("completed")
                    );
                    session.sets.add(set);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return session;
    }

    // ============ Weight Log ============

    /**
     * ذخیره‌سازی وزن
     */
    public void saveWeight(WeightLog log) {
        try {
            JSONObject json = new JSONObject();
            json.put("weight", log.weight);
            json.put("date", log.date);
            json.put("timestamp", log.timestamp);

            prefs.edit().putString("weight_" + log.date, json.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * بازیابی وزن یک روز
     */
    public WeightLog getWeight(String date) {
        String json = prefs.getString("weight_" + date, null);
        if (json == null) return null;

        try {
            JSONObject obj = new JSONObject(json);
            return new WeightLog(obj.getDouble("weight"), obj.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * گرفتن تمام وزن‌ها
     */
    public List<WeightLog> getAllWeights() {
        List<WeightLog> weights = new ArrayList<>();
        // نحوه جستجوی تمام کلیدهای weight_*
        // این نسخه ساده است؛ برای بهتری می‌توان از Database استفاده کرد
        return weights;
    }

    // ============ Mental Exercise ============

    /**
     * ذخیره‌سازی تمرین ذهنی
     */
    public void saveMentalExercise(MentalExercise exercise) {
        try {
            JSONObject json = new JSONObject();
            json.put("id", exercise.id);
            json.put("name", exercise.name);
            json.put("description", exercise.description);
            json.put("date", exercise.date);
            json.put("completed", exercise.completed);
            json.put("timestamp", exercise.timestamp);

            String key = "mental_" + exercise.date + "_" + exercise.id;
            prefs.edit().putString(key, json.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * بازیابی تمرین ذهنی
     */
    public MentalExercise getMentalExercise(String date, int id, String name, String description) {
        String key = "mental_" + date + "_" + id;
        String json = prefs.getString(key, null);

        MentalExercise exercise = new MentalExercise(id, name, description, date);

        if (json != null) {
            try {
                JSONObject obj = new JSONObject(json);
                exercise.completed = obj.getBoolean("completed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return exercise;
    }

    // ============ Daily Progress ============

    /**
     * ذخیره‌سازی پیشرفت روز
     */
    public void saveDailyProgress(DailyProgress progress) {
        try {
            JSONObject json = new JSONObject();
            json.put("date", progress.date);
            json.put("dayNumber", progress.dayNumber);
            json.put("workoutStatus", progress.workoutStatus);
            json.put("mentalStatus", progress.mentalStatus);
            json.put("nutritionStatus", progress.nutritionStatus);
            json.put("weightLogged", progress.weightLogged);
            json.put("streakMaintained", progress.streakMaintained);
            json.put("timestamp", progress.timestamp);

            prefs.edit().putString("progress_" + progress.date, json.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * بازیابی پیشرفت روز
     */
    public DailyProgress getDailyProgress(String date, int dayNumber) {
        String json = prefs.getString("progress_" + date, null);

        DailyProgress progress = new DailyProgress(date, dayNumber);

        if (json != null) {
            try {
                JSONObject obj = new JSONObject(json);
                progress.workoutStatus = obj.getInt("workoutStatus");
                progress.mentalStatus = obj.getInt("mentalStatus");
                progress.nutritionStatus = obj.getInt("nutritionStatus");
                progress.weightLogged = obj.getBoolean("weightLogged");
                progress.streakMaintained = obj.getBoolean("streakMaintained");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return progress;
    }
}
