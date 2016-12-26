package defer

/**
  * Created by v-igsyro on 12/26/2016.
  */
class ActionsWrapper {
    private var defferedActions = List[() => Any]()
    def apply(action: => Any) = { defferedActions = (() => action) :: defferedActions }
    def runAllDefferedActions() = defferedActions.foreach { x => x.apply() }
}
