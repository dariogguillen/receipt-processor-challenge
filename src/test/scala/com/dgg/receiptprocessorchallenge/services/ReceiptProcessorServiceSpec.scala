package com.dgg.receiptprocessorchallenge.services

import cats.effect.IO
import com.dgg.receiptprocessorchallenge.{ BaseSpec, ValidationsSpec }
import io.circe._
import munit.CatsEffectSuite
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{ Method, Request, Uri }

import java.util.UUID

class ReceiptProcessorServiceSpec extends CatsEffectSuite with BaseSpec {

  private def postRequest(str: String) = Request[IO](
    method = Method.POST,
    uri    = uri"http://localhost:8080/receipts/process",
    body   = fs2.Stream.emits(str.getBytes)
  )

  private def getRequest(id: String) = Request[IO](
    method = Method.GET,
    uri    = Uri.unsafeFromString(s"http://localhost:8080/receipts/$id/points")
  )

  test("Save receipt and get points") {
    assertIO(
      EmberClientBuilder
        .default[IO]
        .build
        .use { client =>
          client
            .run(postRequest(ValidationsSpec.receiptT))
            .use(_.body.compile.toList.map(str => new String(str.toArray)))
            .flatMap { resId =>
              IO.fromEither(parser.parse(resId).flatMap(_.hcursor.downField("id").as[String]))
                .flatMap(id =>
                  client.run(getRequest(id)).use(_.body.compile.toList.map(str => new String(str.toArray)))
                )
            }
        },
      "{\"points\":26}"
    )
  }

  test("Save receipt error") {
    assertIO(
      EmberClientBuilder
        .default[IO]
        .build
        .use { client =>
          client
            .run(postRequest("{}"))
            .use(_.body.compile.toList.map(str => new String(str.toArray)))
        },
      "The request body was invalid. Missing required field: DownField(retailer)"
    )
  }

}
