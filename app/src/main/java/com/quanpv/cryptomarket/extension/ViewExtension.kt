package com.quanpv.cryptomarket.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.lang.Exception

/**
 * Created by QuanPham on 5/31/18.
 */

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
    Picasso.get().load(url).into(this)
}

inline fun ImageView.loadUrl(url: String, callback: KCallback.() -> Unit) {
    Picasso.get().load(url).intoWithCallback(this, callback)
}

inline fun RequestCreator.intoWithCallback(target: ImageView, callback: KCallback.() -> Unit) {
    this.into(target, KCallback().apply(callback))
}

class KCallback : Callback {

    private var onSuccess: (() -> Unit)? = null
    private var onError: ((Exception) -> Unit)? = null

    override fun onSuccess() {
        onSuccess?.invoke()
    }

    override fun onError(e: Exception?) {
        onError?.invoke(e!!)
    }

    fun onSuccess(function: () -> Unit) {
        this.onSuccess = function
    }

    fun onError(function: (Exception) -> Unit) {
        this.onError = function
    }
}


//public static void setPercentChangeTextView(TextView textView, String pctChange, String time,
//String negativePercentStringResource, String positivePercentStringResource,
//int negativeRedColor, int positiveGreenColor, String pctChangeNotAvailableStringResource) {
//    if (pctChange == null) {
//        textView.setText(String.format(pctChangeNotAvailableStringResource, time));
//    } else {
//        double change = Double.parseDouble(pctChange);
//        if (change < 0) {
//            textView.setText(String.format(negativePercentStringResource, time, change));
//            textView.setTextColor(negativeRedColor);
//        } else {
//            textView.setText(String.format(positivePercentStringResource, time, change));
//            textView.setTextColor(positiveGreenColor);
//        }
//    }
//}


fun TextView.setPercentChangeTextView(pctChange: String?, time: String, negativePercentStringResource: String, positivePercentStringResource: String,
                                      negativeRedColor: Int, positiveGreenColor: Int, pctChangeNotAvailableStringResource: String) {

    if (pctChange == null) {
        this.text = String.format(pctChangeNotAvailableStringResource, time)
    } else {
        val change = pctChange.toDouble()
        if (change < 0) {
            this.text = String.format(negativePercentStringResource, time, change)
            this.setTextColor(negativeRedColor)
        } else {
            this.text = String.format(positivePercentStringResource, time, change)
            this.setTextColor(positiveGreenColor)
        }
    }

}




