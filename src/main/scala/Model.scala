import org.apache.spark.rdd.RDD

/**
  * Created by ellenwong on 3/19/17.
  * Baseline Model use the median of the stores as prediction
  */
class BaseLineModel {

  def train(inputData: RDD[WeeklyWalmartSalesData]): (Store) => Double = {
    val model: (Store => Double) = inputData.groupBy(_.store).map {
      case (store, storeSalesData) =>
        val sortedSalesAll = storeSalesData.map(_.sales).toSeq.sorted
        (store -> sortedSalesAll.drop(sortedSalesAll.size / 2).head)
    }.collect().toMap
    model
  }
}
