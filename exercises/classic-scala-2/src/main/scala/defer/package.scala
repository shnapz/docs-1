/**
  * Created by v-igsyro on 12/26/2016.
  */

package object defer {
  def withDefer[ActionType](inDefferedContext: ActionsWrapper => ActionType):ActionType = {
    val actionWrapper = new ActionsWrapper()
    try {
      inDefferedContext(actionWrapper)
    }finally {
      actionWrapper.runAllDefferedActions()
    }
  }
}
