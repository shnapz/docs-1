package com.example.vendingmachine

case class State(
                  currentSum: Int = 0,
                  currentItem: Option[Int] = None,
                  coinsContainers: CoinsContainers = Map(),
                  itemsContainers: ItemsContainers = Map()
                )