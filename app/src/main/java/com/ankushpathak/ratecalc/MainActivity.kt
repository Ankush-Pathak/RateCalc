package com.ankushpathak.ratecalc

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ankushpathak.ratecalc.model.Item
import com.ankushpathak.ratecalc.viewmodel.ItemViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var itemList: MutableList<Item>
    private lateinit var binding: ViewDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ViewDataBinding>(
            this@MainActivity,
            R.layout.activity_main
        )

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        itemList = ItemListHelper().generateItemList()
        var spinnerItemList = findViewById<Spinner>(R.id.spinner_item_list)
        spinnerItemList.onItemSelectedListener = spinnerItemSelectedListener

        populateSpinner(itemList, spinnerItemList)


    }

    private var spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val itemViewModel = ItemViewModel(itemList[position])
            Log.e("Spinner", "Item selected")
            binding.setVariable(BR.item, itemViewModel)
            binding.executePendingBindings()
        }
    }

    private fun populateSpinner(itemList: MutableList<Item>, spinnerItemList: Spinner) {
        val arrayAdapterItemList =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, itemList)
        arrayAdapterItemList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerItemList.adapter = arrayAdapterItemList
        arrayAdapterItemList.notifyDataSetChanged()
    }


}