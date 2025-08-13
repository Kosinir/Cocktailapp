package com.example.cocktailapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerViewModel : ViewModel() {

    private var timerJob: Job? = null
    private var initialTime: Int = 0

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    fun setTime(seconds: Int) {
        initialTime = seconds
        _remainingSeconds.value = seconds
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun start() {
        if (_isRunning.value || _remainingSeconds.value <= 0) return

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0) {
                delay(1000L)
                _remainingSeconds.value -= 1
            }
            _isRunning.value = false
        }
    }

    fun pause() {
        timerJob?.cancel()
        _isRunning.value = false
    }

    fun reset() {
        pause()
        _remainingSeconds.value = initialTime
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}