package com.dgg.receiptprocessorchallenge.persistance.postgres

import cats.effect.{ Async, Resource }
import com.dgg.receiptprocessorchallenge.PostgresConfig
import com.zaxxer.hikari.HikariConfig
import doobie.hikari.HikariTransactor
import org.typelevel.log4cats.Logger

object PostgresConnection {

  def transactorResource[F[_]: Logger: Async](config: PostgresConfig): Resource[F, HikariTransactor[F]] =
    Resource.eval {
      Async[F].delay {
        val hc = new HikariConfig()
        hc.setDriverClassName("org.postgresql.Driver")
        hc.setJdbcUrl(s"jdbc:postgresql://${config.host}:${config.port}/${config.db}")
        hc.setUsername(config.user)
        hc.setPassword(config.pwd)
        hc
      }
    }.flatMap(HikariTransactor.fromHikariConfig[F](_))

}
