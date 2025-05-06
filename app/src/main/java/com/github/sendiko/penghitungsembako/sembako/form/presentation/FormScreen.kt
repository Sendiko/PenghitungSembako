package com.github.sendiko.penghitungsembako.sembako.form.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.github.sendiko.penghitungsembako.core.di.SembakoApplication
import com.github.sendiko.penghitungsembako.core.di.viewModelFactory

@Composable
fun FormScreenRoot(
    navController: NavHostController
) {

    val viewModel = viewModel<FormViewModel>(
        factory = viewModelFactory {
            FormViewModel(SembakoApplication().module.sembakoDao)
        }
    )
    val state = viewModel.state.collectAsState()

}