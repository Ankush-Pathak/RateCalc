package com.ankushpathak.ratecalc

import android.util.Log
import com.ankushpathak.ratecalc.model.Item
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ItemListHelper {

    fun generateItemList(): MutableList<Item> {

        var itemList = generateListFromDB()
        return if (itemList.isNotEmpty()) itemList
        else generateDummyList()
    }

    private fun generateDummyList(): MutableList<Item> {
        var itemsList = mutableListOf<Item>()

        for (i in 1..100) {
            var item = Item()
            item.hSNCode = i.toString()
            item.itemName = "Dummy $i"
            item.rate = if (i % 2 == 0) 5 else 12
            itemsList.add(item)
        }

        return itemsList
    }

    private fun generateListFromDB(): MutableList<Item> {

        if (isDatabaseInsufficient()) {
            var downloadedFile = downloadDataFromWebToFile()
            Log.e("DB", "File downloaded")

            var itemList = parseCSVFileIntoList(downloadedFile)
            Log.e("DB", "File parsed into list")

            populateDatabaseFromList(itemList)
            Log.e("DB", "DB Populated with size: ${itemList.size}")

            return itemList
        }
        Log.e("DB", "Fetching existing DB")
        return fetchListFromDatabase()
    }

    private fun isDatabaseInsufficient(): Boolean {
        var itemBox: Box<Item> = ObjectBox.boxStore.boxFor()

        return itemBox.all.size <= 0
    }

    private fun downloadDataFromWebToFile(): File {
        val fileURL =
            "https://raw.githubusercontent.com/Ankush-Pathak/GSTRateData/master/gSTRates.csv"
        val localFile = File.createTempFile("gSTRates", ".csv")
        URL(fileURL).openStream().use { inputStream ->
            FileOutputStream(localFile).use { output ->
                inputStream.copyTo(output)
            }
        }

        return localFile
    }

    private fun parseCSVFileIntoList(downloadedFile: File): MutableList<Item> {
        var itemList = mutableListOf<Item>()
        var firstSkipped = false
        for (record in csvReader().readAll(downloadedFile)) {
            var item = Item()
            if (record.size == 4 && firstSkipped) {
                item.hSNCode = record[0]
                item.itemName = record[1]
                item.gST = record[2].trimEnd('%').toDouble()
                item.rate = record[3].toInt()

                itemList.add(item)
            }
            firstSkipped = true
        }
        return itemList
    }

    private fun populateDatabaseFromList(itemList: MutableList<Item>) {
        val itemBox: Box<Item> = ObjectBox.boxStore.boxFor()
        itemBox.put(itemList)
    }

    private fun fetchListFromDatabase(): MutableList<Item> {
        val itemBox: Box<Item> = ObjectBox.boxStore.boxFor()
        Log.e("DB", "Fetched existing DB with size: ${itemBox.all.size}")
        return itemBox.all
    }
}