package com.dgg.receiptprocessorchallenge.services

import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.traverse._
import com.dgg.api.server.definitions.Receipt
import com.dgg.api.server.{ Handler, Resource }
import com.dgg.receiptprocessorchallenge.utils.Validations._
import doobie.implicits
import doobie.util.transactor.Transactor
import io.circe.Json
import io.circe.syntax._

import java.time.LocalTime
import scala.util.Try

class ReceiptProcessorService[F[_]: Applicative](
  tx: Transactor[F]
) extends Handler[F] {

  def validateReceipt(receipt: Receipt): Either[Throwable, Int] =
    for {
      retailerPts <- validateRetailer(receipt.retailer)
      // TODO valid total against item list ???
      total50Pts  <- validateTotal50(receipt.total)
      total25Pts  <- validateTotal25(receipt.total)
      everyTwoPts <- validateEveryTwo(receipt.items.length)
      itemDescPts <- receipt.items.traverse(i => validateItemDesc(i.shortDescription, i.price)).map(_.sum)
      datePts     <- validatePurchaseDate(receipt.purchaseDate)
      time        <- Try(LocalTime.parse(receipt.purchaseTime)).toEither
      timePts     <- validatePurchaseTime(time)
    } yield retailerPts + total50Pts + total25Pts + everyTwoPts + itemDescPts + datePts + timePts

  override def receiptProcessPost(respond: Resource.ReceiptProcessPostResponse.type)(
    body: Receipt
  ): F[Resource.ReceiptProcessPostResponse] = {
    for {
      _ <- implicits.WeakAsyncConnectionIO.unit
    } yield ()

    respond.Ok(Json.obj("res" -> "OK post".asJson)).asInstanceOf[Resource.ReceiptProcessPostResponse].pure[F]
  }

  override def receiptsPointsGet(respond: Resource.ReceiptsPointsGetResponse.type)(
    id: String
  ): F[Resource.ReceiptsPointsGetResponse] =
    respond.Ok(Json.obj("res" -> "OK get".asJson)).asInstanceOf[Resource.ReceiptsPointsGetResponse].pure[F]
}
