package com.ankushpathak.ratecalc.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
class Item : Observable() {
    @Id
    var id: Long = 0
    var gST: Double = 5.0
        set(value) {
            field = value
            setChangedAndNotify("gST")
        }

    var hSNCode: String = ""
        set(value) {
            field = value
            setChangedAndNotify("hSNCode")
        }

    var itemName: String = ""
        set(value) {
            field = value
            setChangedAndNotify("itemName")
        }

    //Try using two-way binding
    var rate: Int = 0
        set(value) {
            field = value
            setChangedAndNotify("rate")
        }

    private fun setChangedAndNotify(field: Any) {
        setChanged()
        notifyObservers(field)
    }

    override fun toString(): String {
        return itemName
    }

}