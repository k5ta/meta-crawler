lazy val root =
  Project("MetaCrawler", file("."))
    .settings(
      version := "1.0",
      scalaVersion := "2.13.3",
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.4.0",
        "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0",

        "org.apache.httpcomponents" % "httpclient" % "4.5.12",

        "org.jsoup" % "jsoup" % "1.13.1"
      )
    )