package com.dgg.receiptprocessorchallenge.utils

object Validations {

  def validateRetailer(retailer: String): Either[Throwable, Int] = {
    val str = retailer.trim
    if (str.isEmpty) Left(EmptyValueException("retailer"))
    else Right(str.length)
  }

}
