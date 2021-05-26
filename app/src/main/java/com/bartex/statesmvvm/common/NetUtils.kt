package com.bartex.statesmvvm.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService


fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
    return netInfo != null && netInfo.isConnected && netInfo.isAvailable()
}
