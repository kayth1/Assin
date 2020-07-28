package com.ceaver.assin.action

import android.os.Handler
import android.os.Looper
import com.ceaver.assin.database.Database
import com.ceaver.assin.positions.Position
import com.ceaver.assin.positions.PositionRepository
import com.ceaver.assin.threading.BackgroundThreadExecutor
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

object ActionRepository {

    fun loadAction(id: Long): Action {
        return getActionDao().loadAction(id)
    }

    fun loadActionAsync(id: Long, callbackInMainThread: Boolean, callback: (Action) -> Unit) {
        BackgroundThreadExecutor.execute {
            val action = getActionDao().loadAction(id)
            if (callbackInMainThread)
                Handler(Looper.getMainLooper()).post { callback.invoke(action) }
            else {
                callback.invoke(action);
            }
        }
    }

    fun loadAllActions(): List<Action> {
        return getActionDao().loadAllActions()
    }

    fun loadAllActionsAsync(callbackInMainThread: Boolean, callback: (List<Action>) -> Unit) {
        BackgroundThreadExecutor.execute {
            val actions = loadAllActions()
            if (callbackInMainThread)
                Handler(Looper.getMainLooper()).post { callback.invoke(actions) }
            else
                callback.invoke(actions)
        }
    }


    fun loadActions(symbol: String): List<Action> {
        return getActionDao().loadAllActions().filter { it.buyTitle?.symbol == symbol || it.sellTitle?.symbol == symbol }
    }

    fun insertDeposit(action: Action) {
        insertAction(action)
    }

    fun insertDepositAsync(action: Action, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute {
            insertDeposit(action)
            if (callbackInMainThread) Handler(Looper.getMainLooper()).post(callback) else callback.invoke()
        }
    }

    fun insertTrade(action: Action) {
        insertAction(action)
    }

    fun insertTradeAsync(action: Action, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute {
            insertTrade(action)
            if (callbackInMainThread) Handler(Looper.getMainLooper()).post(callback) else callback.invoke()
        }
    }

    fun insertWithdraw(action: Action) {
        if (action.positionId != null) {
            insertAction(action)
        } else {
            val positions = PositionRepository.loadPositions(action.sellTitle!!).filter { it.isActive() }
            val oldestPosition = positions.first()
            when (oldestPosition.amount.compareTo(action.sellAmount)) {
                0 -> {
                    insertWithdraw(action.copy(positionId = oldestPosition.id))
                }
                1 -> {
                    insertSplit(oldestPosition, action.sellAmount!!);
                    insertWithdraw(action)
                }
                -1 -> {
                    var index = 1
                    val mergeList = mutableListOf<Position>(oldestPosition, positions.get(1))
                    while (mergeList.map { it.amount }.reduce { acc, bigDecimal -> acc.add(bigDecimal) }.compareTo(action.sellAmount) == -1) {
                        index++
                        mergeList.add(positions.get(index))
                    }

                    when (mergeList.map { it.amount }.reduce { acc, bigDecimal -> acc.add(bigDecimal) }.compareTo(action.sellAmount)) {
                        0 -> {
                            mergeList.forEach {
                                insertWithdraw(action.copy(positionId = it.id, sellAmount = it.amount))
                            }
                        }
                        1 -> {
                            val splitPosition = mergeList.last()
                            val splitAmount = mergeList.map { it.amount }.reduce { acc, bigDecimal -> acc.add(bigDecimal) }.minus(action.sellAmount!!)
                            insertSplit(splitPosition, splitAmount)
                            insertWithdraw(action)
                        }
                    }
                }
            }
        }
    }

    fun insertWithdrawAsync(action: Action, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute {
            insertWithdraw(action)
            if (callbackInMainThread) Handler(Looper.getMainLooper()).post(callback) else callback.invoke()
        }
    }

    fun insertSplit(position: Position, sellAmount: BigDecimal) {
        insertAction(Action.split(position, sellAmount))
    }

    fun insertActions(actions: List<Action>) {
        getActionDao().insertActions(actions)
        EventBus.getDefault().post(ActionEvents.Insert())
    }

    fun insertActionsAsync(actions: List<Action>, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute { insertActions(actions); if (callbackInMainThread) Handler(Looper.getMainLooper()).post(callback) else callback.invoke() }
    }

    private fun insertActionAsync(action: Action, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute {
            insertAction(action)
            if (callbackInMainThread) Handler(Looper.getMainLooper()).post(callback) else callback.invoke()
        }
    }

    private fun insertAction(action: Action) {
        getActionDao().insertAction(action)
        EventBus.getDefault().post(ActionEvents.Insert())
    }

    fun updateAction(action: Action) {
        getActionDao().updateAction(action); EventBus.getDefault().post(ActionEvents.Update())
    }

    fun updateActionAsync(action: Action, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute { updateAction(action); if (callbackInMainThread) Handler(Looper.getMainLooper()).post(callback) else callback.invoke() }
    }

    fun deleteAction(action: Action) {
        getActionDao().deleteAction(action); EventBus.getDefault().post(ActionEvents.Delete())
    }

    fun deleteActionAsync(action: Action) {
        BackgroundThreadExecutor.execute { deleteAction(action) }
    }

    fun deleteAllActions() {
        getActionDao().deleteAllActions(); EventBus.getDefault().post(ActionEvents.DeleteAll())
    }

    fun deleteAllActionsAsync() {
        deleteAllActionsAsync() {}
    }

    fun deleteAllActionsAsync(callback: () -> Unit) {
        BackgroundThreadExecutor.execute { deleteAllActions(); callback.invoke() }
    }

    private fun getActionDao(): ActionDao {
        return getDatabase().actionDao()
    }

    private fun getDatabase(): Database {
        return Database.getInstance()
    }
}