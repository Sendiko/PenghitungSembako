package com.github.sendiko.penghitungsembako.profile.presentation

sealed interface ProfileEvent {
    data object OnLogoutClicked : ProfileEvent
    data object ClearState : ProfileEvent
    data object LoadData : ProfileEvent
    data class OnThemeChanged(val dynamicTheme: Boolean) : ProfileEvent
}