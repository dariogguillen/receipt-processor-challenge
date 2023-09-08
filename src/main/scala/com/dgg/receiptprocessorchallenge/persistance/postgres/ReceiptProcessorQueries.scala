package com.dgg.receiptprocessorchallenge.persistance.postgres

import com.dgg.api.server.definitions.Receipt
import doobie.{ ConnectionIO, Meta }
import doobie.util.update.Update0
import doobie.postgres.circe.json.implicits.{ pgDecoderGet, pgEncoderPut }
import doobie.postgres.implicits._
import doobie.util.query.Query0
import io.circe.Json
import io.circe.syntax._

import java.util.UUID

object ReceiptProcessorQueries extends PostgresImplicits {

  implicit val metaJson: Meta[Json] = new Meta(pgDecoderGet, pgEncoderPut)

  def insertReceiptUpdate(receipt: Receipt, points: Int): Update0 =
    sql"""
         |insert into receipt_processor (retailer, purchase_date, purchase_time, items, total, points)
         |values (
         |  ${receipt.retailer},
         |  ${receipt.purchaseDate},
         |  ${receipt.purchaseTime},
         |  ${receipt.items.asJson},
         |  ${receipt.total},
         |  $points
         |)
         |""".stripMargin.update

  def insertReceipt(receipt: Receipt, points: Int): ConnectionIO[UUID] =
    insertReceiptUpdate(receipt, points).withUniqueGeneratedKeys("id")

  def selectReceiptPointsByIdQry(id: UUID): Query0[Int] =
    sql"select points from receipt_processor where id = $id".query[Int]

  def selectReceiptPointsById(id: UUID): doobie.ConnectionIO[Option[Int]] = selectReceiptPointsByIdQry(id).option
}
