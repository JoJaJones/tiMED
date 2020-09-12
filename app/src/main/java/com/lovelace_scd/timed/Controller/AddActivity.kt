package com.lovelace_scd.timed.Controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lovelace_scd.timed.R

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