import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import scala.util.Try

/**
  * Created by ellen.wong on 3/18/17.
  */
class DataLoader {

}

//stores
//Store,Type,Size

//features (additional features related to weekly sales data)
//Store,Date,Temperature,Fuel_Price,MarkDown1,MarkDown2,MarkDown3,MarkDown4,MarkDown5,CPI,Unemployment,IsHoliday
//1,2010-02-05,42.31,2.572,NA,NA,NA,NA,NA,211.0963582,8.106,FALSE
//1,2010-02-12,38.51,2.548,NA,NA,NA,NA,NA,211.2421698,8.106,TRUE


//train (weekly sales data)
//Store,Dept,Date,Weekly_Sales,IsHoliday

//test
//Store,Dept,Date,IsHoliday


trait TimeSeriesData {
  val dateTime: java.time.LocalDateTime
}

case class Store(id:Int, storeType: String, size: Int)
object Store extends Serializable {
  def apply(rawData: Array[String]): Store = Store(id = rawData(0).toInt, storeType = rawData(1), size = rawData(2).toInt)
}

case class WeeklyStoreData(id: Int,
                           department: Int,
                           isHoliday: Boolean,
                           sales: Double,
                           dateTime: LocalDateTime
                          ) extends TimeSeriesData
object WeeklyStoreData extends Serializable {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  def apply(rawData: Array[String]): WeeklyStoreData = {
    WeeklyStoreData(
      id = rawData(0).toInt,
      department = rawData(1).toInt,
      dateTime = LocalDate.parse(rawData(2), formatter).atStartOfDay(),
      sales = rawData(3).toDouble,
      isHoliday = rawData(4).toBoolean)
  }
}

/* store weekly features, irrespective of deparatment*/
case class WeeklyStoreFeatures( id: Int,
                                temperature: Double,
                                fuelPrices: Double,
                                markDown1: Option[Double],
                                markDown2: Option[Double],
                                markDown3: Option[Double],
                                markDown4: Option[Double],
                                markDown5: Option[Double],
                                cpi: Option[Double],
                                unemployment: Option[Double],
                                isHoliday_feature: Boolean,
                                dateTime: LocalDateTime
                              ) extends TimeSeriesData

object WeeklyStoreFeatures extends Serializable {
  def apply(rawData: Array[String]): WeeklyStoreFeatures = {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    WeeklyStoreFeatures(
      id = rawData(0).toInt,
      dateTime = LocalDate.parse(rawData(1), formatter).atStartOfDay(),
      temperature = rawData(2).toDouble,
      fuelPrices = rawData(3).toDouble,
      markDown1 = Try(rawData(4).toDouble).toOption,
      markDown2 = Try(rawData(5).toDouble).toOption,
      markDown3 = Try(rawData(6).toDouble).toOption,
      markDown4 = Try(rawData(7).toDouble).toOption,
      markDown5 = Try(rawData(8).toDouble).toOption,
      cpi = Try(rawData(9).toDouble).toOption,
      unemployment = Try(rawData(10).toDouble).toOption,
      isHoliday_feature = rawData(11).toBoolean
    )
  }
}

case class WeeklyWalmartSalesData(store: Store,
                            department: Int,
                            dateTime: LocalDateTime,
                            sales: Double,
                            isHoliday: Boolean,
                            isHoliday_feature: Boolean,
                            temperature: Double,
                            fuelPrice: Double,
                            cpi: Option[Double],
                            unemployment: Option[Double],
                            markDown1 :Option[Double],
                            markDown2 :Option[Double],
                            markDown3 :Option[Double],
                            markDown4 :Option[Double],
                            markDown5 :Option[Double]
                           ) extends TimeSeriesData
