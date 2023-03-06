package ca.stayfit

import android.app.Application

class StayFitApp: Application() {
    val db:HistoryDatabase by lazy {
        HistoryDatabase.getInstance(this)
    }
}