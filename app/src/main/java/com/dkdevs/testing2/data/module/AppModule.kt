package com.dkdevs.testing2.data.module

import com.dkdevs.testing2.data.local.PlantDatabase
import com.dkdevs.testing2.data.remote.ApiService
import com.dkdevs.testing2.data.repo.IdentificationRepoImpl
import com.dkdevs.testing2.data.repo.PlantDetailsRepoImpl
import com.dkdevs.testing2.data.repo.PlantListRepoImpl
import org.koin.dsl.module

val dataModules = module {
    factory { IdentificationRepoImpl(get()) }

    factory { PlantDetailsRepoImpl(get(), get()) }

    factory { PlantListRepoImpl(get()) }

    single { ApiService.getIdentifyApiService() }

    single { ApiService.getDetailsApiService() }

    single { PlantDatabase.getInstance(get()) }

}