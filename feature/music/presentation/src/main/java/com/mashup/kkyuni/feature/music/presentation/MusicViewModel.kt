package com.mashup.kkyuni.feature.music.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.kkyuni.feature.music.domain.GetMusicDurationUseCase
import com.mashup.kkyuni.feature.music.domain.GetMusicUseCase
import com.mashup.kkyuni.feature.music.domain.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicViewModel @Inject constructor(
    private val getMusic: GetMusicUseCase,
    private val getDuration: GetMusicDurationUseCase
) : ViewModel() {

    private val _videoList = MutableSharedFlow<List<Video>>()
    val videoList: SharedFlow<List<Video>> get() = _videoList

    fun search(query: String) {
        viewModelScope.launch {
            try {
                val videos = getMusic(query).items
                videos.forEach {
                    val duration = getDuration(it.id.videoId)
                    it.duration = timeConvertor(duration.items[0].contentDetail.duration)
                }
                _videoList.emit(videos)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun timeConvertor(time: String): String {
        var duration = time.substring(2)
        var h = ""
        var m = ""
        var s = ""

        if (duration.indexOf("H") > -1) {
            h = duration.substring(0, duration.indexOf("H"))
            duration = duration.substring(duration.indexOf("H")+1)
        } else {
            h = ""
        }

        if (duration.indexOf("M") > -1) {
            m = duration.substring(0, duration.indexOf("M"))
            duration = duration.substring(duration.indexOf("M")+1)
            if (m.length == 1) {
                m = "0$m"
            }
        } else {
            m = if (h.isNotEmpty()) "00"
            else "0"
        }

        if (duration.indexOf("S") > -1) {
            s = duration.substring(0, duration.indexOf("S"))
            duration = duration.substring(duration.indexOf("S")+1)
            if (s.length == 1) {
                s = "0$s"
            }
        } else {
            s = "00"
        }

        if (h.isNotEmpty()) {
            return "$h:$m:$s"
        } else {
            return "$m:$s"
        }
    }
}