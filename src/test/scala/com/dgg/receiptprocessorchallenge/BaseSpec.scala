package com.dgg.receiptprocessorchallenge

import cats.effect.unsafe.implicits.global
import cats.effect.{ IO, OutcomeIO, ResourceIO }
import doobie.Transactor
import munit.FunSuite
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait BaseSpec extends FunSuite {

  lazy val config = Configuration.loadDefault().left.map(err => throw err).merge

  implicit lazy val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  implicit lazy val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver     = "org.postgresql.Driver",
    url        = s"jdbc:postgresql://${config.postgres.host}:${config.postgres.port}/${config.postgres.db}",
    user       = config.postgres.user,
    password   = config.postgres.pwd,
    logHandler = None
  )

  val resource: ResourceIO[IO[OutcomeIO[Nothing]]] = Server.run[IO](xa).background

  resource.use_.unsafeRunSync()

}
