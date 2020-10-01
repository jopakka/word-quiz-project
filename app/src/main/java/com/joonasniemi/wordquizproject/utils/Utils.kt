/*
 * Joonas Niemi
 * 1909887
 */

/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.game.Quiz

object Utils {

    /**
     * Check if phone has WIFI or cellular network on.
     * Copy/paste code from https://stackoverflow.com/a/53532456
     * Why change it if it works
     */
    fun isConnected(context: Context): Boolean{
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val networkCapabilities = cm.activeNetwork ?: return false
            val activeNetwork = cm.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }
}