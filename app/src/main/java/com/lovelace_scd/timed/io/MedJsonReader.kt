/// filename: MedJsonReader.kt
/// description: A house of cards... Class responsible for reading and writing Timer instances to
///     and from json files. Only methods exposed are read, which reads smaple.json file found in
///     this same dir and returns ArrayList<Timer>, and write, which overwrites all ArrayList<Timer>
///     in this same sample.json file.

package com.lovelace_scd.timed.io

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.json
import com.lovelace_scd.timed.Model.Medication
import com.lovelace_scd.timed.model.Timer
import java.io.File


class MedJsonReader(val filename: String = "src/main/java/com/lovelace_scd/timed/io/sample.json") {
    val klaxon = Klaxon();


    fun write(timers: ArrayList<Timer>?) {
        val json = medTimersToJson(timers);
        File(filename).writeText("{ \"timers\": " + json.toJsonString() + " }\n");
//        val logic = json {
//            array(timers).map {
//                obj(it.toString() to it);
//            }
//        }
    }

    fun read(): ArrayList<Timer>? {
        // Klaxon doesn't seem to allow a top level json array...so this is a jenky workaround.
        val fileAsString = File(filename).readText();
        val res = klaxon.parse<Map<String, JsonArray<JsonObject>>>(fileAsString);

        return medJsonToTimers(res!!["timers"]);
//        return medJsonToTimers(res!!["timers"]);
    }

    private fun medTimersToJson(timers: ArrayList<Timer>?): JsonArray<JsonObject> {
        val jsonArr = JsonArray<JsonObject>();
        timers?.forEach {
            jsonArr.add(JsonObject(it.toMap()))
        }
        return jsonArr;
    }

    private fun medJsonToTimers(medJsonTransfer: JsonArray<JsonObject>?): ArrayList<Timer> {
        val timers = ArrayList<Timer>();

        if (medJsonTransfer != null) {
            for (tim in medJsonTransfer) {
                val med = Medication(
                        tim["medicationName"] as String,
                        tim["rxFullSize"] as Double,
                        tim["amountRemaining"] as Double,
                        tim["numRefillsRemaining"] as Int,
                        tim["isRefillable"] as Boolean,
                        tim["dosesPerTimePeriod"] as Int,
                        tim["daysPerTimePeriod"] as Int,
                        tim["doseSize"] as Double,
                        tim["takeWithFood"] as Boolean,
                        tim["doseUnit"] as String,
                );
                timers.add(Timer(med, tim["skipNextDose"] as Boolean, tim["nextDoseReady"] as Boolean));
            }
        }
        return timers;
    }
}

// Short lived class for reading and writing Medication/Timer instances to json.
//data class MedJsonTransfer(
//        val medicationName: String,
//        val dosesPerTimePeriod: Int,
//        val daysPerTimePeriod: Int,
//        val doseSize: Double,
//        val doseUnit: String,
//        val takeWithFood: Boolean,
//        val nextDose: Long,
//        val rxFullSize: Double,
//        val amountRemaining: Double,
//        val numRefillsRemaining: Int,
//        val isRefillable: Boolean,
//        val skipNextDose: Boolean
//);