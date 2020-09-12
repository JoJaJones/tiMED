package com.lovelace_scd.timed.Controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.lovelace_scd.timed.R
import java.sql.Time

class AddActivity: AppCompatActivity() {
    private val TAG = "ActivityLifeCycle(AA): "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_med_activity)
    }

    fun cancelAdd(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    fun saveMed(view: View){
        var tempString = ""
        var doseFreq = -1
        var dosePeriod = -1
        var doseSize = -1.0
        var refillRemaining = -1
        var refillSize = -1.0
        var name = findViewById<TextInputEditText>(R.id.medNameInput).text.toString()

        tempString = findViewById<EditText>(R.id.doseFreqInput).text.toString()
        if (tempString.length > 0) {
            doseFreq = tempString.toDouble().toInt()
        }

        tempString = findViewById<EditText>(R.id.dosePeriodInput).text.toString()
        if (tempString.length > 0) {
            dosePeriod = tempString.toDouble().toInt()
        }

        tempString = findViewById<EditText>(R.id.doseSizeInput).text.toString()
        if (tempString.length > 0) {
            doseSize = tempString.toDouble()
        }
        var doseTimeStr = findViewById<EditText>(R.id.doseTimeInput).text.toString()
        var doseDateStr = findViewById<EditText>(R.id.doseDateInput).text.toString()

        tempString  = findViewById<EditText>(R.id.remRefillInput).text.toString()
        if (tempString.length > 0) {
            refillRemaining = tempString.toDouble().toInt()
        }

        tempString = findViewById<EditText>(R.id.refillSizeInput).text.toString()
        if (tempString.length > 0) {
            refillSize = tempString.toDouble()
        }

        var withFood = findViewById<Switch>(R.id.foodSwitch).isChecked
        var doseUnit: String = "N/A"

        if(findViewById<RadioButton>(R.id.pillBtn).isChecked) {
            doseUnit = "pill"
        } else if (findViewById<RadioButton>(R.id.mlBtn).isChecked) {
            doseUnit = "mL"
        } else if (findViewById<RadioButton>(R.id.ccBtn).isChecked) {
            doseUnit = "CC"
        }
        Log.d("Form Test: ", "${name} ${doseFreq} ${dosePeriod} ${doseSize}\n" +
                "${doseTimeStr} ${doseDateStr} ${refillRemaining} ${refillSize}\n" +
                "${withFood} $doseUnit")
        // TODO: med saving/validating code here
        finish()
    }

    override fun onStart() {
        Log.d(TAG, "onStart called")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume called")
        super.onResume()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart called")
        super.onRestart()
    }

    override fun onPause() {
        Log.d(TAG, "onPause called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy caled")
        super.onDestroy()
    }
}