package com.example.vendingmachine

import org.scalatest.FunSuite

class VendingMachineTest extends FunSuite {

  test("sunny day scenario") {
    val initialState = State(0, None, Map(), Map(Items.ESPRESSO_ID -> ItemContainer(Items.ESPRESSO_DESCRIPTION, 5, 1)))
    val (stateWithMoney, _) = Ops.userInteraction(PutCoin(5))(initialState)
    val (finalState, output) = Ops.userInteraction(ChooseItem(Items.ESPRESSO_ID))(stateWithMoney)
    assert(finalState == State(0, None, Map(5 -> 1), Map()))
    assert(output == Seq(OutItem(Items.ESPRESSO_ID)))
  }

  test("choose item no money") {
    val initialState = State(0, None, Map(), Map(Items.ESPRESSO_ID -> ItemContainer(Items.ESPRESSO_DESCRIPTION, 5, 1)))
    val (newState, output) = Ops.userInteraction(ChooseItem(Items.ESPRESSO_ID))(initialState)
    assert(newState == initialState)
    assert(output == Seq(DisplayReject(ErrorMessages.NOT_ENOUGH_MONEY)))
  }

  test("choose item not enough money") {
    val initialState = State(0, None, Map(), Map(Items.ESPRESSO_ID -> ItemContainer(Items.ESPRESSO_DESCRIPTION, 5, 1)))
    val (stateWithMoney, _) = Ops.userInteraction(PutCoin(2))(initialState)
    val (finalState, output) = Ops.userInteraction(ChooseItem(Items.ESPRESSO_ID))(stateWithMoney)
    assert(finalState == stateWithMoney)
    assert(output == Seq(DisplayReject(ErrorMessages.NOT_ENOUGH_MONEY)))
  }

  test("choose item with wrong id") {
    val initialState = State(0, None, Map(), Map(Items.ESPRESSO_ID -> ItemContainer(Items.ESPRESSO_DESCRIPTION, 5, 1)))
    val (finalState, output) = Ops.userInteraction(ChooseItem(-1))(initialState)
    assert(finalState == initialState)
    assert(output == Seq(DisplayReject(ErrorMessages.NO_ITEM_WITH_SUCH_ID)))
  }

  test("cannot give change") {
    val initialState = State(0, None, Map(), Map(Items.ESPRESSO_ID -> ItemContainer(Items.ESPRESSO_DESCRIPTION, 5, 1)))
    val (stateWithMoney, _) = Ops.userInteraction(PutCoin(10))(initialState)
    val (finalState, output) = Ops.userInteraction(ChooseItem(Items.ESPRESSO_ID))(stateWithMoney)
    assert(finalState == stateWithMoney)
    assert(output == Seq(DisplayReject(ErrorMessages.CANNOT_GIVE_CHANGE)))
  }

  test("cancel") {
    val initialState = State(0, None, Map(), Map(Items.ESPRESSO_ID -> ItemContainer(Items.ESPRESSO_DESCRIPTION, 5, 1)))
    val (stateWithMoney, _) = Ops.userInteraction(PutCoin(10))(initialState)
    val (finalState, output) = Ops.userInteraction(ChooseCancel)(stateWithMoney)
    assert(finalState == initialState)
    assert(output == Seq(OutCoin(10)))
  }

}
