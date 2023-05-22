package com.side.project.air.di

import com.google.android.gms.location.LocationServices
import com.side.project.air.data.repo.MainRepo
import com.side.project.air.data.repo.MainRepoImpl
import com.side.project.air.ui.viewModel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val other = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
}

val repoModule = module {
    single<MainRepo> { MainRepoImpl() }
}

val viewModel = module {
    viewModel { MainViewModel(androidApplication()) }
}