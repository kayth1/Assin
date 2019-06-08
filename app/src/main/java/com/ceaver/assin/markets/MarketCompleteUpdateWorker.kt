package com.ceaver.assin.markets

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ceaver.assin.logging.LogRepository

class MarketCompleteUpdateWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val allRemoteTitles = MarketRepository.loadAllTitles()
        val allLocalTitles = TitleRepository.loadAllTitles()

        if (allLocalTitles.isEmpty()) { // initial load
            TitleRepository.insertAll(allRemoteTitles.map { it.copy(active = Integer(50)) }.toSet())
        } else {
            val newTitlesToInsert = allRemoteTitles - allLocalTitles
            val existingTitlesToUpdate = (allRemoteTitles - newTitlesToInsert).map { it.incrementActiveCounter() }
            val removedTitles = allLocalTitles - allRemoteTitles
            val removedTitlesToDelete = removedTitles.filter { it.inactive() }
            val removedTitlesToUpdate = (removedTitles - removedTitlesToDelete).map { it.decreaseActiveCounter() }

            TitleRepository.insertAll(newTitlesToInsert)
            TitleRepository.updateAll((existingTitlesToUpdate + removedTitlesToUpdate).toSet())
            TitleRepository.deleteTitles(removedTitlesToDelete.toSet())

            existingTitlesToUpdate.filter { it.active.toInt() == 50 }.forEach { LogRepository.insertLog("Activated  ${it.name} (${it.symbol}).") }
            removedTitlesToDelete.forEach { LogRepository.insertLog("Removed  ${it.name} (${it.symbol}) from local database due to long time inactivity.") }
        }

        TitleRepository.update(TitleRepository.loadTitleBySymbol("USD").copy(priceBtc = 1 / TitleRepository.loadTitleBySymbol("BTC").priceUsd))

        return Result.success()
    }
}