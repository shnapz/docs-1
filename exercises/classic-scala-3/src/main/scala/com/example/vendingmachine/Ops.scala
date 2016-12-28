package com.example.vendingmachine

object Ops {

  def userInteraction(action: UserInputAction)(state: State): (State, Seq[OutputAction]) =
    action match {
      case PutCoin(value) => putCoin(value)(state)
      case ChooseItem(id) => chooseItem(id)(state)
      case ChooseCancel => chooseCancel(state)
    }

  private def putCoin(coinValue: Int)(state: State): (State, Seq[OutputAction]) = {
    (
      state.copy(currentSum = state.currentSum + coinValue,
        coinsContainers = putCoin(coinValue, state.coinsContainers))
      ,
      Seq())


  }

  private def putCoin(coinValue: Int, to: CoinsContainers): CoinsContainers = {
    val currentNumberOfCoins = to.getOrElse(coinValue, 0)
    to.updated(coinValue, currentNumberOfCoins + 1)
  }

  private def chooseItem2(id: Int)(state: State): (State, Seq[OutputAction]) = {
    val change = state.itemsContainers.get(id) match {
      case None => displayReject(ErrorMessages.NO_ITEM_WITH_SUCH_ID)
      case Some(itemContainer) => {
        calculateChange(state.currentSum, itemContainer.price) match {
          case None => displayReject(ErrorMessages.NOT_ENOUGH_MONEY)
          case Some(change) => {
            val (stateWithoutItem, itemSeq) = outItem(id)(state)
            outCoins(change)(stateWithoutItem) match {
              case None => displayReject(ErrorMessages.CANNOT_GIVE_CHANGE)
              case Some((stateWithoutChange, changeSeq)) =>
                          (s:State) => (stateWithoutChange, itemSeq ++ changeSeq)
            }
          }
        }
      }
    }

    change(state)

  }

  private def chooseItem3(id: Int)(state: State): (State, Seq[OutputAction]) = {
    import ErrorMessages._
    val either = for {
      itemContainer <- state.itemsContainers.get(id).toRight(NO_ITEM_WITH_SUCH_ID)
      change <- calculateChange(state.currentSum, itemContainer.price).toRight(NOT_ENOUGH_MONEY)
      (stateWithoutItem, itemSeq) = outItem(id)(state)
      (stateWithoutChange, changeSeq) <- outCoins(change)(stateWithoutItem).toRight(CANNOT_GIVE_CHANGE)
    } yield {
      (stateWithoutChange, itemSeq ++ changeSeq)
    }
    either match {
      case Left(message) => displayReject(message)(state)
      case Right(newState) => newState
    }

  }


    def displayReject(message:String)(state:State):(State,Seq[OutputAction]) = {
    (state, Seq(DisplayReject(message)))
  }


  private def chooseItem(id: Int)(state: State): (State, Seq[OutputAction]) = {
    checkItemId(id, state)
  }

  def checkItemId(implicit id: Int, state: State): (State, Seq[OutputAction]) = state.itemsContainers.get(id) match {
    case None => (state, Seq(DisplayReject(ErrorMessages.NO_ITEM_WITH_SUCH_ID)))
    case Some(itemContainer) => checkEnoughMoney(itemContainer)
  }

  def checkEnoughMoney(itemContainer: ItemContainer)(implicit id: Int, state: State): (State, Seq[OutputAction]) = {
    calculateChange(state.currentSum, itemContainer.price) match {
      case None => (state, Seq(DisplayReject(ErrorMessages.NOT_ENOUGH_MONEY)))
      case Some(change) => checkCanGiveChange(change)
    }
  }

  def checkCanGiveChange(change: Int)(implicit id: Int, state: State): (State, Seq[OutputAction]) = {
    val (stateWithoutItem, itemSeq) = outItem(id)(state)
    outCoins(change)(stateWithoutItem) match {
      case None => (state, Seq(DisplayReject(ErrorMessages.CANNOT_GIVE_CHANGE)))
      case Some((stateWithoutChange, changeSeq)) => (stateWithoutChange, itemSeq ++ changeSeq)
    }
  }

  private def calculateChange(sum: Int, price: Int): Option[Int] = {
    val change = sum - price
    if (change < 0) None else Some(change)
  }

  private def outItem(id: Int)(state: State): (State, Seq[OutputAction]) =
    (state.copy(itemsContainers = removeItem(id, state.itemsContainers)),
      Seq(OutItem(id)))

  private def removeItem(id: Int, from: ItemsContainers): ItemsContainers = {
    val container = from(id)
    if (container.items == 1) from - id
    else from.updated(id, ItemContainer(container.description, container.price, container.items - 1))
  }

  private def chooseCancel(state: State): (State, Seq[OutputAction]) = outCoins(state.currentSum)(state).get

  private def outCoins(sum: Int)(state: State): Option[(State, Seq[OutputAction])] = {
    val availableCoins = toList(state.coinsContainers)
    transferCoins(availableCoins, Nil, sum) match {
      case None => None
      case Some((remaining, change)) => Some(State(0, None, toContainers(remaining), state.itemsContainers), change.map(coin => OutCoin(coin)))
    }
  }

  def transferCoins(from: List[Int], to: List[Int], remaining: Int): Option[(List[Int], List[Int])] = {
    if (remaining == 0) Some(from, to)
    else from.lastIndexWhere(_ <= remaining) match {
      case -1 => None
      case idx => transferCoins(from.take(idx) ::: from.drop(idx + 1), from(idx) :: to, remaining - from(idx))
    }
  }

  private def toList(coins: CoinsContainers): List[Int] =
    coins.toList.flatMap({
      case (coin, amount) => List.fill(amount)(coin)
    }).sorted

  private def toContainers(coins: List[Int]): CoinsContainers =
    coins.groupBy(coin => coin).map({
      case (coin, list) => (coin, list.size)
    })

}
