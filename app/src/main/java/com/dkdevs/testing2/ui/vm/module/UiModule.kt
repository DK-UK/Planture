package com.dkdevs.testing2.ui.vm.module

import com.dkdevs.testing2.ui.utility.MyPreferences
import com.dkdevs.testing2.ui.vm.DashboardViewModel
import com.dkdevs.testing2.ui.vm.DetailViewModel
import com.dkdevs.testing2.ui.vm.IdentifyViewModel
import com.dkdevs.testing2.ui.vm.PlantListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModules = module {
    this.viewModel { DashboardViewModel(get(), get()) }
    this.viewModel { IdentifyViewModel(get(), get(), get())}
    this.viewModel { DetailViewModel(get(), get()) }
    this.viewModel { PlantListViewModel(get(), get()) }

    factory { MyPreferences(get()) }
}