package com.c22027.nyoombang.ui.details

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.TransactionResponse
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.data.remote.ApiConfig
import com.c22027.nyoombang.databinding.ActivityDetailsEventBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.details.CommunityDetailsActivity.Companion.EXTRA_USER
import com.c22027.nyoombang.ui.profile.community.CommunityProfileActivity
import com.c22027.nyoombang.ui.profile.community.CommunityProfileViewModel
import com.c22027.nyoombang.utils.Utilization
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_details_event.*
import kotlinx.android.synthetic.main.dialog_payment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsEventActivity : AppCompatActivity(), TransactionFinishedCallback {

    private val binding by lazy { ActivityDetailsEventBinding.inflate(layoutInflater) }
    private val ref by lazy { FirebaseDatabase.getInstance().reference }
    private val db by lazy { Firebase.firestore}
    private val viewModel: DetailsViewModel by viewModels()
    private val storage by lazy { FirebaseStorage.getInstance()}
    private val pref by lazy { SharedPreferencesHelper(this) }
    var name =""
    var phoneNumber = ""
    var email = ""
    var tempAmount = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setData()
        setupListener()
        initMidtransSdk()

//        getDataAmountCampaign()


    }

    override fun onStart() {
        val campaign = intent.getStringExtra(EXTRA_EVENT).toString()
        super.onStart()

        db.collection("UsersProfile").document(pref.prefUid.toString()).get().addOnCompleteListener {
            if (it.isSuccessful){
                val document = it.result
                name = document.data!!["name"].toString()
                phoneNumber = document.data!!["phoneNumber"].toString()
                email = document.data!!["email"].toString()
            }
        }
        db.collection("Event").document(campaign).get().addOnCompleteListener {
            if (it.isSuccessful){
                val document = it.result
                tempAmount = document.data!!["totalAmount"].toString()
            }
        }

//        ref.child("UsersProfile").child(pref.prefUid.toString()).addValueEventListener(object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                name = snapshot.child("name").value.toString()
//                phoneNumber = snapshot.child("phoneNumber").value.toString()
//                email = snapshot.child("email").value.toString()
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//
//        })
//
//        ref.child("Event").child(campaign).get().addOnSuccessListener {
//            tempAmount = it.child("totalAmount").value.toString()
//        }
    }

    private fun setupListener() {
        binding.bayarDonasi.setOnClickListener {
            setupDialogPayment()
        }
        binding.refreshData.setOnRefreshListener {
            fetchApi()
            getDataTotalAmount()
//            getDataAmountCampaign()
            binding.refreshData.isRefreshing = false
        }
    }

    private fun setupDialogPayment(){
        val campaign = intent.getStringExtra(EXTRA_EVENT).toString()
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_payment, null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val btnPay = dialogBinding.button_pay
        btnPay.setOnClickListener {
            val paymentAmount = dialogBinding.input_price.text.toString()
            if (paymentAmount.isEmpty()){
                Toast.makeText(this, "Payment can't null!", Toast.LENGTH_SHORT).show()
            } else if (paymentAmount.toDouble() < 10000.0) {
                Toast.makeText(this, "The minimum payment is RP 10.000", Toast.LENGTH_SHORT).show()
            }
            else {
                MidtransSDK.getInstance().transactionRequest = initTransactionRequest(paymentAmount.toDouble()) //userDetails
                MidtransSDK.getInstance().startPaymentUiFlow(this@DetailsEventActivity)



            }
        }

    }

    //get data campaign from Home using parcelable
    private fun setData(){
        binding.apply {
             var communityId: String? = null
            val campaign = intent.getStringExtra(EXTRA_EVENT)
            Log.d("campaign",campaign.toString())

            db.collection("Event").document(campaign.toString()).get().addOnCompleteListener {
                if (it.isSuccessful){
                    val document = it.result
                    Glide.with(this@DetailsEventActivity).load(document.data!!["eventPicture"].toString()).into(imageCampaign)
                            communityId = document.data!!["user_id"].toString()
                            tvCampaignName.text = document.data!!["eventName"].toString()
                            tvCampaignDescription.text = document.data!!["eventDescription"].toString()
                            tvUserDate.text = document.data!!["endOfDate"].toString()
                            tvAmount.text = document.data!!["totalAmount"].toString()

                    db.collection("UsersProfile").document(communityId.toString()).get().addOnCompleteListener { task->
                        if (task.isSuccessful){
                            val snapshot = task.result
                            Glide.with(this@DetailsEventActivity).load(snapshot.data!!["picture"].toString()).into(imageUser)
                            tvUserName.text = snapshot.data!!["name"].toString()
                            binding.tvUserName.setOnClickListener {
                                        intent = Intent(this@DetailsEventActivity,CommunityDetailsActivity::class.java)
                                        intent.putExtra(EXTRA_USER,communityId)
                                        startActivity(intent)
                                    }
                        }

                    }
                }
            }
//                ref.child("Event").child(campaign.toString()).get()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val snapshot = task.result
//                           Glide.with(this@DetailsEventActivity).load(snapshot.child("eventPicture").value).into(imageCampaign)
//                            communityId = snapshot.child("user_id").value.toString()
//                            tvCampaignName.text = snapshot.child("eventName").value.toString()
//                            tvCampaignDescription.text = snapshot.child("eventDescription").value.toString()
//                            tvUserDate.text = snapshot.child("endOfDate").value.toString()
//                            tvAmount.text = snapshot.child("totalAmount").value.toString()
//
//                            ref.child("UsersProfile").child(communityId.toString()).get().addOnCompleteListener {
//                                if (it.isSuccessful){
//                                    val snapshot = it.result
//                                    tvUserName.text = snapshot.child("name").value.toString()
//                                    Glide.with(this@DetailsEventActivity).load(snapshot.child("picture").value).into(imageUser)
//                                    binding.tvUserName.setOnClickListener {
//                                        intent = Intent(this@DetailsEventActivity,CommunityDetailsActivity::class.java)
//                                        intent.putExtra(EXTRA_USER,communityId)
//                                        startActivity(intent)
//                                    }
//                                }
//                            }
//                        }
//                    }



            Log.e("TAG", campaign.toString())
        }
    }

    //get data amount donation campaign from firebase

    private fun getDataTotalAmount() {
        val campaign = intent.getStringExtra(EXTRA_EVENT)
        var totalAmount = binding.tvAmount.text.toString().toDouble()
        //get data from firestore
        db.collection("transaction")
            .whereEqualTo("eventId",campaign.toString() )
            .whereEqualTo("status", "settlement")
            .get()
            .addOnSuccessListener { result ->
                result.forEach { doc ->
                    totalAmount += doc.data["amount"].toString().toInt()
                }
                binding.tvAmount.text = totalAmount.toString()
            }
    }





    private fun fetchApi() {
        val campaign = intent.getStringExtra(EXTRA_EVENT).toString()
        val orderId = pref.prefOrderId.toString()
        Log.d("OrderID", orderId)
        //Log.e("TAG", orderId.toString() )
        if (orderId?.isEmpty() == false){
            val client = ApiConfig.getApiService().getTransaction(orderId)
            client.enqueue(object : Callback<TransactionResponse> {
                override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                    if (response.isSuccessful){
                        //response success from Api and add to firebase
                        val responseBody = response.body()!!
                        val addTransactionData =
                            UserTransaction(
                                responseBody.orderId,
                                pref.prefUid.toString(),
                                campaign,
                                name,
                                binding.tvCampaignName.text.toString(),
                                Utilization.amountFormat(responseBody.grossAmount.toDouble()),
                                responseBody.transactionStatus,
                                responseBody.transactionTime,
                                "${campaign}-${responseBody.transactionStatus}",
                                "${pref.prefUid.toString()}-${responseBody.transactionStatus}"
                            )
                        Log.d("gross",responseBody.grossAmount.toDouble().toString())
                        if (responseBody.transactionStatus == "settlement"){
                            pref.clearOrderId()
                        }
                        db.collection("transaction")
                            .add(addTransactionData)

                        /*.addOnSuccessListener {
                            //progress(false)
                            //Toast.makeText(this, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            //finish()
                            //pref.clearOrderId()
                        }*/
                    }
                }
                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            Log.e("TAG", "Do nothing", )
        }





    }

    private fun addDb(userTransaction: UserTransaction){
        ref.child("Transaction").child(userTransaction.orderId.toString()).setValue(userTransaction).addOnSuccessListener {
            Toast.makeText(this@DetailsEventActivity, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            pref.clearOrderId()

        }

    }

    private fun initMidtransSdk() {
        val clientKey: String = Utilization.MERCHANT_CLIENT_KEY
        val baseUrl: String = Utilization.MERCHANT_BASE_CHECKOUT_URL

        val sdkUIFlowBuilder: SdkUIFlowBuilder = SdkUIFlowBuilder.init()
            .setClientKey(clientKey)
            .setContext(this)
            .setTransactionFinishedCallback(this)
            .setMerchantBaseUrl(baseUrl)
            .setUIkitCustomSetting(uiKitCustomSetting())
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
        sdkUIFlowBuilder.buildSDK()
    }


    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uiKitCustomSetting = UIKitCustomSetting()
        uiKitCustomSetting.isSkipCustomerDetailsPages = true
        uiKitCustomSetting.isShowPaymentStatus = true
        return uiKitCustomSetting
    }



    private fun initCustomerDetails(): CustomerDetails {
        val customerDetails = CustomerDetails()
                customerDetails.phone =phoneNumber
                customerDetails.firstName =name
                customerDetails.email = email
                customerDetails.customerIdentifier =email
        return customerDetails

    }


    private fun initTransactionRequest(payment: Double): TransactionRequest {
        val orderId = "Nyoombang:" + System.currentTimeMillis().toString()
        pref.prefOrderId = orderId
        val transactionRequest = TransactionRequest(orderId, payment)
        transactionRequest.customerDetails = initCustomerDetails()
        transactionRequest.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequest.shopeepay = Shopeepay("mysamplesdk:://midtrans")
        return transactionRequest
    }


    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null){
            Toast.makeText(this, "Terima kasih atas donasinya", Toast.LENGTH_LONG).show()
            when(result.status){
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(this, "Transaction Finished. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_PENDING-> Toast.makeText(this, "Transaction Pending. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(this, "Transaction Failed. ID: " + result.response.transactionId.toString() + result.response.statusMessage, Toast.LENGTH_LONG).show()
            }
            result.response.validationMessages
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)){
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Transaction Finished with Failure.", Toast.LENGTH_SHORT).show()
            }
        }
        /*val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)*/
        //finish()
        fetchApi()
    }

    companion object{
        const val EXTRA_EVENT = "extra_event"
    }


}