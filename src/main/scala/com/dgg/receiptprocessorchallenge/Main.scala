package com.dgg.receiptprocessorchallenge

import cats.effect.{ IO, IOApp, Resource }
import com.dgg.receiptprocessorchallenge.persistance.postgres.PostgresConnection
import doobie.hikari.HikariTransactor
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {
  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  val resources: Resource[IO, HikariTransactor[IO]] = for {
    config <- Resource.eval(IO.fromEither(Configuration.loadDefault()))
    tx     <- PostgresConnection.transactorResource[IO](config.postgres)
  } yield tx

  val run: IO[Unit] = resources.use(Server.run[IO])
}
