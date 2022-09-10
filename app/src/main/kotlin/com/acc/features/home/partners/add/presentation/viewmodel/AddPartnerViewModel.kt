package com.acc.features.home.partners.add.presentation.viewmodel

import com.acc.features.home.partners.data.repository.PartnersRepository
import com.acc.features.organization.data.repository.OrganizationRepository
import com.navigation.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AddPartnerViewModel @Inject constructor(
    private val repository: PartnersRepository,
    private val organizationRepository: OrganizationRepository,
    @Named("io") private val ioCoroutineScope: CoroutineScope
) : Entry {

    private val _partnerName = MutableStateFlow("")
    val partnerName: StateFlow<String> = _partnerName

    fun setPartnerName(partnerName: String) {
        _partnerName.tryEmit(partnerName)
    }

    private val _partnerAddress = MutableStateFlow("")
    val partnerAddress: StateFlow<String> = _partnerAddress

    fun setPartnerAddress(partnerAddress: String) {
        _partnerAddress.tryEmit(partnerAddress)
    }

    private val _partnerPhoneNumber = MutableStateFlow("")
    val partnerPhoneNumber: StateFlow<String> = _partnerPhoneNumber

    fun setPartnerPhoneNumber(partnerPhoneNumber: String) {
        _partnerPhoneNumber.tryEmit(partnerPhoneNumber)
    }

    fun addPartner() {
        ioCoroutineScope.launch {
            val name = _partnerName.value.trim()
            val address = _partnerAddress.value.trim()
            val phoneNumber = _partnerPhoneNumber.value.trim()
            val organization = organizationRepository.getSelectedOrganization().first() ?: return@launch
            repository.insertPartner(
                name = name,
                address = address,
                phoneNumber = phoneNumber,
                organizationId = organization.organizationId
            )
            setPartnerName("")
            setPartnerAddress("")
            setPartnerPhoneNumber("")
        }
    }
}