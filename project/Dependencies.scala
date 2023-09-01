import sbt.*

object Dependencies {

  val circe        = "io.circe"      %% "circe-generic"       % Version.circe
  val http4sClient = "org.http4s"    %% "http4s-ember-client" % Version.http4s
  val http4sCirce  = "org.http4s"    %% "http4s-circe"        % Version.http4s
  val http4sDsl    = "org.http4s"    %% "http4s-dsl"          % Version.http4s
  val http4sServer = "org.http4s"    %% "http4s-ember-server" % Version.http4s
  val log4cats     = "org.typelevel" %% "log4cats-slf4j"      % Version.log4cats
  val logback      = "ch.qos.logback" % "logback-classic"     % Version.logback % Runtime

  object Test {
    val munit   = "org.scalameta" %% "munit"               % Version.Munit           % "test"
    val munitCE = "org.typelevel" %% "munit-cats-effect-3" % Version.MunitCatsEffect % "test"
  }

  object Version {
    val circe           = "0.14.6"
    val http4s          = "0.23.23"
    val log4cats        = "2.4.0"
    val logback         = "1.4.11"
    val Munit           = "0.7.29"
    val MunitCatsEffect = "1.0.7"
  }
}
