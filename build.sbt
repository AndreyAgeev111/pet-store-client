ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

val catsVersion = "2.9.0"
val catsEffect3Version = "3.4.8"
val circeVersion = "0.14.5"
val sttpClientVersion = "3.9.0"
val testVersion = "1.4.0"
val mockitoVersion = "3.2.16.0"
val catsBackendVersion = "3.8.13"
val configVersion = "1.4.2"
val ficusVersion = "1.5.2"
val wireVersion = "2.5.8"
val wireMockVersion = "3.0.0"
val testContainersVersion = "0.40.15"

lazy val root = (project in file("."))
  .settings(
    name := "pet-store-client",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-effect" % catsEffect3Version,
      "org.typelevel" %% "cats-effect-testing-scalatest" % testVersion % Test,
      "com.softwaremill.sttp.client3" %% "core" % sttpClientVersion,
      "com.softwaremill.sttp.client3" %% "circe" % sttpClientVersion,
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % catsBackendVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.scalatestplus" %% "mockito-4-11" % mockitoVersion % Test,
      "com.dimafeng" %% "testcontainers-scala-scalatest" % testContainersVersion,
      "com.typesafe" % "config" % configVersion,
      "com.iheart" %% "ficus" % ficusVersion,
      "com.softwaremill.macwire" %% "macros" % wireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % wireVersion,
      "com.softwaremill.macwire" %% "proxy" % wireVersion,
    ),
  )
