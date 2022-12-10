package com.c22027.nyoombang.ui.details

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.TransactionResponse
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.data.remote.ApiConfig
import com.c22027.nyoombang.databinding.ActivityDetailsEventBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.ui.dashboard.DashboardCommunity
import com.c22027.nyoombang.ui.profile.community.CommunityProfileActivity
import com.c22027.nyoombang.utils.Constant
import com.c22027.nyoombang.utils.Utilization
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlinx.android.synthetic.main.dialog_payment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DetailsEventActivity : AppCompatActivity(), TransactionFinishedCallback {

    private val binding by lazy { ActivityDetailsEventBinding.inflate(layoutInflater) }
    private val fireStore by lazy { Firebase.firestore}
    private val sharedPreferences by lazy { SharedPreferencesHelper(this) }
    private var currentTime: String? =  null
    private var currentDate: String? = null
    private lateinit var eventDataClass: EventDataClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val formatterDate = DateTimeFormatter.ofPattern("dd/MMMM/yyyy")
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        currentTime = LocalDateTime.now().format(formatterTime).toString()
        currentDate = LocalDateTime.now().format(formatterDate).toString()
        setData()
        setupListener()
        initMidtransSdk()
        getAmountTransactionDonation()
        eventDataClass = intent.getParcelableExtra<EventDataClass>(EXTRA_EVENT) as EventDataClass

    }

    override fun onBackPressed() {
        super.onBackPressed()

        sharedPreferences.clearAmount()
    }

    private fun setupListener() {
        binding.bayarDonasi.setOnClickListener {
            setupDialogPayment()
        }
        binding.btnDelete.setOnClickListener {
            deleteEvent()
        }
        binding.refreshData.setOnRefreshListener {
            updateTransaction()
//            worstScenario()
            getAmountTransactionDonation()
            updateAmountDonation()
            binding.refreshData.isRefreshing = false
        }
    }

    private fun setupDialogPayment() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_payment, null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val btnPay = dialogBinding.button_pay
        btnPay.setOnClickListener {
            val paymentAmount = dialogBinding.input_price.text.toString()
            if (paymentAmount.isEmpty()) {
                Toast.makeText(this, "Payment can't null!", Toast.LENGTH_SHORT).show()
            } else if (paymentAmount.toDouble() < 10000.0) {
                Toast.makeText(this, "The minimum payment is RP 10.000", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferences.inputDonation = paymentAmount //worst scenario
                MidtransSDK.getInstance().transactionRequest = initTransactionRequest(paymentAmount.toDouble()) //userDetails
                MidtransSDK.getInstance().startPaymentUiFlow(this@DetailsEventActivity)
            }
        }
    }


    //get data campaign from Home using parcelable
    private fun setData() {
        eventDataClass = intent.getParcelableExtra<EventDataClass>(EXTRA_EVENT) as EventDataClass

        binding.apply {
            Glide.with(this@DetailsEventActivity)
                .load(eventDataClass.eventPicture)
                .into(imageCampaign)
            tvCampaignName.text = eventDataClass.eventName
            tvUserName.text = eventDataClass.userName
            tvUserDate.text = eventDataClass.endOfDate
            tvCampaignDescription.setText(eventDataClass.eventDescription)
            tvUserName.setOnClickListener {
                intent = Intent(this@DetailsEventActivity,CommunityProfileActivity::class.java)
                intent.putExtra(CommunityProfileActivity.EXTRA_ID,eventDataClass.userId)
                startActivity(intent)
            }
        }
    }

    //worst scenario if cannot hit API
    private fun worstScenario(){
        val orderId = sharedPreferences.prefOrderId
        if (orderId?.isEmpty() == false){
            val addTransactionData =
                UserTransaction(
                    id = null,
                    userId = sharedPreferences.prefUid!!,
                    orderId = sharedPreferences.prefOrderId!!,
                    eventName = eventDataClass.eventName,
                    eventId = eventDataClass.eventId!!,
                    username = sharedPreferences.prefUsername!!,
                    amount = sharedPreferences.inputDonation!!,
                    status = "settlement",
                    transactionTime = currentTime!!,
                    transactionDate = currentDate!!//change using time format
                )
            Log.e("TAG", eventDataClass.eventId )
            fireStore.collection("Transaction")
                .add(addTransactionData)
                .addOnSuccessListener {
                    sharedPreferences.clearOrderId()
                    sharedPreferences.inputDonation()
                }
        }
    }


    private fun updateTransaction() {
        val orderId = sharedPreferences.prefOrderId.toString()
        Log.e("TAG", "orderId = $orderId ")
        if (orderId?.isEmpty() == false) {
            val client = ApiConfig.getApiService().getTransaction(orderId)
            client.enqueue(object : Callback<TransactionResponse> {
                override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                    if (response.isSuccessful) {
                        //response success from Api and add to firebase
                        val responseBody = response.body()!!
                        if (responseBody.transactionStatus == "settlement") {
                            //Toast.makeText(this@DetailsEventActivity, "Pembayaran berhasil! terimakasih!", Toast.LENGTH_LONG).show()
                            val addTransactionData =
                                UserTransaction(
                                    id = null,
                                    userId = sharedPreferences.prefUid!!,
                                    orderId = responseBody.orderId,
                                    eventName = eventDataClass.eventName,
                                    eventId = eventDataClass.eventId,
                                    username = sharedPreferences.prefUsername!!,
                                    amount = Utilization.amountFormat(responseBody.grossAmount.toDouble()),
                                    status = responseBody.transactionStatus,
                                    transactionTime = responseBody.transactionTime,
                                    transactionDate = currentDate!!
                                )
                            fireStore.collection("Transaction").add(addTransactionData)
                            sharedPreferences.clearOrderId()
                            getAmountTransactionDonation()
                        } else {
                            Toast.makeText(this@DetailsEventActivity, "Selesaikan pembayaran anda!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }


    //get all transaction from firestore depend on eventName
    private fun getAmountTransactionDonation() {
        var allDonationAmount = 0
        fireStore.collection("Transaction")
            .whereEqualTo("eventId", eventDataClass.eventId)
            .get()
            .addOnSuccessListener { result ->
                result.forEach { doc ->
                    allDonationAmount += doc.data["amount"].toString().toInt()
                    sharedPreferences.prefAmount = allDonationAmount
                }
                val formatAmount = Utilization.amountDonationFormat(allDonationAmount)
                binding.tvAmount.text = "Rp. $formatAmount"
            }
    }
    private fun deleteEvent(){
        fireStore.collection("Event").document(eventDataClass.eventId).delete().addOnSuccessListener {
            startActivity(Intent(this@DetailsEventActivity, DashboardCommunity::class.java))
            finish()
            Toast.makeText(this@DetailsEventActivity, "Event Berhasil Dihapus",Toast.LENGTH_SHORT).show()
        }
    }

    //update amount in Event
    private fun updateAmountDonation(){
        fireStore.collection("Event")
            .document(eventDataClass.eventId)
            .get()
            .addOnSuccessListener { result ->
                val currentAmount = result.data?.get("totalAmount").toString().toInt()
                val updatedAmountDonation = sharedPreferences.prefAmount

                if (currentAmount != updatedAmountDonation){
                    fireStore.collection("Event")
                        .document(eventDataClass.eventId)
                        .update("totalAmount", updatedAmountDonation)
                        .addOnSuccessListener {
                            sharedPreferences.clearAmount()
                        }
                }
            }
    }





    private fun initMidtransSdk() {
        val clientKey: String = Constant.MERCHANT_CLIENT_KEY
        val baseUrl: String = Constant.MERCHANT_BASE_CHECKOUT_URL

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
        customerDetails.phone = sharedPreferences.prefPhone
        customerDetails.firstName = sharedPreferences.prefUsername
        customerDetails.email = sharedPreferences.prefEmail
        customerDetails.customerIdentifier = sharedPreferences.prefEmail
        return customerDetails
    }


    private fun initTransactionRequest(payment: Double): TransactionRequest {
        val orderId = "Nyoombang:" + System.currentTimeMillis().toString()
        sharedPreferences.prefOrderId = orderId
        val transactionRequest = TransactionRequest(orderId, payment)
        transactionRequest.customerDetails = initCustomerDetails()
        transactionRequest.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequest.shopeepay = Shopeepay("mysamplesdk:://midtrans")
        return transactionRequest
    }


    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null){
            Toast.makeText(this, "Terima kasih atas donasinya", Toast.LENGTH_LONG).show()
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)){
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Transaction Finished with Failure.", Toast.LENGTH_SHORT).show()
            }
        }
        updateTransaction()
//        worstScenario()
        updateAmountDonation()
        getAmountTransactionDonation()
    }

    override fun onStart() {
        super.onStart()
        if (sharedPreferences.prefLevel.equals("User")){
            binding.bayarDonasi.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.GONE
        }else if (sharedPreferences.prefLevel.equals("Community")){
            binding.bayarDonasi.visibility = View.GONE
            binding.btnDelete.visibility = View.VISIBLE
        }
    }






    companion object{
        const val EXTRA_EVENT = "extra_event"
    }


}
