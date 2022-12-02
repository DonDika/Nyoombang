package com.c22027.nyoombang.ui.profile.user


private fun getUserTrans() {
    //val transactions: ArrayList<UserTransaction> = arrayListOf()
    //get transaction data from firestore

    /*
    *Ambil collection "transaction"
    firestore.collection("transaction")
        *berdasarkan field "username" yang sesuai dengan yg ada di pref username
        .whereEqualTo("username", pref.getString(Utils.PREF_USERNAME))
        *berdasarkan field "status" yang sudah settlement
        .whereEqualTo("status", "settlement")
        .get()
        .addOnSuccessListener { result ->
            result.forEach { doc ->
                val transactionData = UserTransaction(
                    //id document
                    id = doc.reference.id,
                    orderId = doc.data["orderId"].toString(),
                    username = doc.data["username"].toString(),
                    campaignName = doc.data["campaignName"].toString(),
                    amount = doc.data["amount"].toString(),
                    status = doc.data["status"].toString(),
                    transactionTime = doc.data["transactionTime"].toString()
                )
                transactions.add(transactionData)
            }
            //kirim ke recycler view
            userTransactionAdapter.setData(transactions)
        }
     */
}