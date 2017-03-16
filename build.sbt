name := "WalmartSalesProjection"

version := "1.0"

scalaVersion := "2.10.6"

val sparkVersion = "2.1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "2.1.0",
  "org.apache.spark" % "spark-mllib_2.10" % "2.1.0"
)

//https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.10/2.1.0
//https://mvnrepository.com/artifact/org.apache.spark/spark-mllib_2.10/2.1.0