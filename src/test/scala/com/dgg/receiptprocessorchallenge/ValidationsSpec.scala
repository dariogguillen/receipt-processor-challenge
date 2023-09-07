package com.dgg.receiptprocessorchallenge

import com.dgg.receiptprocessorchallenge.utils.EmptyValueException
import com.dgg.receiptprocessorchallenge.utils.Validations._
import munit.FunSuite

import scala.math.BigDecimal.double2bigDecimal

class ValidationsSpec extends FunSuite {
  private val retailer   = gen[String]
  private val totalRange = 0d.to(10, 0.01).toList.map(_.toString())

  test("Validate retailer") {
    assert(validateRetailer("") == Left(EmptyValueException("retailer")))
    assert(validateRetailer(retailer) == Right(retailer.length))
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
}
