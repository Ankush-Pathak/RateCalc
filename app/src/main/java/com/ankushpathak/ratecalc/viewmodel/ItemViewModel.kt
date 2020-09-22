package com.ankushpathak.ratecalc.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ankushpathak.ratecalc.BR
import com.ankushpathak.ratecalc.model.Item
import java.util.*


class ItemViewModel(private var item: Item) : Observer, BaseObservable() {
    init {
        item.addObserver(this)
    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg is String) {
            when (arg) {
                "gST" -> notifyPropertyChanged(BR.gST)
                "hSNCode" -> notifyPropertyChanged(BR.hSNCode)
                "itemName" -> notifyPropertyChanged(BR.itemName)
                "rate" -> {
                    notifyPropertyChanged(BR.rate)
                    notifyPropertyChanged(BR.amount)
                }
            }
        }
    }

    val gST: String
        @Bindable get() {
            return "${item.gST}%"
        }

    val hSNCode: String
        @Bindable get() {
            return item.hSNCode
        }

    val itemName: String
        @Bindable get() {
            return item.itemName
        }

    val rate: String
        @Bindable get() {
            return item.rate.toString()
        }


    val amount: String
        @Bindable get() {
            return getAmount().toString()
        }

    private fun getAmount() = item.rate + (item.rate * item.gST / 100)


    fun onRateChanged(text: CharSequence?) {

        if (text != null && text.isNotEmpty()) {
            this.item.rate = text.toString().toInt()
            notifyPropertyChanged(BR.amount)
        }
    }
}