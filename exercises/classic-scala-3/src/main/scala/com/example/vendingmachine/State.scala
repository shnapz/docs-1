package com.example.vendingmachine

case class ItemContainer(nItems:Int, description:String, price:Int)


  case class State(
     currentSum: Int = 0,
     currentItem: Option[Int] = None,
     coinsContainers: State.CoinsContainers = Map(),
     itemsContainers: State.ItemsContainers = Map()
  ) 

object State
{
  type CoinsContainers = Map[Int,Int]
  type ItemsContainers = Map[Int,ItemContainer]
}
