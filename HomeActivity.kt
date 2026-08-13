package com.example.yourapp

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private var water = 2.5f
    private val waterGoal = 3f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val txtWater = findViewById<TextView>(R.id.txtWater)
        val progressWater = findViewById<ProgressBar>(R.id.progressWater)

        fun refreshWater() {
            txtWater.text = "$water از $waterGoal لیتر"
            progressWater.progress = (water / waterGoal * 100).toInt()
        }
        refreshWater()

        findViewById<TextView>(R.id.btnAddWater).setOnClickListener {
            if (water < waterGoal) {
                water = minOf(waterGoal, water + 0.25f)
                refreshWater()
                if (water == waterGoal)
                    Toast.makeText(this, "هدف آب تکمیل شد 💧✅", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "هدف امروز کامل شده 🎉", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.btnStartWorkout).setOnClickListener {
            Toast.makeText(this, "تمرین شروع شد 💪", Toast.LENGTH_SHORT).show()
        }
    }
}
