package com.ceaver.assin.alerts

import com.ceaver.assin.extensions.asCurrencyString
import com.ceaver.assin.markets.Title
import com.ceaver.assin.markets.TitleRepository
import org.apache.commons.csv.CSVRecord
import java.math.BigDecimal

data class MarketcapAlert(
        override val id: Long = 0,
        override val active: Boolean = true,
        val baseTitle: Title,
        override val quoteTitle: Title,
        override val last: BigDecimal,
        override val target: BigDecimal,
        override val diff: BigDecimal?
) : Alert {
    companion object Factory {
        fun fromDto(alertDto: AlertDto): MarketcapAlert {
            require(AlertType.MARKETCAP == alertDto.alert.type)
            return MarketcapAlert(
                    id = alertDto.alert.id,
                    active = alertDto.alert.active,
                    baseTitle = alertDto.baseTitle!!.toTitle(),
                    quoteTitle = alertDto.quoteTitle!!.toTitle(),
                    last = alertDto.alert.last,
                    target = alertDto.alert.target,
                    diff = alertDto.alert.diff
            )
        }

        suspend fun fromImport(csvRecord: CSVRecord): MarketcapAlert {
            require(AlertType.MARKETCAP.name == csvRecord.get(0))
            return MarketcapAlert(
                    active = csvRecord.get(1).toBoolean(),
                    baseTitle = TitleRepository.loadById(csvRecord.get(2)),
                    quoteTitle = TitleRepository.loadById(csvRecord.get(3)),
                    last = csvRecord.get(4).toBigDecimal(),
                    target = csvRecord.get(5).toBigDecimal(),
                    diff = csvRecord.get(6).toBigDecimalOrNull()
            )
        }
    }

    override fun toEntity(): AlertEntity {
        return AlertEntity(
                id = id,
                type = AlertType.MARKETCAP,
                active = active,
                baseTitleId = baseTitle.id,
                quoteTitleId = quoteTitle.id,
                last = last,
                target = target,
                diff = diff
        )
    }

    override fun toExport(): List<String> {
        return listOf(
                AlertType.MARKETCAP.name,
                active.toString(),
                baseTitle.id,
                quoteTitle.id,
                last.toPlainString(),
                target.toPlainString(),
                diff?.toPlainString() ?: ""
        )
    }

    override suspend fun lookupCurrent(): BigDecimal = baseTitle.lookupMarketcap(quoteTitle).toBigDecimal()

    override fun getBaseImageResource(): Int = baseTitle.getIcon()
    override fun getQuoteImageResource(): Int = quoteTitle.getIcon()

    override fun getNotificationTitle(direction: String): String = "${baseTitle.name} Market Cap $direction"
    override fun getNotificationContent(target: BigDecimal): String = "Target of ${target.asCurrencyString(quoteTitle)} reached."

    override fun getBaseText(): String = baseTitle.symbol
    override fun getAlertType(): String = "Market Cap"

    override fun copyWithCurrent(current: BigDecimal): Alert = copy(last = current)
    override fun copyWithCurrentAndDeactivated(current: BigDecimal): Alert = copy(last = current, active = false)
    override fun copyWithCurrentAndTarget(current: BigDecimal, target: BigDecimal): Alert = copy(last = current, target = target)
}