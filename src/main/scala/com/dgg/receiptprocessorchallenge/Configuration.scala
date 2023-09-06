package com.dgg.receiptprocessorchallenge

import org.http4s.Uri.Path
import pureconfig._
import pureconfig.generic.auto._

class ConfigLoadException(reason: String) extends Exception(s"Failed to load application config: $reason")

final case class AppConfig(
  postgres: PostgresConfig,
  port: String
)

object Configuration {

  implicit val uriReader: ConfigReader[Path] = ConfigReader[String].map(Path.unsafeFromString)

  def loadDefault(): Either[ConfigLoadException, AppConfig] =
    ConfigSource.default
      .at("app")
      .load[AppConfig]
      .left
      .map(fails => new ConfigLoadException(fails.toList.mkString("; ")))
}

final case class PostgresConfig(host: String, port: Int, db: String, user: String, pwd: String)
