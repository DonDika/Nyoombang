package com.c22027.nyoombang.detailsEvent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.data.model.TransactionResponse
import com.c22027.nyoombang.databinding.ActivityDetailsEventBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.utils.Utilization
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class DetailsEventActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailsEventBinding.inflate(layoutInflater) }
    private val ref by lazy { FirebaseDatabase.getInstance().reference }
    private val storage by lazy { FirebaseStorage.getInstance()}
    private val pref by lazy { SharedPreferencesHelper(this) }

    private lateinit var event: EventDataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        setData()
//        getDataTotalAmount()
//        setupListener()
    }

//    private fun setupListener() {
//        binding.bayarDonasi.setOnClickListener {
//            startActivity(Intent(this, PaymentActivity::class.java))
//        }
//        binding.refreshData.setOnRefreshListener {
//            fetchApi()
//            getDataTotalAmount()
//            binding.refreshData.isRefreshing = false
//        }
//    }
//
//
//
//    private fun setData(){
//        campaign = intent.getParcelableExtra<Campaign>(EXTRA_CAMPAIGN) as Campaign
//
//        binding.apply {
//            tvCampaignName.text = campaign!!.campaign_name
//            //tvAmount.text = campaign.amount.toString()
//        }
//        Log.e("TAG", campaign.toString() )
//    }
//
//
//    private fun getDataTotalAmount() {
//        var totalAmount = campaign!!.amount.toString().toInt()
//        //get data from firestore
//
//    }
//
//
//    private fun fetchApi() {
//        //val orderId = intent.getStringExtra(EXTRA_ORDER_ID)
//        val orderId = pref.getString(Utilization.PREF_ORDER_ID)
//        //Log.e("TAG", orderId.toString() )
//        if (orderId?.isEmpty() == false){
//            val client = ApiConfig.getApiService().getTransaction(orderId)
//            client.enqueue(object : Callback<TransactionResponse> {
//                override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
//                    if (response.isSuccessful){
//                        //response success from Api and add to firebase
//                        val responseBody = response.body()!!
//                        val addTransactionData =
//                            UserTransaction(
//                                id = null,
//                                orderId = responseBody.orderId,
//                                campaignName = campaign!!.campaign_name,
//                                username = pref.getString(Utils.PREF_USERNAME)!!,
//                                amount = Utils.amountFormat(responseBody.grossAmount.toDouble()),
//                                status = responseBody.transactionStatus,
//                                transactionTime = responseBody.transactionTime
//                            )
//                        if (responseBody.transactionStatus == "settlement"){
//                            pref.clearOrderId()
//                        }
//                        firestore.collection("transaction")
//                            .add(addTransactionData)
//                        /*.addOnSuccessListener {
//                            //progress(false)
//                            //Toast.makeText(this, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
//                            //finish()
//                            //pref.clearOrderId()
//                        }*/
//                    }
//                }
//                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//            })
//        } else {
//            Log.e("TAG", "Do nothing", )
//        }
//
//    }
//
//
//
//
//
//
//    companion object {
//        const val EXTRA_CAMPAIGN = "campaign"
//        const val EXTRA_ORDER_ID = "order_id"
//    }
}