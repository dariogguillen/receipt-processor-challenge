package com.dgg.receiptprocessorchallenge

import cats.effect.IO
import doobie.Transactor
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait BaseSpec {

  val config = Configuration.loadDefault().left.map(err => throw err).merge

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  implicit val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver     = "org.postgresql.Driver",
    url        = s"jdbc:postgresql://${config.postgres.host}:${config.postgres.port}/${config.postgres.db}",
    user       = config.postgres.user,
    password   = config.postgres.pwd,
    logHandler = None
  )

}
