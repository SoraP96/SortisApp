package com.example.banca.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.banca.data.database.DatabaseProvider
import kotlinx.coroutines.launch
import com.example.banca.data.repository.ListRepository
import com.example.banca.data.entities.ListEntity
import java.util.Calendar


class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ListRepository

    init {
        val db = DatabaseProvider.getDatabase(application)
        repository = ListRepository(db.listDao())
    }

    private val _lists = MutableStateFlow<List<ListEntity>>(emptyList())
    val lists: StateFlow<List<ListEntity>> = _lists

    private val _selectedShift = MutableStateFlow("DAY")
    val selectedShift: StateFlow<String> = _selectedShift

    fun setShift(shift: String) {
        _selectedShift.value = shift
    }

    fun loadLists(date: Long) {

        val start = getStartOfDay(date)
        val end = getEndOfDay(date)

        viewModelScope.launch {
            _lists.value = repository.getListsByDateAndShift(
                start,
                end,
                _selectedShift.value
            )
        }
    }

    private fun getStartOfDay(date: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfDay(date: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        return cal.timeInMillis
    }
}