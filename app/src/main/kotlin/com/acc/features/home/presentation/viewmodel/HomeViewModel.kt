package com.acc.features.home.presentation.viewmodel

import com.acc.features.organization.data.repository.OrganizationRepository
import com.navigation.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class HomeViewModel @Inject constructor(
    organizationRepository: OrganizationRepository,
    @Named("io") ioCoroutineScope: CoroutineScope
) : Entry {

    val selectedOrganizationName = organizationRepository.getSelectedOrganization().map {
        it?.name.orEmpty()
    }.stateIn(
        ioCoroutineScope,
        SharingStarted.WhileSubscribed(1000),
        ""
    )
}