package com.dgg.receiptprocessorchallenge.models

case class Receipt(
  retailer: String,
  purchaseDate: String,
  purchaseTime: String,
  total: String,
  items: List[Item]
)
