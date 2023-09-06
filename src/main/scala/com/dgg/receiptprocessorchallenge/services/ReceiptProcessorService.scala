package com.dgg.receiptprocessorchallenge.services

import cats.Applicative
import cats.syntax.applicative._
import com.dgg.api.server.definitions.Receipt
import com.dgg.api.server.{ Handler, Resource }
import com.dgg.receiptprocessorchallenge.utils.Validations._
import doobie.implicits
import doobie.util.transactor.Transactor
import io.circe.Json
import io.circe.syntax._

class ReceiptProcessorService[F[_]: Applicative](
  tx: Transactor[F]
) extends Handler[F] {

  def validateReceipt(receipt: Receipt): Either[Throwable, Int] =
    for {
      retailerPts <- validateRetailer(receipt.retailer)
    } yield retailerPts

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
