package com.dkdevs.testing2.ui.utility

import android.content.Context
import androidx.core.content.edit

class MyPreferences(context: Context) {

    private val INFO_SCREEN_VISITED = "info_screen_visited"

    private var pref = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    fun setInfoScreenVisitedStatus(isVisited: Boolean) {
        pref?.edit { putBoolean(INFO_SCREEN_VISITED, isVisited) }
    }

    fun getInfoScreenVisitedStatus(): Boolean {
        return pref?.getBoolean(INFO_SCREEN_VISITED, false) ?: false
    }

}