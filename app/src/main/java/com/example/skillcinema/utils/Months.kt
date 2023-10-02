package com.example.skillcinema.utils

enum class Months(val number: Int) {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12)
}

fun convertToMonth(monthNumber: Int): Months {
    for (month in Months.values()) {
        if (month.number == monthNumber)
            return month
    }
    return Months.JANUARY
}