package com.dgg.receiptprocessorchallenge

import cats.effect.{ Async, Resource }
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.io.net.Network
import org.http4s.HttpRoutes
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object Server {

  def run[F[_]: Async: Network]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
      httpApp = (HttpRoutes.empty[F]).orNotFound

      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
      _ <- EmberServerBuilder
             .default[F]
             .withHost(ipv4"0.0.0.0")
             .withPort(port"8080")
             .withHttpApp(finalHttpApp)
             .build
    } yield ()
  }.useForever
}
