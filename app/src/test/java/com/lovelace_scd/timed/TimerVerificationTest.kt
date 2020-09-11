package com.lovelace_scd.timed

import com.lovelace_scd.timed.io.MedJsonReader
import com.lovelace_scd.timed.model.Timer
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File


class TimerVerificationTest {
//    @Test
//    fun jsonReaderTest() {
//        val medJsonReader = MedJsonReader();
//        val timers : ArrayList<Timer>? = medJsonReader.read()
//    }

    @Test
    fun jsonReadWriteTest() {
        val jsonTextBefore = File("src/main/java/com/lovelace_scd/timed/io/sample.json").readText();

        // Not a robust test (pretty bad actually), but good enough for what we're doing.
        for(i in 0..5) {
            val medJsonReader = MedJsonReader();
            val timers: ArrayList<Timer>? = medJsonReader.read()
            medJsonReader.write(timers);
        }

        val jsonTextAfter = File("src/main/java/com/lovelace_scd/timed/io/sample.json").readText();
        assertEquals("Compare json string before and after reads/writes.", jsonTextBefore, jsonTextAfter );
    }
}