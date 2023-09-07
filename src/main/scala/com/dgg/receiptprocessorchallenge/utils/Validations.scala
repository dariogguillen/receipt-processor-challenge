package com.dgg.receiptprocessorchallenge.utils

import scala.util.Try

object Validations {

  def validateRetailer(retailer: String): Either[Throwable, Int] = {
    val str = retailer.trim
    if (str.isEmpty) Left(EmptyValueException("retailer"))
    else Right(str.length)
  }

  def validateTotal50(total: String): Either[Throwable, Int] =
    Try(total.toDouble).toEither.map(d =>
      if ((d - d.toInt) == 0 && d != 0) 50
      else 0
    )

  def validateTotal25(total: String): Either[Throwable, Int] =
    Try(total.toDouble).toEither.map(d =>
      if ((d % 0.25) == 0 && d != 0) 25
      else 0
    )

  def validateEveryTwo(length: Int): Either[Throwable, Int] =
    Right(((length - (length % 2)) / 2) * 5)

}
