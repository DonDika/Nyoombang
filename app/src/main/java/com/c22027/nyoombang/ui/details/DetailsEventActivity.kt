package com.c22027.nyoombang.ui.details

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.data.model.TransactionResponse
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.data.remote.ApiConfig
import com.c22027.nyoombang.databinding.ActivityDetailsEventBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.utils.Utilization
import com.google.firebase.database.FirebaseDatabase
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
import kotlinx.android.synthetic.main.dialog_payment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsEventActivity : AppCompatActivity(), TransactionFinishedCallback {

    private val binding by lazy { ActivityDetailsEventBinding.inflate(layoutInflater) }
    /*private val ref by lazy { FirebaseDatabase.getInstance().reference }
    private val storage by lazy { FirebaseStorage.getInstance()}
    private val pref by lazy { SharedPreferencesHelper(this) }

    private lateinit var event: EventDataClass*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setData()
        setupListener()
        initMidtransSdk()
        getDataAmountCampaign()
    }

    private fun setupListener() {
        binding.bayarDonasi.setOnClickListener {
            setupDialogPayment()
        }
        binding.refreshData.setOnRefreshListener {
            fetchApi()
            getDataAmountCampaign()
            binding.refreshData.isRefreshing = false
        }
    }

    private fun setupDialogPayment(){
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
        /*
        campaign = intent.getParcelableExtra<Campaign>(EXTRA_CAMPAIGN) as Campaign
        binding.apply {
            tvCampaignName.text = campaign!!.campaign_name
            //tvAmount.text = campaign.amount.toString()
        }
        Log.e("TAG", campaign.toString() )
        */
    }

    //get data amount donation campaign from firebase
    private fun getDataAmountCampaign() {
        /*
        var totalAmount = campaign.amount.toString().toInt()
        //get data from firestore
        firestore.collection("transaction")
            .whereEqualTo("campaignName", campaign.campaign_name)
            .whereEqualTo("status", "settlement")
            .get()
            .addOnSuccessListener { result ->
                result.forEach { doc ->
                    totalAmount += doc.data["amount"].toString().toInt()
                }
                val formatAmount = Utils.amountDonationFormat(totalAmount)
                binding.tvAmount.text = "Rp. $formatAmount"
            }
            */
    }



    private fun fetchApi() {
        /*val orderId = ""
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
                                id = null,
                                orderId = responseBody.orderId,
                                campaignName = campaign!!.campaign_name,
                                username = pref.getString(Utils.PREF_USERNAME)!!,
                                amount = Utils.amountFormat(responseBody.grossAmount.toDouble()),
                                status = responseBody.transactionStatus,
                                transactionTime = responseBody.transactionTime
                            )
                        if (responseBody.transactionStatus == "settlement"){
                            //pref.clearOrderId()
                        }
                        firestore.collection("transaction")
                            .add(addTransactionData)
                        *//*.addOnSuccessListener {
                            //progress(false)
                            //Toast.makeText(this, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            //finish()
                            //pref.clearOrderId()
                        }*//*
                    }
                }
                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            Log.e("TAG", "Do nothing", )
        }*/

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
        customerDetails.phone = "082345678999" //from firebase or pref
        customerDetails.firstName = "Budi" //from firebase or pref
        customerDetails.email = "mail@mail.com" //from firebase or pref
        customerDetails.customerIdentifier = "mail@mail.com" //from firebase or pref
        return customerDetails
    }


    private fun initTransactionRequest(payment: Double): TransactionRequest {
        val orderId = "Nyoombang:" + System.currentTimeMillis().toString()
        //pref.put(Utils.PREF_ORDER_ID, orderId)
        val transactionRequest = TransactionRequest(orderId, payment)
        transactionRequest.customerDetails = initCustomerDetails()
        transactionRequest.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequest.shopeepay = Shopeepay("mysamplesdk:://midtrans")
        return transactionRequest
    }


    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null){
            Toast.makeText(this, "Terima kasih atas donasinya", Toast.LENGTH_LONG).show()
            /*when(result.status){
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(this, "Transaction Finished. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_PENDING-> Toast.makeText(this, "Transaction Pending. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(this, "Transaction Failed. ID: " + result.response.transactionId.toString() + result.response.statusMessage, Toast.LENGTH_LONG).show()
            }
            result.response.validationMessages*/
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


}