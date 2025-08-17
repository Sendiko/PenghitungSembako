package id.my.sendiko.sembako.onboarding.presentation

sealed interface OnboardingEvent {
    data object OnNextClicked: OnboardingEvent
}