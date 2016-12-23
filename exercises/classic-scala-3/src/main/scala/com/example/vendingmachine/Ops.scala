package com.example.vendingmachine

object Ops
{

  def userInteraction(action:UserInputAction)(state:State):(State,Seq[OutputAction]) =
   action match {
     case PutCoin(value) => putCoin(value)(state)
     case ChooseItem(id) => chooseItem(id)(state)
     case ChooseCancel   => chooseCancel(state)
   }

 def putCoin(coinValue:Int)(state:State):(State,Seq[OutputAction]) = ???
  
 def chooseItem(id:Int)(state:State):(State,Seq[OutputAction]) = ???

 def chooseCancel(state:State):(State,Seq[OutputAction]) = ???
 
 def outCoins(sum:Int)(state:State):(State,Seq[OutputAction]) = ???

}
