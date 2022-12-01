package com.c22027.nyoombang.ui.preview



/*
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    setData()
    getDataTotalAmount()
    setupListener()
}
*/


/*
private fun setupListener() {
    binding.bayarDonasi.setOnClickListener {
        startActivity(Intent(this, PaymentActivity::class.java))
    }
    binding.refreshData.setOnRefreshListener {
        fetchApi()
        getDataTotalAmount()
        binding.refreshData.isRefreshing = false
    }
}
*/


/*
private fun setData(){
    *pass data parcelable from home
    campaign = intent.getParcelableExtra<Campaign>(EXTRA_CAMPAIGN) as Campaign
    binding.apply {
        tvCampaignName.text = campaign!!.campaign_name
    }
}
*/


/*
private fun getDataTotalAmount() {
    *data amount terakhir
    var totalAmount = campaign!!.amount.toString().toInt()
    *get data from firestore
    firestore.collection("transaction")
        .whereEqualTo("campaignName", campaign!!.campaign_name)
        .whereEqualTo("status", "settlement")
        .get()
        .addOnSuccessListener { result ->
            result.forEach { doc ->
                *jumlahkan total amount
                totalAmount += doc.data["amount"].toString().toInt()
            }
            binding.tvAmount.text = totalAmount.toString()
        }
}*/

/*

private fun fetchApi() {
    *orderId diambil dari preference
    val orderId = pref.getString(Utils.PREF_ORDER_ID)

    if (orderId?.isEmpty() == false){
        val client = ApiConfig.getApiService().getTransaction(orderId)
        client.enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {

                if (response.isSuccessful){
                    *ketika response success from Api kemudian add data to firebase
                    val responseBody = response.body()!!
                    val addTransactionData =
                        UserTransaction(
                            id = null, *id ini akan generate random di firebase
                            orderId = responseBody.orderId, *ambil dari firebase
                            campaignName = campaign!!.campaign_name, *ambil dari parcelable
                            username = pref.getString(Utils.PREF_USERNAME)!!, *ambil dari preference
                            amount = Utils.amountFormat(responseBody.grossAmount.toDouble()), *ambil dari api namun perlu di format dahulu
                            status = responseBody.transactionStatus, *ambil dari api
                            transactionTime = responseBody.transactionTime *ambil dari api
                        )
                    *ketika telah melakukan pembayaran maka orderId di pref akan dihapus
                    if (responseBody.transactionStatus == "settlement"){
                        pref.clearOrderId()
                    }
                    *send data ke firebase
                    firestore.collection("transaction")
                        .add(addTransactionData)
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
*/
