package com.github.calo001.hazel.util

class TimeText(private val hour: Int, private val minute: Int) {
    private val hourWord = hour.let{
        if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    }.toWords()
    private val minuteWord = minute.toWords()
    private val minuteDifference = (60 - minute).toWords()
    private val nextHourWord = if (hour == 12 || hour == 0) 1.toWords() else (hour + 1).toWords()

    fun getTimePhases(): List<String> {

        if (minute == 0) {
            return listOf(
                "It's $hourWord o'clock",
                "It's $hourWord"
            )
        }
        if (minute == 15) {
            return listOf(
                "It's a quarter past $hourWord",
                "It's quarter past $hourWord",
                "It's $hourWord ${if(minute < 10) "oh $minuteWord" else minuteWord}",
            )
        }
        if (minute == 30) {
            return listOf(
                "It's half past $hourWord",
                "It's $hourWord ${if(minute < 10) "oh $minuteWord" else minuteWord}",
            )
        }
        if (minute == 45) {
            return listOf(
                "It's a quarter to $hourWord",
                "It's quarter to $hourWord",
                "It's $hourWord ${if(minute < 10) "oh $minuteWord" else minuteWord}",
            )
        }
        if (minute in (1 until 15) ||
            minute in (16 until 30)) {
            return listOf(
                "It's $minuteWord past $hourWord",
                "It's $minuteWord ${if(minute == 1) "minute" else "minutes"} past $hourWord",
                "It's $hourWord ${if(minute < 10) "oh $minuteWord" else minuteWord}",
            )
        }
        if (minute in (31 until 45) ||
            minute in (46 until 60)) {
            return listOf(
                "It's $minuteDifference to $nextHourWord",
                "It's $minuteDifference ${if(minute == 1) "minute" else "minutes"} to $nextHourWord",
                "It's $hourWord ${if(minute < 10) "oh $minuteWord" else minuteWord}",
            )
        }
        return listOf()
    }
}