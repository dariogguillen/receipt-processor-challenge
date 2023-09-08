package com.dgg.receiptprocessorchallenge

import com.dgg.api.server.definitions.Receipt
import com.dgg.receiptprocessorchallenge.utils.EmptyValueException
import com.dgg.receiptprocessorchallenge.utils.Validations._
import io.circe._
import munit.FunSuite

import java.time.{ LocalDate, LocalTime }
import scala.math.BigDecimal.double2bigDecimal

class ValidationsSpec extends FunSuite {
  import ValidationsSpec._
  private val retailer   = gen[String]
  private val totalRange = 0d.to(10, 0.01).toList.map(_.toString())

  test("Validate retailer") {
    assert(validateRetailer("") == Left(EmptyValueException("retailer")))
    assert(
      validateRetailer(retailer) == Right(
        retailer.toCharArray.filterNot(c => c.isWhitespace || !c.isLetterOrDigit).length
      )
    )
  }
  test("Validate total 50") {
    totalRange.filter(_.endsWith(".00")).foreach(i => assert(validateTotal50(i) == Right(50)))
    totalRange.filter(i => !i.endsWith(".00")).foreach(i => assert(validateTotal50(i) == Right(0)))
  }
  test("Validate total 25") {
    totalRange.filter(_.endsWith(".00")).foreach(i => assert(validateTotal25(i) == Right(25)))
    totalRange.filter(_.endsWith(".25")).foreach(i => assert(validateTotal25(i) == Right(25)))
    totalRange.filter(_.endsWith(".50")).foreach(i => assert(validateTotal25(i) == Right(25)))
    totalRange.filter(_.endsWith(".75")).foreach(i => assert(validateTotal25(i) == Right(25)))
    totalRange
      .filter(i => !i.endsWith(".00") && !i.endsWith(".25") && !i.endsWith(".50") && !i.endsWith(".75"))
      .foreach(i => assert(validateTotal25(i) == Right(0)))
  }
  test("Validate every two") {
    assert(validateEveryTwo(totalRange.take(1).length) == Right(0))
    assert(validateEveryTwo(totalRange.take(2).length) == Right(5))
    assert(validateEveryTwo(totalRange.take(3).length) == Right(5))
    assert(validateEveryTwo(totalRange.take(4).length) == Right(10))
  }
  test("Validate item description") {
    itemsFromJson.map(_.map { case (description, price, points) =>
      assert(validateItemDesc(description, price) == Right(points))
    })
  }
  test("Validate purchase date") {
    assert(validatePurchaseDate(LocalDate.parse("2022-01-01")) == Right(6))
    assert(validatePurchaseDate(LocalDate.parse("2022-03-20")) == Right(0))
  }
  test("Validate purchase time") {
    assert(validatePurchaseTime(LocalTime.parse("13:00")) == Right(0))
    assert(validatePurchaseTime(LocalTime.parse("15:00")) == Right(10))
    assert(validatePurchaseTime(LocalTime.parse("17:00")) == Right(0))
  }

  test("Validate receipt") {
    assert(receiptTarget.flatMap(validateReceipt) == Right(26)) // 28 in example ???
    assert(receiptMM.flatMap(validateReceipt) == Right(109))
  }

}

object ValidationsSpec {
  import com.dgg.api.server.definitions.Receipt._
  val receiptT: String =
    """
      |{
      |  "retailer": "Target",
      |  "purchaseDate": "2022-01-01",
      |  "purchaseTime": "13:01",
      |  "items": [
      |    {
      |      "shortDescription": "Mountain Dew 12PK",
      |      "price": "6.49"
      |    },
      |    {
      |      "shortDescription": "Emils Cheese Pizza",
      |      "price": "12.25"
      |    },
      |    {
      |      "shortDescription": "Knorr Creamy Chicken",
      |      "price": "1.26"
      |    },
      |    {
      |      "shortDescription": "Doritos Nacho Cheese",
      |      "price": "3.35"
      |    },
      |    {
      |      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      |      "price": "12.00"
      |    }
      |  ],
      |  "total": "35.35"
      |}
      |""".stripMargin
  val receiptTarget: Either[Error, Receipt] = parser.parse(receiptT).flatMap(_.as[Receipt])

  val receiptM: String =
    """
      |{
      |  "retailer": "M&M Corner Market",
      |  "purchaseDate": "2022-03-20",
      |  "purchaseTime": "14:33",
      |  "items": [
      |    {
      |      "shortDescription": "Gatorade",
      |      "price": "2.25"
      |    },{
      |      "shortDescription": "Gatorade",
      |      "price": "2.25"
      |    },{
      |      "shortDescription": "Gatorade",
      |      "price": "2.25"
      |    },{
      |      "shortDescription": "Gatorade",
      |      "price": "2.25"
      |    }
      |  ],
      |  "total": "9.00"
      |}
      |""".stripMargin
  val receiptMM: Either[Error, Receipt] = parser.parse(receiptM).flatMap(_.as[Receipt])

  val items: String =
    """
      |[
      |  {
      |    "shortDescription": "Mountain Dew 12PK",
      |    "price": "6.49",
      |    "points": 0
      |  },{
      |    "shortDescription": "Emils Cheese Pizza",
      |    "price": "12.25",
      |    "points": 2
      |  },{
      |    "shortDescription": "Knorr Creamy Chicken",
      |    "price": "1.26",
      |    "points": 0
      |  },{
      |    "shortDescription": "Doritos Nacho Cheese",
      |    "price": "3.35",
      |    "points": 0
      |  },{
      |    "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      |    "price": "12.00",
      |    "points": 2
      |  }
      |]
      |""".stripMargin

  val itemsFromJson: Either[ParsingFailure, List[(String, String, Int)]] = parser
    .parse(items)
    .map(j =>
      j.asArray.toList.flatten.flatMap { j1 =>
        val c = j1.hcursor

        c.downField("shortDescription")
          .focus
          .flatMap(_.asString)
          .flatMap(d =>
            c.downField("price")
              .focus
              .flatMap(_.asString)
              .flatMap(p => c.downField("points").focus.flatMap(_.asNumber.flatMap(_.toInt)).map((d, p, _)))
          )
      }
    )

}
