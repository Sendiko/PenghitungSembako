package id.my.sendiko.sembako.onboarding.data

import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.onboarding.domain.OnboardingRepository

class OnboardingRepositoryImpl(private val userPreferences: UserPreferences): OnboardingRepository {
    override suspend fun setHasBoarding(hasBoarding: Boolean) {
        userPreferences.setHasBoarding(hasBoarding)
    }
}