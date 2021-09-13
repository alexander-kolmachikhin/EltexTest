package eltex.test.core

import android.widget.TextView

fun TextView.setTextIfNotEquals(value: String?) {
    if (text?.toString() != value) {
        text = value
    }
}