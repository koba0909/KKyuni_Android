package com.mashup.kkyuni.feature.calendar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.kkyuni.feature.calendar.domain.GetDiary
import com.mashup.kkyuni.feature.calendar.domain.model.DiaryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getDiary: GetDiary
) : ViewModel() {

    private val _onSetting = MutableSharedFlow<Unit>()
    val onSetting: SharedFlow<Unit> get() = _onSetting

    private val _onPlayList = MutableSharedFlow<Pair<Int, Int>>()
    val onPlayList= _onPlayList.asSharedFlow()

    private val _diary = MutableSharedFlow<DiaryEntity>()
    val diary: SharedFlow<DiaryEntity> get() = _diary

    private var _currentDate: String = ""

    fun requestDiary(date: String) = viewModelScope.launch {
        _currentDate = date

        kotlin.runCatching {
            _diary.emit(getDiary(date))
        }.onFailure {
            Log.v("test", it.toString())
        }

    }

    fun onClickSetting() = viewModelScope.launch {
        _onSetting.emit(Unit)
    }

    fun onClickPlayList() = viewModelScope.launch {
        if(_currentDate.isEmpty()) return@launch

        val (year, month) = _currentDate.split("-").map { it.toInt() }
        _onPlayList.emit(year to month)
    }
}
