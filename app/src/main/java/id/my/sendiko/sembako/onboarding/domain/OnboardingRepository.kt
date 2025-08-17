package id.my.sendiko.sembako.onboarding.domain

interface OnboardingRepository {
    suspend fun setHasBoarding(hasBoarding: Boolean)
}