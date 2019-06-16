package com.ceaver.assin.intentions

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ceaver.assin.markets.Title
import java.time.LocalDate
import java.util.*

@Entity(tableName = "intention")
data class Intention(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = "type") val type: IntentionType,
        @ColumnInfo(name = "title") var title: Title,
        @ColumnInfo(name = "amount") var amount: Optional<Double>,
        @ColumnInfo(name = "referenceTitle") var referenceTitle: Title,
        @ColumnInfo(name = "referencePrice") var referencePrice: Double,
        @ColumnInfo(name = "creationDate") var creationDate: LocalDate = LocalDate.now(),
        @ColumnInfo(name = "status") val status: IntentionStatus = IntentionStatus.WAIT,
        @ColumnInfo(name = "comment") var comment: String = "") {

    fun percentToReferencePrice(): Double {
        val price = when (referenceTitle.symbol) {
            "USD" -> title.priceUsd
            "BTC" -> title.priceBtc
            "ETH" -> title.priceEth
            else -> throw IllegalStateException()
        }
        return when (type) {
            IntentionType.SELL -> (100.div(referencePrice)).times(price)
            IntentionType.BUY -> (100.div(price)).times(referencePrice)
        }
    }

    fun amountAsString(): String {
        return if (amount.isPresent) amount.get().toString() else ""
    }

    fun calculateState(): IntentionStatus {
        return when (percentToReferencePrice()) {
            in 0.0..80.0 -> IntentionStatus.WAIT
            in 80.0..100.0 -> IntentionStatus.NEAR
            else -> IntentionStatus.ACT
        }
    }
}