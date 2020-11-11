package com.mprajadinata.happyplace

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

class GetAddressFromLatLng(
    context: Context,
    private val latitude: Double,
    private val longtitude: Double
) : AsyncTask<Void, String, String>() {

    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListener: AddressListener


    override fun doInBackground(vararg p0: Void?): String {
        try {
            val addressList: List<Address>? = geocoder.getFromLocation(latitude, longtitude, 1)

            if (addressList != null && addressList.isNotEmpty()) {
                val address: Address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(",")
                }

                sb.deleteCharAt(sb.length - 1)
                return sb.toString()

            }
        } catch (e: IOException) {
            Log.e("HappyPlaces", "Unable connect to Geocoder")

        }

        return ""
    }

    override fun onPostExecute(result: String?) {
        if (result == null) {
            mAddressListener.onError()

        } else {
            mAddressListener.onAddressFound(result)

        }

        super.onPostExecute(result)
    }

    fun setAddressListener(addressListener: AddressListener) {
        mAddressListener = addressListener
    }

    fun getAddress() {
        execute()
    }

    interface AddressListener {
        fun onAddressFound(adress: String?)
        fun onError()
    }
}