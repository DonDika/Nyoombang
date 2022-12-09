package com.c22027.nyoombang.ui.donation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.databinding.ActivityUserHistoryBinding
import com.c22027.nyoombang.databinding.FragmentHistoryDonationBinding
import com.c22027.nyoombang.ui.profile.user.UserHistoryAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HistoryDonation : Fragment() {
    private var _binding: FragmentHistoryDonationBinding? = null
    private val binding get() = _binding!!
    private val fireStore by lazy { Firebase.firestore }
    private val sharedPreferences by lazy { SharedPreferencesHelper(requireContext()) }

    private lateinit var userHistoryAdapter: UserHistoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentHistoryDonationBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            .whereEqualTo("userId", sharedPreferences.prefUid.toString())
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
                        transactionTime = doc.data["transactionTime"].toString(),
                        transactionDate = doc.data["transactionDate"].toString()
                    )
                    transactions.add(transactionData)
                }
                //tampilkan di recycler view
                userHistoryAdapter.setData(transactions)
            }
        userHistoryAdapter.setData(transactions)




    }

    override fun onResume() {
        super.onResume()
        setupAdapter()
        getHistoryUserTransaction()
    }

}