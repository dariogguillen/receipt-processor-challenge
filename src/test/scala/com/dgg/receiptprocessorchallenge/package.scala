package com.dgg

import com.dgg.api.server.definitions.{ Item, Receipt }
import org.scalacheck.rng.Seed
import org.scalacheck.{ Arbitrary, Gen }

import java.time.format.DateTimeFormatter
import java.time.{ LocalDate, LocalTime }

package object receiptprocessorchallenge {

  def gen[T](implicit a: Arbitrary[T]): T = gen_(a.arbitrary)
  def gen_[T](implicit gen: Gen[T]): T    = gen.pureApply(Gen.Parameters.default, Seed.random())

  def string: Gen[String]  = Gen.alphaStr.filter(_.nonEmpty)
  def long: Gen[Long]      = Gen.long.filter(a => a.toString.length <= 2)
  def price: Gen[String]   = Gen.chooseNum(0.0, 99.99).map(_.toString.take(5))
  def date: Gen[LocalDate] = Gen.const(LocalDate.now())
  def time: Gen[String]    = Gen.const(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))

  def item: Gen[Item] = for {
    description <- string
    price       <- price
  } yield Item(shortDescription = description, price = price)

  implicit lazy val receiptGen: Gen[Receipt] = for {
    retailer <- string.suchThat(_.length < 64)
    date     <- date
    time     <- time
    items    <- Gen.listOf(item).suchThat(l => l.length < 10 && l.nonEmpty)
    total = items.foldLeft(0d)((p, i) => i.price.toDouble + p)
  } yield Receipt(
    retailer     = retailer,
    purchaseDate = date,
    purchaseTime = time,
    items        = items.toVector,
    total        = total.toString
  )

}
