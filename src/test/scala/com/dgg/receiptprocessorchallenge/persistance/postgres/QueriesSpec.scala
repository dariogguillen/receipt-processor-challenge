package com.dgg.receiptprocessorchallenge.persistance.postgres

import cats.effect.IO
import com.dgg.api.server.definitions.Receipt
import com.dgg.receiptprocessorchallenge.{ gen, receiptGen, BaseSpec }
import doobie.Transactor
import doobie.util.Colors
import doobie.util.testing.{ analyze, formatReport, Analyzable }
import munit.CatsEffectSuite
import org.scalacheck.Arbitrary
import org.typelevel.log4cats.Logger

import java.util.UUID

class QueriesSpec extends CatsEffectSuite with BaseSpec {

  implicit val arbReceipt: Arbitrary[Receipt] = Arbitrary(receiptGen)

  private val receipt = gen[Receipt]
  private val points  = gen[Int]
  private val id      = gen[UUID]

  test("validate insertReceiptUpdate") {
    assertIO(analyzeQuery(ReceiptProcessorQueries.insertReceiptUpdate(receipt, points)), true)
  }
  test("validate selectReceiptPointsById") {
    assertIO(analyzeQuery(ReceiptProcessorQueries.selectReceiptPointsByIdQry(id)), true)
  }

  protected def analyzeQuery[T](
    query: T
  )(implicit xa: Transactor[IO], log: Logger[IO], analyzable: Analyzable[T]): IO[Boolean] =
    xa.trans.apply(analyze(Analyzable.unpack(query))).flatMap { rep =>
      if (rep.succeeded) IO.pure(true)
      else {
        lazy val report =
          formatReport(Analyzable.unpack(query), rep, Colors.Ansi).padLeft("  ").toString
        log.info(report) *> IO.pure(false)
      }
    }
}
