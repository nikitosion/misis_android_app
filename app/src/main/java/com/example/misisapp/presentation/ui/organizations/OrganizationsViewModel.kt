package com.example.misisapp.presentation.ui.organizations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrganizationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is organizations Fragment"
    }
    val text: LiveData<String> = _text
}