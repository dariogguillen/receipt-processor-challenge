package com.dgg.receiptprocessorchallenge.utils

case class EmptyValueException(field: String) extends Exception(s"Empty value found in field: $field")
