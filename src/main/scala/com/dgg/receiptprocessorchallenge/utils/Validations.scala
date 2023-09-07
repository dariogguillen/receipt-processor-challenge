package com.dgg.receiptprocessorchallenge.utils

import java.time.{ LocalDate, LocalTime }
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

  def validateItemDesc(desc: String, price: String): Either[Throwable, Int] = {
    val l = desc.trim.length

    if ((l % 3) == 0) Try(price.toDouble).toEither.map(i => Math.round(i * 0.2).toInt)
    else Right(0)
  }

  def validatePurchaseDate(date: LocalDate): Either[Throwable, Int] =
    if ((date.getDayOfMonth % 2) != 0) Right(6)
    else Right(0)

  def validatePurchaseTime(time: LocalTime): Either[Throwable, Int] =
    if (time.isAfter(LocalTime.of(14, 0)) && time.isBefore(LocalTime.of(16, 0))) Right(10)
    else Right(0)
}
