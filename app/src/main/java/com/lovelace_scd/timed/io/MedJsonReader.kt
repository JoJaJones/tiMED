/// filename: MedJsonReader.kt
/// description: A house of cards... Class responsible for reading and writing Timer instances to
///     and from json files. Only methods exposed are read, which reads smaple.json file found in
///     this same dir and returns ArrayList<Timer>, and write, which overwrites all ArrayList<Timer>
///     in this same sample.json file.

package com.lovelace_scd.timed.io

import android.os.Build
import androidx.annotation.RequiresApi
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.lovelace_scd.timed.model.Medication


import com.lovelace_scd.timed.model.Timer
import java.io.File


class MedJsonReader(val filename: String = "src/main/java/com/lovelace_scd/timed/io/sample.json") {
    val klaxon = Klaxon();

    @RequiresApi(Build.VERSION_CODES.O)
    fun write(timers: ArrayList<Timer>?, saveFile: File) {
        val json = medTimersToJson(timers);

        saveFile.writeText("{ \"timers\": " + json.toJsonString() + " }\n");
//        val logic = json {
//            array(timers).map {
//                obj(it.toString() to it);
//            }
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun read(saveFile: File): ArrayList<Timer>? {
        try {
            val fileAsString = saveFile.readText();
            val res = klaxon.parse<Map<String, JsonArray<JsonObject>>>(fileAsString);

            return medJsonToTimers(res!!["timers"]);
        } catch(err : Throwable) {
            print(err);
            return ArrayList<Timer>();
        }
//        return medJsonToTimers(res!!["timers"]);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun medTimersToJson(timers: ArrayList<Timer>?): JsonArray<JsonObject> {
        val jsonArr = JsonArray<JsonObject>();
        timers?.forEach {
            jsonArr.add(JsonObject(it.toMap()))
        }
        return jsonArr;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun medJsonToTimers(medJsonTransfer: JsonArray<JsonObject>?): ArrayList<Timer> {
        val timers = ArrayList<Timer>();

        if (medJsonTransfer != null) {
            for (tim in medJsonTransfer) {
                val med = Medication(
                        tim["medicationName"] as String,
                        tim["rxFullSize"] as Double,
                        tim["amountRemaining"] as Double,
                        tim["numRefillsRemaining"] as Int,
                        tim["dosesPerTimePeriod"] as Int,
                        tim["daysPerTimePeriod"] as Int,
                        tim["doseSize"] as Double,
                        tim["takeWithFood"] as Boolean,
                        tim["doseUnit"] as String,
                        tim["isRefillable"] as Boolean,
                );
                timers.add(Timer(med, (tim["baseDate"] as Long), tim["skipNextDose"] as Boolean, tim["nextDoseReady"] as Boolean));
            }
        }
        return timers;
    }
}
