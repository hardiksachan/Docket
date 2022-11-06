package com.hardiksachan.auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardiksachan.auth_application.AuthPresenter
import kotlinx.coroutines.launch

internal class AuthViewModel(
    private val presenter: AuthPresenter
): ViewModel() {
    val state = presenter.state

    fun handleEvent(event: AuthPresenter.Event) {
        viewModelScope.launch {
            presenter.handleEvent(event)
        }
    }
}