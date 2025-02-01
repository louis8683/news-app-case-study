package com.louislu.core.presentation.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


@Composable
fun rememberConnectivityStatus(): Boolean {
    val context = LocalContext.current
    val connectivityManager = remember { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    val networkStatus = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                networkStatus.value = false
            }

            override fun onAvailable(network: Network) {
                networkStatus.value = true
            }
        }

        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, callback)

//        onDispose {
//            connectivityManager.unregisterNetworkCallback(callback)
//        }
    }

    return networkStatus.value
}
