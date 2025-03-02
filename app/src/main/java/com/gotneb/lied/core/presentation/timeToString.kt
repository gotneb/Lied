package com.gotneb.lied.core.presentation

fun Int.timeToString(): String {
    val hour = this / 60
    val minute = this % 60
    return String.format("%02d:%02d", hour, minute)
}