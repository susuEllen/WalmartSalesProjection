import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.util.Try

/**
  * Created by ellen.wong on 3/18/17.
  */
object DataLoader {

  def loadRDDFromPath(sc: SparkContext, path: String): RDD[String] = {
    println(s"loading into RDD from path: $path")
    sc.textFile(path)
  }
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



