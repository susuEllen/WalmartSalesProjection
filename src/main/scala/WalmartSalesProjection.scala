/**
  * Created by ellenwong on 3/15/17.
  * Kaggle challenge:
  * https://www.kaggle.com/c/walmart-recruiting-store-sales-forecasting/data
  *
  * Objective:
  * You are provided with the historical sales data for 45 Walmart stores located in different regions.
  * Each store contains many departments,
  * and participants must project the sales for each department in each store.
  * To add to the challenge, selected holiday markdown events are included in the dataset.
  * These markdowns are known to affect sales,
  * but it is challenging to predict which departments are affected and the extent of the impact.
  */

import java.io.File
import java.time.LocalDateTime

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD

object WalmartSalesProjection {

  def main(args: Array[String]) = {
    println("Hello! I am WalmartSalesProjectionApp")
    //val value = ConfigFactory.load().getString("filepaths.inputdir")
//    println(s"My inputdir is $value")
    implicit val config = ConfigFactory.load()
    val walmartSalesProjectionPipeline = new WalmartSalesProjectionPipeline()
    walmartSalesProjectionPipeline.run()
  }
}


class WalmartSalesProjectionPipeline(implicit val config: Config) extends Serializable {

  val currentDir = new File(".").getCanonicalPath
  val featureInputCSV = s"$currentDir/src/main/resources/${config.getString("filepaths.features")}"
  val storesInfoCSV = s"$currentDir/src/main/resources/${config.getString("filepaths.stores")}"
  //val trainingDataCSV = s"$currentDir/src/main/resources/${config.getString("filepaths.train")}" //TODO: comment this once the pipeline is done to run on the whole set
  val trainingDataCSV = s"$currentDir/src/main/resources/${config.getString("filepaths.train100")}"
  val HeaderPrefix = "Store,"

  def run() = {

    println(s"featureInputCSV: $featureInputCSV\nstoresInfoCSV: $storesInfoCSV\ntrainingDataCSV: $trainingDataCSV\n")
    //Read the raw file
    val conf = new SparkConf().setAppName("WalmartSalesProjectionPipeline").setMaster("local")
    val sc = new SparkContext(conf)

    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.FATAL)

    val storeInfoRDD: RDD[String] = sc.textFile(storesInfoCSV).filter(!_.startsWith(HeaderPrefix))
    val featuresInfoRDD: RDD[String] = sc.textFile(featureInputCSV).filter(!_.startsWith(HeaderPrefix))
    val trainingDataRDD: RDD[String] = sc.textFile(trainingDataCSV).filter(!_.startsWith(HeaderPrefix))

    val stores: Array[Store] = storeInfoRDD.map{
      storeInfoRow =>
        val splittedRow = storeInfoRow.split(",")
        Store(splittedRow)}.collect()

    val weeklyStoreFeatures: Array[WeeklyStoreFeatures] = featuresInfoRDD.map {
      featurePerStorePerWeek =>
        val splittedRow = featurePerStorePerWeek.split(",")
        WeeklyStoreFeatures(splittedRow)
    }.collect()

    val trainingData: RDD[WeeklyWalmartSalesData] = trainingDataRDD.map{
      trainingDataRow =>
        val splittedRow = trainingDataRow.split(",")
        val weeklyStoreData = WeeklyStoreData(splittedRow)
        val featureForThisStoreThisWeek = weeklyStoreFeatures.filter(
          storeFeature => (storeFeature.id == weeklyStoreData.id) && (storeFeature.dateTime == weeklyStoreData.dateTime))
        require(!featureForThisStoreThisWeek.isEmpty)  // throw if can't find feature for the store
        val storeInfoForThisStore: Array[Store] = stores.filter(_.id == weeklyStoreData.id)

        WeeklyWalmartSalesData(
          store = storeInfoForThisStore.head,
          department = weeklyStoreData.department,
          dateTime = weeklyStoreData.dateTime,
          sales = weeklyStoreData.sales,
          isHoliday = weeklyStoreData.isHoliday,
          isHoliday_feature = featureForThisStoreThisWeek.head.isHoliday_feature,
          temperature = featureForThisStoreThisWeek.head.temperature,
          fuelPrice = featureForThisStoreThisWeek.head.fuelPrices,
          cpi = featureForThisStoreThisWeek.head.cpi,
          unemployment = featureForThisStoreThisWeek.head.unemployment,
          markDown1 = featureForThisStoreThisWeek.head.markDown1,
          markDown2 = featureForThisStoreThisWeek.head.markDown2,
          markDown3 = featureForThisStoreThisWeek.head.markDown3,
          markDown4 = featureForThisStoreThisWeek.head.markDown4,
          markDown5 = featureForThisStoreThisWeek.head.markDown5)
    }

    /* print out so statistics*/
    /* count training data, count store data, count features, first few training data, features count
    * */
    println(s"\nStore Count : ${stores.length}" +
      s"\nFeaturesInfo Count :${weeklyStoreFeatures.length}" +
      s"\nTrainingSet Count :${trainingData.count()}" +
      s"\nTrainingDataRaw :\n${trainingData.take(10).mkString("\n")}\n")

    sc.stop()
  }
}



//first need data in a consumable format: Load Store with weeklySalesData with date
//Randomly Sample the timeSeries data as validation set
//Separate the set to training and validation
//train the training set with baseline
//code evaluation metric: weighted mean absolute error (WMAE):
//Calculate the evaluation metric with validation set
