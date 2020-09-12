package com.lovelace_scd.timed.Controller

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.lovelace_scd.timed.model.Medication
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.model.Timer
import com.lovelace_scd.timed.services.TimerList
import java.sql.Time
import java.util.*

class AddActivity: AppCompatActivity() {
    private val TAG = "ActivityLifeCycle(AA): "
    var name: String = ""
    var doseFreq: Int = -1
    var dosePeriod: Int = -1
    var doseSize: Double = 1.0
    var refillRemaining: Int = 0
    var refillSize: Double = -1.0
    lateinit var doseTime: TimePicker
    lateinit var doseDate: DatePicker
    var withFood: Boolean = false
    var doseUnit: String = "N/A"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_med_activity)
    }

    fun cancelAdd(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveMed(view: View){
        readForm()

        Log.d("Form Test: ", "${name} ${doseFreq} ${dosePeriod} ${doseSize}\n" +
                "${doseTime} ${doseDate} ${refillRemaining} ${refillSize}\n" +
                "${withFood} $doseUnit")

        if(validateMed(name, doseFreq, dosePeriod, doseSize, refillSize)) {
            Log.d("Dir", "${this.filesDir}")
            TimerList.data.addTimer(makeTimer(), this)
            finish()
        } else {
            Toast.makeText(this, "Invalid data entered please ensure all data is " +
                    "entered correctly", Toast.LENGTH_SHORT).show()
        }

    }

    fun readForm(){
        var tempString = ""

        name = findViewById<TextInputEditText>(R.id.medNameInput).text.toString()

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

        tempString  = findViewById<EditText>(R.id.remRefillInput).text.toString()
        if (tempString.length > 0) {
            refillRemaining = tempString.toDouble().toInt()
        }

        tempString = findViewById<EditText>(R.id.refillSizeInput).text.toString()
        if (tempString.length > 0) {
            refillSize = tempString.toDouble()
        }

        if(findViewById<RadioButton>(R.id.pillBtn).isChecked) {
            doseUnit = "pill"
        } else if (findViewById<RadioButton>(R.id.mlBtn).isChecked) {
            doseUnit = "mL"
        } else if (findViewById<RadioButton>(R.id.ccBtn).isChecked) {
            doseUnit = "CC"
        }

        doseTime = findViewById(R.id.doseTimeInput)
        doseDate = findViewById(R.id.doseDateInput)
        withFood = findViewById<Switch>(R.id.foodSwitch).isChecked
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeTimer(): Timer {
        val newMed = Medication(name, refillSize, refillSize, refillRemaining, true,
                doseFreq, dosePeriod, doseSize, withFood, doseUnit)

        val calendar = Calendar.getInstance()
        calendar.set(doseDate.year, doseDate.month, doseDate.dayOfMonth,
                doseTime.hour, doseTime.minute, 0)

        return Timer(newMed, calendar.timeInMillis)
    }

    fun validateMed(name: String, doseFreq: Int, dosePeriod: Int, doseSize: Double, refillSize: Double): Boolean {
        if(name.length == 0) {
            return false
        }

        return !( doseFreq <= 0 || dosePeriod <= 0 || doseSize <= 0 || refillSize <= 0)
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