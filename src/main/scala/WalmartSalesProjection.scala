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

import com.typesafe.config.ConfigFactory
import org.apache.spark.{SparkConf, SparkContext}

object WalmartSalesProjection {

  def main(args: Array[String]) = {

    println("Hello! I am WalmartSalesProjectionApp")
    try {
      val config = ConfigFactory.load()
      val inputDir = config.getString("filepaths.inputdir")
      println(s"My inputdir is $inputDir")
      val trainingFile = inputDir + config.getString("filepaths.train")
      println(s"My training file is $trainingFile")
      val featuresFile = inputDir + config.getString("filepaths.features")
      println(s"My features file is $featuresFile")

      println(s"***Now Loading Spark***")
      val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
      val sc = new SparkContext(conf)
      println(s"***Loading Spark Complete***")
      val data: Array[String] = sc.textFile(trainingFile).collect()
      println(s"***Training File first line: ${data(0)}")
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

}


class WalmartSalesProjectionPipeline {

}

//first need data in a consumable format: Load Store with weeklySalesData with date
//Randomly Sample the timeSeries data as validation set
//Separate the set to training and validation
//train the training set with baseline
//code evaluation metric: weighted mean absolute error (WMAE):
//Calculate the evaluation metric with validation set
