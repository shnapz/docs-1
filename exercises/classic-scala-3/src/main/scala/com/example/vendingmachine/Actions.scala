package com.example.vendingmachine

sealed trait InputAction

sealed trait UserInputAction extends InputAction

case class PutCoin(value: Int) extends UserInputAction

case class ChooseItem(itemId: Int) extends UserInputAction

case object ChooseCancel extends UserInputAction

sealed trait OutputAction

sealed trait UserOutputAction extends OutputAction

case class ListItems(itemId: Int, description: String, price: String) extends UserOutputAction

case class OutItem(itemId: Int) extends UserOutputAction

case class OutCoin(value: Int) extends UserOutputAction

case class DisplayReject(message: String) extends UserOutputAction
