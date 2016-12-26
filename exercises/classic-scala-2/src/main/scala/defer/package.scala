/**
  * Created by v-igsyro on 12/26/2016.
  */

package object defer {
  def withDefer[ActionType](inDeferredContext: ActionsWrapper => ActionType):ActionType = {
    val actionWrapper = new ActionsWrapper()
    try {
      inDeferredContext(actionWrapper)
    }finally {
      actionWrapper.runAllDeferredActions()
    }
  }
}
