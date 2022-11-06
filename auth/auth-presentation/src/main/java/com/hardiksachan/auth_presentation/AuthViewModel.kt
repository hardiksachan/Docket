package com.hardiksachan.auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardiksachan.auth_application.AuthPresenter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val presenter: AuthPresenter
): ViewModel() {
    val state = presenter.state

    fun handleEvent(event: AuthPresenter.Event) {
        viewModelScope.launch {
            presenter.handleEvent(event)
        }
    }
}