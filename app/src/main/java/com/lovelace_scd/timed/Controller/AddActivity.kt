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
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddActivity: AppCompatActivity() {
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

    /***********************************************************************************************
     * Function to implement the functionality of the cancel button
     **********************************************************************************************/
    fun cancelAdd(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        setResult(0)
        finish()
    }

    /***********************************************************************************************
     * Function to read the form and create a timer using the information if it's valid
     **********************************************************************************************/
    fun saveMed(view: View){
        readForm()

        if(validateMed(name, doseFreq, dosePeriod, doseSize, refillSize)) {
            TimerList.data.addTimer(makeTimer(), this)
            setResult(1)  // signal to the parent activity that the dataset has changed
            finish()
        } else {
            Toast.makeText(this, "Invalid information entered please ensure all " +
                    "information is entered correctly", Toast.LENGTH_SHORT).show()
        }

    }

    /***********************************************************************************************
     * Function to gather the data from the various input fields that make up the form
     **********************************************************************************************/
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

    /***********************************************************************************************
     * Function to use the gathered and validated data to generate a Timer object
     **********************************************************************************************/
    fun makeTimer(): Timer {
        val newMed = Medication(name, refillSize, refillSize, refillRemaining, doseFreq,
                dosePeriod, doseSize, withFood, doseUnit, true)

        val calendar = Calendar.getInstance()
        calendar.set(doseDate.year, doseDate.month, doseDate.dayOfMonth,
                doseTime.hour, doseTime.minute, 0)

        return Timer(newMed, calendar.timeInMillis)
    }

    /***********************************************************************************************
     * Function to ensure that data needed to make a Timer object has been entered and is a valid
     * value
     **********************************************************************************************/
    fun validateMed(name: String, doseFreq: Int, dosePeriod: Int, doseSize: Double, refillSize: Double): Boolean {
        if(name.length == 0) {
            return false
        }

        return !( doseFreq <= 0 || dosePeriod <= 0 || doseSize <= 0 || refillSize <= 0)
    }
}