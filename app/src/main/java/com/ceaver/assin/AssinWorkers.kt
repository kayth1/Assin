package com.ceaver.assin

import android.content.Context
import androidx.work.*
import com.ceaver.assin.alerts.AlertRepository
import com.ceaver.assin.alerts.AlertWorker
import com.ceaver.assin.assets.AssetCategory
import com.ceaver.assin.intentions.IntentionRepository
import com.ceaver.assin.intentions.IntentionWorker
import com.ceaver.assin.logging.LogRepository
import com.ceaver.assin.markets.MarketCompleteUpdateWorker
import com.ceaver.assin.markets.MarketPartialUpdateWorker
import com.ceaver.assin.markets.Title
import com.ceaver.assin.trades.TradeRepository
import org.greenrobot.eventbus.EventBus
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

object AssinWorkers {

    fun completeUpdate() {
        val identifier = UUID.randomUUID();
        WorkManager.getInstance()
                .beginWith(notifyCompleteStart(identifier))
                .then(updateAllTitles())
                .then(listOf(checkAlerts(), checkIntentions()))
                .then(notifyCompleteEnd(identifier))
                .enqueue()
    }


    fun observedUpdate() {
        val identifier = UUID.randomUUID();
        WorkManager.getInstance()
                .beginWith(notifyObservedStart(identifier))
                .then(updateObservedTitles())
                .then(listOf(checkAlerts(), checkIntentions()))
                .then(notifyObservedEnd(identifier))
                .enqueue()
    }


    private fun notifyCompleteStart(identifier: UUID): OneTimeWorkRequest {
        val data = Data.Builder().putString(AssinWorkers.toString(), identifier.toString()).build()
        return OneTimeWorkRequestBuilder<StartCompleteNotificationWorker>().setInputData(data).build()
    }

    private fun notifyObservedStart(identifier: UUID): OneTimeWorkRequest {
        val data = Data.Builder().putString(AssinWorkers.toString(), identifier.toString()).build()
        return OneTimeWorkRequestBuilder<StartObservedNotificationWorker>().setInputData(data).build()
    }

    private fun notifyCompleteEnd(identifier: UUID): OneTimeWorkRequest {
        val data = Data.Builder().putString(AssinWorkers.toString(), identifier.toString()).build()
        return OneTimeWorkRequestBuilder<EndCompleteNotificationWorker>().setInputData(data).build()
    }

    private fun notifyObservedEnd(identifier: UUID): OneTimeWorkRequest {
        val data = Data.Builder().putString(AssinWorkers.toString(), identifier.toString()).build()
        return OneTimeWorkRequestBuilder<EndObservedNotificationWorker>().setInputData(data).build()
    }

    private fun checkIntentions(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<IntentionWorker>().build()
    }

    private fun checkAlerts(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<AlertWorker>().build()
    }

    private fun updateAllTitles(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<MarketCompleteUpdateWorker>().build()
    }

    private fun updateObservedTitles(): MutableList<OneTimeWorkRequest> {
        var index: Int = 0
        val tradeTitles = TradeRepository.loadAllTrades().flatMap { it.getTitles() }.toSet() // TODO only trades with positive sum
        val intentionTitles = IntentionRepository.loadAllIntentions().map { it.title }.toSet()
        val alertTitles = AlertRepository.loadAllAlerts().flatMap { setOf(it.symbol, it.reference) }.toSet()
        return (tradeTitles + intentionTitles + alertTitles).filter { it.category == AssetCategory.CRYPTO }.map { marketPartialUpdateRequestBuilder(it, index++) }.toMutableList()
    }

    private fun marketPartialUpdateRequestBuilder(title: Title, index: Int): OneTimeWorkRequest {
        val data = Data.Builder().putString("Symbol", title.symbol).putInt("sleep", index).build() // TODO use better identifier
        return OneTimeWorkRequestBuilder<MarketPartialUpdateWorker>().setInputData(data).build()
    }

    class StartCompleteNotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val uuid = UUID.fromString(inputData.getString(AssinWorkers.toString()))
            LogRepository.insertLog("Assin workers: starting complete update...", uuid)
            return Result.success()
        }
    }

    class StartObservedNotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val uuid = UUID.fromString(inputData.getString(AssinWorkers.toString()))
            LogRepository.insertLog("Assin workers: starting observed update...", uuid)
            return Result.success()
        }
    }

    class EndCompleteNotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val uuid = UUID.fromString(inputData.getString(AssinWorkers.toString()))
            val log = LogRepository.loadLog(uuid)
            val duration = log.timestamp.until(LocalDateTime.now(), ChronoUnit.MILLIS)
            LogRepository.updateLog(log.copy(message = log.message + " done. (${duration} ms)"))
            EventBus.getDefault().post(AssinWorkerEvents.Complete())
            return Result.success()
        }
    }

    class EndObservedNotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val uuid = UUID.fromString(inputData.getString(AssinWorkers.toString()))
            val log = LogRepository.loadLog(uuid)
            val duration = log.timestamp.until(LocalDateTime.now(), ChronoUnit.MILLIS)
            LogRepository.updateLog(log.copy(message = log.message + " done. (${duration} ms)"))
            EventBus.getDefault().post(AssinWorkerEvents.Observed())
            return Result.success()
        }
    }
}