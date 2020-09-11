package com.lovelace_scd.timed.io

import com.beust.klaxon.Klaxon
import com.lovelace_scd.timed.Model.Medication
import com.lovelace_scd.timed.model.Timer


class MedJsonReader {

    val klaxon = Klaxon();


    constructor(filename: String = "sample.json") {

    }

    fun read(): ArrayList<Timer>? {
        val res = klaxon.parse<ArrayList<MedJsonTransfer>>("""{
           [ "medicationName": {
            "dosesPerTimePeriod": 2,
            "daysPerTimePeriod": 3,
            "doseSize": 3,
            "doseUnit": "pill",
            "takeWithFood": true,
            "nextDose": 1599789560
            "rxFullSize": 30
            "amountRemaining": 24,
            "numRefillsRemaining": 2,
             
            "skipNextDose": false]
        }
        }""");

        print(res);
        return medJsonToTimers(res);
//        return res;
    }

    private fun medJsonToTimers(medJsonTransfer: ArrayList<MedJsonTransfer>?): ArrayList<Timer> {

        val timers = medJsonTransfer?.map {
            it ->
                val med = Medication(
                        it.medicationName,
                        it.rxFullSize,
                        it.amountRemaining,
                        it.numRefillsRemaining,
                        it.isRefillable,
                        it.dosesPerTimePeriod,
                        it.daysPerTimePeriod,
                        it.doseSize,
                        it.takeWithFood,
                        it.doseUnit,
                );
                Timer(med);
        }
        return timers as ArrayList;
    }

    fun write(timers: ArrayList<Timer>) {

    }
}

// Short lived class for reading and writing Medication/Timer instances to json.
class MedJsonTransfer(
        val medicationName: String,
        val dosesPerTimePeriod: Int,
        val daysPerTimePeriod: Int,
        val doseSize: Double,
        val doseUnit: String,
        val takeWithFood: Boolean,
        val nextDose: Long,
        val rxFullSize: Double,
        val amountRemaining: Double,
        val numRefillsRemaining: Int,
        val isRefillable: Boolean,
        val skipNextDose: Boolean
);