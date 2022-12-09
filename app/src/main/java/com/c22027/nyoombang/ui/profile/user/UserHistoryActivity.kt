package com.c22027.nyoombang.ui.profile.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.databinding.ActivityUserHistoryBinding
import com.c22027.nyoombang.utils.Utilization
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserHistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUserHistoryBinding.inflate(layoutInflater) }
    private val fireStore by lazy { Firebase.firestore }
    private val sharedPreferences by lazy { SharedPreferencesHelper(this) }

    private lateinit var userHistoryAdapter: UserHistoryAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapter()
        setupListener()
        getHistoryUserTransaction()
    }


    private fun setupAdapter() {
        userHistoryAdapter = UserHistoryAdapter(arrayListOf())
        binding.rvUsers.adapter = userHistoryAdapter
    }


    private fun setupListener() {
        binding.refreshData.setOnRefreshListener {
            getHistoryUserTransaction()
            binding.refreshData.isRefreshing = false
        }
    }

    private fun getHistoryUserTransaction() {
        val transactions: ArrayList<UserTransaction> = arrayListOf()
        //get transaction data from firestore
        fireStore.collection("Transaction")
            .whereEqualTo("userId", sharedPreferences.prefUid)
            .get()
            .addOnSuccessListener { result ->
                result.forEach { doc ->
                    val transactionData = UserTransaction(
                        id = doc.reference.id,
                        userId = doc.data["userId"].toString(),
                        orderId = doc.data["orderId"].toString(),
                        eventName = doc.data["eventName"].toString(),
                        eventId = doc.data["eventId"].toString(),
                        username = doc.data["username"].toString(),
                        amount = doc.data["amount"].toString(),
                        status = doc.data["status"].toString(),
                        transactionTime = doc.data["transactionTime"].toString()
                    )
                    transactions.add(transactionData)
                }
                //tampilkan di recycler view
                userHistoryAdapter.setData(transactions)
            }



    }


}