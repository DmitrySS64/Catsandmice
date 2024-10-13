package ru.sergeevdmitry8i11.catsandmice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartViewModel: ViewModel() {
    private val _miceCount = MutableLiveData(3)
    val miceCount: LiveData<Int> = _miceCount

    private val _speed = MutableLiveData(200)
    val speed: LiveData<Int> = _speed

    private val _mouseSize = MutableLiveData(1)
    val mouseSize: LiveData<Int> = _mouseSize

    fun updateMiceCount(value: Int) {
        _miceCount.value = value
    }

    fun updateSpeed(value: Int) {
        _speed.value = value
    }

    fun updateMouseSize(value: Int) {
        _mouseSize.value = value
    }
}