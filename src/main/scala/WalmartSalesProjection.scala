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

object WalmartSalesProjection {

  def main(args: Array[String]) = {
    println("Hello! I am WalmartSalesProjectionApp")
    val value = ConfigFactory.load().getString("filepaths.inputdir")
    println(s"My inputdir is $value")
  }

}


class WalmartSalesProjectionPipeline {


}