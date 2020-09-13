package com.example.hollyapp.utils

import java.text.SimpleDateFormat
import java.util.*

val localeBR = Locale("pt", "BR")

fun dataComDiaMesAno(datetime: Long): String? {
    val calendar: Calendar = Calendar.getInstance()
    val date = Date(datetime)
    calendar.time = date
    return SimpleDateFormat("dd/MM/yyyy", localeBR).format(date)
}

fun dataComDiaMes(datetime: Long): String? {
    val calendar: Calendar = Calendar.getInstance()
    val date = Date(datetime)
    calendar.time = date
    return SimpleDateFormat("dd/MM", localeBR).format(date)
}

fun dataSoAno(datetime: Long): String? {
    val calendar: Calendar = Calendar.getInstance()
    val date = Date(datetime)
    calendar.time = date
    return SimpleDateFormat("yyyy", localeBR).format(date)
}