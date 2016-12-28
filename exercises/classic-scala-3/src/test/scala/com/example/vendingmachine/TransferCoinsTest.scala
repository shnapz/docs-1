package com.example.vendingmachine

import org.scalatest.FunSuite

class TransferCoinsTest extends FunSuite {

  test("sunny day scenario 1") {
    assert(Ops.transferCoins(List(5, 5, 5, 10, 10, 20), Nil, 15) === Some(List(5, 5, 10, 20), List(5, 10)))
  }

  test("sunny day scenario 2") {
    assert(Ops.transferCoins(List(5, 5, 5, 20, 20, 20), Nil, 15) === Some(List(20, 20, 20), List(5, 5, 5)))
  }

  test("sunny day scenario 2") {
    assert(Ops.transferCoins(List(5, 5, 5, 20, 20, 20), Nil, 17).isEmpty)
  }



  test("no change") {
    assert(Ops.transferCoins(Nil, Nil, 15).isEmpty)
  }

  test("not enough change") {
    assert(Ops.transferCoins(List(5, 5), Nil, 15).isEmpty)
  }

  test("no suitable coins") {
    assert(Ops.transferCoins(List(20), Nil, 15).isEmpty)
  }

  test("give all coins") {
    assert(Ops.transferCoins(List(20), Nil, 20) === Some(Nil, List(20)))
  }

  test("nop") {
    assert(Ops.transferCoins(List(20), Nil, 0) === Some(List(20), Nil))
  }

}
