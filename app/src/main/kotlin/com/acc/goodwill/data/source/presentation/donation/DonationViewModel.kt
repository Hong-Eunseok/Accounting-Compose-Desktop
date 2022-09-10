package com.acc.goodwill.data.source.presentation.donation

import com.acc.goodwill.data.source.ContributorDao
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.CreateContributorResult
import com.acc.goodwill.domain.model.CreateContributorState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class DonationViewModel @Inject constructor(
    private val userDao: ContributorDao,
    @Named("io") private val ioCoroutineScope: CoroutineScope
) {

    private val _result: MutableStateFlow<CreateContributorResult> = MutableStateFlow(CreateContributorResult.IDLE)
    val result: StateFlow<CreateContributorResult> = _result

    private val _searchResult: MutableStateFlow<List<Contributor>> = MutableStateFlow(listOf())
    val searchResult: StateFlow<List<Contributor>> = _searchResult

    fun addContributor(contributor: CreateContributorState, join: Int) {
        ioCoroutineScope.launch {
            userDao.addContributor(
                contributor.name,
                contributor.phoneNumber,
                contributor.address,
                contributor.registrationNumber,
                join
            )
            _result.emit(CreateContributorResult.SUCCESS)
            println("addContributor success")
        }
    }

    fun searchContributor(keyword: String) {
        println("searchContributor $keyword")
        ioCoroutineScope.launch {
            _searchResult.emit(userDao.searchUser(keyword))
        }

//        return flow {
//            val data = userDao.searchUser(keyword)
//            emit(data)
//            _searchResult.emit(data)
//        }
    }

}