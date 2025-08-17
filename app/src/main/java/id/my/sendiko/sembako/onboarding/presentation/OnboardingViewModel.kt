package id.my.sendiko.sembako.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.onboarding.domain.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: OnboardingRepository): ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    fun onEvent(event: OnboardingEvent){
        when(event){
            is OnboardingEvent.OnNextClicked -> viewModelScope.launch {
                if (state.value.stage != 3) {
                    _state.update { it.copy(stage = it.stage + 1) }
                } else {
                    repository.setHasBoarding(true)
                }
            }
        }
    }
}