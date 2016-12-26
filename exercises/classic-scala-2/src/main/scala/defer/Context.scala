package defer

/**
  * Created by v-igsyro on 12/26/2016.
  */
class ActionsWrapper {
    private var deferredActions = List[() => Any]()
    def apply(action: => Any) = { deferredActions = (() => action) :: deferredActions }
    def runAllDeferredActions() = deferredActions.foreach { x => x.apply() }
}
