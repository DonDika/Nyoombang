package com.c22027.nyoombang.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c22027.nyoombang.data.model.TransactionResponse
import com.c22027.nyoombang.repository.AppsRepositoryImpl
import kotlinx.coroutines.launch

class DetailsViewModel(private val repositoryImpl: AppsRepositoryImpl = AppsRepositoryImpl()): ViewModel() {

    val transaction: LiveData<TransactionResponse> = repositoryImpl.transaction

    fun fetchData(orderId: String){
        viewModelScope.launch {
            repositoryImpl.getApi(orderId)
        }
    }
}