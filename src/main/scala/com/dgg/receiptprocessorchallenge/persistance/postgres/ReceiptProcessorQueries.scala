package com.dgg.receiptprocessorchallenge.persistance.postgres

import com.dgg.api.server.definitions.Receipt
import doobie.{ ConnectionIO, Meta }
import doobie.util.update.Update0
import doobie.postgres.circe.json.implicits.{ pgDecoderGet, pgEncoderPut }
import doobie.postgres.implicits._
import io.circe.Json
import io.circe.syntax._

import java.util.UUID

object ReceiptProcessorQueries extends PostgresImplicits {

  implicit val metaJson: Meta[Json] = new Meta(pgDecoderGet, pgEncoderPut)

  def insertReceiptUpdate(receipt: Receipt): Update0 =
    sql"""
         |insert into receipt_processor (retailer, purchase_date, purchase_time, items, total)
         |values (
         |  ${receipt.retailer},
         |  ${receipt.purchaseDate},
         |  ${receipt.purchaseTime},
         |  ${receipt.items.asJson},
         |  ${receipt.total}
         |)
         |""".stripMargin.update

  def insertReceipt(receipt: Receipt): ConnectionIO[UUID] = insertReceiptUpdate(receipt).withUniqueGeneratedKeys("id")
}
