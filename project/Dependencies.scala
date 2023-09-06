import sbt.*

object Dependencies {

  val circe          = "io.circe"              %% "circe-generic"       % Version.circe
  val doobieCore     = "org.tpolecat"          %% "doobie-core"         % Version.doobie
  val doobieHikari   = "org.tpolecat"          %% "doobie-hikari"       % Version.doobie
  val doobiePostgres = "org.tpolecat"          %% "doobie-postgres"     % Version.doobie
  val http4sClient   = "org.http4s"            %% "http4s-ember-client" % Version.http4s
  val http4sCirce    = "org.http4s"            %% "http4s-circe"        % Version.http4s
  val http4sDsl      = "org.http4s"            %% "http4s-dsl"          % Version.http4s
  val http4sServer   = "org.http4s"            %% "http4s-ember-server" % Version.http4s
  val log4cats       = "org.typelevel"         %% "log4cats-slf4j"      % Version.log4cats
  val logback        = "ch.qos.logback"         % "logback-classic"     % Version.logback % Runtime
  val postgresql     = "org.postgresql"         % "postgresql"          % Version.postgresql
  val pureconfig     = "com.github.pureconfig" %% "pureconfig"          % Version.pureconfig

  object Test {
    val munit   = "org.scalameta" %% "munit"               % Version.Munit           % "test"
    val munitCE = "org.typelevel" %% "munit-cats-effect-3" % Version.MunitCatsEffect % "test"
  }

  object Version {
    val circe           = "0.14.6"
    val doobie          = "1.0.0-RC4"
    val http4s          = "0.23.23"
    val log4cats        = "2.5.0"
    val logback         = "1.4.11"
    val Munit           = "0.7.29"
    val MunitCatsEffect = "1.0.7"
    val postgresql      = "42.5.4"
    val pureconfig      = "0.17.4"
  }
}
