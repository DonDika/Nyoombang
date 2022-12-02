package com.c22027.nyoombang.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.c22027.nyoombang.R
import com.c22027.nyoombang.databinding.ActivityPaymentBinding
import com.c22027.nyoombang.utils.MERCHANT_BASE_CHECKOUT_URL
import com.c22027.nyoombang.utils.MERCHANT_CLIENT_KEY
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

class PaymentActivity : AppCompatActivity(), TransactionFinishedCallback {

    private val binding by lazy { ActivityPaymentBinding.inflate(layoutInflater) }
    //private val pref by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initMidtransSdk()
        setListener()
    }

    //DONE
    private fun initMidtransSdk() {
        val clientKey: String = MERCHANT_CLIENT_KEY
        val baseUrl: String = MERCHANT_BASE_CHECKOUT_URL
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

    //DONE
    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uiKitCustomSetting = UIKitCustomSetting()
        uiKitCustomSetting.isSkipCustomerDetailsPages = true
        uiKitCustomSetting.isShowPaymentStatus = true
        return uiKitCustomSetting
    }

    //Need Update
    private fun initCustomerDetails(): CustomerDetails {
        val customerDetails = CustomerDetails()
        customerDetails.phone = "082345678999" //get from pref or db
        customerDetails.firstName = "Dika" //get from pref or db
        customerDetails.email = "mail@mail.com" //get from pref or db
        customerDetails.customerIdentifier = "mail@mail.com" //get from pref or db
        return customerDetails
    }


    //Need Update
    private fun setListener() {
        binding.buttonPay.setOnClickListener {
            val paymentAmount = binding.inputPrice.text.toString()
            if (paymentAmount.isEmpty()){
                Toast.makeText(this, "Payment can't null!", Toast.LENGTH_SHORT).show()
            } else if (paymentAmount.toDouble() < 10000.0) {
                Toast.makeText(this, "The minimum payment is RP 10.000", Toast.LENGTH_SHORT).show()
            }
            else {
                MidtransSDK.getInstance().transactionRequest = initTransactionRequest(paymentAmount.toDouble()) //userDetails
                MidtransSDK.getInstance().startPaymentUiFlow(this@PaymentActivity)
            }
        }
    }

    //Need update
    private fun initTransactionRequest(payment: Double): TransactionRequest {
        val orderId = "Nyoombang:" + System.currentTimeMillis().toString()
        //save orderId to pref
        val transactionRequest = TransactionRequest(orderId, payment)
        transactionRequest.customerDetails = initCustomerDetails()
        transactionRequest.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequest.shopeepay = Shopeepay("mysamplesdk:://midtrans")
        return transactionRequest
    }

    //Need Update
    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null){
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
        //Give intent to detail campaign
    }


}