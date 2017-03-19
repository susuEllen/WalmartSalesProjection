/**
  * Created by ellenwong on 3/19/17.
  */
object Metrics {

  /*
  * https://en.wikipedia.org/wiki/Mean_squared_error
  */

  def MeanSquareError(predictedAndActualResult: Array[(Double,Double)]) : Double = {
    // take the diff, square it, sum the result, divide by total no of results
    val sumOFMSE = predictedAndActualResult.foldLeft(0.0){
      (sumOfSquareDifferenceSoFar, currentResult)  =>
          val diff = currentResult._2 - currentResult._1
          val squareError = diff * diff
          println(s"diff: $diff, squreError: $squareError")
        sumOfSquareDifferenceSoFar + squareError
      }
    println(s"sumOFMSE: $sumOFMSE")
    sumOFMSE/ predictedAndActualResult.length
  }
}
