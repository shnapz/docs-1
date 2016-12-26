/**
  * Created by v-igsyro on 12/26/2016.
  */

package object defer {
  def withDefer[ActionType](defferedContext: ActionsWrapper => ActionType) = {
    val actionWrapper = new ActionsWrapper()
    val actionsContext = defferedContext(actionWrapper)
    actionWrapper.runAllDefferedActions()
    actionsContext
  }
}
