package com.lovelace_scd.timed

import com.lovelace_scd.timed.io.MedJsonReader
import com.lovelace_scd.timed.model.Timer
import org.junit.Test


class TimerVerificationTest {
    @Test
    fun jsonReaderTest() {
        val medJsonReader = MedJsonReader();
        val timers : ArrayList<Timer>? = medJsonReader.read()
        print(timers);
    }

    @Test
    fun jsonWriterTest() {
        val medJsonReader = MedJsonReader();
        val timers : ArrayList<Timer>? = medJsonReader.read()
        medJsonReader.write(timers);
    }
}