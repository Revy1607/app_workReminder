package com.datnt.app_workreminder

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel: ViewModel() {
    val data = MutableLiveData<Cursor>()
}