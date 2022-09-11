package com.acc.goodwill.data.source.presentation.donation

import com.acc.goodwill.data.source.ContributorDao
import com.acc.goodwill.data.source.DonationDao
import com.acc.goodwill.domain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class DonationViewModel @Inject constructor(
    private val contributorDao: ContributorDao,
    private val donationDao: DonationDao,
    @Named("io") private val ioCoroutineScope: CoroutineScope
) {

    private val _result: MutableStateFlow<CreateContributorResult> = MutableStateFlow(CreateContributorResult.IDLE)
    val result: StateFlow<CreateContributorResult> = _result

    private val _searchResult: MutableStateFlow<List<Contributor>> = MutableStateFlow(listOf())
    val searchResult: StateFlow<List<Contributor>> = _searchResult

    private val _addDonationResult: MutableStateFlow<DatabaseResult> = MutableStateFlow(DatabaseResult.IDLE)
    val addDonationResult = _addDonationResult.asStateFlow()

    private val _todayDonation: MutableStateFlow<List<TodayDonate>> = MutableStateFlow(listOf())
    val todayDonation = _todayDonation.asStateFlow()

    fun addContributor(contributor: CreateContributorState, join: Int) {
        ioCoroutineScope.launch {
            contributorDao.addContributor(
                contributor.name,
                contributor.phoneNumber,
                contributor.address,
                contributor.registrationNumber,
                contributor.registrationType,
                join
            )
            _result.emit(CreateContributorResult.SUCCESS)
            println("addContributor success")
        }
    }

    fun modifyContributor(contributor: CreateContributorState, join: Int, id: Long) {
        println("modifyContributor $id")
        ioCoroutineScope.launch {
            contributorDao.modifyContributor(
                contributor.name,
                contributor.phoneNumber,
                contributor.address,
                contributor.registrationNumber,
                contributor.registrationType,
                join,
                id
            )
            _result.emit(CreateContributorResult.SUCCESS)
            println("addContributor success")
        }
    }

    fun searchContributor(keyword: String) {
        println("searchContributor $keyword")
        ioCoroutineScope.launch {
            _searchResult.emit(contributorDao.searchContributor(keyword))
        }
    }

    fun clearSearchContributor() {
        println("clearSearchContributor")
        ioCoroutineScope.launch {
            _searchResult.emit(listOf())
        }
    }

    fun addDonation(
        contributor: Contributor,
        products: List<Product>,
        confirmInfo: Triple<Int, Int, Boolean>
    ) {
        ioCoroutineScope.launch {
            val throwable = donationDao.addDonation(contributor.primaryKey.value, products, confirmInfo)
            if (throwable == null) {
                _addDonationResult.emit(DatabaseResult.SUCCESS)
            } else {
                throwable.printStackTrace()
                _addDonationResult.emit(DatabaseResult.ERROR)
            }
        }
    }

    fun queryTodayDonation() {
        ioCoroutineScope.launch {
            _todayDonation.emit(donationDao.queryTodayDonation())
        }
    }

}