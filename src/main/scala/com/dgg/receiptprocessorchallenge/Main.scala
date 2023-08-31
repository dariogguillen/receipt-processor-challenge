package com.dgg.receiptprocessorchallenge

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = ReceiptprocessorchallengeServer.run[IO]
}
