package com.dgg.receiptprocessorchallenge.services

import cats.Applicative
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.applicative._
import cats.syntax.flatMap._
import com.dgg.api.server.definitions.Receipt
import com.dgg.api.server.{ Handler, Resource }
import com.dgg.receiptprocessorchallenge.persistance.postgres.ReceiptProcessorQueries
import com.dgg.receiptprocessorchallenge.utils.Validations._
import doobie.implicits._
import doobie.util.transactor.Transactor
import io.circe.Json
import io.circe.syntax._

import java.util.UUID
import scala.util.Try

class ReceiptProcessorService[F[_]: Applicative](
  tx: Transactor[F]
)(implicit m: MonadCancelThrow[F])
    extends Handler[F] {

  override def receiptProcessPost(respond: Resource.ReceiptProcessPostResponse.type)(
    body: Receipt
  ): F[Resource.ReceiptProcessPostResponse] =
    validateReceipt(body) match {
      case Left(err) =>
        respond
          .BadRequest(Json.obj("message" -> err.getMessage.asJson))
          .asInstanceOf[Resource.ReceiptProcessPostResponse]
          .pure[F]
      case Right(points) =>
        ReceiptProcessorQueries
          .insertReceipt(body, points)
          .transact(tx)
          .flatMap(id =>
            respond.Ok(Json.obj("id" -> id.toString.asJson)).asInstanceOf[Resource.ReceiptProcessPostResponse].pure[F]
          )

    }

  override def receiptsPointsGet(respond: Resource.ReceiptsPointsGetResponse.type)(
    id: String
  ): F[Resource.ReceiptsPointsGetResponse] =
    Try(UUID.fromString(id)).toEither match {
      case Left(err) =>
        respond
          .NotFound(Json.obj("message" -> err.getMessage.asJson))
          .asInstanceOf[Resource.ReceiptsPointsGetResponse]
          .pure[F]
      case Right(id) =>
        ReceiptProcessorQueries.selectReceiptPointsById(id).transact(tx).flatMap {
          case None =>
            respond
              .NotFound(Json.obj("message" -> s"Not Found Receipt with id: $id".asJson))
              .asInstanceOf[Resource.ReceiptsPointsGetResponse]
              .pure[F]
          case Some(points) =>
            respond.Ok(Json.obj("points" -> points.asJson)).asInstanceOf[Resource.ReceiptsPointsGetResponse].pure[F]
        }
    }
}
